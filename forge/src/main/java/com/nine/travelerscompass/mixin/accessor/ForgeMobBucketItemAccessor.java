package com.nine.travelerscompass.mixin.accessor;

import com.nine.travelerscompass.platform.accessor.MobBucketItemAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.MobBucketItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.Supplier;

@Mixin(MobBucketItem.class)
public abstract class ForgeMobBucketItemAccessor implements MobBucketItemAccessor {

    @Accessor("entityTypeSupplier")
    public abstract Supplier<? extends EntityType<?>> travelerscompass$entityTypeSupplier();

    @Override
    public EntityType<?> travelerscompass$getType() {
        return travelerscompass$entityTypeSupplier().get();
    }

}
