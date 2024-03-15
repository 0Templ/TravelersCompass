package com.nine.travelerscompass.init;

import com.nine.travelerscompass.TravelersCompass;
import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.common.container.menu.CompassMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuRegistry {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, TravelersCompass.MODID);
    public static final RegistryObject<MenuType<CompassMenu>> COMPASS_MENU = MENUS.register("compass_menu", () -> IForgeMenuType.create((windowId, inv, data) -> new CompassMenu(windowId, inv, CompassContainer.container(getAnyStack(inv.player, ItemRegistry.TRAVELERS_COMPASS.get())))));

    public static ItemStack getAnyStack(Player player, Item item) {
        Inventory inventory = player.getInventory();
        return inventory.getSelected().is(item) ? inventory.getSelected() :
                inventory.offhand.get(0).is(item) ? inventory.offhand.get(0) :
                        ItemStack.EMPTY;
    }

}
