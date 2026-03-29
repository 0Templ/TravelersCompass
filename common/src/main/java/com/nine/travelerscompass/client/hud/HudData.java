package com.nine.travelerscompass.client.hud;

import com.nine.travelerscompass.client.utils.ClientUtils;
import com.nine.travelerscompass.common.search.location.ILocationObject;
import com.nine.travelerscompass.common.utils.SearchState;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

import java.util.Objects;


public class HudData {
	
	private ILocationObject locationObject;
	
	private boolean paused;
	
	private SearchState searchState;
	
	private boolean selected;
	
	public boolean dirty;
	
	private HudSettings settings;
	
	private Component targetName = Component.empty();
	
	private Component state = Component.translatable("hud.travelerscompass.compact.state.idle").withStyle(ChatFormatting.GRAY);
	
	public HudData(HudSettings settings, boolean paused, boolean selected, SearchState searchState) {
		this.settings = settings;
		this.paused = paused;
		this.selected = selected;
		this.searchState = searchState;
	}
	
	public ILocationObject getLocationObject() {
		return locationObject;
	}
	
	public boolean isPaused() {
		return paused;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public SearchState searchState() {
		return searchState;
	}
	
	public HudSettings getSettings() {
		return settings;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public void setSearchState(SearchState state) {
		this.searchState = state;
	}
	
	public void setLocationObject(ILocationObject newLocationObject) {
		if (!Objects.equals(this.locationObject, newLocationObject)) {
			this.locationObject = newLocationObject;
			markDirty();
		}
	}
	
	public void setPaused(boolean paused) {
		this.paused = paused;
		markDirty();
	}
	
	public void updateSettings(HudSettings value) {
		if (!value.equals(settings)) {
			this.settings = value;
			markDirty();
		}
	}
	
	public Component getTargetName() {
		return targetName;
	}
	
	public void setTargetName(Component targetName) {
		this.targetName = targetName;
	}
	
	public Component getState() {
		return state;
	}
	
	public void setState(Component state) {
		this.state = state;
	}
	
	public void markDirty() {
		this.dirty = true;
	}
	
	public boolean isDirty() {
		return dirty;
	}
	
	public void clearDirty() {
		this.dirty = false;
	}
	
	public void updateCachedData(Font font, int targetPrefLength) {
		if (this.locationObject == null) {
			this.targetName = Component.empty();
		} else {
			Component formattedNameComponent = ClientUtils.getLocationObjectTargetName(this.locationObject);
			setTargetName(ClientUtils.cutComponent(font, formattedNameComponent, this.settings.width() - targetPrefLength));
		}
		
		if (this.locationObject != null) {
			this.state = (Component.translatable("hud.travelerscompass.compact.state.found")
					.withStyle(ChatFormatting.GRAY));
		} else if (this.searchState == SearchState.SEARCHING || this.searchState == SearchState.WIDE_SEARCHING) {
			this.state = (Component.translatable("hud.travelerscompass.compact.state.searching")
					.withStyle(ChatFormatting.GRAY));
		} else if (this.paused) {
			this.state = (Component.translatable("hud.travelerscompass.compact.state.pause")
					.withStyle(ChatFormatting.GRAY));
		} else {
			this.state = (Component.translatable("hud.travelerscompass.compact.state.idle")
					.withStyle(ChatFormatting.GRAY));
		}
	}
	
}
