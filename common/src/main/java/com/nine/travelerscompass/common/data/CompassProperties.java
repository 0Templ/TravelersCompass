package com.nine.travelerscompass.common.data;

import com.mojang.serialization.Codec;
import com.nine.travelerscompass.client.hud.Alignment;
import com.nine.travelerscompass.client.hud.HudRenderMode;
import com.nine.travelerscompass.client.hud.HudSize;
import com.nine.travelerscompass.client.hud.HudType;
import com.nine.travelerscompass.client.hud.anchor.HudAnchorX;
import com.nine.travelerscompass.client.hud.anchor.HudAnchorY;
import com.nine.travelerscompass.common.data.codec.MoreCodecs;
import com.nine.travelerscompass.common.utils.*;
import com.nine.travelerscompass.compat.lootr.LootrSearchMode;
import com.nine.travelerscompass.config.TCConfig;
import com.nine.travelerscompass.platform.Platform;
import net.minecraft.Util;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

//Todo: group props
public class CompassProperties {
	
	public static final Map<String, DataStorage<?>> REGISTRY = new HashMap<>();
	
	public static final Map<Byte, DataStorage<?>> CLIENT_EDITABLE = new HashMap<>();
	
	public static final Map<Byte, DataStorage<?>> NETWORK_REGISTRY = new HashMap<>();
	
	public static final Set<DataStorage<?>> SEARCH_OPTIONS = new HashSet<>();
	
	private enum RegistrationOption {
		//Can be changed on the client and synchronized with the server.
		CLIENT_EDITABLE,
		CACHE_ENCODING,
		SEARCH_OPTION
//      REQUIRES_VALIDATION
	}
	
	private static byte networkDataIndex = 0;
	
	public static <T> void putDefaultToServer(ItemStack stack, DataStorage<T> data) {
		data.syncToServer(stack, data.defaultValue());
	}
	
	public static void toggleToServer(ItemStack stack, DataStorage<?> data) {
		toggleToServer(stack, data, false);
	}
	
	@SuppressWarnings("unchecked")
	public static void toggleToServer(ItemStack stack, DataStorage<?> data, boolean forward) {
		Object current = data.get(stack);
		if (current instanceof Boolean v) {
			((DataStorage<Boolean>) data).syncToServer(stack, !v);
		} else if (data.defaultValue().getClass().isEnum()) {
			Class<Enum<?>> enumClass = (Class<Enum<?>>) data.defaultValue().getClass();
			Enum<?> enumValue = (Enum<?>) current;
			Enum<?>[] values = enumClass.getEnumConstants();
			int nextIndex = (enumValue.ordinal() + (forward ? 1 : values.length - 1)) % values.length;
			var next = values[nextIndex];
			((DataStorage<Enum<?>>) data).syncToServer(stack, next);
		} else {
			throw new IllegalArgumentException("Unsupported data type for toggling: " + current.getClass());
		}
	}
	
	public static <T> T get(ItemStack stack, DataStorage<T> dataStorage) {
		return dataStorage.get(stack);
	}
	
	public static final DataStorage<UUID> COMPASS_UUID = register(
			"compass_uuid", Util.NIL_UUID, UUIDUtil.CODEC, UUIDUtil.STREAM_CODEC);
	
	public static final DataStorage<List<Integer>> PRIORITY_SLOTS = register(
			"priority_slots",
			(Supplier<List<Integer>>) ArrayList::new,
			Codec.list(Codec.INT),
			RegistrationOption.CLIENT_EDITABLE
	);
	
	
	public static final DataStorage<ItemContainerContents> CONTAINER = register(
			"compass_container", ItemContainerContents.fromItems(new ArrayList<>()), ItemContainerContents.CODEC, ItemContainerContents.STREAM_CODEC, RegistrationOption.CLIENT_EDITABLE);
	
	//Search logic
	public static final DataStorage<Boolean> FORCE_CHUNKS_LOAD = register(
			"force_chunks_load", false, Codec.BOOL, ByteBufCodecs.BOOL, RegistrationOption.CLIENT_EDITABLE);
	
	public static final DataStorage<Integer> SEARCH_COOLDOWN = register(
			"cooldown", 60, Codec.INT, ByteBufCodecs.INT);
	
	
	public static final DataStorage<Integer> ENTITIES_SEARCH_RANGE = register(
			"entities_search_range", 100, (value -> Mth.clamp(value, 1, TCConfig.ENTITIES_SEARCH_RANGE.get())), Codec.INT, ByteBufCodecs.INT, RegistrationOption.CLIENT_EDITABLE);
	
	public static final DataStorage<Integer> BLOCK_SEARCH_CHUNK_RANGE = register(
			"blocks_chunk_search_range", 6, (value -> Mth.clamp(value, 1, TCConfig.BLOCKS_CHUNK_SEARCH_RANGE.get())), Codec.INT, ByteBufCodecs.INT, RegistrationOption.CLIENT_EDITABLE);
	
	public static final DataStorage<Integer> WIDE_SEARCH_RANGE = register(
			"wide_search_range", 10, (value -> Mth.clamp(value, 1, TCConfig.WIDE_SEARCH_RANGE.get())), Codec.INT, ByteBufCodecs.INT, RegistrationOption.CLIENT_EDITABLE);
	
	public static final DataStorage<Boolean> PAUSE = register(
			"pause", false, Codec.BOOL, ByteBufCodecs.BOOL, RegistrationOption.CLIENT_EDITABLE);
	
	public static final DataStorage<Boolean> TARGET_VALIDATION = register(
			"target_validation", true, Codec.BOOL, ByteBufCodecs.BOOL, RegistrationOption.CLIENT_EDITABLE);
	
	public static final DataStorage<HeightAttitude> TARGET_HEIGHT = register(
			"target_height", HeightAttitude.NONE, MoreCodecs.enumCodec(HeightAttitude.class), MoreCodecs.enumStreamCodec(HeightAttitude.class));
	
	public static final DataStorage<String> TARGET_ID = register(
			"target_id", "", Codec.STRING, ByteBufCodecs.STRING_UTF8);
	
	public static final DataStorage<UUID> TARGET_UUID = register(
			"target_uuid", Util.NIL_UUID, UUIDUtil.CODEC, UUIDUtil.STREAM_CODEC);
	
	public static final DataStorage<Boolean> PRIORITY_ITEM_FOUND = register(
			"priority_item_found", false, Codec.BOOL, ByteBufCodecs.BOOL);
	
	public static final DataStorage<Integer> COMPASS_STATE = register(
			"state", 0, Codec.INT, ByteBufCodecs.INT);
	
	public static final DataStorage<SearchState> SEARCH_STATE = register(
			"searching", SearchState.IDLE, MoreCodecs.enumCodec(SearchState.class), MoreCodecs.enumStreamCodec(SearchState.class));
	
	public static final DataStorage<FoundBlockPos> FOUND_BLOCK_POS = register(
			"found_block_pos", (Supplier<FoundBlockPos>) FoundBlockPos::new, FoundBlockPos.CODEC, FoundBlockPos.STREAM_CODEC);
	
	public static final DataStorage<TabPage> TAB_PAGE = register(
			"tab_page", TabPage.SEARCH, MoreCodecs.enumCodec(TabPage.class), MoreCodecs.enumStreamCodec(TabPage.class), RegistrationOption.CLIENT_EDITABLE);
	
	public static final DataStorage<Boolean> HEIGHT_MARKER = register(
			"height_marker", true, Codec.BOOL, ByteBufCodecs.BOOL, RegistrationOption.CLIENT_EDITABLE);
	
	public static final DataStorage<PriorityMode> PRIORITY_MODE = register(
			"priority_mode", PriorityMode.NORMAL, MoreCodecs.enumCodec(PriorityMode.class), MoreCodecs.enumStreamCodec(PriorityMode.class), RegistrationOption.CLIENT_EDITABLE);
	
	public static final DataStorage<Boolean> SOUND_PING = register(
			"sound_ping", false, Codec.BOOL, ByteBufCodecs.BOOL, RegistrationOption.CLIENT_EDITABLE);
	
	//Hud props
	public static final DataStorage<Alignment> HUD_ALIGNMENT = register(
			"hud_alignment", Alignment.LEFT, MoreCodecs.enumCodec(Alignment.class), MoreCodecs.enumStreamCodec(Alignment.class), RegistrationOption.CLIENT_EDITABLE);
	
	public static final DataStorage<HudType> HUD_TYPE = register(
			"hud_type", HudType.COMPACT, MoreCodecs.enumCodec(HudType.class), MoreCodecs.enumStreamCodec(HudType.class), RegistrationOption.CLIENT_EDITABLE);
	
	public static final DataStorage<HudRenderMode> HUD_RENDER_MODE = register(
			"hud_render_mode", HudRenderMode.OFF, MoreCodecs.enumCodec(HudRenderMode.class), MoreCodecs.enumStreamCodec(HudRenderMode.class), RegistrationOption.CLIENT_EDITABLE);
	
	public static final DataStorage<HudSize> HUD_SIZE = register(
			"hud_size", (Supplier<HudSize>) HudSize::new, HudSize.CODEC, HudSize.STREAM_CODEC, RegistrationOption.CLIENT_EDITABLE);
	
	public static final DataStorage<Boolean> HUD_WITH_CHAT = register(
			"hud_with_chat", false, Codec.BOOL, ByteBufCodecs.BOOL, RegistrationOption.CLIENT_EDITABLE);
	
	public static final DataStorage<Float> HUD_SCALE = register(
			"hud_scale", (Supplier<Float>) () -> (TCConfig.DEFAULT_HUD_SCALE.get().floatValue()), Codec.FLOAT, ByteBufCodecs.FLOAT, RegistrationOption.CLIENT_EDITABLE);
	
	public static final DataStorage<Integer> HUD_X_POS =
			register("hud_x_pos", (Supplier<Integer>) TCConfig.DEFAULT_HUD_X_POSITION::get, Codec.INT, ByteBufCodecs.INT, RegistrationOption.CLIENT_EDITABLE);
	
	public static final DataStorage<Integer> HUD_Y_POS = register(
			"hud_y_pos", (Supplier<Integer>) TCConfig.DEFAULT_HUD_Y_POSITION::get, Codec.INT, ByteBufCodecs.INT, RegistrationOption.CLIENT_EDITABLE);
	
	public static final DataStorage<HudAnchorX> HUD_X_ANCHOR = register(
			"hud_anchor_x", HudAnchorX.LEFT, MoreCodecs.enumCodec(HudAnchorX.class), MoreCodecs.enumStreamCodec(HudAnchorX.class), RegistrationOption.CLIENT_EDITABLE);
	
	public static final DataStorage<HudAnchorY> HUD_Y_ANCHOR = register(
			"hud_anchor_y", HudAnchorY.TOP, MoreCodecs.enumCodec(HudAnchorY.class), MoreCodecs.enumStreamCodec(HudAnchorY.class), RegistrationOption.CLIENT_EDITABLE);
	
	
	//Search Modes
	public static final DataStorage<Boolean> CONTAINERS = register(
			"containers", false, Codec.BOOL, ByteBufCodecs.BOOL, RegistrationOption.SEARCH_OPTION, RegistrationOption.CLIENT_EDITABLE);
	
	public static final DataStorage<Boolean> VILLAGERS = register(
			"villagers", false, Codec.BOOL, ByteBufCodecs.BOOL, RegistrationOption.SEARCH_OPTION, RegistrationOption.CLIENT_EDITABLE);
	
	public static final DataStorage<Boolean> VILLAGERS_BUYS = register(
			"villagers_buys", true, Codec.BOOL, ByteBufCodecs.BOOL, RegistrationOption.SEARCH_OPTION, RegistrationOption.CLIENT_EDITABLE);
	
	public static final DataStorage<Boolean> VILLAGERS_SELLS = register(
			"villagers_sells", true, Codec.BOOL, ByteBufCodecs.BOOL, RegistrationOption.SEARCH_OPTION, RegistrationOption.CLIENT_EDITABLE);
	
	public static final DataStorage<Boolean> ITEM_ENTITIES = register(
			"item_entities", false, Codec.BOOL, ByteBufCodecs.BOOL, RegistrationOption.SEARCH_OPTION, RegistrationOption.CLIENT_EDITABLE);
	
	public static final DataStorage<Boolean> INVENTORIES = register(
			"inventories", false, Codec.BOOL, ByteBufCodecs.BOOL, RegistrationOption.SEARCH_OPTION, RegistrationOption.CLIENT_EDITABLE);
	
	public static final DataStorage<Boolean> INVENTORIES_PLAYERS = register(
			"inv_players", true, Codec.BOOL, ByteBufCodecs.BOOL, RegistrationOption.SEARCH_OPTION, RegistrationOption.CLIENT_EDITABLE);
	
	public static final DataStorage<Boolean> INVENTORIES_MOBS = register(
			"inv_mobs", true, Codec.BOOL, ByteBufCodecs.BOOL, RegistrationOption.SEARCH_OPTION, RegistrationOption.CLIENT_EDITABLE);
	
	public static final DataStorage<Boolean> FLUIDS = register(
			"fluids", false, Codec.BOOL, ByteBufCodecs.BOOL, RegistrationOption.SEARCH_OPTION, RegistrationOption.CLIENT_EDITABLE);
	
	public static final DataStorage<Boolean> SPAWNERS = register(
			"spawners", false, Codec.BOOL, ByteBufCodecs.BOOL, RegistrationOption.SEARCH_OPTION, RegistrationOption.CLIENT_EDITABLE);
	
	public static final DataStorage<Boolean> BLOCKS = register(
			"blocks", false, Codec.BOOL, ByteBufCodecs.BOOL, RegistrationOption.SEARCH_OPTION, RegistrationOption.CLIENT_EDITABLE);
	
	public static final DataStorage<Boolean> MOBS = register(
			"mobs", false, Codec.BOOL, ByteBufCodecs.BOOL, RegistrationOption.SEARCH_OPTION, RegistrationOption.CLIENT_EDITABLE);
	
	public static final DataStorage<Boolean> DROP = register(
			"drop", false, Codec.BOOL, ByteBufCodecs.BOOL, RegistrationOption.SEARCH_OPTION, RegistrationOption.CLIENT_EDITABLE);
	
	public static final DataStorage<Boolean> ENTITY_CONTAINERS = register(
			"entity_containers", true, Codec.BOOL, ByteBufCodecs.BOOL, RegistrationOption.SEARCH_OPTION, RegistrationOption.CLIENT_EDITABLE);
	
	public static final DataStorage<Boolean> BLOCK_CONTAINERS = register(
			"block_containers", true, Codec.BOOL, ByteBufCodecs.BOOL, RegistrationOption.SEARCH_OPTION, RegistrationOption.CLIENT_EDITABLE);
	
	public static final DataStorage<LootrSearchMode> LOOTR_MODE = register(
			"lootr_containers", LootrSearchMode.CLOSED, MoreCodecs.enumCodec(LootrSearchMode.class), MoreCodecs.enumStreamCodec(LootrSearchMode.class), RegistrationOption.SEARCH_OPTION, RegistrationOption.CLIENT_EDITABLE);
	
	private static <T> DataStorage<T> register(String id, Supplier<T> defaultSupplier, Codec<T> codec, RegistrationOption... regOpts) {
		return register(id, defaultSupplier, v -> v, codec, null, regOpts);
	}
	
	private static <T> DataStorage<T> register(String id, Supplier<T> defaultSupplier, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec, RegistrationOption... regOpts) {
		return register(id, defaultSupplier, v -> v, codec, streamCodec, regOpts);
	}
	
	private static <T> DataStorage<T> register(String id, T defaultValue, Codec<T> codec, RegistrationOption... regOpts) {
		return register(id, defaultValue, codec, null, regOpts);
	}
	
	private static <T> DataStorage<T> register(String id, T defaultValue, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec, RegistrationOption... regOpts) {
		return register(id, defaultValue, v -> v, codec, streamCodec, regOpts);
	}
	
	private static <T> DataStorage<T> register(String id, T defaultValue, Function<T, T> validator, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec, RegistrationOption... regOpts) {
		return register(id, (Supplier<T>) () -> defaultValue, validator, codec, streamCodec, regOpts);
	}
	
	private static <T> DataStorage<T> register(String id, Supplier<T> defaultSupplier, Function<T, T> validator, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec, RegistrationOption... regOpts) {
		byte networkId = networkDataIndex++;
		DataStorage<T> ret = Platform.PLATFORM_REGISTRY.registerDataComponent(
				id,
				networkId,
				defaultSupplier,
				validator,
				codec,
				streamCodec,
				Arrays.stream(regOpts).toList().contains(RegistrationOption.CACHE_ENCODING)
		);
		REGISTRY.put(id, ret);
		NETWORK_REGISTRY.put(networkId, ret);
		for (var option : regOpts) {
			if (option == RegistrationOption.SEARCH_OPTION) {
				SEARCH_OPTIONS.add(ret);
			} else if (option == RegistrationOption.CLIENT_EDITABLE) {
				CLIENT_EDITABLE.put(networkId, ret);
			}
		}
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> DataStorage<T> getById(byte id) {
		return (DataStorage<T>) NETWORK_REGISTRY.get(id);
	}
	
	public static void init() {
	
	}
	
}
