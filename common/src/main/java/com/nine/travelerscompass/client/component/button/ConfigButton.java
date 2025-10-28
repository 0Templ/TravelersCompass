package com.nine.travelerscompass.client.component.button;

import com.nine.travelerscompass.client.component.button.base.BaseIconButton;
import com.nine.travelerscompass.client.component.button.settings.ButtonGenericSettings;
import com.nine.travelerscompass.client.utils.Icon;
import com.nine.travelerscompass.client.utils.TextureData;
import com.nine.travelerscompass.common.data.DataStorage;

import java.util.function.BiFunction;

public abstract class ConfigButton<T> extends BaseIconButton {
	
	public final DataStorage<T> data;
	public final BiFunction<T, Boolean, Icon> iconProvider;
	
	public T cached;
	
	public ConfigButton(ButtonGenericSettings settings, DataStorage<T> data, BiFunction<T, Boolean, Icon> iconProvider) {
		super(settings);
		this.data = data;
		this.iconProvider = iconProvider;
	}
	
	public void updateState() {
		this.cached = data.get(stack());
		refreshTooltip();
	}
	
	@Override
	protected TextureData getMainLayerTexture() {
		return buttonTexturesSet().get(false, isHovered, true);
	}
	
	@Override
	protected Icon getIcon() {
		return iconProvider.apply(cached, isHovered);
	}
	
}
