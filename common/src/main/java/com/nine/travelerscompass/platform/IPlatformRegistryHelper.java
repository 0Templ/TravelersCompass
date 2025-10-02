package com.nine.travelerscompass.platform;

import com.mojang.serialization.Codec;
import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.data.DataStorage;
import com.nine.travelerscompass.init.MenuRegistry;
import com.nine.travelerscompass.init.RegistryProvider;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.swing.plaf.PanelUI;
import java.util.function.Function;
import java.util.function.Supplier;

public interface IPlatformRegistryHelper {

    RegistryProvider<Item> registerTravelersCompassItem(String id, Item.Properties properties);

    RegistryProvider<Item> registerItem(String id, Supplier<Item> itemSupplier);

    <T extends AbstractContainerMenu> RegistryProvider<MenuType<T>> registerMenu(String id, MenuRegistry.CommonMenuFactory<T> menuType);

    <T> DataStorage<T> registerDataComponent(String id, byte networkId, Supplier<T> defaultProvider, Function<T, T> validator, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec, boolean cacheEncoding);

    RegistryProvider<CreativeModeTab> registerCreativeTab(String id, CreativeModeTab.Builder builder);

}
