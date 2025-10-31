package com.nine.travelerscompass.client.utils;

import com.nine.travelerscompass.compat.NEI;
import net.minecraft.world.item.ItemStack;

public record GhostStack(ItemStack stack, NEI nei) {
	
	public static final GhostStack EMPTY = new GhostStack(ItemStack.EMPTY, NEI.NONE);
	
	public boolean valid() {
		return stack != null && !stack.isEmpty();
	}
	
	public boolean allowed() {
		return nei.allowed();
	}
	
}
