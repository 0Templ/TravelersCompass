package com.nine.travelerscompass.init;

import com.nine.travelerscompass.TravelersCompass;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TravelersCompass.MODID);

    public static final RegistryObject<CreativeModeTab> ITEMS = TAB.register("creative_tc_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemgroup.travelerscompass"))
            .icon(() -> new ItemStack(ItemRegistry.TRAVELERS_COMPASS.get()))
            .displayItems((parameters, output) -> {
                output.accept(ItemRegistry.TRAVELERS_COMPASS.get());
            }).build());
}
