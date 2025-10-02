package com.nine.travelerscompass.compat;

import com.nine.travelerscompass.client.screen.CompassScreen;
import com.nine.travelerscompass.network.packet.c2s.GhostTargetPacket;
import com.nine.travelerscompass.platform.Platform;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class BaseGhostTargetHandler {

    protected static final int COMPASS_SLOTS_COUNT = 9;
    protected static final int SLOT_SIZE = 16;

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

    protected void applyGhostStack(Slot slot, ItemStack stack, NEI nei) {
        slot.set(stack);
        Platform.PLATFORM_NETWORK.sendToServer(new GhostTargetPacket(slot.index, stack, nei));
    }

    protected Optional<Slot> slotUnderMouse(CompassScreen screen, int mouseX, int mouseY) {
        return screen.getMenu().slots.stream()
                .filter(this::isCompassSlot)
                .filter(slot -> slotArea(screen, slot).contains(mouseX, mouseY))
                .findFirst();
    }
}
