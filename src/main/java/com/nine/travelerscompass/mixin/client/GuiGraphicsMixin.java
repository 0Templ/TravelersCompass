package com.nine.travelerscompass.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.nine.travelerscompass.TravelersCompass;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {

    @Inject(method = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V", at = @At("TAIL"))
    private void travelerscompass$renderItem(@Nullable LivingEntity living, @Nullable Level level, ItemStack stack, int x, int y, int p_283260_, int p_281995_, CallbackInfo ci) {
        if (!stack.isEmpty() && stack.getItem() instanceof TravelersCompassItem travelersCompassItem) {
            GuiGraphics guiGraphics = (GuiGraphics) (Object) this;
            boolean favorite = travelersCompassItem.hasFavoriteItem(stack);
            if (travelersCompassItem.showLabels(stack)) {
                return;
            }
            switch (travelersCompassItem.positionRelativeToTarget(stack)) {
                case 0 -> {
                }
                case 1 -> {
                    renderLabel(guiGraphics, x, y, "compass_up_layer", favorite);
                    break;
                }
                case 2 -> {
                    renderLabel(guiGraphics, x, y, "compass_down_layer", favorite);
                    break;
                }
                case 3 -> {
                    renderLabel(guiGraphics, x, y, "compass_default_layer", favorite);
                    break;
                }
            }

        }
    }

    @Unique
    private static void renderLabel(GuiGraphics context, int x, int y, String name, boolean favorite) {
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1);
        context.blit(new ResourceLocation(TravelersCompass.MODID, "textures/item/travelerscompass/" + name + (favorite ? "_favorite" : "") + ".png"), x, y, 0, 0, 16, 16, 16, 16);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

}
