package com.nine.travelerscompass.client.component.button;

import com.nine.travelerscompass.client.CompassUI;
import com.nine.travelerscompass.client.component.button.base.BaseIconButton;
import com.nine.travelerscompass.client.component.button.settings.ButtonGenericSettings;
import com.nine.travelerscompass.client.utils.Icon;
import com.nine.travelerscompass.client.utils.TextureData;
import com.nine.travelerscompass.client.utils.TooltipBuilder;
import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.utils.SearchState;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class WarningButton extends BaseIconButton {
	
	public enum Warning {
		PAUSED,
		EMPTY,
		INACTIVE,
		NORMAL,
	}
	
	public boolean hidden = false;
	
	private Warning warning;
	
	public WarningButton(ButtonGenericSettings settings) {
		super(settings);
		refreshTooltip();
	}
	
	@Override
	protected TextureData getMainLayerTexture() {
		return CompassUI.TabTextures.WARNING_TAB;
	}
	
	@Override
	protected Icon getIcon() {
		return CompassUI.SettingsTextures.WARNING_SIGN_ICON;
	}
	
	public void updateState(ItemStack stack) {
		if (hidden) {
			return;
		}
		if (CompassProperties.PAUSE.get(stack) && CompassProperties.SEARCH_STATE.get(stack) != SearchState.WIDE_SEARCHING) {
			this.warning = Warning.PAUSED;
		} else if (CompassContainer.container(stack).isEmpty()) {
			this.warning = Warning.EMPTY;
		} else if (!CompassProperties.BLOCKS.get(stack)
				&& !CompassProperties.MOBS.get(stack)
				&& !CompassProperties.SPAWNERS.get(stack)
				&& !CompassProperties.INVENTORIES.get(stack)
				&& !CompassProperties.DROP.get(stack)
				&& !CompassProperties.VILLAGERS.get(stack)
				&& !CompassProperties.CONTAINERS.get(stack)
				&& !CompassProperties.FLUIDS.get(stack)
				&& !CompassProperties.ITEM_ENTITIES.get(stack)
		) {
			this.warning = Warning.INACTIVE;
		} else {
			this.warning = Warning.NORMAL;
		}
		refreshTooltip();
	}
	
	@Override
	public void tick() {
		refreshTooltip();
	}
	
	@Override
	public void refreshTooltip() {
		if (warning == Warning.NORMAL || hidden) {
			setTooltip(Tooltip.create(Component.empty()));
			return;
		}
		String key = "tooltip.travelerscompass.warning." + warning.name().toLowerCase();
		setTooltip(TooltipBuilder.builder().title(key).desc(key).buildAsTooltip());
	}
	
}
