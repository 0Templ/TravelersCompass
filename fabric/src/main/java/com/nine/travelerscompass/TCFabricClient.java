package com.nine.travelerscompass;

import com.nine.travelerscompass.client.hud.HudRenderer;
import com.nine.travelerscompass.client.render.item.TravelersCompassAngle;
import com.nine.travelerscompass.client.render.item.TravelersCompassPriority;
import com.nine.travelerscompass.client.render.item.TravelersCompassState;
import com.nine.travelerscompass.client.screen.CompassScreen;
import com.nine.travelerscompass.init.ItemRegistry;
import com.nine.travelerscompass.init.MenuRegistry;
import com.nine.travelerscompass.network.FabricNetworkHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class TCFabricClient implements ClientModInitializer {
	
	
	@Override
	public void onInitializeClient() {
		FabricNetworkHandler.clientSetup();
		MenuScreens.register(MenuRegistry.COMPASS_MENU.get(), CompassScreen::new);
		TCClient.init();
		clientEvents();
		setupItemProperties();
	}
	
	public void setupItemProperties() {
		ConditionalItemModelProperties.ID_MAPPER.put(
				ResourceLocation.fromNamespaceAndPath(TCCommon.MODID, "priority"),
				TravelersCompassPriority.MAP_CODEC);
		
		RangeSelectItemModelProperties.ID_MAPPER.put(
				ResourceLocation.fromNamespaceAndPath(TCCommon.MODID, "angle"),
				TravelersCompassAngle.MAP_CODEC);
		
		RangeSelectItemModelProperties.ID_MAPPER.put(
				ResourceLocation.fromNamespaceAndPath(TCCommon.MODID, "state"),
				TravelersCompassState.MAP_CODEC);
	}
	
	
	public void clientEvents() {
		HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT, ResourceLocation.fromNamespaceAndPath(TCCommon.MODID, "compass_hud"), HudRenderer::renderTick);
		ClientPlayConnectionEvents.DISCONNECT.register((listener, minecraft) -> {
			LocalPlayer player = minecraft.player;
			TCClient.onClientPlayerLogout(player);
		});
		
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> {
			entries.addAfter(Items.COMPASS, new ItemStack(ItemRegistry.TRAVELERS_COMPASS.get()));
		});
	}
	
}
