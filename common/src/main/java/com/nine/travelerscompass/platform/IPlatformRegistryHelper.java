package com.nine.travelerscompass.platform;

import com.nine.travelerscompass.init.MenuRegistry;
import com.nine.travelerscompass.init.RegistryProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public interface IPlatformRegistryHelper {

    RegistryProvider<Item> registerTravelersCompassItem(String id, Item.Properties properties);

    RegistryProvider<Item> registerItem(String id, Supplier<Item> itemSupplier);

    <T extends AbstractContainerMenu> RegistryProvider<MenuType<T>> registerMenu(String id, MenuRegistry.CommonMenuFactory<T> menuType);

    RegistryProvider<CreativeModeTab> registerCreativeTab(String id, CreativeModeTab.Builder builder);

}
