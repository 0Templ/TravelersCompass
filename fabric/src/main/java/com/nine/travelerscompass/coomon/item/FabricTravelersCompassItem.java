package com.nine.travelerscompass.coomon.item;

import com.nine.travelerscompass.common.item.TravelersCompassItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class FabricTravelersCompassItem extends TravelersCompassItem {

    public FabricTravelersCompassItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean allowComponentsUpdateAnimation(Player player, InteractionHand hand, ItemStack oldStack, ItemStack newStack) {
        return false;
    }
}
