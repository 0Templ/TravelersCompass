
package com.nine.travelerscompass.compat.jei;


import com.nine.travelerscompass.TCConfig;
import com.nine.travelerscompass.client.screen.CompassScreen;
import com.nine.travelerscompass.common.network.NetworkHandler;
import com.nine.travelerscompass.common.network.packet.GhostTargetPacket;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class JeiGhostTargetHandler implements IGhostIngredientHandler<CompassScreen>{

    @Override
    public <I> List<Target<I>> getTargetsTyped(CompassScreen screen, ITypedIngredient<I> ingredient, boolean doStart) {
            List<Target<I>> targets = new ArrayList<>();
            if (ingredient.getType() == VanillaTypes.ITEM_STACK && TCConfig.JEICompatibility.get()) {
                for (Slot slot : screen.getMenu().slots) {
                    if (slot.index < 9) {
                        Rect2i area = new Rect2i(screen.getGuiLeft() + slot.x, screen.getGuiTop() + slot.y, 16, 16);
                        targets.add(new Target<>() {
                            @Override
                            public Rect2i getArea() {
                                return area;
                            }
                            @Override
                            public void accept(I stack) {
                                screen.getMenu().slots.get(slot.index).set((ItemStack) stack);
                                NetworkHandler.CHANNEL.sendToServer(new GhostTargetPacket(slot.index, (ItemStack) stack));
                            }
                        });
                    }
                }
            }
            return targets;
    }
    @Override
    public boolean shouldHighlightTargets() {
        return true;
    }

    @Override
    public void onComplete() {}
}


