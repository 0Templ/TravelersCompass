/*
package com.nine.travelerscompass.compat.emi;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.client.screen.CompassScreen;
import com.nine.travelerscompass.config.TCConfig;
import com.nine.travelerscompass.init.ItemRegistry;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiInfoRecipe;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.List;

public class FabricEmiSetup implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {
        if (TCConfig.EMI_COMPATIBILITY.get()) {
            registry.addDragDropHandler(CompassScreen.class, new EmiGhostTargetHandler());
            registry.addRecipe(new EmiInfoRecipe(
                    List.of(EmiStack.of(ItemRegistry.TRAVELERS_COMPASS.get())),
                    List.of(Component.translatable("nei.travelerscompass.info")),
                    Identifier.fromNamespaceAndPath(TCCommon.MODID, "/info/travelerscompass")
            ));
        }
    }

}
*/
