package com.nine.travelerscompass.compat.rei;

import com.nine.travelerscompass.config.TCConfig;
import com.nine.travelerscompass.init.ItemRegistry;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
import me.shedaniel.rei.plugin.common.displays.DefaultInformationDisplay;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

@REIPluginClient
public class NeoForgeReiSetup implements REIClientPlugin {

	@Override
	public void registerScreens(ScreenRegistry registry) {
		if (TCConfig.REI_COMPATIBILITY.get()) {
			registry.registerDraggableStackVisitor(new ReiGhostTargetHandler());
		}
	}

	@Override
	public void registerDisplays(DisplayRegistry registry) {
		if (TCConfig.REI_COMPATIBILITY.get()) {
			ItemStack compass = new ItemStack(ItemRegistry.TRAVELERS_COMPASS.get());
			DefaultInformationDisplay info = DefaultInformationDisplay.createFromEntry(EntryStacks.of(compass),
					compass.getItem().getName(compass));
			info.lines(Component.translatable("nei.travelerscompass.info"));
			registry.add(info);
		}
	}
}
