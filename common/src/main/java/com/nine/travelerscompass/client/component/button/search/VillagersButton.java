package com.nine.travelerscompass.client.component.button.search;

import com.nine.travelerscompass.client.component.button.settings.ButtonSearchModeSettings;
import com.nine.travelerscompass.client.ui.constant.TCIcons;
import com.nine.travelerscompass.client.ui.constant.TCTextures;
import com.nine.travelerscompass.common.data.CompassComponents;

public class VillagersButton extends SearchModePopupButton {
	
	public VillagersButton(ButtonSearchModeSettings settings) {
		super(
				settings,
				TCTextures.Popup.POPUP_2X1.layer(-30, -2),
				TCTextures.Popup.CONNECTOR_VERTICAL.layer(-1, 1)
		);
		
		var builder = ButtonSearchModeSettings.builder()
				.position(getX(), getY())
				.size(10, 10)
				.lockIcon(TCIcons.Common.SMALL_LOCK)
				.mainLayerSet(TCTextures.Buttons.SMALL_TOGGLE_BUTTON)
				.stackSup(stackSupplier());
		
		var villagerSellsButton = new SearchModeButton(builder.moved(-26, 2)
				.data(CompassComponents.VILLAGERS_SELLS)
				.configEnabled(true)
				.icons(TCIcons.SearchMode.VILLAGERS_SELLS_ACTIVE,
						TCIcons.SearchMode.VILLAGERS_SELLS_INACTIVE)
				.build());
		
		var villagerBuysButton = new SearchModeButton(builder.moved(11, 0)
				.data(CompassComponents.VILLAGERS_BUYS)
				.configEnabled(true)
				.icons(TCIcons.SearchMode.VILLAGERS_BUYS_ACTIVE,
						TCIcons.SearchMode.VILLAGERS_BUYS_INACTIVE)
				.build());
		
		popupButtons.add(villagerSellsButton);
		popupButtons.add(villagerBuysButton);
		
	}
	
	@Override
	public void checkRelevancy() {
		setRelevant(isToggled() &&
				(CompassComponents.VILLAGERS_SELLS.get(stack()) || CompassComponents.VILLAGERS_BUYS.get(stack()))
		);
	}
	
}
