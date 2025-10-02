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
            target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderSlotContents(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/inventory/Slot;Ljava/lang/String;)V",
            shift = At.Shift.AFTER), remap = false)
    public void travelerscompass$renderSlot(GuiGraphics graphics, Slot slot, CallbackInfo info)
    {
        HeightMarkerRenderer.render(graphics, slot.getItem(), slot.x, slot.y);
    }
}
