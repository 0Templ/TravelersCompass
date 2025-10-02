package com.nine.travelerscompass;

import com.nine.travelerscompass.compat.top.TheOneProbeSetup;
import com.nine.travelerscompass.init.CreativeTabRegistry;
import com.nine.travelerscompass.init.ItemRegistry;
import com.nine.travelerscompass.init.MenuRegistry;
import com.nine.travelerscompass.network.NetworkHandler;
import com.nine.travelerscompass.platform.ForgePlatformConfigHelper;
import com.nine.travelerscompass.platform.ForgePlatformRegistryHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TCCommon.MODID)
public class TCForge {

    @SuppressWarnings("removal")
    public TCForge() {
        FMLJavaModLoadingContext context = FMLJavaModLoadingContext.get();
        IEventBus modEventBus = context.getModEventBus();

        loadConfig(context);

        ItemRegistry.init();
        MenuRegistry.init();
        CreativeTabRegistry.init();

        ForgePlatformRegistryHelper.ITEMS.register(modEventBus);
        ForgePlatformRegistryHelper.MENUS.register(modEventBus);
        ForgePlatformRegistryHelper.TAB.register(modEventBus);

        NetworkHandler.register();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::imeRegistry);
    }

    public void loadConfig(FMLJavaModLoadingContext context){
        context.registerConfig(ModConfig.Type.COMMON, ForgePlatformConfigHelper.COMMON_CONFIG, "travelerscompass-common.toml");
        context.registerConfig(ModConfig.Type.COMMON, ForgePlatformConfigHelper.COST_CONFIG, "travelerscompass-cost.toml");
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        TCCommon.updateCache();
    }

    public void imeRegistry(InterModEnqueueEvent evt) {
        if (ModList.get().isLoaded("theoneprobe")) {
            InterModComms.sendTo("theoneprobe", "getTheOneProbe", TheOneProbeSetup::new);
        }
    }
}