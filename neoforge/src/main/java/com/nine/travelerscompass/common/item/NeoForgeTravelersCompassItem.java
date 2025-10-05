package com.nine.travelerscompass.common.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class NeoForgeTravelersCompassItem extends TravelersCompassItem {

    public NeoForgeTravelersCompassItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }
}
