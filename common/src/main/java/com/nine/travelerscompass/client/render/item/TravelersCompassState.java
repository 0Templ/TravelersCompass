package com.nine.travelerscompass.client.render.item;

import com.mojang.serialization.MapCodec;
import com.nine.travelerscompass.common.data.CompassProperties;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class TravelersCompassState implements RangeSelectItemModelProperty {
	
	public static final MapCodec<TravelersCompassState> MAP_CODEC =
			MapCodec.unit(new TravelersCompassState());
	
	@Override
	public float get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
		return CompassProperties.COMPASS_STATE.get(stack);
	}
	
	@Override
	public MapCodec<? extends RangeSelectItemModelProperty> type() {
		return MAP_CODEC;
	}
	
}
