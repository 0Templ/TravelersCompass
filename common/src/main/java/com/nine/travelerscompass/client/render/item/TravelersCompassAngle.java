package com.nine.travelerscompass.client.render.item;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;


public class TravelersCompassAngle implements RangeSelectItemModelProperty {
	
	public static final MapCodec<TravelersCompassAngle> MAP_CODEC = MapCodec.unit(new TravelersCompassAngle());
	
	private final TravelersCompassAngleState state;
	
	public TravelersCompassAngle() {
		this.state = new TravelersCompassAngleState();
	}
	
	@Override
	public float get(ItemStack stack, ClientLevel level, LivingEntity living, int seed) {
		return state.get(stack, level, living, seed);
	}
	
	@Override
	public MapCodec<TravelersCompassAngle> type() {
		return MAP_CODEC;
	}
	
}
