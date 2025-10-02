package com.nine.travelerscompass.common.item;

import net.minecraft.world.item.ItemStack;

public class ForgeTravelersCompassItem extends TravelersCompassItem{

    public ForgeTravelersCompassItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }
}
