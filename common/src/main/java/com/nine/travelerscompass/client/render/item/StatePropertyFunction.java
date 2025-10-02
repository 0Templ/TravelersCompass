package com.nine.travelerscompass.client.render.item;

import com.nine.travelerscompass.common.data.CompassProperties;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class StatePropertyFunction implements ClampedItemPropertyFunction {

    @Override
    public float unclampedCall(ItemStack stack, ClientLevel level, LivingEntity entity, int seed) {
        return 0;
    }

    @Deprecated
    public float call(ItemStack stack, ClientLevel level, LivingEntity entity, int i) {
        return CompassProperties.COMPASS_STATE.get(stack);
    }
}
