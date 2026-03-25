/*
package com.nine.travelerscompass.compat.jade;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import com.nine.travelerscompass.config.TCConfig;
import com.nine.travelerscompass.config.cost.SearchCost;
import com.nine.travelerscompass.config.cost.SearchCostHelper;
import com.nine.travelerscompass.config.filter.FilterManager;
import com.nine.travelerscompass.config.filter.FilterReason;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
		ItemStack stack = player.getMainHandItem();
		if (!(stack.getItem() instanceof TravelersCompassItem)) {
			return;
		}
		FilterReason filterReason = FilterManager.getFilterReason(block.asItem());
		if (filterReason.isAllowed()) {
			CompassContainer compassContainer = CompassContainer.container(stack);
			if (!compassContainer.getList().contains(block.asItem())) {
				tooltip.add(Component.translatable("waila.travelerscompass.info.block").withStyle(ChatFormatting.GRAY));
			}
			if (!TCConfig.SEARCH_REQUIRES_XP.get()) return;
			SearchCost sc = SearchCostHelper.getCost(block.asItem());
			if (sc.isFree()) {
				return;
			}
			boolean reqOk = sc.meetsRequirement(player.experienceLevel) || player.isCreative();
			if (!sc.meetsRequirement(player.experienceLevel)) {
				tooltip.add(sc.reqAsComponent().withStyle(ChatFormatting.RED));
			}
			if (sc.costLevel() > 0) {
				tooltip.add(sc.costAsComponent().withStyle(reqOk ? ChatFormatting.GRAY : ChatFormatting.RED));
			}
		} else {
			tooltip.add(Component.translatable("waila.travelerscompass.forbidden_block").withStyle(ChatFormatting.RED));
		}
	}
	
	@Override
	public Identifier getUid() {
		return Identifier.parse(TCCommon.MODID);
	}
}*/
