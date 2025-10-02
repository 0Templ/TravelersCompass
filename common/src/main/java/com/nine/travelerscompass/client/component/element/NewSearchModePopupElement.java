package com.nine.travelerscompass.client.component.element;

import com.nine.travelerscompass.client.ClientData;
import com.nine.travelerscompass.client.utils.ClientUtils;
import com.nine.travelerscompass.client.utils.IconTexture;
import com.nine.travelerscompass.client.utils.TextureData;
import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.data.DataStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class NewSearchModePopupElement extends NewTogglePopupElement {

    private final ItemStack stack;

    private final DataStorage<?> data;

    public NewSearchModePopupElement(int width, int height, boolean active,
                                     IconTexture iconActive,
                                     IconTexture iconInactive,
                                     DataStorage<?> data,
                                     ItemStack stack) {
        super(width, height, iconActive, iconInactive);
        this.active = active;
        this.data = data;
        this.stack = stack;
    }

    @Override
    protected TextureData getBaseLayer() {
        return buttonTextures.get(isToggled(), isHovered, active);
    }

    @Override
    protected IconTexture getIconTexture() {
        return active ? isToggled() ? iconActive : iconInactive : new IconTexture(ClientData.LOCK_SMALL, 2, 1);
    }

    @Override
    public void refreshTooltip() {
        String key = "tooltip.travelerscompass.toggle_element." + data.id();
        List<Component> components = new ArrayList<>();
        Component title = Component.translatable(key);
        components.add(title);
        Component state = active ? (isToggled() ?  ENABLED : DISABLED) : CONFIG_DISABLED;
        if (shiftPressed && active) {
            components.add(Component.empty().append(ClientUtils.DESC_ARROW).append(Component.translatable(key + ".desc").withStyle(ChatFormatting.GRAY)));
        }
        components.add(state);
        setTooltip(components);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onClick(int button){
        CompassProperties.toggleToServer(stack, data);
        setToggled(((DataStorage<Boolean>)data).get(stack));
    }

    @Override
    public void init(int x, int y){
        super.init(x, y);
        setToggleState();
        refreshTooltip();
    }

    public void setToggleState(){
        setToggled(((DataStorage<Boolean>)data).get(stack));
    }

}
