/*
package com.nine.travelerscompass.compat.top;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import com.nine.travelerscompass.config.TCConfig;
import com.nine.travelerscompass.config.cost.SearchCost;
import com.nine.travelerscompass.config.cost.SearchCostHelper;
import com.nine.travelerscompass.config.filter.FilterManager;
import com.nine.travelerscompass.config.filter.FilterReason;
import mcjty.theoneprobe.api.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class TOPBlockComponentProvider implements IProbeInfoProvider {

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo info, Player player, Level level, BlockState state, IProbeHitData data) {
        Block block = state.getBlock();
        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof TravelersCompassItem)) {
            return;
        }
        if (probeMode == ProbeMode.EXTENDED && !player.isShiftKeyDown()) {
            return;
        }
        FilterReason reason = FilterManager.getFilterReason(block.asItem());
        if (reason instanceof FilterReason.Allowed) {
            CompassContainer compassContainer = CompassContainer.container(stack);
            if (!compassContainer.getList().contains(block.asItem())) {
                if (!compassContainer.getList().contains(stack.getItem())) {
                    info.text(CompoundText.create().style(TextStyleClass.LABEL)
                            .text(Component.translatable("waila.travelerscompass.info.block"))
                            .style(TextStyleClass.LABEL));
                    if (!TCConfig.SEARCH_REQUIRES_XP.get()) return;
                    SearchCost sc = SearchCostHelper.getCost(block.asItem());
                    if (sc.isFree()){
                        return;
                    }
                    boolean reqOk = sc.meetsRequirement(player.experienceLevel) || player.isCreative();
                    if (!sc.meetsRequirement(player.experienceLevel)){
                        info.text(CompoundText.create().style(TextStyleClass.ERROR)
                                .text(sc.reqAsComponent())
                                .style(TextStyleClass.ERROR));
                    }
                    if (sc.costLevel() > 0) {
                        info.text(CompoundText.create().style(reqOk ? TextStyleClass.LABEL : TextStyleClass.ERROR)
                                .text(sc.costAsComponent())
                                .style(reqOk ? TextStyleClass.LABEL : TextStyleClass.ERROR));
                    }
                }

            }
        } else {
            info.text(CompoundText.create().style(TextStyleClass.ERROR)
                    .text(Component.translatable("waila.travelerscompass.forbidden_block"))
                    .style(TextStyleClass.ERROR));
        }
    }

    @Override
    public ResourceLocation getID() {
        return ResourceLocation.fromNamespaceAndPath(TCCommon.MODID, "block_info");
    }
}*/
