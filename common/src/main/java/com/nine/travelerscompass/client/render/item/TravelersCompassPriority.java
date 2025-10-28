package com.nine.travelerscompass.client.render.item;

import com.mojang.serialization.MapCodec;
import com.nine.travelerscompass.common.data.CompassProperties;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class TravelersCompassPriority implements ConditionalItemModelProperty {
	
	public static final MapCodec<TravelersCompassPriority> MAP_CODEC =
			MapCodec.unit(new TravelersCompassPriority());
	
	@Override
	public boolean get(ItemStack stack, ClientLevel level, LivingEntity entity, int seed, ItemDisplayContext displayContext) {
		return CompassProperties.PRIORITY_ITEM_FOUND.get(stack);
	}
	
	@Override
	public MapCodec<TravelersCompassPriority> type() {
		return MAP_CODEC;
	}
	
}