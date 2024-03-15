package com.nine.travelerscompass.common.container.menu;

import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import com.nine.travelerscompass.init.ItemRegistry;
import com.nine.travelerscompass.init.MenuRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class CompassMenu extends AbstractContainerMenu {
    public static final int CONTAINER_SIZE = 9;

    public CompassMenu(int id, Inventory playerInventory, CompassContainer container) {
        super(MenuRegistry.COMPASS_MENU.get(), id);
        for (int i = 0; i < CONTAINER_SIZE/3; i++) {
            addSlot(new Slot(container, i, 62 + (i * 18), 50 - 18*2) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return !(stack.getItem() instanceof TravelersCompassItem);
                }
                @Override
                public boolean mayPickup(Player CompassMenuIn) {
                    return false;
                }
            });
        }
        for (int i = 0; i < CONTAINER_SIZE/3; i++) {
            addSlot(new Slot(container, i+3, 62 + (i * 18), 50 - 18) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return !(stack.getItem() instanceof TravelersCompassItem);
                }
                @Override
                public boolean mayPickup(Player player) {
                    return false;
                }
            });
        }
        for (int i = 0; i < CONTAINER_SIZE/3; i++) {
            addSlot(new Slot(container, i+6, 62 + (i * 18), 50) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return !(stack.getItem() instanceof TravelersCompassItem);
                }
                @Override
                public boolean mayPickup(Player player) {
                    return false;
                }
            });
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, i * 18 + 90){
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return !(stack.getItem() instanceof TravelersCompassItem);
                    }
                });
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(playerInventory, i, 8 + i * 18, 148){
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return !(stack.getItem() instanceof TravelersCompassItem);
                }

                @Override
                public boolean mayPickup(Player player) {
                    return !(this.getItem().getItem() instanceof TravelersCompassItem);
                }
            });
        }
    }


    @Nonnull
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }


    @Override
    public void clicked(int slot, int dragType, ClickType clickType, Player player) {
        if(slot  >= 0 && slot < CONTAINER_SIZE && !(getSlot(slot).getItem().getItem() instanceof TravelersCompassItem)){
            if (player.getMainHandItem().getItem() instanceof TravelersCompassItem travelersCompassItem && clickType == ClickType.QUICK_MOVE){
                player.playSound(SoundEvents.UI_BUTTON_CLICK.get());
                travelersCompassItem.addFavoriteSlot(player.getMainHandItem(),slot);
            }
            else {
                if (this.getCarried().isEmpty() || this.getCarried().is(ItemRegistry.TRAVELERS_COMPASS.get()))
                    slots.get(slot).set(ItemStack.EMPTY);
                else {
                    ItemStack stack = this.getCarried().copy();
                    stack.setCount(1);
                    slots.get(slot).set(stack);
                }
                slots.get(slot).setChanged();
                return;
            }
        }
        super.clicked(slot, dragType, clickType, player);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;

    }
}
