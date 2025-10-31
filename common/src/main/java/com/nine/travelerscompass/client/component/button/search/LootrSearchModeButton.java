package com.nine.travelerscompass.client.component.button.search;

import com.nine.travelerscompass.client.CompassUI;
import com.nine.travelerscompass.client.component.button.base.ToggleableButton;
import com.nine.travelerscompass.client.component.button.settings.ButtonGenericSettings;
import com.nine.travelerscompass.client.utils.Icon;
import com.nine.travelerscompass.client.utils.TextureData;
import com.nine.travelerscompass.client.utils.TooltipBuilder;
import com.nine.travelerscompass.common.data.CompassComponents;
import com.nine.travelerscompass.common.data.DataStorage;
import com.nine.travelerscompass.compat.lootr.LootrSearchMode;
import com.nine.travelerscompass.config.TCConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class LootrSearchModeButton extends ToggleableButton {
	
	private final DataStorage<LootrSearchMode> data = CompassComponents.LOOTR_MODE;
	
	private final boolean configEnabled;
	
	private LootrSearchMode cached;
	
	public LootrSearchModeButton(
			ButtonGenericSettings settings
	) {
		super(settings);
		this.configEnabled = TCConfig.ENABLE_LOOTR_SEARCH.get();
		this.active = configEnabled;
		updateState();
		refreshTooltip();
	}
	
	@Override
	public boolean onLeftClick(double mouseX, double mouseY) {
		CompassComponents.toggleToServer(stack(), data, true);
		return true;
	}
	
	public void updateState() {
		cached = data.get(stack());
		setToggled(cached != LootrSearchMode.OFF);
		refreshTooltip();
	}
	
	@Override
	protected TextureData getMainLayerTexture() {
		return buttonTexturesSet().get(isToggled(), isHovered, configEnabled);
	}
	
	@Override
	protected Icon getIcon() {
		Icon ret;
		if (configEnabled) {
			ret = switch (cached) {
				case ALL -> CompassUI.SearchModeTextures.LOOTR_ALL_ICON;
				case CLOSED -> CompassUI.SearchModeTextures.LOOTR_CLOSED_ICON;
				case OPENED -> CompassUI.SearchModeTextures.LOOTR_OPENED_ICON;
				case OFF -> CompassUI.SearchModeTextures.LOOTR_INACTIVE_ICON;
			};
		} else {
			ret = CompassUI.CommonTextures.LOCK_ICON;
		}
		return ret;
	}
	
	@Override
	public void refreshTooltip() {
		String key = "tooltip.travelerscompass.search_mode." + data.id();
		String withState = key + "." + cached.key();
		var desc = Component.translatable(withState + ".desc").withStyle(ChatFormatting.GRAY);
		var state = Component.translatable(withState).withStyle(ChatFormatting.GRAY);
		this.setTooltip(TooltipBuilder.builder().title(key).descIf(desc, shiftPressed).state(state).buildAsTooltip());
	}
	
}
