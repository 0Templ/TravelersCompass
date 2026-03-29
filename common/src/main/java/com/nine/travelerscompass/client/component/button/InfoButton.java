package com.nine.travelerscompass.client.component.button;

import com.nine.travelerscompass.client.ClientCache;
import com.nine.travelerscompass.client.component.button.base.BaseIconButton;
import com.nine.travelerscompass.client.component.button.settings.ButtonGenericSettings;
import com.nine.travelerscompass.client.hud.HudData;
import com.nine.travelerscompass.client.ui.constant.TCColors;
import com.nine.travelerscompass.client.ui.constant.TCIcons;
import com.nine.travelerscompass.client.utils.Icon;
import com.nine.travelerscompass.client.utils.SearchProgress;
import com.nine.travelerscompass.client.utils.TextureData;
import com.nine.travelerscompass.client.utils.TooltipBuilder;
import com.nine.travelerscompass.common.data.CompassComponents;
import com.nine.travelerscompass.common.search.location.ILocationObject;
import com.nine.travelerscompass.common.utils.FoundBlockPos;
import com.nine.travelerscompass.common.utils.SearchState;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import java.util.UUID;

public class InfoButton extends BaseIconButton {
	
	private final UUID uuid;
	
	public InfoButton(ButtonGenericSettings settings) {
		super(settings);
		this.uuid = CompassComponents.COMPASS_UUID.get(stack());
		refreshTooltip();
	}
	
	@Override
	protected TextureData getMainLayerTexture() {
		return buttonTexturesSet().get(false, isHovered);
	}
	
	@Override
	protected Icon getIcon() {
		return isHovered ? TCIcons.Common.INFO_HOVERED : TCIcons.Common.INFO;
	}
	
	@Override
	public void refreshTooltip() {
		var builder = TooltipBuilder.builder();
		Component title = Component.translatable("tooltip.travelerscompass.settings.info");
		Component status;
		FoundBlockPos pos = CompassComponents.FOUND_BLOCK_POS.get(stack());
		SearchState state = CompassComponents.SEARCH_STATE.get(stack());
		if (CompassComponents.PAUSE.get(stack())) {
			status = Component.translatable("tooltip.travelerscompass.settings.info.status.paused").withStyle(ChatFormatting.GRAY);
		} else if (state == SearchState.SEARCHING) {
			SearchProgress searchProgress = ClientCache.PROGRESS_DATA_CACHE.get(uuid);
			int percent = 0;
			if (searchProgress != null) {
				percent = searchProgress.percent();
			}
			Component progress = Component.literal(percent + "%").withColor(TCColors.SOFT_GRAY) ;
			if (pos.isValid()) {
				status = Component.translatable("tooltip.travelerscompass.settings.info.status.scanning", progress).withStyle(ChatFormatting.GRAY);
			} else {
				status = Component.translatable("tooltip.travelerscompass.settings.info.status.searching", progress).withStyle(ChatFormatting.GRAY);
			}
		} else {
			status = Component.translatable("tooltip.travelerscompass.settings.info.status.idle").withStyle(ChatFormatting.GRAY);
		}
		HudData data = ClientCache.HUD_DATA_CACHE.get(CompassComponents.COMPASS_UUID.get(stack()));
		if (data != null) {
			ILocationObject locationObject = data.getLocationObject();
			if (pos.isValid() && locationObject != null) {
				Component target = Component.translatable("tooltip.travelerscompass.settings.info.target",
								Component.translatable(locationObject.descriptionId())
										.withStyle(ChatFormatting.GRAY))
						.withStyle(ChatFormatting.WHITE
						);
				builder.line(target);
				BlockPos blockPos = pos.blockPos();
				if (blockPos != null) {
					Component position = Component.translatable("tooltip.travelerscompass.settings.info.position",
									Component.literal(blockPos.getX() + ", " + blockPos.getY() + ", " + blockPos.getZ())
											.withStyle(ChatFormatting.GRAY))
							.withStyle(ChatFormatting.WHITE
							);
					builder.line(position);
				}
			}
		}
		builder.title(title);
		builder.line(Component.translatable("tooltip.travelerscompass.settings.info.status", status));
		this.setTooltip(builder.buildAsTooltip());
	}
	
	@Override
	public void tick() {
		refreshTooltip();
	}
	
}
