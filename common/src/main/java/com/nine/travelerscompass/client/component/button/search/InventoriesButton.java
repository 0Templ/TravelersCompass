package com.nine.travelerscompass.client.component.button.search;

import com.nine.travelerscompass.client.ui.constant.TCTextures;
import com.nine.travelerscompass.client.ui.constant.TCIcons;

import com.nine.travelerscompass.client.component.button.settings.ButtonSearchModeSettings;
import com.nine.travelerscompass.common.data.CompassComponents;
import com.nine.travelerscompass.config.TCConfig;

public class InventoriesButton extends SearchModePopupButton {
	
	public InventoriesButton(ButtonSearchModeSettings settings) {
		super(
				settings,
				TCTextures.Popup.POPUP_1X2.layer(-19, -8),
				TCTextures.Popup.CONNECTOR_VERTICAL.layer(-1, 1)
		);
		
		var builder = ButtonSearchModeSettings.builder()
				.position(getX(), getY())
				.size(10, 10)
				.lockIcon(TCIcons.Common.SMALL_LOCK)
				.mainLayerSet(TCTextures.Buttons.SMALL_TOGGLE_BUTTON)
				.stackSup(stackSupplier());
		
		var playersInvButton = new SearchModeButton(builder.moved(-15, -4)
				.data(CompassComponents.INVENTORIES_PLAYERS)
				.configEnabled(TCConfig.ENABLE_PLAYERS_INVENTORIES_SEARCH.get())
				.icons(TCIcons.SearchMode.INVENTORIES_PLAYERS_ACTIVE,
						TCIcons.SearchMode.INVENTORIES_PLAYERS_INACTIVE)
				.build());
		
		var mobsInvButton = new SearchModeButton(builder.moved(0, 11)
				.data(CompassComponents.INVENTORIES_MOBS)
				.configEnabled(true)
				.icons(TCIcons.SearchMode.INVENTORIES_MOBS_ACTIVE,
						TCIcons.SearchMode.INVENTORIES_MOBS_INACTIVE)
				.build());
		
		popupButtons.add(playersInvButton);
		popupButtons.add(mobsInvButton);
		
	}
	
	@Override
	public void checkRelevancy() {
		setRelevant(isToggled() &&
				(CompassComponents.INVENTORIES_PLAYERS.get(stack()) || CompassComponents.INVENTORIES_MOBS.get(stack()))
		);
	}
	
}
