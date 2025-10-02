package com.nine.travelerscompass.client.component.button;

import com.nine.travelerscompass.client.ClientData;
import com.nine.travelerscompass.client.utils.ClientUtils;
import com.nine.travelerscompass.client.utils.IconTexture;
import com.nine.travelerscompass.client.utils.TextureData;
import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.data.DataStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SearchModePopupButton extends TogglePopupButton {

    protected final DataStorage<Boolean> data;
    protected final ItemStack stack;
    protected final IconTexture iconActive;
    protected final IconTexture iconInactive;
    protected final boolean enabled;

    protected SearchModePopupButton(int x, int y, ItemStack stack,
                                    boolean enabled,
                                    IconTexture iconActive,
                                    IconTexture iconInactive,
                                    TextureData popupConnector,
                                    TextureData popupTexture,
                                    DataStorage<Boolean> data,
                                    Rect2i mainRect,
                                    Rect2i popupRect) {
        super(x, y, 14, 14, ClientData.TOGGLE_BUTTON, popupConnector, popupTexture, mainRect, popupRect);
        this.stack = stack;
        this.enabled = enabled;
        this.data = data;
        this.iconActive = iconActive;
        this.iconInactive = iconInactive;

    }

    public void updateState() {
    }

    @Override
    protected boolean isEnabled(){
        return enabled;
    }

    @Override
    protected void onMainButtonClick(int button){
        CompassProperties.toggleFromClient(stack, data);
        setToggled(data.get(stack));
    }

    @Override
    protected void afterClick(int button){
        updateState();
        refreshTooltip();
    }

    @Override
    protected void renderMainLayer(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        TextureData baseLayer = buttonTextures.get(toggled, isHovered, isEnabled());
        ClientUtils.renderTexture(graphics, baseLayer, this.getX(), this.getY());
    }

    @Override
    protected void renderIcon(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        IconTexture icon = isEnabled() ? isActivated() ? iconActive : iconInactive : new IconTexture(ClientData.LOCK, 3, 2);
        ClientUtils.renderTexture(graphics, icon, this.getX(), this.getY());
    }

    @Override
    public void refreshTooltip(){
        List<Component> list = new ArrayList<>();
        String key = "tooltip.travelerscompass.search_mode." + data.getKey();
        Component title = Component.translatable(key);
        list.add(title);
        Component state = (isToggled() ? (isActivated() ? ENABLED : INACTIVE) : DISABLED);
        if (isEnabled()){
            if (shiftPressed){
                list.add(Component.empty().append(ClientUtils.DESC_ARROW).append(Component.translatable(key + ".desc").withStyle(ChatFormatting.GRAY)));
            }
        }
        else {
            state = CONFIG_DISABLED;
        }
        list.add(state);
        this.setTooltip(Tooltip.create(ClientUtils.buildTooltip(list)));
    }
}
