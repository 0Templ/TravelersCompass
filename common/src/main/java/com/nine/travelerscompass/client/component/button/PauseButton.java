package com.nine.travelerscompass.client.component.button;

import com.nine.travelerscompass.client.ui.constant.TCIcons;

import com.nine.travelerscompass.client.ClientCache;
import com.nine.travelerscompass.client.component.button.settings.ButtonGenericSettings;
import com.nine.travelerscompass.client.utils.TooltipBuilder;
import com.nine.travelerscompass.common.data.CompassComponents;
import com.nine.travelerscompass.common.utils.SearchState;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class PauseButton extends ConfigButton<Boolean> {
	
	private SearchState cachedState;
	
	public PauseButton(ButtonGenericSettings settings) {
		super(settings,
				CompassComponents.PAUSE,
				((value, hovered) ->
						!value ? (hovered ? TCIcons.Common.PAUSE_HOVERED : TCIcons.Common.PAUSE)
								: (hovered ? TCIcons.Common.RESUME_HOVERED : TCIcons.Common.RESUME))
		);
		updateState();
	}
	
	@Override
	public boolean onLeftClick(double mouseX, double mouseY) {
		CompassComponents.toggleToServer(stack(), data, true);
		ClientCache.HUD_DATA_CACHE.computeIfPresent(uuid, (id, hudData) -> {
					hudData.setPaused(cached);
					return hudData;
				}
		);
		ClientCache.PROGRESS_DATA_CACHE.remove(uuid);
		updateState();
		return true;
	}
	
	@Override
	public void updateState() {
		super.updateState();
		this.cachedState = CompassComponents.SEARCH_STATE.get(stack());
	}
	
	@Override
	public void refreshTooltip() {
		var builder = TooltipBuilder.builder();
		String key = "tooltip.travelerscompass.settings." + (cached ? "resume" : "pause");
		builder.title(key);
		builder.lineIf(Component.translatable(key + ".warning").withStyle(ChatFormatting.GRAY), cachedState == SearchState.WIDE_SEARCHING && cached);
		builder.descIf(key, shiftPressed);
		this.setTooltip(builder.buildAsTooltip());
	}
	
}
