package com.nine.travelerscompass.client.component.button;

import com.nine.travelerscompass.client.ui.constant.TCTextures;
import com.nine.travelerscompass.client.ui.constant.TCIcons;

import com.nine.travelerscompass.client.component.button.base.BaseIconButton;
import com.nine.travelerscompass.client.component.button.settings.ButtonGenericSettings;
import com.nine.travelerscompass.client.utils.Icon;
import com.nine.travelerscompass.client.utils.TextureData;
import com.nine.travelerscompass.client.utils.TooltipBuilder;
import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.common.data.CompassComponents;
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
		return TCTextures.Tabs.WARNING_TAB;
	}
	
	@Override
	protected Icon getIcon() {
		return TCIcons.Settings.WARNING_SIGN;
	}
	
	public void updateState(ItemStack stack) {
		if (hidden) {
			return;
		}
		if (CompassComponents.PAUSE.get(stack) && CompassComponents.SEARCH_STATE.get(stack) != SearchState.WIDE_SEARCHING) {
			this.warning = Warning.PAUSED;
		} else if (CompassContainer.container(stack).isEmpty()) {
			this.warning = Warning.EMPTY;
		} else if (!CompassComponents.BLOCKS.get(stack)
				&& !CompassComponents.MOBS.get(stack)
				&& !CompassComponents.SPAWNERS.get(stack)
				&& !CompassComponents.INVENTORIES.get(stack)
				&& !CompassComponents.DROP.get(stack)
				&& !CompassComponents.VILLAGERS.get(stack)
				&& !CompassComponents.CONTAINERS.get(stack)
				&& !CompassComponents.FLUIDS.get(stack)
				&& !CompassComponents.ITEM_ENTITIES.get(stack)
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
