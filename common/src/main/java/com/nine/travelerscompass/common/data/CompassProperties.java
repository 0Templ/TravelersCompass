package com.nine.travelerscompass.common.data;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.common.data.codec.*;
import com.nine.travelerscompass.common.utils.*;
import com.nine.travelerscompass.compat.lootr.LootrSearchMode;
import com.nine.travelerscompass.config.TCConfig;
import com.nine.travelerscompass.platform.Platform;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.function.Function;

public class CompassProperties {

    public static final Map<ResourceLocation, DataStorage<?>> REGISTRY = new HashMap<>();

    public static final Map<ResourceLocation, DataStorage<?>> CLIENT_EDITABLE = new HashMap<>();

    public static final Set<DataStorage<?>> SEARCH_OPTIONS = new HashSet<>();

    private enum RegistrationOption {
        //Can be changed on the client and synchronized with the server.
        CLIENT_EDITABLE,
        SEARCH_OPTION
//      REQUIRES_VALIDATION
    }

    public static <T> T get(ItemStack stack, DataStorage<T> data) {
        return data.read(stack);
    }

    public static <T> T get(CompoundTag tag, DataStorage<T> data) {
        return data.read(tag);
    }

    public static <T> void put(ItemStack stack, DataStorage<T> data, T value) {
        put(stack.getOrCreateTag(), data, value);
    }

    public static <T> void put(CompoundTag tag, DataStorage<T> data, T value) {
        data.write(tag, value);
    }

    public static <T> void putWithValidation(ItemStack stack, DataStorage<T> data, T value) {
        putWithValidation(stack.getOrCreateTag(), data, value);
    }

    public static <T> void putWithValidation(CompoundTag tag, DataStorage<T> data, T value) {
        data.write(tag, data.validate(value));
    }

    public static <T> void writeDefaultFromClient(ItemStack stack, DataStorage<T> data) {
        putFromClient(stack, data, data.getDefault(stack));
    }

    @SuppressWarnings("unchecked")
    public static void toggleFromClient(ItemStack stack, DataStorage<?> data, boolean forward) {
        Object current = data.get(stack);
        DataCodec<?> codec = data.getCodec();
        if (current instanceof Boolean bool) {
            putFromClient(stack, (DataStorage<Boolean>) data, !bool);
        } else if (codec instanceof EnumCodec<?> enumCodec) {
            Enum<?> enumValue = (Enum<?>) current;
            Enum<?>[] values = enumCodec.getEnumClass().getEnumConstants();
            int nextIndex = (enumValue.ordinal() + (forward ? 1 : values.length - 1)) % values.length;
            var next = values[nextIndex];
            putFromClient(stack, (DataStorage<Enum<?>>) data, next);
        } else {
            throw new IllegalArgumentException("Unsupported data type for toggling: " + current.getClass());
        }
    }

    public static void toggleFromClient(ItemStack stack, DataStorage<Boolean> data) {
        putFromClient(stack, data, !get(stack, data));
    }

    public static <T> void putFromClient(ItemStack stack, DataStorage<T> data, T value) {
        putFromClient(stack.getOrCreateTag(), data, value);
    }

    public static <T> void putFromClient(CompoundTag stackTag, DataStorage<T> data, T value) {
        CompoundTag tag = new CompoundTag();
        put(tag, data, value);
        data.put(stackTag, value);
        Platform.PLATFORM_NETWORK.sendC2SDataPacket(data, tag);
    }

    public static <T> void putFromServer(ItemStack stack, ServerPlayer player, DataStorage<T> data, T value) {
        CompoundTag tag = new CompoundTag();
        put(tag, data, value);
        data.put(stack.getOrCreateTag(), value);
        Platform.PLATFORM_NETWORK.sendS2CDataPacket(player, data, tag, COMPASS_UUID.get(stack));
    }

    public static final DataStorage<UUID> COMPASS_UUID = register(
            "compass_uuid", (Function<CompoundTag, UUID>) tag -> UUID.randomUUID(), new UUIDCodec());

    public static final DataStorage<int[]> PRIORITY_SLOTS = register(
            "priority_slots", new int[0], (value -> value.length > 9 ? java.util.Arrays.copyOf(value, 9) : value), new IntArrayCodec(), RegistrationOption.CLIENT_EDITABLE);

    //Search logic
    public static final DataStorage<Boolean> FORCE_CHUNKS_LOAD = register(
            "force_chunks_load", false, new BooleanCodec(), RegistrationOption.CLIENT_EDITABLE);

    public static final DataStorage<Integer> SEARCH_COOLDOWN = register(
            "cooldown", 60, new IntCodec());

    public static final DataStorage<Integer> ENTITIES_SEARCH_RANGE = register(
            "entities_search_range", 100, (value -> Mth.clamp(value, 1, TCConfig.ENTITIES_SEARCH_RANGE.get())), new IntCodec(), RegistrationOption.CLIENT_EDITABLE);

    public static final DataStorage<Integer> BLOCK_SEARCH_CHUNK_RANGE = register(
            "blocks_chunk_search_range", 6, (value -> Mth.clamp(value, 1, TCConfig.BLOCKS_CHUNK_SEARCH_RANGE.get())), new IntCodec(), RegistrationOption.CLIENT_EDITABLE);

    public static final DataStorage<Integer> WIDE_SEARCH_RANGE = register(
            "wide_search_range", 10, (value -> Mth.clamp(value, 1, TCConfig.WIDE_SEARCH_RANGE.get())), new IntCodec(), RegistrationOption.CLIENT_EDITABLE);

    public static final DataStorage<Boolean> PAUSE = register(
            "pause", false, new BooleanCodec(), RegistrationOption.CLIENT_EDITABLE);

    public static final DataStorage<Boolean> TARGET_VALIDATION = register(
            "target_validation", true, new BooleanCodec(), RegistrationOption.CLIENT_EDITABLE);

    //Target data
    public static final DataStorage<HeightAttitude> TARGET_HEIGHT = register(
            "target_attitude", HeightAttitude.NONE, new EnumCodec<>(HeightAttitude.class));

    public static final DataStorage<FoundBlockPos> FOUND_BLOCK_POS = register(
            "found_block_pos", new FoundBlockPos(), new FoundBlockPosCodec());

    public static final DataStorage<String> TARGET_ID = register(
            "target_id", "", new StringCodec());

    public static final DataStorage<UUID> TARGET_UUID = register(
            "target_uuid", Util.NIL_UUID, new UUIDCodec());

    public static final DataStorage<Boolean> PRIORITY_ITEM_FOUND = register(
            "priority_item_found", false, new BooleanCodec());

    public static final DataStorage<Integer> COMPASS_STATE = register(
            "state", 0, new IntCodec());

    public static final DataStorage<SearchState> SEARCH_STATE = register(
            "searching", SearchState.IDLE, new EnumCodec<>(SearchState.class));

    //
    public static final DataStorage<TabPage> TAB_PAGE = register(
            "tab_page", TabPage.SEARCH, new EnumCodec<>(TabPage.class), RegistrationOption.CLIENT_EDITABLE);

    public static final DataStorage<Boolean> HEIGHT_MARKER = register(
            "attitude_marker", true, new BooleanCodec(), RegistrationOption.CLIENT_EDITABLE);

    public static final DataStorage<PriorityMode> PRIORITY_MODE = register(
            "priority_mode", PriorityMode.NORMAL, new EnumCodec<>(PriorityMode.class), RegistrationOption.CLIENT_EDITABLE);

    public static final DataStorage<Boolean> SOUND_PING = register(
            "sound_ping", false, new BooleanCodec(), RegistrationOption.CLIENT_EDITABLE);


    //Hud props
    public static final DataStorage<Alignment> HUD_ALIGNMENT = register(
            "hud_alignment", Alignment.LEFT, new EnumCodec<>(Alignment.class), RegistrationOption.CLIENT_EDITABLE);

    public static final DataStorage<HudType> HUD_TYPE = register(
            "hud_type", HudType.COMPACT, new EnumCodec<>(HudType.class), RegistrationOption.CLIENT_EDITABLE);

    public static final DataStorage<HudRenderMode> HUD_RENDER_MODE = register(
            "hud_render_mode", HudRenderMode.OFF, new EnumCodec<>(HudRenderMode.class), RegistrationOption.CLIENT_EDITABLE);

    public static final DataStorage<Integer> HUD_WIDTH = register("hud_width",
            (tag) -> getCurrentHudKey(tag, "hud_width"),
            (tag) -> get(tag, HUD_TYPE).defaultWidth(),
            (t -> t),
            new IntCodec(), RegistrationOption.CLIENT_EDITABLE);

    public static final DataStorage<Integer> HUD_HEIGHT = register("hud_height",
            (tag) -> getCurrentHudKey(tag, "hud_height"),
            (tag) -> get(tag, HUD_TYPE).defaultHeight(),
            (t -> t),
            new IntCodec(), RegistrationOption.CLIENT_EDITABLE);

    public static final DataStorage<Boolean> HUD_WITH_CHAT = register(
            "hud_with_chat", false, new BooleanCodec(), RegistrationOption.CLIENT_EDITABLE);

    public static final DataStorage<Float> HUD_SCALE = register(
            "hud_scale", TCConfig.DEFAULT_HUD_SCALE.get().floatValue(), new FloatCodec(), RegistrationOption.CLIENT_EDITABLE);

    public static final DataStorage<Integer> HUD_X_POS =
            register("hud_x_pos", TCConfig.DEFAULT_HUD_X_POSITION.get(), new IntCodec(), RegistrationOption.CLIENT_EDITABLE);

    public static final DataStorage<Integer> HUD_Y_POS = register(
            "hud_y_pos", TCConfig.DEFAULT_HUD_Y_POSITION.get(), new IntCodec(), RegistrationOption.CLIENT_EDITABLE);

    //Search Modes
    public static final DataStorage<Boolean> CONTAINERS = register(
            "containers", false, new BooleanCodec(), RegistrationOption.SEARCH_OPTION, RegistrationOption.CLIENT_EDITABLE);

    public static final DataStorage<Boolean> VILLAGERS = register(
            "villagers", false, new BooleanCodec(), RegistrationOption.SEARCH_OPTION, RegistrationOption.CLIENT_EDITABLE);

    public static final DataStorage<Boolean> VILLAGERS_BUYS = register(
            "villagers_buys", true, new BooleanCodec(), RegistrationOption.SEARCH_OPTION, RegistrationOption.CLIENT_EDITABLE);

    public static final DataStorage<Boolean> VILLAGERS_SELLS = register(
            "villagers_sells", true, new BooleanCodec(), RegistrationOption.SEARCH_OPTION, RegistrationOption.CLIENT_EDITABLE);

    public static final DataStorage<Boolean> ITEM_ENTITIES = register(
            "item_entities", false, new BooleanCodec(), RegistrationOption.SEARCH_OPTION, RegistrationOption.CLIENT_EDITABLE);

    public static final DataStorage<Boolean> INVENTORIES = register(
            "inventories", false, new BooleanCodec(), RegistrationOption.SEARCH_OPTION, RegistrationOption.CLIENT_EDITABLE);

    public static final DataStorage<Boolean> INVENTORIES_PLAYERS = register(
            "inv_players", true, new BooleanCodec(), RegistrationOption.SEARCH_OPTION, RegistrationOption.CLIENT_EDITABLE);

    public static final DataStorage<Boolean> INVENTORIES_MOBS = register(
            "inv_mobs", true, new BooleanCodec(), RegistrationOption.SEARCH_OPTION, RegistrationOption.CLIENT_EDITABLE);

    public static final DataStorage<Boolean> FLUIDS = register(
            "fluids", false, new BooleanCodec(), RegistrationOption.SEARCH_OPTION, RegistrationOption.CLIENT_EDITABLE);

    public static final DataStorage<Boolean> SPAWNERS = register(
            "spawners", false, new BooleanCodec(), RegistrationOption.SEARCH_OPTION, RegistrationOption.CLIENT_EDITABLE);

    public static final DataStorage<Boolean> BLOCKS = register(
            "blocks", false, new BooleanCodec(), RegistrationOption.SEARCH_OPTION, RegistrationOption.CLIENT_EDITABLE);

    public static final DataStorage<Boolean> MOBS = register(
            "mobs", false, new BooleanCodec(), RegistrationOption.SEARCH_OPTION, RegistrationOption.CLIENT_EDITABLE);

    public static final DataStorage<Boolean> DROP = register(
            "drop", false, new BooleanCodec(), RegistrationOption.SEARCH_OPTION, RegistrationOption.CLIENT_EDITABLE);

    public static final DataStorage<Boolean> MINECARTS = register(
            "minecarts", true, new BooleanCodec(), RegistrationOption.SEARCH_OPTION, RegistrationOption.CLIENT_EDITABLE);

    public static final DataStorage<Boolean> CONTAINERS_CHESTS = register(
            "containers_chests", true, new BooleanCodec(), RegistrationOption.SEARCH_OPTION, RegistrationOption.CLIENT_EDITABLE);

    public static final DataStorage<LootrSearchMode> LOOTR_MODE = register(
            "lootr_search_mode", LootrSearchMode.CLOSED, new EnumCodec<>(LootrSearchMode.class), RegistrationOption.SEARCH_OPTION, RegistrationOption.CLIENT_EDITABLE);


    private static <T> DataStorage<T> register(String key, T defaultValue, Function<T, T> validator, DataCodec<T> codec, RegistrationOption... regOpts) {
        return register(key, (tag) -> key, (tag)-> defaultValue, validator, codec, regOpts);
    }

    private static <T> DataStorage<T> register(String key, T defaultValue, DataCodec<T> codec, RegistrationOption... regOpts) {
        return register(key, (tag) -> key, (tag)-> defaultValue, (t -> t), codec, regOpts);
    }

    private static <T> DataStorage<T> register(String key, Function<CompoundTag, T> defaultValue, DataCodec<T> codec, RegistrationOption... regOpts) {
        return register(key, (tag) -> key, defaultValue, (t -> t), codec, regOpts);
    }

    private static <T> DataStorage<T> register(
            String id,
            Function<CompoundTag, String> keyResolver,
            Function<CompoundTag, T> defaultValue,
            Function<T, T> validator,
            DataCodec<T> codec,
            RegistrationOption... regOpts) {
        ResourceLocation location = new ResourceLocation(TCCommon.MODID, id);
        DataStorage<T> ret = new DataStorage<>(location, keyResolver, defaultValue, validator, codec);
        REGISTRY.put(location, ret);
        for (var option : regOpts){
            if (option == RegistrationOption.SEARCH_OPTION){
                SEARCH_OPTIONS.add(ret);
            }
            else if(option == RegistrationOption.CLIENT_EDITABLE){
                CLIENT_EDITABLE.put(location, ret);
            }
        }
        return ret;
    }

    private static String getCurrentHudKey(CompoundTag tag, String baseKey) {
        if (tag == null){
            return baseKey;
        }
        HudType type = get(tag, HUD_TYPE);
        return type.name().toLowerCase() + "_" + baseKey;
    }

}
