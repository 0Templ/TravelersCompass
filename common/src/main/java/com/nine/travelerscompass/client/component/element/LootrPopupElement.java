package com.nine.travelerscompass.client.component.element;

import com.nine.travelerscompass.client.ClientData;
import com.nine.travelerscompass.client.utils.ClientUtils;
import com.nine.travelerscompass.client.utils.IconTexture;
import com.nine.travelerscompass.client.utils.TextureData;
import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.data.DataStorage;
import com.nine.travelerscompass.compat.lootr.LootrSearchMode;
import com.nine.travelerscompass.config.TCConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LootrPopupElement extends NewSearchModePopupElement {

    private final ItemStack stack;

    private final DataStorage<LootrSearchMode> data = CompassProperties.LOOTR_MODE;
    private final LootrSearchMode.ConfigMode configMode;
    private final List<LootrSearchMode> availableModes = new ArrayList<>();
    private LootrSearchMode currentSearchMode;

    public LootrPopupElement(int width, int height, ItemStack stack) {
        super(width, height, TCConfig.ENABLE_LOOTR_SEARCH.get(), null, null, null, stack);
        this.configMode = TCConfig.LOOTR_SEARCH.get();
        this.stack = stack;
        this.currentSearchMode = CompassProperties.LOOTR_MODE.get(stack);
        switch (configMode) {
            case ALL -> {
                availableModes.add(LootrSearchMode.OFF);
                availableModes.add(LootrSearchMode.ALL);
                availableModes.add(LootrSearchMode.CLOSED);
                availableModes.add(LootrSearchMode.OPENED);
            }
            case CLOSED -> {
                availableModes.add(LootrSearchMode.OFF);
                availableModes.add(LootrSearchMode.CLOSED);
            }
            case OPENED -> {
                availableModes.add(LootrSearchMode.OFF);
                availableModes.add(LootrSearchMode.OPENED);
            }
        }
    }

    @Override
    protected TextureData getBaseLayer() {
        return buttonTextures.get(toggled, isHovered, active);
    }

    @Override
    public void refreshTooltip() {
        String key = "tooltip.travelerscompass.toggle_element.lootr";
        List<Component> components = new ArrayList<>();
        Component title = Component.translatable(key);
        components.add(title);
        key += "." + currentSearchMode.key();
        Component state = Component.translatable(key).withStyle(ChatFormatting.GRAY);
        if (shiftPressed) {
            components.add(Component.empty().append(ClientUtils.DESC_ARROW)
                    .append(Component.translatable(key + ".desc").withStyle(ChatFormatting.GRAY)));
        }
        components.add(state);
        setTooltip(components);
    }

    private static final IconTexture LOOTR_ALL = new IconTexture(ClientData.LOOTR_ALL, 1, 1);
    private static final IconTexture LOOTR_CLOSED = new IconTexture(ClientData.LOOTR_CLOSED, 1, 1);
    private static final IconTexture LOOTR_OPENED = new IconTexture(ClientData.LOOTR_OPENED, 1, 1);
    private static final IconTexture LOOTR_INACTIVE = new IconTexture(ClientData.LOOTR_INACTIVE, 1, 1);

    @Override
    protected IconTexture getIconTexture() {
        return switch (currentSearchMode){
            case ALL -> LOOTR_ALL;
            case CLOSED -> LOOTR_CLOSED;
            case OPENED -> LOOTR_OPENED;
            case OFF -> LOOTR_INACTIVE;
        };
    }

    @Override
    public void onClick(int button){
        LootrSearchMode searchMode = data.get(stack);
        int currentIndex = availableModes.indexOf(searchMode);
        var next = availableModes.get((currentIndex + 1) % availableModes.size());
        data.sendToServer(stack, next);
        setToggleState();
    }

    public void setToggleState() {
        LootrSearchMode current = data.get(stack);
        currentSearchMode = current;
        setToggled(!current.equals(LootrSearchMode.OFF));
    }

}
