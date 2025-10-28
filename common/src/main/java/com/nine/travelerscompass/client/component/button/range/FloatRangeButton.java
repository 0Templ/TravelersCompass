package com.nine.travelerscompass.client.component.button.range;

import com.nine.travelerscompass.client.component.button.settings.ButtonRangeSettings;
import com.nine.travelerscompass.common.data.DataStorage;


public class FloatRangeButton extends RangeButton<Float> {
	
	public FloatRangeButton(ButtonRangeSettings settings, Float minValue, Float maxValue, Float step, Float ctrlStep, DataStorage<Float> data) {
		super(settings, minValue, maxValue, step, ctrlStep, (value -> (float) value), data);
	}
	
	@Override
	public String cachedAsString() {
		return String.format("%.2f", cached);
	}
	
}