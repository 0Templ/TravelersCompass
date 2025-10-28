package com.nine.travelerscompass.client.component.button.hud;

import com.nine.travelerscompass.client.CompassUI;
import com.nine.travelerscompass.client.component.button.range.IntRangeButton;
import com.nine.travelerscompass.client.component.button.settings.ButtonRangeSettings;
import com.nine.travelerscompass.client.hud.anchor.HudAnchor;
import com.nine.travelerscompass.client.utils.TooltipBuilder;
import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.data.DataStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class HudPosButton extends IntRangeButton {
	
	private final Component axisComponent;
	
	protected HudAnchor cachedAnchor;
	
	private final DataStorage<? extends HudAnchor> dataAnchor;
	
	public HudPosButton(ButtonRangeSettings settings, int min, int max, Component axisComponent, DataStorage<? extends HudAnchor> dataAnchor, DataStorage<Integer> data) {
		super(settings, min, max, 1, 5, data);
		this.axisComponent = axisComponent;
		this.dataAnchor = dataAnchor;
		updateState();
	}
	
	@Override
	public void updateCache() {
		cached = data.get(stack());
		if (dataAnchor == null) return;
		cachedAnchor = dataAnchor.get(stack());
	}
	
	@Override
	protected void onValueChanged(Integer oldValue, Integer newValue) {
		data.syncToServer(stack(), newValue);
	}
	
	@Override
	public boolean onRightClick(double mouseX, double mouseY) {
		CompassProperties.toggleToServer(stack(), dataAnchor);
		return true;
	}
	
	@Override
	public void refreshTooltip() {
		var builder = TooltipBuilder.builder();
		String key = "tooltip.travelerscompass.settings." + data.id();
		builder.title(key);
		
		builder.line(holdToDecreaseComponent());
		builder.line(holdToChangeFaster());
		builder.line(Component.translatable("tooltip.travelerscompass.settings.hud_pos.anchor", CompassUI.RBM).withStyle(ChatFormatting.GRAY));
		
		if (shiftPressed) {
			builder.desc(Component.translatable(key + ".desc", axisComponent).withStyle(ChatFormatting.GRAY));
		}
		
		Component anchorValue = cachedAnchor.asComponent().withColor(CompassUI.Colors.SOFT_GRAY);
		builder.state(Component.translatable("tooltip.travelerscompass.settings.hud_pos.anchor.value", anchorValue).withStyle(ChatFormatting.GRAY));
		
		Component value = Component.literal(String.valueOf(cached)).withColor(CompassUI.Colors.SOFT_GRAY);
		builder.state(Component.translatable(key + ".value", value).withStyle(ChatFormatting.GRAY));
		this.setTooltip(builder.buildAsTooltip());
	}
	
}
