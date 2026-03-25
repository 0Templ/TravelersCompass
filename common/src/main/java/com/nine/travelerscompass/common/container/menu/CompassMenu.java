package com.nine.travelerscompass.common.container.menu;

import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.common.data.CompassComponents;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import com.nine.travelerscompass.common.search.SearchManager;
import com.nine.travelerscompass.config.filter.FilterManager;
import com.nine.travelerscompass.config.filter.FilterReason;
import com.nine.travelerscompass.init.ItemRegistry;
import com.nine.travelerscompass.init.MenuRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.AirItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class CompassMenu extends AbstractContainerMenu {
	
	public static final int COMPASS_SLOTS = 9;
	
	public CompassMenu(int id, Inventory playerInventory, CompassContainer container) {
		super(MenuRegistry.COMPASS_MENU.get(), id);
		int slotIndex = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				
				addSlot(new Slot(container, slotIndex, 73 + (j * 18), 11 + i * 18) {
					
					@Override
					public boolean mayPlace(ItemStack stack) {
						return super.mayPlace(stack);
					}
					
					@Override
					public boolean isHighlightable() {
						Item item = getCarried().getItem();
						FilterReason reason = FilterManager.getFilterReason(item);
						return item instanceof AirItem || reason.isAllowed();
					}
					
					@Override
					public boolean mayPickup(Player CompassMenuIn) {
						return false;
					}
					
				});
				slotIndex++;
			}
		}
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlot(new Slot(playerInventory, slotIndex, 9 + j * 18, i * 18 + 87) {
					
					@Override
					public boolean mayPlace(ItemStack stack) {
						return !(stack.getItem() instanceof TravelersCompassItem);
					}
				});
				slotIndex++;
			}
		}
		
		for (int i = 0; i < 9; i++) {
			addSlot(new Slot(playerInventory, i, 9 + i * 18, 145) {
				
				@Override
				public boolean mayPlace(ItemStack stack) {
					return !(stack.getItem() instanceof TravelersCompassItem);
				}
				
				@Override
				public boolean mayPickup(Player player) {
					return !(this.getItem().getItem() instanceof TravelersCompassItem);
				}
			});
			slotIndex++;
		}
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		Slot slot = slots.get(index);
		ItemStack itemstack;
		if (index > 8 && slot.hasItem()) {
			ItemStack slotStack = slot.getItem();
			for (int i = 0; i < COMPASS_SLOTS; i++) {
				ItemStack stack = slots.get(i).getItem();
				if (slotStack.getItem() == stack.getItem()) {
					return ItemStack.EMPTY;
				}
				if (stack.isEmpty()) {
					itemstack = slotStack.copy();
					if (mayPlaceToSlot(player, itemstack)) {
						itemstack.setCount(1);
						CompassContainer container = (CompassContainer) slots.get(i).container;
						container.setItem(i, itemstack, player);
					}
					break;
				}
			}
			
		}
		return ItemStack.EMPTY;
	}
	
	
	@Override
	public void clicked(int slotIndex, int buttonNum, ContainerInput containerInput, Player player) {
		if (slotIndex >= 0 && slotIndex < COMPASS_SLOTS && !(getSlot(slotIndex).getItem().getItem() instanceof TravelersCompassItem)) {
			ItemStack stack = player.getMainHandItem();
			if (stack.getItem() instanceof TravelersCompassItem && containerInput.equals(ContainerInput.QUICK_MOVE)) {
				{
					List<Integer> cur = CompassComponents.PRIORITY_SLOTS.get(stack);
					boolean contains = cur.contains(slotIndex);
					List<Integer> next = new ArrayList<>(cur);
					if (contains) {
						next.remove(Integer.valueOf(slotIndex));
					} else {
						next.add(slotIndex);
					}
					if (player.level().isClientSide()) {
						player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1.0F, 1.0F);
					}
					CompassComponents.PRIORITY_SLOTS.set(stack, List.copyOf(next));
					if (!getSlot(slotIndex).getItem().is(Items.AIR)) {
						SearchManager.updateSlotPriority(
								CompassComponents.get(stack, CompassComponents.COMPASS_UUID),
								slotIndex,
								!contains
						);
					}
				}
			} else {
				ItemStack carried = this.getCarried().copy();
				if (carried.isEmpty() || carried.is(ItemRegistry.TRAVELERS_COMPASS.get())) {
					slots.get(slotIndex).set(ItemStack.EMPTY);
				} else {
					carried.setCount(1);
					if (mayPlaceToSlot(player, carried)) {
						CompassContainer container = (CompassContainer) slots.get(slotIndex).container;
						container.setItem(slotIndex, carried, player);
					}
				}
				slots.get(slotIndex).setChanged();
				return;
			}
		}
		super.clicked(slotIndex, buttonNum, containerInput, player);
	}
	
	public boolean mayPlaceToSlot(Player player, ItemStack stack) {
		for (int i = 0; i < 9; i++) {
			if (slots.get(i).getItem().is(stack.getItem())) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean stillValid(Player player) {
		return player.getMainHandItem().getItem().equals(ItemRegistry.TRAVELERS_COMPASS.get());
	}
	
	@Override
	public void removed(Player player) {
		super.removed(player);
		ItemStack stack = player.getMainHandItem();
		if (stack.getItem() instanceof TravelersCompassItem && player instanceof ServerPlayer serverPlayer) {
			SearchManager.removeWatcher(CompassComponents.COMPASS_UUID.get(stack), serverPlayer);
		}
	}
	
}
