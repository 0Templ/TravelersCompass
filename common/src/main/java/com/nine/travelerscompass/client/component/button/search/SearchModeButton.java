package com.nine.travelerscompass.client.component.button.search;

import com.nine.travelerscompass.client.CompassUI;
import com.nine.travelerscompass.client.component.button.base.ToggleableButton;
import com.nine.travelerscompass.client.component.button.settings.ButtonSearchModeSettings;
import com.nine.travelerscompass.client.utils.Icon;
import com.nine.travelerscompass.client.utils.TextureData;
import com.nine.travelerscompass.client.utils.TooltipBuilder;
import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.data.DataStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.List;

public class SearchModeButton extends ToggleableButton {
	
	private final DataStorage<Boolean> data;
	
	protected final Icon iconInactive;
	protected final Icon iconActive;
	protected final Icon iconLock;
	private final boolean configEnabled;
	private final List<Component> descriptions;
	
	public SearchModeButton(ButtonSearchModeSettings settings) {
		super(settings);
		this.data = settings.data;
		this.iconActive = settings.iconActive;
		this.iconInactive = settings.iconInactive;
		this.iconLock = settings.iconLock;
		this.configEnabled = settings.configEnabled;
		this.descriptions = settings.descriptions;
		
		this.active = configEnabled;
		
		setToggled(data.get(stack()));
		refreshTooltip();
	}
	
	@Override
	public boolean onLeftClick(double mouseX, double mouseY) {
		CompassProperties.toggleToServer(stack(), data, true);
		setToggled(data.get(stack()));
		refreshTooltip();
		return true;
	}
	
	@Override
	protected TextureData getMainLayerTexture() {
		return buttonTexturesSet().get(toggled, isHovered, configEnabled);
	}
	
	@Override
	protected Icon getIcon() {
		return configEnabled ? (toggled ? iconActive : iconInactive) : iconLock;
	}
	
	@Override
	public void refreshTooltip() {
		String key = "tooltip.travelerscompass.search_mode." + data.id();
		var builder = TooltipBuilder.builder().title(key);
		
		if (configEnabled && shiftPressed) {
			if (descriptions != null && !descriptions.isEmpty()) {
				descriptions.forEach(builder::desc);
			} else {
				builder.desc(Component.translatable(key + ".desc").withStyle(ChatFormatting.GRAY));
			}
		}
		
		builder.state(configEnabled ? (isToggled() ? CompassUI.ENABLED : CompassUI.DISABLED) : CompassUI.CONFIG_DISABLED);
		
		setTooltip(builder.buildAsTooltip());
	}
	
	
}
