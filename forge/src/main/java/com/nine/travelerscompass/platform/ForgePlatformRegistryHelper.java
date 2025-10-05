package com.nine.travelerscompass.platform;

import com.mojang.serialization.Codec;
import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.common.data.DataStorage;
import com.nine.travelerscompass.common.item.ForgeTravelersCompassItem;
import com.nine.travelerscompass.init.ForgeRegistryObject;
import com.nine.travelerscompass.init.MenuRegistry;
import com.nine.travelerscompass.init.RegistryProvider;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;
import java.util.function.Supplier;

public class ForgePlatformRegistryHelper implements IPlatformRegistryHelper {


    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TCCommon.MODID);

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, TCCommon.MODID);

    public static final DeferredRegister<CreativeModeTab> TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TCCommon.MODID);

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, TCCommon.MODID);

    @Override
    public RegistryProvider<Item> registerTravelersCompassItem(String id, Item.Properties properties){
        return registerItem(id, () -> new ForgeTravelersCompassItem(properties));
    }

    @Override
    public RegistryProvider<Item> registerItem(String name, Supplier<Item> itemSupplier){
        return new ForgeRegistryObject<>(ITEMS.register(name, itemSupplier));
    }

    @Override
    public <T extends AbstractContainerMenu> RegistryProvider<MenuType<T>> registerMenu(String id, MenuRegistry.CommonMenuFactory<T> menuType) {
        var ret = MENUS.register(id, () -> IForgeMenuType.create((windowId, inv, data)
                -> menuType.create(windowId, inv)));
        return new ForgeRegistryObject<>(ret);
    }

    @Override
    public <T> DataStorage<T> registerDataComponent(String id, byte networkId, Supplier<T> defaultSupplier, Function<T, T> validator, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec, boolean cacheEncoding){
        var holder = DATA_COMPONENTS.register(id, () -> {
            var builder = DataComponentType.<T>builder()
                    .persistent(codec);
            if (streamCodec != null){
                builder.networkSynchronized(streamCodec);
            }
            if (cacheEncoding){
                builder.cacheEncoding();
            }
            return builder.build();
        });
        return new DataStorage<>(id, networkId, defaultSupplier, validator, new ForgeRegistryObject<>(holder));
    }

    @Override
    public RegistryProvider<CreativeModeTab> registerCreativeTab(String id, CreativeModeTab.Builder builder){
        var tab = TAB.register(id, builder::build);
        return new ForgeRegistryObject<>(tab);
    }


}
