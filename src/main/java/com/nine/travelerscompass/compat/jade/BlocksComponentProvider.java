package com.nine.travelerscompass.compat.jade;

import com.nine.travelerscompass.TCConfig;
import com.nine.travelerscompass.TravelersCompass;
import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import com.nine.travelerscompass.common.utils.ConfigUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum BlocksComponentProvider implements IBlockComponentProvider {

    INSTANCE;
    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig pluginConfig) {
        Block block = blockAccessor.getBlock();
        Player player = blockAccessor.getPlayer();
        if (!(player.getMainHandItem().getItem() instanceof TravelersCompassItem) || !TCConfig.JadeCompatibility.get()){
            return;
        }
        if (!ConfigUtils.isAllowedToSearch(block.asItem().getDefaultInstance())){
            tooltip.add(Component.translatable("options.travelerscompass.tooltip.forbidden.block").withStyle(ChatFormatting.RED));
        }
        else {
            CompassContainer compassContainer = CompassContainer.container(player.getMainHandItem());
            if(!compassContainer.getList().contains(block.asItem())) {
                tooltip.add(Component.translatable("options.travelerscompass.tooltip.shift.block").withStyle(ChatFormatting.GRAY));
            }

        }
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(TravelersCompass.MODID);
    }
}