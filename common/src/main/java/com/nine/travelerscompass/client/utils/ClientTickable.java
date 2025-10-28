package com.nine.travelerscompass.client.utils;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public interface ClientTickable {
	
	void clientInventoryTick(ItemStack stack, ClientLevel level, Entity entity, EquipmentSlot slot);
	
}
