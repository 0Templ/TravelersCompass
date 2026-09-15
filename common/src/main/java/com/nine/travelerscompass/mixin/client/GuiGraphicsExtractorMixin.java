package com.nine.travelerscompass.mixin.client;

import com.nine.travelerscompass.client.render.item.CompassRenderContext;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphicsExtractor.class)
public class GuiGraphicsExtractorMixin {

	@Inject(
			method = "item(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;III)V",
			at = @At("HEAD")
	)
	private void travelerscompass$beginCatalogueItemRender(
			LivingEntity entity, Level level, ItemStack stack, int x, int y, int seed, CallbackInfo ci
	) {
		CompassRenderContext.begin((GuiGraphicsExtractor) (Object) this, stack, x, y);
	}

	@Inject(
			method = "item(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;III)V",
			at = @At("RETURN")
	)
	private void travelerscompass$endCatalogueItemRender(
			LivingEntity entity, Level level, ItemStack stack, int x, int y, int seed, CallbackInfo ci
	) {
		CompassRenderContext.end(stack);
	}
}
