package com.nine.travelerscompass.client.component.button;

import com.nine.travelerscompass.client.ClientData;
import com.nine.travelerscompass.client.component.element.NewSearchModePopupElement;
import com.nine.travelerscompass.client.component.element.NewTogglePopupElement;
import com.nine.travelerscompass.client.utils.IconTexture;
import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.config.TCConfig;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.item.ItemStack;

public class VillagersButton extends SearchModePopupButton {

    public VillagersButton(int x, int y, ItemStack stack) {
        super(x, y, stack, TCConfig.ENABLE_VILLAGERS_SEARCH.get(),
                new IconTexture(ClientData.VILLAGERS_ACTIVE, 1, 1),
                new IconTexture(ClientData.VILLAGERS_INACTIVE, 1, 1),
                ClientData.CONNECTOR_VERTICAL,
                ClientData.POPUP_2X1,
                CompassProperties.VILLAGERS,
                new Rect2i(0, 0, 13, 13),
                new Rect2i(-1, 0, 29, 18));

        NewTogglePopupElement buysElement = new NewSearchModePopupElement(9, 9,
                true,
                new IconTexture(ClientData.VILLAGERS_BUYS_ACTIVE, 1, 0),
                new IconTexture(ClientData.VILLAGERS_BUYS_INACTIVE, 1, 0),
                CompassProperties.VILLAGERS_BUYS, stack);

        NewTogglePopupElement sellsElement = new NewSearchModePopupElement(9, 9,
                true,
                new IconTexture(ClientData.VILLAGERS_SELLS_ACTIVE, 0, 0),
                new IconTexture(ClientData.VILLAGERS_SELLS_INACTIVE, 0, 0),
                CompassProperties.VILLAGERS_SELLS, stack);

        buysElement.init(-26, 2);
        sellsElement.init(-15, 2);

        popupElements.add(buysElement);
        popupElements.add(sellsElement);


        setToggled(data.get(stack));
        refreshTooltip();
        updateState();
    }

    @Override
    protected int getPopupXOffset() {
        return -30;
    }

    @Override
    protected int getPopupYOffset() {
        return -2;
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
                (CompassProperties.VILLAGERS_BUYS.get(stack) || CompassProperties.VILLAGERS_SELLS.get(stack));
        setActivated(state);
    }


}
