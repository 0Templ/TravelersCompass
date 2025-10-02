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
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SearchModeButton extends ToggleButton {

    private final DataStorage<Boolean> data;

    protected final IconTexture iconInactive;
    protected final IconTexture iconActive;

    private final ItemStack stack;

    private final boolean enabled;

    public SearchModeButton(int x, int y, int width, int height, boolean enabled,
                            DataStorage<Boolean> data, IconTexture iconActive, IconTexture iconInactive, ItemStack stack) {
        super(x, y, width, height, ClientData.TOGGLE_BUTTON);
        this.data = data;
        this.stack = stack;
        this.iconActive = iconActive;
        this.iconInactive = iconInactive;
        this.enabled = enabled;
        this.active = enabled;
        setToggled(data.get(stack));
        refreshTooltip();
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        CompassProperties.toggleToServer(stack, data, true);
        setToggled(data.get(stack));
        refreshTooltip();
    }

    @Override
    protected TextureData getBaseLayer(){
        return buttonTextures.get(toggled, isHovered, enabled);
    }

    @Override
    public void refreshTooltip(){
        List<Component> list = new ArrayList<>();
        String key = "tooltip.travelerscompass.search_mode." + data.id();
        Component title = Component.translatable(key);
        Component state;
        list.add(title);
        if (enabled){
            state = (isToggled() ? Toggleable.ENABLED : Toggleable.DISABLED);
            if (shiftPressed){
                list.add(Component.empty().append(ClientUtils.DESC_ARROW).append(Component.translatable(key + ".desc").withStyle(ChatFormatting.GRAY)));
            }
        }
        else {
            state = Toggleable.CONFIG_DISABLED;
        }
        list.add(state);
        this.setTooltip(Tooltip.create(ClientUtils.buildTooltip(list)));
    }

    public void renderIcon(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        IconTexture icon = isToggled() ? iconActive : iconInactive;
        ClientUtils.renderTexture(graphics, icon, this.getX(), this.getY());
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.renderWidget(graphics, mouseX, mouseY, partialTicks);
        if (enabled){
            renderIcon(graphics, mouseX, mouseY, partialTicks);
        }
        else {
            TextureData lock = ClientData.LOCK;
            ClientUtils.renderTexture(graphics, lock, this.getX() + 3, this.getY() + 2);
        }
    }

}
