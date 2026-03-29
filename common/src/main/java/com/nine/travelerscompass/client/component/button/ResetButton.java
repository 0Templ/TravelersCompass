package com.nine.travelerscompass.client.component.button;

import com.nine.travelerscompass.client.component.button.base.BaseIconButton;
import com.nine.travelerscompass.client.component.button.settings.ButtonGenericSettings;
import com.nine.travelerscompass.client.ui.constant.TCIcons;
import com.nine.travelerscompass.client.utils.Icon;
import com.nine.travelerscompass.client.utils.TextureData;
import com.nine.travelerscompass.client.utils.TooltipBuilder;

public class ResetButton extends BaseIconButton {
	
	public ResetButton(ButtonGenericSettings settings) {
		super(settings);
	}
	
	@Override
	protected TextureData getMainLayerTexture() {
		return buttonTexturesSet().get(false, isHovered());
	}
	
	@Override
	protected Icon getIcon() {
		return isHovered ? TCIcons.Settings.RESET_HOVERED : TCIcons.Settings.RESET;
	}
	
	@Override
	public void refreshTooltip() {
		String key = "tooltip.travelerscompass.settings.reset";
		this.setTooltip(TooltipBuilder.builder().title(key).descIf(key, shiftPressed).buildAsTooltip());
	}
	
}
