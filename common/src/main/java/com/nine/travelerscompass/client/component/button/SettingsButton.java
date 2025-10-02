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
import java.util.function.Function;
import java.util.function.Supplier;

public class SettingsButton extends BaseButton {

    protected final Supplier<ItemStack> supplier;
    private final DataStorage<?> data;
    private final Function<Object, DisplayDataHolder> displayDataProvider;
    public Object cachedValue;
    private DisplayDataHolder cachedDisplayData;

    public record DisplayDataHolder(IconTexture icon, IconTexture iconHovered, Component tooltipState){}

    public SettingsButton(int x, int y, int width, int height,
                          DataStorage<?> data,
                          Function<Object, DisplayDataHolder> displayDataProvider,
                          Supplier<ItemStack> supplier) {
        this(x, y, width, height, data, displayDataProvider, supplier, (button) -> {});
    }

    public SettingsButton(int x, int y, int width, int height,
                          DataStorage<?> data,
                          Function<Object, DisplayDataHolder> displayDataProvider,
                          Supplier<ItemStack> supplier, OnPress onPress) {
        super(x, y, width, height, onPress);
        this.data = data;
        this.displayDataProvider = displayDataProvider;
        this.supplier = supplier;
        updateState();
    }

    public void updateState(){
        ItemStack stack = supplier.get();
        cachedValue = data.get(stack);
        cachedDisplayData = displayDataProvider.apply(cachedValue);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        ItemStack stack = supplier.get();
        CompassProperties.toggleFromClient(stack, data, true);
        updateState();
        refreshTooltip();
    }

    @Override
    public void onEnter(){
        updateState();
        refreshTooltip();
    }

    @Override
    protected void renderMainLayer(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        TextureData baseLayer = ClientData.TOGGLE_BUTTON.get(false, isHovered());
        ClientUtils.renderTexture(graphics, baseLayer, this.getX(), this.getY());
    }

    public void renderIcon(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        IconTexture iconLayer = isHovered() ? cachedDisplayData.iconHovered : cachedDisplayData.icon;
        ClientUtils.renderTexture(graphics, iconLayer, this.getX(), this.getY());
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.renderWidget(graphics, mouseX, mouseY, partialTicks);
        renderIcon(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void refreshTooltip(){
        List<Component> list = new ArrayList<>();
        String key = "tooltip.travelerscompass.settings." + data.getKey();
        Component title = Component.translatable(key);
        Component state = cachedDisplayData.tooltipState;
        list.add(title);
        if (shiftPressed){
            list.add(Component.empty().append(ClientUtils.DESC_ARROW).append(Component.translatable(key + ".desc").withStyle(ChatFormatting.GRAY)));
        }
        list.add(state);
        this.setTooltip(Tooltip.create(ClientUtils.buildTooltip(list)));
    }

}
