package com.nine.travelerscompass.compat.rei;

import com.nine.travelerscompass.TCConfig;
import com.nine.travelerscompass.client.screen.CompassScreen;
import com.nine.travelerscompass.common.network.NetworkHandler;
import com.nine.travelerscompass.common.network.packet.GhostTargetPacket;
import me.shedaniel.rei.api.client.gui.drag.DraggableStack;
import me.shedaniel.rei.api.client.gui.drag.DraggableStackVisitor;
import me.shedaniel.rei.api.client.gui.drag.DraggedAcceptorResult;
import me.shedaniel.rei.api.client.gui.drag.DraggingContext;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;


public class ReiGhostTargetHandler implements DraggableStackVisitor<CompassScreen> {
    @Override
    public <R extends Screen> boolean isHandingScreen(R r) {
        return r instanceof CompassScreen;
    }
    @Override
    public DraggedAcceptorResult acceptDraggedStack(DraggingContext<CompassScreen> context, DraggableStack stack) {
        if (!TCConfig.REICompatibility.get()){
            return DraggedAcceptorResult.PASS;
        }
        CompassScreen screen = context.getScreen();
        var entryStack = stack.getStack();
        for (Slot slot : screen.getMenu().slots) {
            if (slot.index < 9) {
                if (entryStack.getType() == VanillaEntryTypes.ITEM && context.getCurrentPosition()!=null) {
                    ItemStack stack2 = entryStack.castValue();
                    Rect2i area = new Rect2i(screen.getGuiLeft() + slot.x, screen.getGuiTop() + slot.y, 16, 16);
                    if (area.contains(context.getCurrentPosition().x,context.getCurrentPosition().y)){
                        screen.getMenu().slots.get(slot.index).set(stack2);
                        NetworkHandler.CHANNEL.sendToServer(new GhostTargetPacket(slot.index, stack2));
                    }
                }
            }
        }
        return DraggedAcceptorResult.ACCEPTED;
    }

    @Override
    public double getPriority() {
        return 0.0;
    }


}
