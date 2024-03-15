package com.nine.travelerscompass;

import com.nine.travelerscompass.common.network.NetworkHandler;
import com.nine.travelerscompass.compat.top.TheOneProbeRegistry;
import com.nine.travelerscompass.init.CreativeTabRegistry;
import com.nine.travelerscompass.init.ItemRegistry;
import com.nine.travelerscompass.init.MenuRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TravelersCompass.MODID)
public class TravelersCompass
{
    public static final String MODID = "travelerscompass";

    public TravelersCompass()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, TCConfig.COMMON);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        CreativeTabRegistry.TAB.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);
        MenuRegistry.MENUS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::imeRegistry);

        NetworkHandler.regiser();
    }
    public void imeRegistry(InterModEnqueueEvent evt) {
        if (ModList.get().isLoaded("theoneprobe")) {
            InterModComms.sendTo("theoneprobe", "getTheOneProbe", TheOneProbeRegistry::new);
        }
    }
}
