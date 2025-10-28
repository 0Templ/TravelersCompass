package com.nine.travelerscompass.init;

import com.nine.travelerscompass.platform.Platform;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class CreativeTabRegistry {
	
	public static RegistryProvider<CreativeModeTab> TAB = Platform.PLATFORM_REGISTRY.registerCreativeTab("tc_compass",
			CreativeModeTab.builder(null, -1).title(Component.translatable("itemgroup.travelerscompass"))
					.icon(() -> new ItemStack(ItemRegistry.TRAVELERS_COMPASS.get()))
					.displayItems((params, output) ->
							output.accept(ItemRegistry.TRAVELERS_COMPASS.get())));
	
	public static void init() {
	
	}
	
}
