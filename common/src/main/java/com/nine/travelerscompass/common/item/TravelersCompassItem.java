package com.nine.travelerscompass.common.item;

import com.nine.travelerscompass.client.ClientData;
import com.nine.travelerscompass.client.hud.HudData;
import com.nine.travelerscompass.client.hud.HudSettings;
import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.search.SearchManager;
import com.nine.travelerscompass.common.search.location.ILocationObject;
import com.nine.travelerscompass.common.search.location.WithUUID;
import com.nine.travelerscompass.common.utils.FoundBlockPos;
import com.nine.travelerscompass.common.utils.HeightAttitude;
import com.nine.travelerscompass.common.utils.PriorityMode;
import com.nine.travelerscompass.common.utils.SearchState;
import com.nine.travelerscompass.config.TCConfig;
import com.nine.travelerscompass.config.filter.FilterManager;
import com.nine.travelerscompass.config.filter.FilterReason;
import com.nine.travelerscompass.platform.Platform;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class TravelersCompassItem extends Item {

    public static final int STATE_EMPTY = 1;
    public static final int STATE_SEARCHING = 2;

    public TravelersCompassItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }
        CompassContainer compassContainer = CompassContainer.container(stack);
        Vec3 lookVector = player.getLookAngle();
        Vec3 eyePosition = player.getEyePosition(1.0F);
        Vec3 traceEnd = eyePosition.add(lookVector.x * 5.0D, lookVector.y * 5.0D, lookVector.z * 5.0D);
        BlockHitResult hitResult = level.clip(new ClipContext(eyePosition, traceEnd, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
        BlockPos blockPos = hitResult.getBlockPos();
        if (!level.isClientSide) {
            if (player.isShiftKeyDown()) {
                ItemStack clickedStack = level.getBlockState(blockPos).getBlock().asItem().getDefaultInstance();
                if (!compassContainer.hasAny(clickedStack)) {
                    compassContainer.setItem(compassContainer.getFirstEmptySlot(), clickedStack, player);
                    if (FilterManager.passesFilters(clickedStack)) {
                        level.playSound(null, player.getOnPos(), SoundEvents.LODESTONE_COMPASS_LOCK, SoundSource.PLAYERS, 1.0F, 1.0F);
                        return InteractionResultHolder.success(player.getItemInHand(hand));
                    }
                }
            }
            UUID uuid = CompassProperties.COMPASS_UUID.get(stack);
            SearchManager.addWatcher(uuid, player.getUUID());
            Platform.PLATFORM.openMenu(player, stack);
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        return InteractionResultHolder.fail(player.getItemInHand(hand));
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity living, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }
        ItemStack stack = player.getItemInHand(hand);
        if (!player.level().isClientSide && player.isShiftKeyDown() && living instanceof Mob mob){
            SpawnEggItem eggItem = SpawnEggItem.byId(mob.getType());
            CompassContainer compassContainer = CompassContainer.container(stack);
            if (eggItem != null){
                ItemStack eggStack = eggItem.getDefaultInstance();
                if (!compassContainer.hasAny(eggStack)) {
                    EntityType<?> type = eggItem.getType(null);
                    FilterReason reason = FilterManager.getFilterReason(type);
                    if (reason instanceof FilterReason.Allowed) {
                        compassContainer.setItem(compassContainer.getFirstEmptySlot(), eggStack, player);
                        player.level().playSound(null, player.getOnPos(), SoundEvents.LODESTONE_COMPASS_LOCK, SoundSource.PLAYERS, 1.0F, 1.0F);
                        return InteractionResult.SUCCESS;
                    }
                }
            }

        }
        return InteractionResult.PASS;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (entity instanceof Player player) {
            final UUID uuid = CompassProperties.get(stack, CompassProperties.COMPASS_UUID);
            if (level.isClientSide){
                if (!ClientData.HUD_DATA_CACHE.containsKey(uuid)){
                    ClientData.HUD_DATA_CACHE.put(
                            uuid, new HudData(
                                    new HudSettings(
                                            CompassProperties.HUD_RENDER_MODE.get(stack),
                                            CompassProperties.HUD_TYPE.get(stack),
                                            CompassProperties.HUD_ALIGNMENT.get(stack),
                                            CompassProperties.HUD_WITH_CHAT.get(stack),
                                            CompassProperties.HUD_SCALE.get(stack),
                                            CompassProperties.HUD_WIDTH.get(stack),
                                            CompassProperties.HUD_HEIGHT.get(stack),
                                            CompassProperties.HUD_X_POS.get(stack),
                                            CompassProperties.HUD_Y_POS.get(stack)
                                    ),
                                    CompassProperties.PAUSE.get(stack),
                                    selected,
                                    CompassProperties.SEARCH_STATE.get(stack)
                            ));
                }
                else {
                    ClientData.HUD_DATA_CACHE.computeIfPresent(uuid, (id, hudData) -> {
                        hudData.setSelected(selected);
                        return hudData;
                    });
                    if (player.tickCount % 20 == 0){
                        ClientData.HUD_DATA_CACHE.computeIfPresent(uuid, (id, hudData) -> {
                            hudData.setSearchState(CompassProperties.SEARCH_STATE.get(stack));
                            return hudData;
                        });
                    }
                }
            }
            if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
                final boolean searching = SearchManager.inQueue(uuid);
                if (!searching) {
                    if (!CompassContainer.container(stack).isEmpty()){
                        tryToScan(stack, serverPlayer, uuid);
                    }
                }
                PriorityMode priorityMode = CompassProperties.PRIORITY_MODE.get(stack);
                ILocationObject locationObject = SearchManager.getClosestLocation(player.blockPosition(), uuid, priorityMode);
                boolean validLocation = locationObject != null;
                if (player.tickCount % 5 == 0) {
                    CompassContainer container = CompassContainer.container(stack);
                    if (player.tickCount % 20 == 0) {
                        SearchState searchState = CompassProperties.SEARCH_STATE.get(stack);
                        if (!searching && searchState != SearchState.IDLE) {
                            CompassProperties.putFromServer(stack, serverPlayer, CompassProperties.SEARCH_STATE, SearchState.IDLE);
                        }
                        if (container.isEmpty()){
                            SearchManager.clearFoundBlocks(uuid);
                        }
                        SearchManager.validatePositions(level, uuid);
                        SearchManager.validatePriority(stack);

                        //Updating hud renderer
                        if (TCConfig.ENABLE_HUD.get()) {
                            var clientLocationObject = locationObject;
                            {
                                if (clientLocationObject != null && !TCConfig.SHOW_COORDS.get()) {
                                    clientLocationObject = clientLocationObject.withBlockPos(null);
                                }
                                Platform.PLATFORM_NETWORK.sendS2CHudLocationDataPacket(serverPlayer,
                                        clientLocationObject, uuid);
                            }
                        }
                    }
                    FoundBlockPos foundBlockPos;
                    if (!validLocation) {
                        CompassProperties.put(stack, CompassProperties.PRIORITY_ITEM_FOUND, false);
                        foundBlockPos = new FoundBlockPos();
                    }
                    else {
                        foundBlockPos = new FoundBlockPos(locationObject.blockPos(), true);
                        CompassProperties.put(stack, CompassProperties.PRIORITY_ITEM_FOUND, locationObject.priority());
                    }
                    updateState(stack, validLocation, container.isEmpty());
                    String targetID = validLocation ? locationObject.descriptionId() : "";
                    UUID targetUUID = validLocation && locationObject instanceof WithUUID withUUID ? withUUID.uuid() : Util.NIL_UUID;

                    //Sound ping
                    {
                        if (validLocation && !CompassProperties.FOUND_BLOCK_POS.get(stack).equals(foundBlockPos) && CompassProperties.SOUND_PING.get(stack)) {
                            boolean shouldPing = false;
                            if (!targetUUID.equals(CompassProperties.TARGET_UUID.get(stack))) {
                                shouldPing = true;
                            }
                            if (!targetID.isEmpty() && !targetID.equals(CompassProperties.TARGET_ID.get(stack))){
                                shouldPing = true;
                            }
                            if (shouldPing){
                                playPingSound(locationObject, player);
                            }
                        }
                    }

                    CompassProperties.put(stack, CompassProperties.TARGET_ID, targetID);
                    CompassProperties.put(stack, CompassProperties.TARGET_UUID, targetUUID);
                    CompassProperties.put(stack, CompassProperties.FOUND_BLOCK_POS, foundBlockPos);

                }
                updateTargetAttitude(stack, player.blockPosition(), locationObject);
            }
        }
    }

    private void playPingSound(ILocationObject locationObject, Player player){
        Level level = player.level();
        if (locationObject.priority()) {
            level.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.3F, 0.3F);
        }
        level.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.1F, 0.44F + level.random.nextFloat() / 10);
        level.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.1F, 1F);
    }

    private void updateState(ItemStack stack, boolean validLocation, boolean empty) {
        int currentState = CompassProperties.COMPASS_STATE.get(stack);
        int newState = STATE_EMPTY;
        if (validLocation){
            newState = -1;
        }
        else if (CompassProperties.SEARCH_STATE.get(stack) != SearchState.IDLE){
            newState = STATE_SEARCHING;
        }
        else if(CompassProperties.PAUSE.get(stack)){
            newState = STATE_EMPTY;
        } else if (!empty) {
            newState = STATE_SEARCHING;
        }
        if (currentState != newState){
            CompassProperties.COMPASS_STATE.put(stack, newState);
        }
    }

    private void updateTargetAttitude(ItemStack stack, BlockPos playerPos, ILocationObject locationData){
        HeightAttitude heightAttitude;
        HeightAttitude current = CompassProperties.TARGET_HEIGHT.get(stack);
        if (locationData == null){
            heightAttitude = HeightAttitude.NONE;
        }
        else {
            BlockPos targetPos = locationData.blockPos();
            int pY = playerPos.getY();
            int tY = targetPos.getY();
            if (pY - tY > 0){
                heightAttitude = HeightAttitude.DOWN;
            }
            else if (pY - tY <= -2){
                heightAttitude = HeightAttitude.UP;
            }
            else {
                heightAttitude = HeightAttitude.SAME;
            }
        }
        if (current != heightAttitude){
            CompassProperties.put(stack, CompassProperties.TARGET_HEIGHT, heightAttitude);
        }
    }

    private void tryToScan(ItemStack stack, ServerPlayer player, UUID uuid) {
        if (!CompassProperties.get(stack, CompassProperties.PAUSE)) {
            int cooldown = CompassProperties.SEARCH_COOLDOWN.get(stack);
            if (cooldown > 0) {
                CompassProperties.put(stack, CompassProperties.SEARCH_COOLDOWN, cooldown - 1);
            }
            if (cooldown <= 0) {
                CompassContainer container = CompassContainer.container(stack);
                if (container.isEmpty()){
                    CompassProperties.put(stack, CompassProperties.SEARCH_COOLDOWN, 20);
                    return;
                }
                CompassProperties.put(stack, CompassProperties.SEARCH_COOLDOWN, TCConfig.SEARCH_INTERVAL.get());
                CompassProperties.putFromServer(stack, player, CompassProperties.SEARCH_STATE, SearchState.SEARCHING);
                SearchManager.startSearch(stack, player, container, false, (result) -> {
                    CompassProperties.putFromServer(stack, player, CompassProperties.SEARCH_STATE, SearchState.IDLE);
                    PriorityMode priorityMode = CompassProperties.PRIORITY_MODE.get(stack);
                    SearchManager.validatePriority(stack);
                    SearchManager.saveClosest(result.get(), player.blockPosition(), uuid, TCConfig.MAX_CACHED_LOCATIONS.get(), priorityMode);
                    SearchManager.validatePositions(player.level(), uuid);
                });
            }
        }
    }

    public static FoundBlockPos getFoundPos(ItemStack stack) {
        return CompassProperties.FOUND_BLOCK_POS.get(stack);
    }

}