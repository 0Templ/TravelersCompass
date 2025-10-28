package com.nine.travelerscompass.mixin.accessor;

import com.nine.travelerscompass.platform.accessor.MobBucketItemAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.MobBucketItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MobBucketItem.class)
public abstract class NeoForgeMobBucketItemAccessor implements MobBucketItemAccessor {
	
	@Accessor("type")
	abstract EntityType<?> travelerscompass$type();
	
	@Override
	public EntityType<?> travelerscompass$getType() {
		return travelerscompass$type();
	}
	
}