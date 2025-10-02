package com.nine.travelerscompass.compat.emi;

import com.nine.travelerscompass.client.screen.CompassScreen;
import com.nine.travelerscompass.compat.BaseGhostTargetHandler;
import com.nine.travelerscompass.compat.NEI;
import com.nine.travelerscompass.config.TCConfig;
import dev.emi.emi.api.EmiDragDropHandler;
import dev.emi.emi.api.stack.EmiIngredient;
import net.minecraft.world.item.ItemStack;

public class EmiGhostTargetHandler extends BaseGhostTargetHandler implements EmiDragDropHandler<CompassScreen> {

    @Override
    public boolean dropStack(CompassScreen screen, EmiIngredient ingredient, int x, int y) {
        if (!TCConfig.EMI_COMPATIBILITY.get() || ingredient.getEmiStacks().size() != 1) {
            return false;
        }

        ItemStack stack = ingredient.getEmiStacks().get(0).getItemStack();
        if (stack.isEmpty()) {
            return false;
        }

        return slotUnderMouse(screen, x, y)
                .map(slot -> {
                    applyGhostStack(slot, stack, NEI.EMI);
                    return true;
                })
                .orElse(false);
    }
}
