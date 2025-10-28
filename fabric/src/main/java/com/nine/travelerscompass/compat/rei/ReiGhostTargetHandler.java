package com.nine.travelerscompass.compat.rei;

import com.nine.travelerscompass.client.screen.CompassScreen;
import com.nine.travelerscompass.compat.BaseGhostTargetHandler;
import com.nine.travelerscompass.compat.NEI;
import com.nine.travelerscompass.config.TCConfig;
import me.shedaniel.rei.api.client.gui.drag.DraggableStack;
import me.shedaniel.rei.api.client.gui.drag.DraggableStackVisitor;
import me.shedaniel.rei.api.client.gui.drag.DraggedAcceptorResult;
import me.shedaniel.rei.api.client.gui.drag.DraggingContext;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;

public class ReiGhostTargetHandler extends BaseGhostTargetHandler implements DraggableStackVisitor<CompassScreen> {
	
	@Override
	public DraggedAcceptorResult acceptDraggedStack(DraggingContext<CompassScreen> context, DraggableStack draggableStack) {
		CompassScreen screen = context.getScreen();
		var entryStack = draggableStack.getStack();
		if (!TCConfig.REI_COMPATIBILITY.get() || entryStack.getType() != VanillaEntryTypes.ITEM || context.getCurrentPosition() == null || entryStack.isEmpty()) {
			return DraggedAcceptorResult.PASS;
		}
		ItemStack stack = entryStack.castValue();
		return slotUnderMouse(screen, context.getCurrentPosition().x, context.getCurrentPosition().y)
				.map(slot -> {
					applyGhostStack(slot, stack, NEI.REI);
					return DraggedAcceptorResult.ACCEPTED;
				})
				.orElse(DraggedAcceptorResult.PASS);
	}
	
	@Override
	public <R extends Screen> boolean isHandingScreen(R r) {
		return r instanceof CompassScreen;
	}
	
}

