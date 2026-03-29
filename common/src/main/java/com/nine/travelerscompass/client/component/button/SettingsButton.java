package com.nine.travelerscompass.client.component.button;

import com.nine.travelerscompass.client.component.button.settings.ButtonGenericSettings;
import com.nine.travelerscompass.client.ui.constant.TCComponents;
import com.nine.travelerscompass.client.utils.Icon;
import com.nine.travelerscompass.client.utils.TooltipBuilder;
import com.nine.travelerscompass.common.data.CompassComponents;
import com.nine.travelerscompass.common.data.DataStorage;
import net.minecraft.network.chat.Component;

import java.util.function.BiFunction;
import java.util.function.Function;

public class SettingsButton<T> extends ConfigButton<T> {
	
	private static final String KEY = "tooltip.travelerscompass.settings.";
	
	public final Function<T, String> descKeyProvider;
	public final Function<T, Component> stateComponentProvider;
	
	
	//Todo: remove
	public static SettingsButton<Boolean> create(ButtonGenericSettings settings, DataStorage<Boolean> data, BiFunction<Boolean, Boolean, Icon> iconProvider) {
		return new SettingsButton<>(settings, data, iconProvider, (v -> (boolean) v ? TCComponents.ENABLED : TCComponents.DISABLED));
	}
	
	public SettingsButton(ButtonGenericSettings settings, DataStorage<T> data, BiFunction<T, Boolean, Icon> iconProvider, Function<T, Component> stateComponentProvider) {
		this(settings, data, iconProvider, (v -> KEY + data.id()), stateComponentProvider);
	}
	
	public SettingsButton(ButtonGenericSettings settings, DataStorage<T> data, BiFunction<T, Boolean, Icon> iconProvider, Function<T, String> descKeyProvider, Function<T, Component> stateComponentProvider) {
		super(settings, data, iconProvider);
		this.descKeyProvider = descKeyProvider;
		this.stateComponentProvider = stateComponentProvider;
		updateState();
	}
	
	@Override
	public boolean onLeftClick(double mouseX, double mouseY) {
		CompassComponents.toggleToServer(stack(), data);
		return true;
	}
	
	@Override
	public void refreshTooltip() {
		var builder = TooltipBuilder.builder();
		String key = KEY + data.id();
		builder.title(key);
		builder.descIf(descKeyProvider.apply(cached), shiftPressed);
		builder.state(stateComponentProvider.apply(cached));
		this.setTooltip(builder.buildAsTooltip());
	}
	
	
}
