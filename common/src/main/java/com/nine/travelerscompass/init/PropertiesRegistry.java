package com.nine.travelerscompass.init;

import com.nine.travelerscompass.client.render.item.CustomCompassItemPropertyFunction;
import com.nine.travelerscompass.client.render.item.StatePropertyFunction;
import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import com.nine.travelerscompass.mixin.client.accessor.ItemPropertiesAccessor;
import net.minecraft.resources.ResourceLocation;

public class PropertiesRegistry {

    public static void register(){
        ItemPropertiesAccessor.travelerscompass$register(ItemRegistry.TRAVELERS_COMPASS.get(), new ResourceLocation("angle"),
                new CustomCompassItemPropertyFunction((level, stack, entity)
                        -> TravelersCompassItem.getFoundPos(stack).blockPos()
                )
        );
        ItemPropertiesAccessor.travelerscompass$register(ItemRegistry.TRAVELERS_COMPASS.get(), new ResourceLocation("state"),
                new StatePropertyFunction()
        );
        ItemPropertiesAccessor.travelerscompass$register(ItemRegistry.TRAVELERS_COMPASS.get(), new ResourceLocation("priority_found"),
                (stack, level, entity, seed) ->
                        CompassProperties.get(stack, CompassProperties.PRIORITY_ITEM_FOUND) ? 1 : 0
        );
    }
}
