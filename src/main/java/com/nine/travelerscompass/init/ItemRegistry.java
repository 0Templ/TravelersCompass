package com.nine.travelerscompass.init;

import com.nine.travelerscompass.TravelersCompass;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, TravelersCompass.MODID);



    public static final RegistryObject<Item> TRAVELERS_COMPASS = ITEMS.register("travelerscompass", () -> new TravelersCompassItem( new Item.Properties().stacksTo(1)));

}
