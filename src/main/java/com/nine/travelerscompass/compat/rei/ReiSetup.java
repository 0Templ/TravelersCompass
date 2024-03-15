package com.nine.travelerscompass.compat.rei;

import com.nine.travelerscompass.init.ItemRegistry;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
import me.shedaniel.rei.plugin.common.displays.DefaultInformationDisplay;
import net.minecraft.network.chat.Component;

@REIPluginClient
public class ReiSetup implements REIClientPlugin {
    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerDraggableStackVisitor(new ReiGhostTargetHandler());
    }
    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registerDescriptions(registry);
    }
    private void registerDescriptions(DisplayRegistry registry) {
        DefaultInformationDisplay info = DefaultInformationDisplay.createFromEntry(EntryStacks.of(ItemRegistry.TRAVELERS_COMPASS.get()),
                ItemRegistry.TRAVELERS_COMPASS.get().asItem().getDescription());
        info.lines(Component.translatable("options.travelerscompass.tooltip.item.info"));
        registry.add(info);
    }

}
