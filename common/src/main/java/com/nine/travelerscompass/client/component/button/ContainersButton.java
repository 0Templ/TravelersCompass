package com.nine.travelerscompass.client.component.button;

import com.nine.travelerscompass.client.ClientData;
import com.nine.travelerscompass.client.component.element.LootrPopupElement;
import com.nine.travelerscompass.client.component.element.NewSearchModePopupElement;
import com.nine.travelerscompass.client.component.element.NewTogglePopupElement;
import com.nine.travelerscompass.client.utils.IconTexture;
import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.compat.CompatibilityHelper;
import com.nine.travelerscompass.compat.lootr.LootrSearchMode;
import com.nine.travelerscompass.config.TCConfig;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.item.ItemStack;

public class ContainersButton extends SearchModePopupButton {

    private final int popupXOffset;

    public ContainersButton(int x, int y, ItemStack stack) {
        super(x, y, stack, TCConfig.ENABLE_CONTAINERS_SEARCH.get(),
                new IconTexture(ClientData.CONTAINERS_ACTIVE, 2, 2),
                new IconTexture(ClientData.CONTAINERS_INACTIVE, 2, 2),
                ClientData.CONNECTOR_HORIZONTAL,
                CompatibilityHelper.LOOTR_LOADED ? ClientData.POPUP_3X1 : ClientData.POPUP_2X1,
                CompassProperties.CONTAINERS,
                new Rect2i(0, 0, 13, 13),
                new Rect2i(-1, 0, 39, 18));

        NewTogglePopupElement blockContainersElement = new NewSearchModePopupElement(9, 9,
                TCConfig.ENABLE_BLOCK_CONTAINERS_SEARCH.get(),
                new IconTexture(ClientData.BLOCK_CONTAINERS_ACTIVE, 1, 1),
                new IconTexture(ClientData.BLOCK_CONTAINERS_INACTIVE, 1, 1),
                CompassProperties.CONTAINERS_CHESTS, stack);

        NewTogglePopupElement minecartsElement = new NewSearchModePopupElement(9, 9,
                true,
                new IconTexture(ClientData.MINECARTS_ACTIVE, 0, 0),
                new IconTexture(ClientData.MINECARTS_INACTIVE, 0, 0),
                CompassProperties.MINECARTS, stack);

        if (CompatibilityHelper.LOOTR_LOADED){
            NewTogglePopupElement lootrElement = new LootrPopupElement(9, 9, stack);
            popupXOffset = -13;
            blockContainersElement.init(-9, -15);
            minecartsElement.init(2, -15);
            lootrElement.init(13, -15);
            popupElements.add(lootrElement);
        }
        else {
            popupXOffset = -8;
            blockContainersElement.init(-4, -15);
            minecartsElement.init(7, -15);
        }

        popupElements.add(blockContainersElement);
        popupElements.add(minecartsElement);

        setToggled(data.get(stack));
        updateState();
        refreshTooltip();
    }

    @Override
    protected int getPopupXOffset() {
        return popupXOffset;
    }

    @Override
    protected int getPopupYOffset() {
        return -19;
    }

    @Override
    protected int getConnectorXOffset() {
        return 1;
    }

    @Override
    protected int getConnectorYOffset() {
        return -1;
    }

    @Override
    public void updateState() {
        boolean state = this.isToggled() &&
                (CompassProperties.CONTAINERS_CHESTS.get(stack)
                || (!CompatibilityHelper.LOOTR_LOADED || !CompassProperties.LOOTR_MODE.get(stack).equals(LootrSearchMode.OFF))
                || CompassProperties.MINECARTS.get(stack));
        setActivated(state);
    }

}
