package com.nine.travelerscompass.compat;

import com.nine.travelerscompass.client.screen.CompassScreen;
import com.nine.travelerscompass.client.utils.GhostStack;
import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.network.packet.c2s.GhostTargetPacket;
import com.nine.travelerscompass.platform.Platform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public abstract class BaseGhostTargetHandler {
	
	protected static final int COMPASS_SLOTS_COUNT = 9;
	protected static final int SLOT_SIZE = 16;
	
	protected void setGhostStack(CompassScreen screen, ItemStack stack) {
		if (stack == null || stack.isEmpty()) {
			screen.ghostStack = GhostStack.EMPTY;
		} else {
			screen.ghostStack = new GhostStack(stack.copy(), nei());
		}
	}
	
	public abstract NEI nei();
	
	protected boolean isCompassSlot(Slot slot) {
		return slot.index < COMPASS_SLOTS_COUNT;
	}
	
	protected Rect2i slotArea(CompassScreen screen, Slot slot) {
		return new Rect2i(
				screen.leftPos() + slot.x,
				screen.topPos() + slot.y,
				SLOT_SIZE, SLOT_SIZE
		);
	}
	
	protected void applyGhostStack(Slot slot, ItemStack stack) {
		Player player = Minecraft.getInstance().player;
		if (slot.container instanceof CompassContainer container && nei().allowed()) {
			container.setItem(slot.index, stack, player);
			Platform.PLATFORM_NETWORK.sendToServer(new GhostTargetPacket(slot.index, stack, nei()));
		}
	}
	
	protected Optional<Slot> slotUnderMouse(CompassScreen screen, int mouseX, int mouseY) {
		return screen.getMenu().slots.stream()
				.filter(this::isCompassSlot)
				.filter(slot -> slotArea(screen, slot).contains(mouseX, mouseY))
				.findFirst();
	}
	
}
