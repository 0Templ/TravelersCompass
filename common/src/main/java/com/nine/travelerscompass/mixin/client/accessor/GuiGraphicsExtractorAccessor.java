package com.nine.travelerscompass.mixin.client.accessor;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiGraphicsExtractor.class)
public interface GuiGraphicsExtractorAccessor {

	@Accessor("mouseX")
	int travelerscompass$getMouseX();

	@Accessor("mouseY")
	int travelerscompass$getMouseY();
}
