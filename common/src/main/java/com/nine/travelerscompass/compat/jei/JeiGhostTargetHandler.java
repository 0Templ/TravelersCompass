
package com.nine.travelerscompass.compat.jei;


import com.nine.travelerscompass.client.screen.CompassScreen;
import com.nine.travelerscompass.compat.BaseGhostTargetHandler;
import com.nine.travelerscompass.compat.NEI;
import com.nine.travelerscompass.config.TCConfig;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class JeiGhostTargetHandler extends BaseGhostTargetHandler implements IGhostIngredientHandler<CompassScreen>{

    @Override
    @SuppressWarnings("unchecked")
    public <I> List<Target<I>> getTargetsTyped(CompassScreen screen, ITypedIngredient<I> ingredient, boolean doStart) {
        List<Target<I>> targets = new ArrayList<>();
        if (!TCConfig.JEI_COMPATIBILITY.get() || ingredient.getType() != VanillaTypes.ITEM_STACK){
            return targets;
        }
        var opt = ingredient.getItemStack();
        if (opt.isPresent() && !opt.get().isEmpty()){
            targets.addAll(screen.getMenu().slots.stream()
                    .filter(this::isCompassSlot)
                    .map(slot -> createTarget(screen, slot))
                    .map(target -> (Target<I>) target)
                    .toList()
            );
        }
        return targets;
    }

    private Target<ItemStack> createTarget(CompassScreen screen, Slot slot) {
        return new Target<>() {

            @Override
            public Rect2i getArea() {
                return slotArea(screen, slot);
            }

            @Override
            public void accept(ItemStack stack) {
                applyGhostStack(slot, stack, NEI.JEI);
            }
        };
    }

    @Override
    public void onComplete() {
    }
}


