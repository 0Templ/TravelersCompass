package com.nine.travelerscompass.platform.accessor;

import net.minecraft.world.entity.EntityType;

//@Mixin(MobBucketItem.class)
//Forge changes type of the field from EntityType<?> to supplier so dividing logic to forge/fabric
public interface MobBucketItemAccessor {

    EntityType<?> travelerscompass$getType();

}
