package com.nine.travelerscompass;


import com.nine.travelerscompass.client.hud.HudRenderer;
import com.nine.travelerscompass.client.screen.CompassScreen;
import com.nine.travelerscompass.common.data.CompassComponents;
import com.nine.travelerscompass.common.search.SearchManager;
import com.nine.travelerscompass.config.TCConfig;
import com.nine.travelerscompass.init.CreativeTabRegistry;
import com.nine.travelerscompass.init.ItemPropertyRegistry;
import com.nine.travelerscompass.init.ItemRegistry;
import com.nine.travelerscompass.init.MenuRegistry;
import com.nine.travelerscompass.network.NeoForgeNetworkHandler;
import com.nine.travelerscompass.platform.NeoForgePlatformRegistryHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@Mod(TCCommon.MODID)
public class TCNeoForge {
	
	public TCNeoForge(IEventBus eventBus) {
		TCConfig.init();
		
		eventBus.addListener(this::setupNetwork);
		eventBus.addListener(this::setupClientEvents);
		eventBus.addListener(this::setupScreen);
		eventBus.addListener(this::setupCommon);
		eventBus.addListener(this::creativeTabSetup);
		eventBus.addListener(this::imeSetup);
		
		setupProperties();
		
		ItemRegistry.init();
		CompassComponents.init();
		CreativeTabRegistry.init();
		MenuRegistry.init();
		
		NeoForgePlatformRegistryHelper.ITEMS.register(eventBus);
		NeoForgePlatformRegistryHelper.DATA_COMPONENTS.register(eventBus);
		NeoForgePlatformRegistryHelper.TAB.register(eventBus);
		NeoForgePlatformRegistryHelper.MENUS.register(eventBus);
		
	}
	
	private void creativeTabSetup(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
			event.insertAfter(new ItemStack(Items.COMPASS),
					new ItemStack(ItemRegistry.TRAVELERS_COMPASS.get()),
					CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
		}
	}
	
	// Register early to ensure it happens before model baking (FMLClientSetupEvent timing is not guaranteed)
	public void setupProperties() {
		if (FMLEnvironment.getDist().isClient()) {
			ItemPropertyRegistry.init();
		}
	}
	
	public void setupCommon(FMLCommonSetupEvent setupEvent) {
		setupEvent.enqueueWork(TCCommon::init);
		NeoForge.EVENT_BUS.addListener((ServerTickEvent.Pre event) -> {
			SearchManager.tick();
		});
		NeoForge.EVENT_BUS.addListener((PlayerEvent.PlayerLoggedInEvent event) -> {
			if (event.getEntity() instanceof ServerPlayer player) {
				TCCommon.onServerPlayerLogin(player);
			}
		});
	}
	
	public void setupScreen(RegisterMenuScreensEvent event) {
		event.register(MenuRegistry.COMPASS_MENU.get(), CompassScreen::new);
	}
	
	public void setupClientEvents(FMLClientSetupEvent setupEvent) {
		NeoForge.EVENT_BUS.addListener((RenderGuiEvent.Post event) -> {
			HudRenderer.renderTick(event.getGuiGraphics(), event.getPartialTick());
		});
		NeoForge.EVENT_BUS.addListener((ClientPlayerNetworkEvent.LoggingOut event) -> {
			TCClient.onClientPlayerLogout(event.getPlayer());
		});
		NeoForge.EVENT_BUS.addListener((RenderGuiEvent.Post event) -> {
			HudRenderer.renderTick(event.getGuiGraphics(), event.getPartialTick());
		});
	}
	
	public void setupNetwork(RegisterPayloadHandlersEvent event) {
		NeoForgeNetworkHandler.init(event);
	}
	
	public void imeSetup(InterModEnqueueEvent event) {
	}
	
	
}