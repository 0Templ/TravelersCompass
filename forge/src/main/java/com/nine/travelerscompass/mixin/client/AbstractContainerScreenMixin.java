package com.nine.travelerscompass.mixin.client;


import com.nine.travelerscompass.client.render.HeightMarkerRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {
	
	@Inject(method = "renderSlot",
			at = @At(value = "INVOKE",
					target = "Lnet/minecraft/client/gui/GuiGraphics;renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
					shift = At.Shift.AFTER))
	public void travelerscompass$renderSlot(GuiGraphics graphics, Slot slot, CallbackInfo info) {
		HeightMarkerRenderer.render(graphics, slot.getItem(), slot.x, slot.y);
	}
	
}
