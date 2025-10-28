package com.nine.travelerscompass.platform;

import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.common.container.menu.CompassMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.items.IItemHandler;

public class ForgePlatformCoreHelper implements IPlatformCoreHelper {
	
	@Override
	public void openMenu(Player player, ItemStack stack) {
		player.openMenu(new MenuProvider() {
			
			@Override
			public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
				return new CompassMenu(id, playerInventory, CompassContainer.container(stack));
			}
			
			@Override
			public Component getDisplayName() {
				return stack.getHoverName();
			}
		});
	}
	
	@Override
	public Item getContainerItemByIndex(int index, BlockEntity be) {
		Item item = Items.AIR;
		LazyOptional<IItemHandler> cap = be.getCapability(ForgeCapabilities.ITEM_HANDLER, null);
		if (cap.isPresent()) {
			var resolve = cap.resolve();
			if (resolve.isPresent()) {
				item = resolve.get().getStackInSlot(index).getItem();
			}
		}
		return item;
	}
	
	@Override
	public boolean isModLoaded(String modId) {
		return ModList.get().isLoaded(modId);
	}
	
	@Override
	public String getModName(String modId) {
		return ModList.get().getModContainerById(modId)
				.map(container -> container.getModInfo().getDisplayName())
				.orElse(modId);
	}
	
	@Override
	public String getModVersion(String modId) {
		return ModList.get().getModContainerById(modId)
				.map(container -> container.getModInfo().getVersion().getQualifier())
				.orElse(modId);
	}
	
	@Override
	public boolean isClientSide() {
		return FMLEnvironment.dist.isClient();
	}
	
}
