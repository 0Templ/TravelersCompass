package com.nine.travelerscompass.mixin.client;

import com.nine.travelerscompass.client.hud.HudRenderer;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixinForge {
	
	@Inject(method = "render", at = @At("TAIL"), remap = false)
	private void travelerscompass$beforeDisableDepth(GuiGraphics guiGraphics, DeltaTracker delta, CallbackInfo ci) {
	}
	
}
