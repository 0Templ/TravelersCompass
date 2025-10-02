package com.nine.travelerscompass.common.search.criterion;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public record FluidCriterion(Fluid fluid, boolean priority, int slot) implements ISearchCriterion, IPlaceableCriterion {

    public boolean check(Fluid check){
        return check.equals(fluid) && !check.equals(Fluids.EMPTY);
    }

    @Override
    public Block asBlock(){
        return fluid.defaultFluidState().createLegacyBlock().getBlock();
    }

}
