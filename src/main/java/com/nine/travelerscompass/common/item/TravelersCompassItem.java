package com.nine.travelerscompass.common.item;

import com.nine.travelerscompass.TCConfig;
import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.common.container.menu.CompassMenu;
import com.nine.travelerscompass.common.utils.ConfigUtils;
import com.nine.travelerscompass.common.utils.PositionUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TravelersCompassItem extends Item {

    public TravelersCompassItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        CompassContainer compassContainer = CompassContainer.container(stack);
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }
        if (player.isShiftKeyDown()) {
            Vec3 lookVector = player.getLookAngle();
            Vec3 eyePosition = player.getEyePosition(1.0F);
            Vec3 traceEnd = eyePosition.add(lookVector.x * 5.0D, lookVector.y * 5.0D, lookVector.z * 5.0D);
            BlockHitResult hitResult = level.clip(new ClipContext(eyePosition, traceEnd, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
            BlockPos blockPos = hitResult.getBlockPos();
            if (!level.isClientSide) {
                ItemStack clickedStack = level.getBlockState(blockPos).getBlock().asItem().getDefaultInstance();
                if (!compassContainer.hasAny(clickedStack) && ConfigUtils.isAllowedToSearch(clickedStack)) {
                    compassContainer.setItem(compassContainer.getFirstEmptySlot(), clickedStack);
                    level.playSound(null, player.getOnPos(), SoundEvents.LODESTONE_COMPASS_LOCK, SoundSource.PLAYERS, 1.0F, 1.0F);
                    return InteractionResultHolder.success(player.getItemInHand(hand));
                }
            }
        }
        if (!level.isClientSide) {
            checkForbiddenItems(compassContainer);
            checkForbiddenOptions(stack);

            player.openMenu(new MenuProvider() {
                @Override
                public AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInventory, @NotNull Player player) {
                    return new CompassMenu(id, playerInventory, CompassContainer.container(stack));
                }

                @Override
                public @NotNull Component getDisplayName() {
                    return stack.getHoverName();
                }
            });
        }
        return InteractionResultHolder.success(stack);
    }


    @Override
    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity living, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isShiftKeyDown()) {
            CompassContainer compassContainer = CompassContainer.container(stack);
            if (player.isShiftKeyDown() && living instanceof Mob mob && SpawnEggItem.byId(mob.getType()) != null) {
                ItemStack eggStack = Objects.requireNonNull(SpawnEggItem.byId(mob.getType())).getDefaultInstance();
                if (!compassContainer.hasAny(eggStack) && ConfigUtils.isAllowedToSearch(eggStack) && ConfigUtils.isAllowedToSearch(living)) {
                    compassContainer.setItem(compassContainer.getFirstEmptySlot(), eggStack);
                    player.level().playSound(null, player.getOnPos(), SoundEvents.LODESTONE_COMPASS_LOCK, SoundSource.PLAYERS, 1.0F, 1.0F);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;

    }

    public void updateBlockPosition(Level level, Entity entity, ItemStack stack, boolean wideSearch) {
        CompassContainer compassContainer = CompassContainer.container(stack);
        if (stack.getItem() instanceof TravelersCompassItem compassItem) {
            if (compassContainer.isEmpty()) {
                compassItem.addBlockPositionTags(null, stack, level, entity);
                return;
            }
            if (entity instanceof Player player) {
                if (!canSearch(player)) {
                    return;
                }
                compassItem.addBlockPositionTags(PositionUtils.getNearestLocation(level, entity, compassItem, compassContainer, stack, wideSearch), stack, level, entity);
            }
        }
    }

    @Nullable
    public static BlockPos getFoundPosition(@NotNull ItemStack stack) {
        if (!stack.getOrCreateTag().getBoolean("found")) {
            return null;
        }
        if (stack.getItem() instanceof TravelersCompassItem compassItem) {
            return NbtUtils.readBlockPos(stack.getOrCreateTagElement("foundPos"));
        }
        return NbtUtils.readBlockPos(stack.getOrCreateTagElement("foundPos"));
    }

    public void addBlockPositionTags(PositionUtils.LocationData data, ItemStack stack, Level level, Entity entity) {
        boolean shouldPing = false;
        CompoundTag compoundTag = stack.getOrCreateTag();
        if (data == null || data.getBlockPos() == null) {
            if (compoundTag.getBoolean("found")) {
                compoundTag.putBoolean("found", false);
            }
            return;
        }
        BlockPos blockPos = data.getBlockPos();
        if (!compoundTag.getBoolean("found")) {
            shouldPing = true;
            compoundTag.putBoolean("found", true);
        }
        if (sound(stack)) {
            if (!Objects.equals(foundTarget(stack), data.getName())) {
                shouldPing = true;
            }
            if (shouldPing) {
                if (data.hasAnyFavorite()) {
                    level.playSound(null, entity.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.3F, 0.3F);
                }
                level.playSound(null, entity.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.1F, 0.44F + level.random.nextFloat() / 10);
                level.playSound(null, entity.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.1F, 1F);
            }
        }
        if (data.getName() != null) {
            writeFoundTarget(stack, data.getName());
        } else {
            writeFoundTarget(stack, "Null");
        }
        compoundTag.put("foundPos", NbtUtils.writeBlockPos(blockPos));
    }

    public static void xpDrain(Player player) {
        if (!player.isCreative() && !player.isSpectator()) {
            int currentExp = player.totalExperience;
            if (currentExp - TCConfig.xpCost.get() > 0) {
                player.giveExperiencePoints(-TCConfig.xpCost.get());
            } else {
                player.experienceLevel = 0;
                player.experienceProgress = 0;
                player.totalExperience = 0;
            }
        }
    }

    public static boolean canSearch(Player player) {
        if (!TCConfig.xpDrain.get()) {
            return true;
        }
        if (!player.isCreative() && !player.isSpectator()) {
            int currentExp = player.totalExperience;
            return currentExp > 0;
        }
        return true;
    }

    public void checkForbiddenItems(CompassContainer compassContainer) {
        for (int index = 0; index < 9; index++) {
            if (!ConfigUtils.isAllowedToSearch(compassContainer.getItem(index))) {
                compassContainer.removeItem(index, 1);
            }
        }
    }

    public void checkForbiddenOptions(ItemStack stack) {
        if (isSearchingEntitiesInv(stack) && !TCConfig.enableMobsInventorySearch.get()) {
            writeCompassData(stack, CompassData.SEARCHING_ENTITIES_INV);
        }
        if (isSearchingBlocks(stack) && !TCConfig.enableBlockSearch.get()) {
            writeCompassData(stack, CompassData.SEARCHING_BLOCKS);
        }
        if (isSearchingFluids(stack) && !TCConfig.enableFluidSearch.get()) {
            writeCompassData(stack, CompassData.SEARCHING_FLUIDS);
        }
        if (isSearchingItemEntities(stack) && !TCConfig.enableItemEntitiesSearch.get()) {
            writeCompassData(stack, CompassData.SEARCHING_DROPPED_ITEMS);
        }
        if (isSearchingVillagers(stack) && !TCConfig.enableVillagersSearch.get()) {
            writeCompassData(stack, CompassData.SEARCHING_VILLAGERS);
        }
        if (isSearchingDrops(stack) && !TCConfig.enableDropSearch.get()) {
            writeCompassData(stack, CompassData.SEARCHING_ENTITIES_DROP);
        }
        if (isSearchingMobs(stack) && !TCConfig.enableMobSearch.get()) {
            writeCompassData(stack, CompassData.SEARCHING_MOBS);
        }
        if (isSearchingContainers(stack) && !TCConfig.enableContainerSearch.get()) {
            writeCompassData(stack, CompassData.SEARCHING_CONTAINERS);
        }
        if (isSearchingMobsInv(stack) && !TCConfig.enableMobsInventorySearch.get()) {
            writeCompassData(stack, CompassData.SEARCHING_MOBS_INV);
        }
        if (isSearchingSpawners(stack) && !TCConfig.enableSpawnerSearch.get()) {
            writeCompassData(stack, CompassData.SEARCHING_SPAWNERS);
        }
        if (!isLazyModeOn(stack) && TCConfig.forcedLazySearchMode.get()) {
            writeCompassData(stack, CompassData.LAZY_MODE);
        }
        if ((hudMode(stack) || hudModeRequiresHeld(stack)) && !TCConfig.enableHud.get()) {
            writeCompassData(stack, hudMode(stack) ? CompassData.HUD_SHOW : CompassData.HUD_SHOW_HAND);
        }
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level pLevel, @NotNull Entity pEntity, int pItemSlot, boolean pIsSelected) {
        if (pEntity instanceof Player player) {
            BlockPos targetBlockPos = getFoundPosition(stack);
            boolean lazyCheck = true;
            CompassContainer compassContainer = CompassContainer.container(stack);
            if (compassContainer.isEmpty() && targetBlockPos == null) {
                if (this.positionRelativeToTarget(stack) != 6) {
                    this.setPositionRelativeToTarget(stack, 6);
                }
                return;
            }
            if (player.tickCount % 20 == 0) {
                checkPossibleResourceRecovery(player, stack, compassContainer);
            }
            if (!canSearch(player)) {
                if (this.positionRelativeToTarget(stack) != 5) {
                    this.setPositionRelativeToTarget(stack, 5);
                }
                return;
            }
            int lazyBonus = 0;
            if (isLazyModeOn(stack)) {
                if (targetBlockPos != null) {
                    if (Math.sqrt(targetBlockPos.distSqr(player.blockPosition())) < 15) {
                        lazyCheck = false;
                    }
                    lazyBonus = TCConfig.searchRate.get() * 2 * (1500 / (int) (Math.sqrt(targetBlockPos.distSqr(player.blockPosition()))));
                }
            }
            if (lazyCheck && (player.tickCount % (TCConfig.searchRate.get() + lazyBonus) == 0 && !pLevel.isClientSide && (!isPaused(stack)) || wideSearchSignal(stack))) {
                updateBlockPosition(pLevel, pEntity, stack, wideSearchSignal(stack));
                if (wideSearchSignal(stack)) {
                    this.writeCompassData(stack, CompassData.WIDE_SEARCH_SIGNAL);
                }
            }
            if (TCConfig.xpDrain.get() && player.tickCount % TCConfig.xpDrainRate.get() == 0 && !isPaused(stack)) {
                if (getFoundPosition(stack) != null) {
                    xpDrain(player);
                }
            }

            if (player.tickCount % 40 == 0) {
                if (targetBlockPos == null) {
                    if (this.positionRelativeToTarget(stack) != 4) {
                        this.setPositionRelativeToTarget(stack, 4);
                    }
                    return;
                }
                BlockPos userPos = player.getOnPos();
                double livingY = userPos.getY();
                double targetY = Objects.requireNonNull(targetBlockPos).getY();
                double point1 = targetY - livingY;
                if (showLabels(stack) && this.positionRelativeToTarget(stack) != -2) {
                    this.setPositionRelativeToTarget(stack, -2);
                }
                if (showLabels(stack)) {
                    return;
                }
                if (targetY == livingY || (point1 <= 2 && point1 >= 1) && this.positionRelativeToTarget(stack) != 3) {
                    this.setPositionRelativeToTarget(stack, 3);
                } else if (targetY > livingY && !(point1 <= 2 && point1 >= 1) && this.positionRelativeToTarget(stack) != 2) {
                    this.setPositionRelativeToTarget(stack, 2);
                } else if (targetY < livingY && this.positionRelativeToTarget(stack) != 1) {
                    this.setPositionRelativeToTarget(stack, 1);
                }
            }
        }
    }

    private void checkPossibleResourceRecovery(Player player, ItemStack stack, CompassContainer compassContainer) {
        List<Item> list = compassContainer.getList();
        int k = 0;

        for (int i = 0; i < 9; i++) {
            Item item = compassContainer.getItem(i).getItem();
            if ((i == 4) && item.equals(Items.COMPASS)) {
                k++;
            }
            if ((i == 7) && item.equals(Items.LODESTONE)) {
                k++;
            } else if (i != 7 && i != 4 && item.equals(Items.IRON_INGOT)) {
                k++;
            }
        }
        if (k == 9) {
            for (Item item : list) {
                ItemEntity itementity = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), item.getDefaultInstance());
                player.level().addFreshEntity(itementity);
            }
            stack.shrink(1);
            if (player.containerMenu instanceof CompassMenu menu) {
                player.closeContainer();
            }
        }
    }

    public boolean isSearchingVillagers(ItemStack stack) {
        return getCompassData(stack, CompassData.SEARCHING_VILLAGERS);
    }

    public boolean isSearchingItemEntities(ItemStack stack) {
        return getCompassData(stack, CompassData.SEARCHING_DROPPED_ITEMS);
    }

    public boolean isSearchingVillagersGoods(ItemStack stack) {
        return getCompassData(stack, CompassData.SEARCHING_VILLAGERS_GOODS);
    }

    public boolean isSearchingVillagersCost(ItemStack stack) {
        return getCompassData(stack, CompassData.SEARCHING_VILLAGERS_COST);
    }

    public boolean isSearchingMobsInv(ItemStack stack) {
        return getCompassData(stack, CompassData.SEARCHING_MOBS_INV);
    }

    public boolean isSearchingMinecartsInv(ItemStack stack) {
        return getCompassData(stack, CompassData.SEARCHING_MINECARTS_INV);
    }

    public boolean isSearchingPlayersInv(ItemStack stack) {
        return getCompassData(stack, CompassData.SEARCHING_PLAYERS_INV);
    }

    public boolean isSearchingFluids(ItemStack stack) {
        return getCompassData(stack, CompassData.SEARCHING_FLUIDS);
    }

    public boolean isSearchingSpawners(ItemStack stack) {
        return getCompassData(stack, CompassData.SEARCHING_SPAWNERS);
    }

    public boolean isSearchingEntitiesInv(ItemStack stack) {
        return getCompassData(stack, CompassData.SEARCHING_ENTITIES_INV);
    }

    public boolean isSearchingContainers(ItemStack stack) {
        return getCompassData(stack, CompassData.SEARCHING_CONTAINERS);
    }

    public boolean isSearchingDrops(ItemStack stack) {
        return getCompassData(stack, CompassData.SEARCHING_ENTITIES_DROP);
    }

    public boolean isSearchingBlocks(ItemStack stack) {
        return getCompassData(stack, CompassData.SEARCHING_BLOCKS);
    }

    public boolean isSearchingMobs(ItemStack stack) {
        return getCompassData(stack, CompassData.SEARCHING_MOBS);
    }

    public boolean isLazyModeOn(ItemStack stack) {
        return getCompassData(stack, CompassData.LAZY_MODE);
    }

    public boolean sound(ItemStack stack) {
        return getCompassData(stack, CompassData.SOUND);
    }

    public boolean isPaused(ItemStack stack) {
        return getCompassData(stack, CompassData.PAUSED);
    }

    public boolean wideSearchSignal(ItemStack stack) {
        return getCompassData(stack, CompassData.WIDE_SEARCH_SIGNAL);
    }

    public boolean showLabels(ItemStack stack) {
        return getCompassData(stack, CompassData.SHOW_LABELS);
    }

    public boolean priorityMode(ItemStack stack) {
        return !getCompassData(stack, CompassData.PRIORITY_MODE);
    }

    public boolean hudMode(ItemStack stack) {
        return getCompassData(stack, CompassData.HUD_SHOW);
    }

    public boolean hudModeRequiresHeld(ItemStack stack) {
        return getCompassData(stack, CompassData.HUD_SHOW_HAND);
    }

    public boolean getCompassData(ItemStack stack, CompassData mode) {
        int id = mode.getID();
        ArrayList<Integer> modeValues = integerArrayList(stack, "compass_data");
        return modeValues.contains(id);
    }

    public void writeCompassData(ItemStack stack, CompassData mode) {
        int id = mode.getID();
        putIntArray(stack, id, "compass_data");
    }

    public void markFavoriteItem(ItemStack stack, boolean mode) {
        stack.getOrCreateTag().putBoolean("favorite", mode);
    }

    public boolean hasFavoriteItem(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("favorite");
    }

    public void addFavoriteSlot(ItemStack stack, int slot) {
        putIntArray(stack, slot, "favoriteSlots");
    }

    public ArrayList<Integer> favoriteSlots(ItemStack stack) {
        return integerArrayList(stack, "favoriteSlots");
    }

    public ArrayList<Integer> selectedModes(ItemStack stack) {
        return integerArrayList(stack, "compass_data");
    }

    public void setConfigMode(ItemStack stack, boolean mode) {
        stack.getOrCreateTag().putBoolean("configMode", mode);
    }

    public boolean configMode(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("configMode");
    }

    public void writeFoundTarget(ItemStack stack, String target) {
        stack.getOrCreateTag().putString("targetData", target);
    }

    public String foundTarget(ItemStack stack) {
        return stack.getOrCreateTag().getString("targetData");
    }

    public void setPositionRelativeToTarget(ItemStack stack, int pos) {
        stack.getOrCreateTag().putInt("pos_to_target", pos);
    }

    public int positionRelativeToTarget(ItemStack stack) {
        return stack.getOrCreateTag().getInt("pos_to_target");
    }


    public int blockSearchRadius(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("block_search_radius", Tag.TAG_INT)) {
            return tag.getInt("block_search_radius");
        } else {
            return (int) (TCConfig.blockSearchRadius.get() * 0.5F);
        }
    }

    public void setBlockSearchRadius(ItemStack stack, int radius) {
        stack.getOrCreateTag().putInt("block_search_radius", radius);
    }

    public int containerSearchRadius(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("container_search_radius", Tag.TAG_INT)) {
            return tag.getInt("container_search_radius");
        } else {
            return (int) (TCConfig.containerSearchRadius.get() * 0.5F);
        }
    }

    public void setContainerSearchRadius(ItemStack stack, int radius) {
        stack.getOrCreateTag().putInt("container_search_radius", radius);
    }

    public int entitySearchRadius(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("entity_search_radius", Tag.TAG_INT)) {
            return tag.getInt("entity_search_radius");
        } else {
            return (int) (TCConfig.entitySearchRadius.get() * 0.5F);
        }
    }

    public void setEntitySearchRadius(ItemStack stack, int radius) {
        stack.getOrCreateTag().putInt("entity_search_radius", radius);
    }

    public int wideSearchRadius(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("wide_search_radius", Tag.TAG_INT)) {
            return tag.getInt("wide_search_radius");
        } else {
            return (int) (TCConfig.wideSearchRadius.get() * 0.5F);
        }
    }

    public void setWideSearchRadius(ItemStack stack, int radius) {
        stack.getOrCreateTag().putInt("wide_search_radius", radius);
    }

    public void setHudWithChatMode(ItemStack stack, boolean mode) {
        stack.getOrCreateTag().putBoolean("hud_chat", mode);
    }

    public boolean hudWithChatMode(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("hud_chat");
    }

    public void setHudPos(ItemStack stack, int x, int y) {
        int[] data = new int[4];
        data[0] = x;
        data[1] = y;
        if (stack.getItem() instanceof TravelersCompassItem) {
            data[2] = getHudAlign(stack);
            data[3] = getHudType(stack);
        }
        stack.getOrCreateTag().putIntArray("hud_data", data);
    }

    public void setHudAlign(ItemStack stack, int type) {
        int[] data = new int[4];
        data[2] = type;
        data[0] = getXHudPos(stack);
        data[1] = getYHudPos(stack);
        data[3] = getHudType(stack);
        stack.getOrCreateTag().putIntArray("hud_data", data);
    }

    public void setHudType(ItemStack stack, int type) {
        int[] data = new int[4];
        data[2] = getHudAlign(stack);
        data[0] = getXHudPos(stack);
        data[1] = getYHudPos(stack);
        data[3] = type;
        stack.getOrCreateTag().putIntArray("hud_data", data);
    }

    public int getHudAlign(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("hud_data", Tag.TAG_INT_ARRAY)) {
            return stack.getOrCreateTag().getIntArray("hud_data")[2];
        }
        return 0;
    }

    public int getHudType(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("hud_data", Tag.TAG_INT_ARRAY)) {
            return stack.getOrCreateTag().getIntArray("hud_data")[3];
        }
        return 0;
    }

    public int getXHudPos(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("hud_data", Tag.TAG_INT_ARRAY)) {
            return stack.getOrCreateTag().getIntArray("hud_data")[0];
        }
        return 0;
    }

    public int getYHudPos(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("hud_data", Tag.TAG_INT_ARRAY)) {
            return stack.getOrCreateTag().getIntArray("hud_data")[1];
        }
        return 0;
    }

    public void putIntArray(ItemStack stack, int slot, String tag) {
        int[] existingSlots = stack.getOrCreateTag().getIntArray(tag);
        ArrayList<Integer> slotsList = new ArrayList<>();

        for (int existingSlot : existingSlots) {
            slotsList.add(existingSlot);
        }
        if (slotsList.contains(slot)) {
            slotsList.remove(Integer.valueOf(slot));
        } else {
            slotsList.add(slot);
        }

        int[] newSlots = new int[slotsList.size()];
        for (int i = 0; i < slotsList.size(); i++) {
            newSlots[i] = slotsList.get(i);
        }

        stack.getOrCreateTag().putIntArray(tag, newSlots);
    }

    public ArrayList<Integer> integerArrayList(ItemStack stack, String tag) {
        int[] slotsArray = stack.getOrCreateTag().getIntArray(tag);
        ArrayList<Integer> slotsList = new ArrayList<>();
        for (int slot : slotsArray) {
            slotsList.add(slot);
        }
        return slotsList;
    }

}
