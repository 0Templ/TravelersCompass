package com.nine.travelerscompass.init;

import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.common.container.menu.CompassMenu;
import com.nine.travelerscompass.platform.Platform;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class MenuRegistry {
    public record EmptyScreenData(String label) {
        public static final StreamCodec<RegistryFriendlyByteBuf, EmptyScreenData> PACKET_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8,
                EmptyScreenData::label, EmptyScreenData::new
        );
    }
    public static RegistryProvider<MenuType<CompassMenu>> COMPASS_MENU = Platform.PLATFORM_REGISTRY.registerMenu(
            "compass_menu", ((i, inventory) -> new CompassMenu(i, inventory,
                    CompassContainer.container(findCompass(inventory.player)))));

    public static ItemStack findCompass(Player player) {
        Inventory inventory = player.getInventory();
        return inventory.getSelected().is(ItemRegistry.TRAVELERS_COMPASS.get()) ? inventory.getSelected() :
                inventory.offhand.getFirst().is(ItemRegistry.TRAVELERS_COMPASS.get()) ? inventory.offhand.getFirst() :
                        ItemStack.EMPTY;
    }

    public interface CommonMenuFactory<T extends AbstractContainerMenu> {
        T create(int id, Inventory inventory);
    }

    public static void init(){

    }

}
