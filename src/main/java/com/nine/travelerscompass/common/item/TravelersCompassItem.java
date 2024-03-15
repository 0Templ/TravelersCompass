package com.nine.travelerscompass.common.item;

import com.nine.travelerscompass.TCConfig;
import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.common.container.menu.CompassMenu;
import com.nine.travelerscompass.common.utils.ConfigUtils;
import com.nine.travelerscompass.common.utils.PositionUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
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
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeSpawnEggItem;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Objects;

public class TravelersCompassItem extends Item {

    public TravelersCompassItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }
        if (player.isShiftKeyDown()){
            CompassContainer compassContainer = CompassContainer.container(stack);
            Vec3 lookVector = player.getLookAngle();
            Vec3 eyePosition = player.getEyePosition(1.0F);
            Vec3 traceEnd = eyePosition.add(lookVector.x * 5.0D, lookVector.y * 5.0D, lookVector.z * 5.0D);
            HitResult hitResult = level.clip(new ClipContext(eyePosition, traceEnd, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
            BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();
            if (hitResult.getType() == HitResult.Type.BLOCK && !level.isClientSide) {
                ItemStack clickedStack = level.getBlockState(blockPos).getBlock().asItem().getDefaultInstance();
                if (!compassContainer.hasAny(clickedStack) && ConfigUtils.isAllowedToSearch(clickedStack)){
                    compassContainer.setItem(compassContainer.getFirstEmptySlot(),clickedStack);
                    level.playSound((Player)null, player.getOnPos(), SoundEvents.LODESTONE_COMPASS_LOCK, SoundSource.PLAYERS, 1.0F, 1.0F);
                    return InteractionResultHolder.success(player.getItemInHand(hand));
                }
            }
        }
        if (!level.isClientSide) {
            player.openMenu(new MenuProvider() {
                @Override
                public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
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
            if (player.isShiftKeyDown() && living instanceof Mob mob && ForgeSpawnEggItem.fromEntityType(mob.getType()) != null) {
                ItemStack eggStack = Objects.requireNonNull(ForgeSpawnEggItem.fromEntityType(mob.getType())).getDefaultInstance();
                if (!compassContainer.hasAny(eggStack) && ConfigUtils.isAllowedToSearch(eggStack)) {
                    compassContainer.setItem(compassContainer.getFirstEmptySlot(), eggStack);
                    player.level().playSound((Player) null, player.getOnPos(), SoundEvents.LODESTONE_COMPASS_LOCK, SoundSource.PLAYERS, 1.0F, 1.0F);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }
    public void updateBlockPosition(Level pLevel, Entity entity, ItemStack stack) {
        CompassContainer compassContainer = CompassContainer.container(stack);
        if (stack.getItem()  instanceof TravelersCompassItem compassItem) {
            if (compassContainer.isEmpty()) {
                compassItem.addBlockPositionTags(null, stack.getOrCreateTag());
                return;
            }
            if (entity instanceof Player player) {

                if (!canSearch(player)) {
                    return;
                }
                compassItem.addBlockPositionTags(PositionUtils.getNearestPos(pLevel, entity, compassItem, compassContainer, stack), stack.getOrCreateTag());

            }
        }
    }
    @Nullable
    public static BlockPos getFoundPosition(@NotNull ItemStack stack) {
        if (stack.getOrCreateTag().getBoolean("notFound")){
            return null;
        }
        return NbtUtils.readBlockPos(stack.getOrCreateTagElement("foundPos"));
    }
    public void addBlockPositionTags(BlockPos blockPos, CompoundTag compoundTag) {
        if (blockPos == null){
            if (!compoundTag.getBoolean("notFound")) {
                compoundTag.putBoolean("notFound", true);
            }
            return;
        }
        if (compoundTag.getBoolean("notFound")){
            compoundTag.putBoolean("notFound", false);
        }
        compoundTag.put("foundPos", NbtUtils.writeBlockPos(blockPos));
    }

    public static void xpDrain(Player player){
        if (!player.isCreative() && !player.isSpectator()){
            int currentExp = player.totalExperience;
            if (currentExp - TCConfig.xpCost.get() > 0 ){
                player.giveExperiencePoints(-TCConfig.xpCost.get());
            }
            else {
                player.experienceLevel = 0;
                player.experienceProgress = 0;
                player.totalExperience = 0;
            }
        }
    }
    public static boolean canSearch(Player player){
        if (!TCConfig.xpDrain.get()){
            return true;
        }
        if (!player.isCreative() && !player.isSpectator()) {
            int currentExp = player.totalExperience;
            return currentExp > 0;
        }
        return true;
    }

    public void checkForbiddenItems(CompassContainer compassContainer){
        for (int index = 0; index < 9; index++){
            if (!ConfigUtils.isAllowedToSearch(compassContainer.getItem(index))){
                compassContainer.removeItem(index,1);
            }
        }
    }
    @Override
    public void inventoryTick(ItemStack stack, Level pLevel, Entity pEntity, int pItemSlot, boolean pIsSelected) {
        if (pEntity instanceof Player player) {
            CompassContainer compassContainer = CompassContainer.container(stack);
            if (compassContainer.isEmpty()){
                if (this.positionRelativeToTarget(stack) != 6) {
                    this.setPositionRelativeToTarget(stack, 6);
                }
                return;
            }
            if (!canSearch(player)) {
                if (this.positionRelativeToTarget(stack) != 5) {
                    this.setPositionRelativeToTarget(stack, 5);
                }
                return;
            }
            if (player.tickCount % TCConfig.searchRate.get() == 0 && !pLevel.isClientSide) {
                updateBlockPosition(pLevel, pEntity, stack);
            }
            if (TCConfig.xpDrain.get() && player.tickCount % TCConfig.xpDrainRate.get() == 0) {
                if (getFoundPosition(stack) != null) {
                    xpDrain(player);
                }
            }

            if (player.tickCount % 40 == 0) {
                checkForbiddenItems(compassContainer);
                BlockPos targetBlockPos = getFoundPosition(stack);
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
    public boolean isSearchingVillagers(ItemStack stack) {
        return getCompassData(stack,CompassMode.SEARCHING_VILLAGERS);
    }
    public boolean isSearchingItemEntities(ItemStack stack) {
        return getCompassData(stack,CompassMode.SEARCHING_DROPPED_ITEMS);
    }
    public boolean isSearchingFluids(ItemStack stack) {
        return getCompassData(stack,CompassMode.SEARCHING_FLUIDS);
    }
    public boolean isSearchingSpawners(ItemStack stack) {
        return getCompassData(stack,CompassMode.SEARCHING_SPAWNERS);
    }
    public boolean isSearchingMobsInv(ItemStack stack) {
        return getCompassData(stack,CompassMode.SEARCHING_MOBS_INV);
    }
    public boolean isSearchingContainers(ItemStack stack) {
        return getCompassData(stack,CompassMode.SEARCHING_CONTAINERS);
    }
    public boolean isSearchingDrops(ItemStack stack) {
        return getCompassData(stack,CompassMode.SEARCHING_MOB_DROP);
    }
    public boolean isSearchingBlocks(ItemStack stack) {
        return getCompassData(stack,CompassMode.SEARCHING_BLOCKS);
    }
    public boolean isSearchingMobs(ItemStack stack) {
        return getCompassData(stack,CompassMode.SEARCHING_MOBS);
    }
    public boolean isPaused(ItemStack stack) {
        return getCompassData(stack,CompassMode.PAUSED);
    }
    public boolean getCompassData(ItemStack stack, CompassMode mode) {
        int id = mode.getID();
        ArrayList<Integer> modeValues = integerArrayList(stack,"compass_data");
        return modeValues.contains(id);
    }
    public void writeCompassData(ItemStack stack, CompassMode mode) {
        int id = mode.getID();
        putIntArray(stack,id,"compass_data");
    }
    public void markFavoriteItem(ItemStack stack, boolean mode) {stack.getOrCreateTag().putBoolean("favorite", mode);}
    public boolean hasFavoriteItem(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("favorite");
    }
    public void addFavoriteSlot(ItemStack stack, int slot) {
        putIntArray(stack,slot,"favoriteSlots");
    }
    public ArrayList<Integer> favoriteSlots(ItemStack stack) {
        return integerArrayList(stack,"favoriteSlots");
    }
    public ArrayList<Integer> selectedModes(ItemStack stack) {
        return integerArrayList(stack,"compass_data");
    }
    public void setConfigMode(ItemStack stack, boolean mode) {stack.getOrCreateTag().putBoolean("configMode", mode);}
    public boolean configMode(ItemStack stack) {return stack.getOrCreateTag().getBoolean("configMode");}
    public void setPositionRelativeToTarget(ItemStack stack, int pos) {stack.getOrCreateTag().putInt("pos_to_target", pos);}
    public int positionRelativeToTarget(ItemStack stack) {return stack.getOrCreateTag().getInt("pos_to_target");}

    public void putIntArray(ItemStack stack, int slot, String tag) {
        int[] existingSlots = stack.getOrCreateTag().getIntArray(tag);
        ArrayList<Integer> slotsList = new ArrayList<>();

        for (int existingSlot : existingSlots) {
            slotsList.add(existingSlot);
        }
        if (slotsList.contains(slot)) {
            slotsList.remove(Integer.valueOf(slot));
        }
        else {
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
