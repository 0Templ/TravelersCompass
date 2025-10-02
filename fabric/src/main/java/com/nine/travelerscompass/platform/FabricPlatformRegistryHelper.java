package com.nine.travelerscompass.platform;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.common.item.FabricTravelersCompassItem;
import com.nine.travelerscompass.init.FabricRegistryProvider;
import com.nine.travelerscompass.init.MenuRegistry;
import com.nine.travelerscompass.init.RegistryProvider;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class FabricPlatformRegistryHelper implements IPlatformRegistryHelper {

    @Override
    public RegistryProvider<Item> registerTravelersCompassItem(String id, Item.Properties properties){
        return registerItem(id, () -> new FabricTravelersCompassItem(properties));
    }

    @Override
    public RegistryProvider<Item> registerItem(String name, Supplier<Item> supplier) {
        Item ret = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(TCCommon.MODID, name), supplier.get());
        return new FabricRegistryProvider<>(ret);
    }

    @Override
    public <T extends AbstractContainerMenu> RegistryProvider<MenuType<T>> registerMenu(String id, MenuRegistry.CommonMenuFactory<T> menuType){
        var ret = new ExtendedScreenHandlerType<>((syncId, inventory, buf) ->
                menuType.create(syncId, inventory)
        );
        Registry.register(BuiltInRegistries.MENU, new ResourceLocation(TCCommon.MODID, id), ret);
        return new FabricRegistryProvider<>(ret);
    }

    @Override
    public RegistryProvider<CreativeModeTab> registerCreativeTab(String id, CreativeModeTab.Builder builder){
        var tab = builder.build();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, new ResourceLocation(TCCommon.MODID), tab);
        return new FabricRegistryProvider<>(tab);
    }


}
