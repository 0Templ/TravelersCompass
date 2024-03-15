package com.nine.travelerscompass.compat.top;

import com.nine.travelerscompass.TCConfig;
import com.nine.travelerscompass.TravelersCompass;
import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import com.nine.travelerscompass.common.utils.ConfigUtils;
import mcjty.theoneprobe.api.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class TOPBlockComponentProvider implements IProbeInfoProvider {
    @Override
    public ResourceLocation getID() {
        return new ResourceLocation(TravelersCompass.MODID, "block_info");
    }


    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo info, Player player, Level level, BlockState state, IProbeHitData data) {
        ItemStack stack = data.getPickBlock();
        if (!(player.getMainHandItem().getItem() instanceof TravelersCompassItem) || !TCConfig.TheOneProbeCompatibility.get()){
            return;
        }
        if (probeMode == ProbeMode.EXTENDED && !player.isShiftKeyDown()){
            return;
        }
        if (!ConfigUtils.isAllowedToSearch(stack)){
            info.text(CompoundText.create().style(TextStyleClass.ERROR)
                    .text(Component.translatable("options.travelerscompass.tooltip.forbidden.block"))
                    .style(TextStyleClass.ERROR));
        }
        else {
            CompassContainer compassContainer = CompassContainer.container(player.getMainHandItem());
            if(!compassContainer.getList().contains(stack.getItem())) {
                info.text(CompoundText.create().style(TextStyleClass.LABEL)
                        .text(Component.translatable("options.travelerscompass.tooltip.shift.block"))
                        .style(TextStyleClass.LABEL));
            }
        }
    }
}