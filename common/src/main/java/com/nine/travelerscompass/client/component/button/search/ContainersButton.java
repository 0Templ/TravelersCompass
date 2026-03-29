package com.nine.travelerscompass.client.component.button.search;

import com.nine.travelerscompass.client.component.button.settings.ButtonSearchModeSettings;
import com.nine.travelerscompass.client.ui.constant.TCIcons;
import com.nine.travelerscompass.client.ui.constant.TCTextures;
import com.nine.travelerscompass.common.data.CompassComponents;
import com.nine.travelerscompass.compat.lootr.LootrSearchMode;
import com.nine.travelerscompass.config.TCConfig;
import com.nine.travelerscompass.platform.Platform;

public class ContainersButton extends SearchModePopupButton {
	
	private final boolean LOOTR = Platform.PLATFORM.isModLoaded("lootr");
	
	public ContainersButton(ButtonSearchModeSettings settings) {
		super(
				settings,
				Platform.PLATFORM.isModLoaded("lootr") ? TCTextures.Popup.POPUP_3X1.layer(-13, -19) : TCTextures.Popup.POPUP_2X1.layer(-7, -19),
				TCTextures.Popup.CONNECTOR_HORIZONTAL.layer(1, -1)
		);
		
		var builder = ButtonSearchModeSettings.builder()
				.position(getX(), getY())
				.size(10, 10)
				.lockIcon(TCIcons.Common.SMALL_LOCK)
				.mainLayerSet(TCTextures.Buttons.SMALL_TOGGLE_BUTTON)
				.stackSup(stackSupplier());
		
		var blockContainersButton = new SearchModeButton(builder.moved(LOOTR ? -9 : -3, -15)
				.data(CompassComponents.BLOCK_CONTAINERS)
				.icons(TCIcons.SearchMode.BLOCK_CONTAINERS_ACTIVE,
						TCIcons.SearchMode.BLOCK_CONTAINERS_INACTIVE)
				.configEnabled(TCConfig.ENABLE_BLOCK_CONTAINERS_SEARCH.get())
				.build());
		
		var minecartsContainersButton = new SearchModeButton(builder.moved(11, 0)
				.data(CompassComponents.ENTITY_CONTAINERS)
				.icons(TCIcons.SearchMode.ENTITY_CONTAINERS_ACTIVE,
						TCIcons.SearchMode.ENTITY_CONTAINERS_INACTIVE)
				.configEnabled(true)
				.build());
		
		popupButtons.add(blockContainersButton);
		popupButtons.add(minecartsContainersButton);
		
		if (LOOTR) {
			var lootrContainersButton = new LootrSearchModeButton(builder.moved(11, 0).build());
			popupButtons.add(lootrContainersButton);
		}
	}
	
	@Override
	public void checkRelevancy() {
		setRelevant(isToggled() &&
				(CompassComponents.BLOCK_CONTAINERS.get(stack())
						|| CompassComponents.ENTITY_CONTAINERS.get(stack())
						|| (LOOTR && CompassComponents.LOOTR_MODE.get(stack()) != LootrSearchMode.OFF)
				)
		);
	}
	
}
