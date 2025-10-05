package com.nine.travelerscompass.platform;

import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.common.container.menu.CompassMenu;
import com.nine.travelerscompass.init.MenuRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class FabricPlatformCoreHelper implements IPlatformCoreHelper {

    @Override
    public void openMenu(Player player, ItemStack stack) {
        player.openMenu(new ExtendedScreenHandlerFactory<>() {

            @Override
            public Object getScreenOpeningData(ServerPlayer player) {
                return new MenuRegistry.EmptyScreenData("");
            }

            @Override
            public Component getDisplayName() {
                return stack.getHoverName();
            }
            @Override
            public @NotNull AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                return new CompassMenu(i, inventory, CompassContainer.container(stack));
            }

        });
    }

    @Override
    public Item getContainerItemByIndex(int index, BlockEntity be) {
        Item item = Items.AIR;
        if (be instanceof Container container) {
            if (index < container.getContainerSize()) {
                return container.getItem(index).getItem();
            }
            return Items.AIR;
        }
        for (Direction value : Direction.values()) {
            Storage<ItemVariant> storage = ItemStorage.SIDED.find(be.getLevel(), be.getBlockPos(), be.getBlockState(), be, value);
            if (storage == null) {
                continue;
            }
            int viewed = 0;
            for (StorageView<ItemVariant> variantStorageView : storage) {
                if (viewed >= index) {
                    item = variantStorageView.getResource().getItem();
                    break;
                }
                viewed++;
            }
        }
        return item;
    }

    @Override
    public boolean isModLoaded(String modId){
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public String getModName(String modId){
        return FabricLoader.getInstance().getModContainer(modId)
                .map(mod -> mod.getMetadata().getName())
                .orElse(modId);
    }

    @Override
    public String getModVersion(String modId){
        return FabricLoader.getInstance().getModContainer(modId)
                .map(mod -> mod.getMetadata().getVersion().getFriendlyString())
                .orElse(modId);
    }


    @Override
    public boolean isClientSide() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }
}
