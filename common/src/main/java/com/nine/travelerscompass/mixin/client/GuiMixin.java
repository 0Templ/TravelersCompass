package com.nine.travelerscompass.mixin.client;

import com.nine.travelerscompass.client.render.HeightMarkerRenderer;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Gui.class)
public class GuiMixin {

    @Inject(method = "renderSlot",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;II)V",
                    shift = At.Shift.AFTER))
    private void travelerscompass$renderSlot(GuiGraphics guiGraphics, int x, int y, DeltaTracker deltaTracker, Player player, ItemStack stack, int seed, CallbackInfo ci) {
        HeightMarkerRenderer.render(guiGraphics, stack, x, y);
    }

}
