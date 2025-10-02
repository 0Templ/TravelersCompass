package com.nine.travelerscompass.client.component.button;

import com.nine.travelerscompass.client.ClientData;
import com.nine.travelerscompass.client.component.element.NewSearchModePopupElement;
import com.nine.travelerscompass.client.component.element.NewTogglePopupElement;
import com.nine.travelerscompass.client.utils.IconTexture;
import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.config.TCConfig;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.item.ItemStack;

public class InventoriesButton extends SearchModePopupButton {

    public InventoriesButton(int x, int y, ItemStack stack) {
        super(x, y, stack, TCConfig.ENABLE_INVENTORIES_SEARCH.get(),
                new IconTexture(ClientData.INVENTORIES_ACTIVE, 2, 1),
                new IconTexture(ClientData.INVENTORIES_INACTIVE, 2, 1),
                ClientData.CONNECTOR_VERTICAL,
                ClientData.POPUP_1X2,
                CompassProperties.INVENTORIES,
                new Rect2i(0, 0, 13, 13),
                new Rect2i(-1, 0, 18, 29));

        NewTogglePopupElement playersElement = new NewSearchModePopupElement(9, 9,
                TCConfig.ENABLE_PLAYERS_INVENTORIES_SEARCH.get(),
                new IconTexture(ClientData.PLAYERS_INV_ACTIVE, 1, 1),
                new IconTexture(ClientData.PLAYERS_INV_INACTIVE, 1, 1),
                CompassProperties.INVENTORIES_PLAYERS, stack);

        NewTogglePopupElement mobsElement = new NewSearchModePopupElement(9, 9,
                true,
                new IconTexture(ClientData.MOBS_INV_ACTIVE, 1, 1),
                new IconTexture(ClientData.MOBS_INV_INACTIVE, 1, 1),
                CompassProperties.INVENTORIES_MOBS, stack);

        playersElement.init(-15, -4);
        mobsElement.init(-15, 7);

        popupElements.add(playersElement);
        popupElements.add(mobsElement);

        setToggled(data.get(stack));
        refreshTooltip();
        updateState();


    }

    @Override
    protected int getPopupXOffset() {
        return -19;
    }

    @Override
    protected int getPopupYOffset() {
        return -8;
    }

    @Override
    protected int getConnectorXOffset() {
        return -1;
    }

    @Override
    protected int getConnectorYOffset() {
        return 1;
    }

    @Override
    public void updateState() {
        boolean state = this.isToggled() &&
                (CompassProperties.INVENTORIES_PLAYERS.get(stack) || CompassProperties.INVENTORIES_MOBS.get(stack));
        setActivated(state);
    }

}
