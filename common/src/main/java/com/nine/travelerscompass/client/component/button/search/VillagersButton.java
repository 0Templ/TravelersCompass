package com.nine.travelerscompass.client.component.button.search;

import com.nine.travelerscompass.client.CompassUI;
import com.nine.travelerscompass.client.component.button.settings.ButtonSearchModeSettings;
import com.nine.travelerscompass.common.data.CompassComponents;

public class VillagersButton extends SearchModePopupButton {
	
	public VillagersButton(ButtonSearchModeSettings settings) {
		super(
				settings,
				CompassUI.PopupTextures.POPUP_2X1.layer(-30, -2),
				CompassUI.PopupTextures.CONNECTOR_VERTICAL.layer(-1, 1)
		);
		
		var builder = ButtonSearchModeSettings.builder()
				.position(getX(), getY())
				.size(10, 10)
				.lockIcon(CompassUI.CommonTextures.SMALL_LOCK_ICON)
				.mainLayerSet(CompassUI.ButtonTextures.SMALL_TOGGLE_BUTTON)
				.stackSup(stackSupplier());
		
		var villagerSellsButton = new SearchModeButton(builder.moved(-26, 2)
				.data(CompassComponents.VILLAGERS_SELLS)
				.configEnabled(true)
				.icons(CompassUI.SearchModeTextures.VILLAGERS_SELLS_ACTIVE_ICON,
						CompassUI.SearchModeTextures.VILLAGERS_SELLS_INACTIVE_ICON)
				.build());
		
		var villagerBuysButton = new SearchModeButton(builder.moved(11, 0)
				.data(CompassComponents.VILLAGERS_BUYS)
				.configEnabled(true)
				.icons(CompassUI.SearchModeTextures.VILLAGERS_BUYS_ACTIVE_ICON,
						CompassUI.SearchModeTextures.VILLAGERS_BUYS_INACTIVE_ICON)
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
