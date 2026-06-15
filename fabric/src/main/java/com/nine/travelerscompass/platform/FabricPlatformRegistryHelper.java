package com.nine.travelerscompass.platform;

import com.mojang.serialization.Codec;
import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.common.data.DataStorage;
import com.nine.travelerscompass.common.item.FabricTravelersCompassItem;
import com.nine.travelerscompass.init.FabricRegistryProvider;
import com.nine.travelerscompass.init.MenuRegistry;
import com.nine.travelerscompass.init.RegistryProvider;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.function.Function;
import java.util.function.Supplier;

public class FabricPlatformRegistryHelper implements IPlatformRegistryHelper {
	
	@Override
	public RegistryProvider<Item> registerTravelersCompassItem(ResourceKey<Item> id, Item.Properties properties) {
		return registerItem(id, () -> new FabricTravelersCompassItem(properties.setId(id)));
	}
	
	@Override
	public RegistryProvider<Item> registerItem(ResourceKey<Item> id, Supplier<Item> supplier) {
		Item ret = Registry.register(BuiltInRegistries.ITEM, id, supplier.get());
		return new FabricRegistryProvider<>(ret);
	}
	
	@Override
	public <T extends AbstractContainerMenu> RegistryProvider<MenuType<T>> registerMenu(String id, MenuRegistry.CommonMenuFactory<T> menuType) {
		var ret = new ExtendedMenuType<>((syncId, inventory, buf) ->
				menuType.create(syncId, inventory),
				MenuRegistry.EmptyScreenData.PACKET_CODEC
		);
		Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(TCCommon.MODID, id), ret);
		return new FabricRegistryProvider<>(ret);
	}
	
	@Override
	public <T> DataStorage<T> registerDataComponent(String id, byte networkId, Supplier<T> defaultSupplier, Function<T, T> validator, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec, boolean cacheEncoding) {
		Supplier<DataComponentType<T>> builderSupplier = () -> {
			var builder = DataComponentType.<T>builder()
					.persistent(codec);
			if (streamCodec != null) {
				builder.networkSynchronized(streamCodec);
			}
			if (cacheEncoding) {
				builder.cacheEncoding();
			}
			return builder.build();
		};
		DataComponentType<T> dataComponent = Registry.register(
				BuiltInRegistries.DATA_COMPONENT_TYPE,
				Identifier.fromNamespaceAndPath(TCCommon.MODID, id),
				builderSupplier.get()
		);
		return new DataStorage<>(id, networkId, defaultSupplier, validator, () -> dataComponent);
	}
	
	@Override
	public RegistryProvider<CreativeModeTab> registerCreativeTab(String id, CreativeModeTab.Builder builder) {
		var tab = builder.build();
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Identifier.parse(TCCommon.MODID), tab);
		return new FabricRegistryProvider<>(tab);
	}
	
}
