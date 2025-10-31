package com.nine.travelerscompass.client.component.button.hud;

import com.nine.travelerscompass.client.CompassUI;
import com.nine.travelerscompass.client.component.button.range.IntRangeButton;
import com.nine.travelerscompass.client.component.button.settings.ButtonRangeSettings;
import com.nine.travelerscompass.client.hud.HudSize;
import com.nine.travelerscompass.client.hud.HudType;
import com.nine.travelerscompass.client.utils.TooltipBuilder;
import com.nine.travelerscompass.common.data.CompassComponents;
import com.nine.travelerscompass.common.data.DataStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class HudSizeButton extends IntRangeButton {
	
	private final DataStorage<HudSize> sizeData = CompassComponents.HUD_SIZE;
	
	private HudSize cachedSize;
	private HudType cachedType;
	
	private final boolean isWidth;
	
	public HudSizeButton(ButtonRangeSettings settings, boolean isWidth) {
		super(settings, 0, 999, 1, 5, null);
		this.isWidth = isWidth;
		updateState();
	}
	
	@Override
	protected Integer min() {
		return isWidth ? cachedType.minWidth() : cachedType.minHeight();
	}
	
	@Override
	protected Integer max() {
		return isWidth ? cachedType.maxWidth() : cachedType.maxHeight();
	}
	
	@Override
	public void updateCache() {
		cachedSize = CompassComponents.HUD_SIZE.get(stack());
		cachedType = CompassComponents.HUD_TYPE.get(stack());
		cached = isWidth ? CompassComponents.HUD_SIZE.get(stack()).getWidth(cachedType)
				: CompassComponents.HUD_SIZE.get(stack()).getHeight(cachedType);
	}
	
	@Override
	protected void onValueChanged(Integer oldValue, Integer newValue) {
		sizeData.syncToServer(stack(), isWidth ? cachedSize.withWidth(cachedType, newValue) : cachedSize.withHeight(cachedType, newValue));
	}
	
	
	@Override
	public void refreshTooltip() {
		var builder = TooltipBuilder.builder();
		String key = "tooltip.travelerscompass.settings." + (isWidth ? "hud_width" : "hud_height");
		builder.title(key);
		
		builder.line(holdToDecreaseComponent());
		builder.line(holdToChangeFaster());
		builder.line(Component.translatable("tooltip.travelerscompass.settings.hud_pos.anchor", CompassUI.RBM).withStyle(ChatFormatting.GRAY));
		
		builder.descIf(key, shiftPressed);
		
		Component value = Component.literal(String.valueOf(cached)).withColor(CompassUI.Colors.SOFT_GRAY);
		builder.state(Component.translatable(key + ".value", value).withStyle(ChatFormatting.GRAY));
		this.setTooltip(builder.buildAsTooltip());
	}
	
}
