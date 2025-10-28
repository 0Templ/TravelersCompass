package com.nine.travelerscompass.client.utils;

import com.nine.travelerscompass.client.CompassUI;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;


public class TooltipBuilder {
	
	private final List<Component> lines = new ArrayList<>();
	private final List<Component> bottomLines = new ArrayList<>();
	
	private TooltipBuilder() {
	}
	
	public static TooltipBuilder builder() {
		return new TooltipBuilder();
	}
	
	public TooltipBuilder title(String key) {
		return title(Component.translatable(key));
	}
	
	public TooltipBuilder title(Component component) {
		lines.addFirst(component);
		return this;
	}
	
	//Descriptions
	public TooltipBuilder descIf(Component desc, boolean cond) {
		if (cond) {
			return desc(desc);
		}
		return this;
	}
	
	public TooltipBuilder descIf(String baseKey, boolean cond) {
		if (cond) {
			return desc(baseKey);
		}
		return this;
	}
	
	public TooltipBuilder desc(String baseKey) {
		return desc(Component.translatable(baseKey + ".desc").copy().withStyle(ChatFormatting.GRAY));
	}
	
	public TooltipBuilder desc(Component component) {
		return line(CompassUI.DESC_ARROW.copy().append(component.copy()));
	}
	
	//Lines
	public TooltipBuilder lineIf(String component, boolean condition) {
		if (condition) {
			return line(component);
		}
		return this;
	}
	
	public TooltipBuilder lineIf(Component component, boolean condition) {
		if (condition) {
			return line(component);
		}
		return this;
	}
	
	public TooltipBuilder line(Component component) {
		lines.add(component);
		return this;
	}
	
	public TooltipBuilder line(String text) {
		lines.add(Component.translatable(text));
		return this;
	}
	
	//State
	public TooltipBuilder state(String key) {
		return state(Component.translatable(key));
	}
	
	public TooltipBuilder state(Component state) {
		bottomLines.add(state);
		return this;
	}
	
	public Component build() {
		boolean firstLine = true;
		MutableComponent ret = Component.empty();
		for (var line : lines) {
			if (firstLine) {
				ret.append(line);
				firstLine = false;
				continue;
			}
			ret.append("\n").append(line);
		}
		for (var line : bottomLines) {
			if (firstLine) {
				ret.append(line);
				firstLine = false;
				continue;
			}
			ret.append("\n").append(line);
		}
		return ret;
	}
	
	public Tooltip buildAsTooltip() {
		return Tooltip.create(this.build());
	}
	
}
