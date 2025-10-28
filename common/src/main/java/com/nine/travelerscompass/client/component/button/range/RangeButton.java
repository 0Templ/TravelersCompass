package com.nine.travelerscompass.client.component.button.range;

import com.nine.travelerscompass.client.CompassUI;
import com.nine.travelerscompass.client.component.button.base.BaseIconButton;
import com.nine.travelerscompass.client.component.button.settings.ButtonRangeSettings;
import com.nine.travelerscompass.client.utils.*;
import com.nine.travelerscompass.common.data.DataStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;

import java.util.Objects;
import java.util.function.DoubleFunction;

public class RangeButton<T extends Number> extends BaseIconButton {
	
	public T cached;
	protected boolean outOfBounds = false;
	
	private final Icon icon;
	
	private final RangeIconVariants plusIcons;
	private final RangeIconVariants minusIcons;
	
	protected final DataStorage<T> data;
	private final T min, max;
	private final T step, ctrlStep;
	
	private final DoubleFunction<T> fromDouble;
	
	
	public RangeButton(ButtonRangeSettings settings, T minValue, T maxValue, T step, T ctrlStep, DoubleFunction<T> fromDouble, DataStorage<T> data) {
		super(settings);
		this.data = data;
		this.fromDouble = fromDouble;
		this.min = minValue;
		this.max = maxValue;
		this.step = step;
		this.ctrlStep = ctrlStep;
		this.icon = settings.icon;
		this.plusIcons = settings.plusIcons;
		this.minusIcons = settings.minusIcons;
		updateState();
	}
	
	protected T min() {
		return min;
	}
	
	protected T max() {
		return max;
	}
	
	protected boolean set(T value) {
		value = clamp(value);
		if (!Objects.equals(cached, value)) {
			onValueChanged(cached, value);
			return true;
		}
		return false;
	}
	
	protected void onValueChanged(T oldValue, T newValue) {
		data.syncToServer(stack(), newValue);
	}
	
	public final boolean isOutOfBounds(double v) {
		return Double.isNaN(v) || v < min().doubleValue() || v > max().doubleValue();
	}
	
	
	private T clamp(T value) {
		double v = value.doubleValue();
		double minVal = min().doubleValue();
		double maxVal = max().doubleValue();
		if (v < minVal) return min();
		if (v > maxVal) return max();
		return value;
	}
	
	protected Component holdToDecreaseComponent() {
		var shift = ClientUtils.coloredComponent(
				Component.translatable("tooltip.travelerscompass.settings.modification.shift"), shiftPressed ? CompassUI.Colors.GRAY : CompassUI.Colors.SOFT_GRAY);
		return Component.translatable("tooltip.travelerscompass.settings.modification.hold_to_decrease", shift).withStyle(ChatFormatting.GRAY);
	}
	
	protected Component holdToChangeFaster() {
		var ctrl = ClientUtils.coloredComponent(
				Component.translatable("tooltip.travelerscompass.settings.modification.ctrl"), ctrlPressed ? CompassUI.Colors.GRAY : CompassUI.Colors.SOFT_GRAY);
		return Component.translatable("tooltip.travelerscompass.settings.modification.hold_to_change_faster", ctrl).withStyle(ChatFormatting.GRAY);
	}
	
	public void updateCache() {
		cached = data.get(stack());
	}
	
	@Override
	protected void onKeyModifierChanged() {
		super.onKeyModifierChanged();
		updateState();
	}
	
	@Override
	public void updateState() {
		updateCache();
		outOfBounds = isOutOfBounds(cached.doubleValue() + (shiftPressed ? -1 : 1) * (ctrlPressed ? ctrlStep : step).doubleValue());
	}
	
	@Override
	public boolean onMouseScroll(double mouseX, double mouseY, double scrollX, double scrollY) {
		return set(fromDouble.apply(cached.doubleValue() + (scrollY < 0 ? -1 : 1) * (ctrlPressed ? ctrlStep : step).doubleValue()));
	}
	
	@Override
	public boolean onLeftClick(double mouseX, double mouseY) {
		return set(fromDouble.apply(cached.doubleValue() + (shiftPressed ? -1 : 1) * (ctrlPressed ? ctrlStep : step).doubleValue()));
	}
	
	@Override
	public void playMouseScrollSound(SoundManager soundManager, double scrollX, double scrollY) {
		soundManager.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK.value(), 1.05F, 0.12F));
	}
	
	@Override
	protected TextureData getMainLayerTexture() {
		return buttonTexturesSet().get(false, isHovered, !outOfBounds || !isHovered);
	}
	
	@Override
	protected Icon getIcon() {
		Icon ret;
		if (isHovered) {
			ret = shiftPressed ? minusIcons.get(ctrlPressed, !outOfBounds) : plusIcons.get(ctrlPressed, !outOfBounds);
		} else {
			ret = icon;
		}
		return ret;
	}
	
	public String cachedAsString() {
		return String.valueOf(cached);
	}
	
	@Override
	public void refreshTooltip() {
		var builder = TooltipBuilder.builder();
		String key = "tooltip.travelerscompass.settings." + data.id();
		builder.title(key);
		
		builder.line(holdToDecreaseComponent());
		builder.line(holdToChangeFaster());
		builder.descIf(key, shiftPressed);
		
		MutableComponent value = Component.literal(String.valueOf(cachedAsString())).withColor(CompassUI.Colors.SOFT_GRAY);
		builder.state(Component.translatable(key + ".value", value).withStyle(ChatFormatting.GRAY));
		this.setTooltip(builder.buildAsTooltip());
	}
	
}