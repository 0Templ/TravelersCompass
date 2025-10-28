package com.nine.travelerscompass.mixin.client;

import com.nine.travelerscompass.client.utils.ClientTickable;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ClientTickable {
	
	@Inject(method = "inventoryTick", at = @At("HEAD"))
	public void inventoryTick(Level level, Entity entity, EquipmentSlot slot, CallbackInfo ci) {
		if (!(level instanceof ClientLevel clientLevel)) return;
		ItemStack stack = (ItemStack) (Object) this;
		if (stack.getItem() instanceof ClientTickable clientTickable) {
			clientTickable.clientInventoryTick(stack, clientLevel, entity, slot);
		}
	}
	
}
