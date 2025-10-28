package com.nine.travelerscompass.platform;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IPlatformCoreHelper {
	
	void openMenu(Player player, ItemStack stack);
	
	Item getContainerItemByIndex(int index, BlockEntity be);
	
	boolean isModLoaded(String modId);
	
	String getModName(String modId);
	
	String getModVersion(String modId);
	
	boolean isClientSide();
	
}
