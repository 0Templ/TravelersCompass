package com.nine.travelerscompass.client.component.button.range;

import com.nine.travelerscompass.client.component.button.settings.ButtonRangeSettings;
import com.nine.travelerscompass.common.data.DataStorage;

public class IntRangeButton extends RangeButton<Integer> {
	
	public IntRangeButton(ButtonRangeSettings settings, Integer minValue, Integer maxValue, Integer step, Integer ctrlStep, DataStorage<Integer> data) {
		super(settings, minValue, maxValue, step, ctrlStep, (value -> (int) Math.round(value)), data);
	}
	
}
