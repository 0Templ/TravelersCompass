/*
package com.nine.travelerscompass.compat.jei;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.client.screen.CompassScreen;
import com.nine.travelerscompass.init.ItemRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

@JeiPlugin
public class NeoForgeJeiSetup implements IModPlugin {
	
	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addGhostIngredientHandler(CompassScreen.class, new JeiGhostTargetHandler());
	}
	
	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		registration.addIngredientInfo(new ItemStack(ItemRegistry.TRAVELERS_COMPASS.get()), VanillaTypes.ITEM_STACK,
				Component.translatable("nei.travelerscompass.info"));
	}
	
	@Override
	public Identifier getPluginUid() {
		return Identifier.fromNamespaceAndPath(TCCommon.MODID, "jei_compat_plugin");
	}
	
}
*/
