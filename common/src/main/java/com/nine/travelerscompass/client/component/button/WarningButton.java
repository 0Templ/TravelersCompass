package com.nine.travelerscompass.client.component.button;

import com.nine.travelerscompass.client.ClientData;
import com.nine.travelerscompass.client.utils.ClientUtils;
import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.utils.SearchState;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class WarningButton extends BaseButton {

    public enum Warning {
        PAUSED,
        EMPTY,
        INACTIVE,
        NORMAL,
    }

    public boolean hidden = false;

    private Warning warning;

    public WarningButton(int x, int y, int width, int height, ItemStack stack) {
        super(x, y, width, height);
        updateState(stack);
        refreshTooltip();
    }

    protected void renderMainLayer(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        if (warning != Warning.NORMAL){
            ClientUtils.renderTexture(graphics, ClientData.WARNING_SIGN, this.getX(), this.getY());
        }
    }

    public void updateState(ItemStack stack){
        if (hidden){
            return;
        }
        if (CompassProperties.PAUSE.get(stack) && CompassProperties.SEARCH_STATE.get(stack) != SearchState.WIDE_SEARCHING) {
            this.warning = Warning.PAUSED;
        }
        else if (CompassContainer.container(stack).isEmpty()) {
            this.warning = Warning.EMPTY;
        }
        else if (!CompassProperties.BLOCKS.get(stack)
        && !CompassProperties.MOBS.get(stack)
        && !CompassProperties.SPAWNERS.get(stack)
        && !CompassProperties.INVENTORIES.get(stack)
        && !CompassProperties.DROP.get(stack)
        && !CompassProperties.VILLAGERS.get(stack)
        && !CompassProperties.CONTAINERS.get(stack)
        && !CompassProperties.FLUIDS.get(stack)
        && !CompassProperties.ITEM_ENTITIES.get(stack)
        ){
            this.warning = Warning.INACTIVE;
        }
        else {
            this.warning = Warning.NORMAL;
        }
        refreshTooltip();
    }

    @Override
    public void refreshTooltip(){
        List<Component> components = new ArrayList<>();
        if (warning == Warning.NORMAL || hidden){
            setTooltip(Tooltip.create(Component.empty()));
            return;
        }
        String key = "tooltip.travelerscompass.warning." + warning.name().toLowerCase();
        Component title = Component.translatable(key);
        Component desc = Component.translatable(key + ".desc").withStyle(ChatFormatting.GRAY);
        components.add(title);
        components.add(desc);
        setTooltip(Tooltip.create(ClientUtils.buildTooltip(components)));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        return false;
    }

}
