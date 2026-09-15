package com.nine.travelerscompass.mixin.client;

import com.nine.travelerscompass.client.render.item.CompassRenderContext;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenCatalogMixin {

	@Inject(method = "extractSlot", at = @At("HEAD"))
	private void travelerscompass$beginSlotRender(
			GuiGraphicsExtractor graphics, Slot slot, int mouseX, int mouseY, CallbackInfo ci
	) {
		CompassRenderContext.beginSlot(this, slot);
	}

	@Inject(method = "extractSlot", at = @At("RETURN"))
	private void travelerscompass$endSlotRender(
			GuiGraphicsExtractor graphics, Slot slot, int mouseX, int mouseY, CallbackInfo ci
	) {
		CompassRenderContext.endSlot(this);
	}
}
