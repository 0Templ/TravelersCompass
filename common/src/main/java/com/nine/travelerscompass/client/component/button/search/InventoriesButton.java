package com.nine.travelerscompass.client.component.button.search;

import com.nine.travelerscompass.client.CompassUI;
import com.nine.travelerscompass.client.component.button.settings.ButtonSearchModeSettings;
import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.config.TCConfig;

public class InventoriesButton extends SearchModePopupButton {
	
	public InventoriesButton(ButtonSearchModeSettings settings) {
		super(
				settings,
				CompassUI.PopupTextures.POPUP_1X2.layer(-19, -8),
				CompassUI.PopupTextures.CONNECTOR_VERTICAL.layer(-1, 1)
		);
		
		var builder = ButtonSearchModeSettings.builder()
				.position(getX(), getY())
				.size(10, 10)
				.lockIcon(CompassUI.CommonTextures.SMALL_LOCK_ICON)
				.mainLayerSet(CompassUI.ButtonTextures.SMALL_TOGGLE_BUTTON)
				.stackSup(stackSupplier());
		
		var playersInvButton = new SearchModeButton(builder.moved(-15, -4)
				.data(CompassProperties.INVENTORIES_PLAYERS)
				.configEnabled(TCConfig.ENABLE_PLAYERS_INVENTORIES_SEARCH.get())
				.icons(CompassUI.SearchModeTextures.INVENTORIES_PLAYERS_ACTIVE_ICON,
						CompassUI.SearchModeTextures.INVENTORIES_PLAYERS_INACTIVE_ICON)
				.build());
		
		var mobsInvButton = new SearchModeButton(builder.moved(0, 11)
				.data(CompassProperties.INVENTORIES_MOBS)
				.configEnabled(true)
				.icons(CompassUI.SearchModeTextures.INVENTORIES_MOBS_ACTIVE_ICON,
						CompassUI.SearchModeTextures.INVENTORIES_MOBS_INACTIVE_ICON)
				.build());
		
		popupButtons.add(playersInvButton);
		popupButtons.add(mobsInvButton);
		
	}
	
	@Override
	public void checkRelevancy() {
		setRelevant(isToggled() &&
				(CompassProperties.INVENTORIES_PLAYERS.get(stack()) || CompassProperties.INVENTORIES_MOBS.get(stack()))
		);
	}
	
}
