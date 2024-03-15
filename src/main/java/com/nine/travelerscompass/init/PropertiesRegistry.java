package com.nine.travelerscompass.init;

import com.nine.travelerscompass.client.render.item.CustomCompassItemPropertyFunction;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class PropertiesRegistry {
    public static void register() {
        makeTravelersCompassItem(ItemRegistry.TRAVELERS_COMPASS.get());
        makeAltitudeDependentItem(ItemRegistry.TRAVELERS_COMPASS.get());
        makeFavoriteDependentItem(ItemRegistry.TRAVELERS_COMPASS.get());

    }
    private static void makeTravelersCompassItem(Item item) {
        ItemProperties.register(item, new ResourceLocation("angle"), new CustomCompassItemPropertyFunction((level, stack, entity) -> {
            if (stack.getItem() instanceof TravelersCompassItem) {
                return TravelersCompassItem.getFoundPosition(stack);
            }
            return null;
        }));
    }
    private static void makeAltitudeDependentItem(Item item) {
        ItemProperties.register(item, new ResourceLocation("height"), (stack, level, entity, seed) -> {
            if (stack.getItem() instanceof TravelersCompassItem){
                return ((TravelersCompassItem) stack.getItem()).positionRelativeToTarget(stack);
            }
            return 1F;
        });

    }
    private static void makeFavoriteDependentItem(Item item) {
        ItemProperties.register(item, new ResourceLocation("has_favorite"), (stack, level, entity, seed) -> {
            if (stack.getItem() instanceof TravelersCompassItem && ((TravelersCompassItem) stack.getItem()).hasFavoriteItem(stack)){
                return 1F;
            }
            return 0F;
        });
    }
}
