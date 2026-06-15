package com.nine.travelerscompass.platform;

import com.mojang.serialization.Codec;
import com.nine.travelerscompass.common.data.DataStorage;
import com.nine.travelerscompass.init.MenuRegistry;
import com.nine.travelerscompass.init.RegistryProvider;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.function.Function;
import java.util.function.Supplier;

public interface IPlatformRegistryHelper {
	
	RegistryProvider<Item> registerTravelersCompassItem(ResourceKey<Item> id, Item.Properties properties);
	
	RegistryProvider<Item> registerItem(ResourceKey<Item> id, Supplier<Item> supplier);
	
	<T extends AbstractContainerMenu> RegistryProvider<MenuType<T>> registerMenu(String id, MenuRegistry.CommonMenuFactory<T> menuType);
	
	<T> DataStorage<T> registerDataComponent(String id, byte networkId, Supplier<T> defaultProvider, Function<T, T> validator, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec, boolean cacheEncoding);
	
	RegistryProvider<CreativeModeTab> registerCreativeTab(String id, CreativeModeTab.Builder builder);
	
}
