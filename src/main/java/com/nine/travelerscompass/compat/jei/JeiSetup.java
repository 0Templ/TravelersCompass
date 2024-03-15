
package com.nine.travelerscompass.compat.jei;


import com.nine.travelerscompass.TravelersCompass;
import com.nine.travelerscompass.client.screen.CompassScreen;
import com.nine.travelerscompass.init.ItemRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;


@JeiPlugin
public class JeiSetup implements IModPlugin {


    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGhostIngredientHandler(CompassScreen.class, new JeiGhostTargetHandler());

    }
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
            registration.addIngredientInfo(new ItemStack(ItemRegistry.TRAVELERS_COMPASS.get()), VanillaTypes.ITEM_STACK, Component.translatable("options.travelerscompass.tooltip.item.info"));
       }
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(TravelersCompass.MODID, TravelersCompass.MODID);
    }
}

