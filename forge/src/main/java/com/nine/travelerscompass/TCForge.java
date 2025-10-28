package com.nine.travelerscompass;

import com.nine.travelerscompass.client.hud.HudRenderer;
import com.nine.travelerscompass.client.render.item.TravelersCompassAngle;
import com.nine.travelerscompass.client.render.item.TravelersCompassPriority;
import com.nine.travelerscompass.client.render.item.TravelersCompassState;
import com.nine.travelerscompass.client.screen.CompassScreen;
import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.search.SearchManager;
import com.nine.travelerscompass.init.CreativeTabRegistry;
import com.nine.travelerscompass.init.ItemRegistry;
import com.nine.travelerscompass.init.MenuRegistry;
import com.nine.travelerscompass.mixin.client.accessor.ConditionalItemModelPropertiesAccessor;
import com.nine.travelerscompass.mixin.client.accessor.RangeSelectItemModelPropertiesAccessor;
import com.nine.travelerscompass.network.ForgeNetworkHandler;
import com.nine.travelerscompass.platform.ForgePlatformConfigHelper;
import com.nine.travelerscompass.platform.ForgePlatformRegistryHelper;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.AddGuiOverlayLayersEvent;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(TCCommon.MODID)
public class TCForge {
	
	public TCForge(FMLJavaModLoadingContext context) {
		loadConfig(context);
		var busGroup = context.getModBusGroup();
		
		setupProperties();
		FMLCommonSetupEvent.getBus(busGroup).addListener(this::commonSetup);
		FMLCommonSetupEvent.getBus(busGroup).addListener(this::setupNetwork);
		FMLClientSetupEvent.getBus(busGroup).addListener(this::setupClient);
		AddGuiOverlayLayersEvent.getBus(busGroup).addListener(this::setupHud);
		
		ItemRegistry.init();
		CompassProperties.init();
		CreativeTabRegistry.init();
		MenuRegistry.init();
		
		ForgePlatformRegistryHelper.ITEMS.register(busGroup);
		ForgePlatformRegistryHelper.DATA_COMPONENTS.register(busGroup);
		ForgePlatformRegistryHelper.TAB.register(busGroup);
		ForgePlatformRegistryHelper.MENUS.register(busGroup);
		
	}

	// Register early to ensure it happens before model baking (FMLClientSetupEvent timing is not guaranteed)
	private void setupProperties() {
		//TODO: make such registration Common
		if (FMLEnvironment.dist.isClient()) {
			ConditionalItemModelPropertiesAccessor.travelerscompass$getConditionalIdMapper()
					.put(ResourceLocation.fromNamespaceAndPath(TCCommon.MODID, "priority"),
							TravelersCompassPriority.MAP_CODEC);
			RangeSelectItemModelPropertiesAccessor.travelerscompass$getRangeSelectIdMapper()
					.put(ResourceLocation.fromNamespaceAndPath(TCCommon.MODID, "angle"),
							TravelersCompassAngle.MAP_CODEC);
			RangeSelectItemModelPropertiesAccessor.travelerscompass$getRangeSelectIdMapper()
					.put(ResourceLocation.fromNamespaceAndPath(TCCommon.MODID, "state"),
							TravelersCompassState.MAP_CODEC);
		}
	}
	
	public void setupClient(FMLClientSetupEvent setupEvent) {
		MenuScreens.register(MenuRegistry.COMPASS_MENU.get(), CompassScreen::new);
		ClientPlayerNetworkEvent.LoggingOut.BUS.addListener(event -> {
			SearchManager.tick();
		});
		
	}
	
	private void setupHud(AddGuiOverlayLayersEvent event) {
		event.getLayeredDraw().add(ResourceLocation.fromNamespaceAndPath(TCCommon.MODID, "compass_hud"), HudRenderer::renderTick);
	}
	
	private void setupNetwork(FMLCommonSetupEvent event) {
		ForgeNetworkHandler.init();
	}
	
	public void loadConfig(FMLJavaModLoadingContext context) {
		context.registerConfig(ModConfig.Type.COMMON, ForgePlatformConfigHelper.COMMON_CONFIG, "travelerscompass-common.toml");
		context.registerConfig(ModConfig.Type.COMMON, ForgePlatformConfigHelper.COST_CONFIG, "travelerscompass-cost.toml");
	}
	
	public void commonSetup(FMLCommonSetupEvent setupEvent) {
		TCCommon.updateCache();
		TickEvent.ServerTickEvent.Pre.BUS.addListener(event -> {
			SearchManager.tick();
		});
		PlayerEvent.PlayerLoggedInEvent.BUS.addListener(event -> {
			if (event.getEntity() instanceof ServerPlayer player) {
				TCCommon.onServerPlayerLogin(player);
			}
		});
	}
	
}