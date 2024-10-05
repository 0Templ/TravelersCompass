package com.nine.travelerscompass.client.render.item;

import com.nine.travelerscompass.common.item.TravelersCompassItem;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class CustomHeightPropertyFunction implements ClampedItemPropertyFunction {

    @Override
    public float unclampedCall(ItemStack itemStack, @Nullable ClientLevel clientWorld, @Nullable LivingEntity livingEntity, int i) {
        if (itemStack.getItem() instanceof TravelersCompassItem travelersCompassItem) {
            return travelersCompassItem.positionRelativeToTarget(itemStack);
        }
        return 1F;
    }

    @Deprecated
    public float call(ItemStack itemStack, @Nullable ClientLevel clientWorld, @Nullable LivingEntity livingEntity, int i) {
        return Mth.clamp(this.unclampedCall(itemStack, clientWorld, livingEntity, i), -100, 100.0F);
    }
}