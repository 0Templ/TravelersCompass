package com.nine.travelerscompass.client.component.button;

import com.nine.travelerscompass.client.ClientData;
import com.nine.travelerscompass.client.utils.ClientUtils;
import com.nine.travelerscompass.client.utils.IconTexture;
import com.nine.travelerscompass.client.utils.RangeUtils;
import com.nine.travelerscompass.client.utils.TextureData;
import com.nine.travelerscompass.common.data.DataStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.nine.travelerscompass.client.utils.RangeUtils.RangeAdapter;
import static com.nine.travelerscompass.client.utils.RangeUtils.RangeButtonHandler;

public class RangeButton<T extends Number> extends BaseButton implements RangeButtonHandler<T> {

    private final IconTexture PLUS = new IconTexture(ClientData.PLUS, 3, 3);
    private final IconTexture PLUS_INACTIVE = new IconTexture(ClientData.PLUS_INACTIVE, 3, 3);
    private final IconTexture PLUS_CTRL = new IconTexture(ClientData.PLUS_HOVERED, 3, 3);

    private final IconTexture MINUS = new IconTexture(ClientData.MINUS, 3, 3);
    private final IconTexture MINUS_INACTIVE = new IconTexture(ClientData.MINUS_INACTIVE, 3, 3);
    private final IconTexture MINUS_CTRL = new IconTexture(ClientData.MINUS_HOVERED, 3, 3);

    private final IconTexture icon;
    private final Supplier<ItemStack> supplier;

    private final DataStorage<T> data;
    private final T min, max;
    private final RangeAdapter<T> adapter;

    private T cached;

    public RangeButton(int x, int y, int width, int height, T minValue, T maxValue, RangeAdapter<T> adapter, IconTexture textureData, DataStorage<T> data, Supplier<ItemStack> supplier) {
        super(x, y, width, height);
        this.data = data;
        this.supplier = supplier;
        this.min = minValue;
        this.max = maxValue;
        this.adapter = adapter;
        this.icon = textureData;
        updateState();
    }

    @Override
    public T current(){
        return cached;
    }

    @Override
    public void set(T value){
        data.sendToServer(supplier.get(), value);
    }

    @Override
    public T min(){
        return this.min;
    }

    @Override
    public T max(){
        return this.max;
    }

    @Override
    public RangeAdapter<T> adapter(){
        return this.adapter;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        boolean changed = RangeUtils.changeValue(this, shiftPressed, ctrlPressed);
        if (changed){
            updateState();
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        boolean changed = RangeUtils.changeValue(this, scrollY < 0, ctrlPressed);
        if (changed){
            updateState();
        }
        return changed && super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    public void updateState(){
        ItemStack stack = supplier.get();
        cached = data.get(stack);
        refreshTooltip();
    }

    public void renderIcon(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks, boolean outOfBounds){
        IconTexture result;
        int xOff = 2;
        if (isHovered){
            if (shiftPressed){
                result = outOfBounds ? MINUS_INACTIVE : (ctrlPressed ? MINUS_CTRL : MINUS);
            }
            else {
                result = outOfBounds ? PLUS_INACTIVE : (ctrlPressed ? PLUS_CTRL : PLUS);
            }
            xOff = 3;
        }
        else {
            result = icon;
        }
        ClientUtils.renderTexture(graphics, result, this.getX(), this.getY());
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.renderWidget(graphics, mouseX, mouseY, partialTicks);
        boolean outOfBounds = RangeUtils.isOutOfBounds(this, adapter().add(cached, (ctrlPressed ? 5 : 1) * (shiftPressed ? -1 : 1)));
        TextureData baseLayer = ClientData.TOGGLE_BUTTON.get(false, isHovered(), !outOfBounds || !isHovered);
        ClientUtils.renderTexture(graphics, baseLayer, this.getX(), this.getY());
        renderIcon(graphics, mouseX, mouseY, partialTicks, outOfBounds);
    }

    @Override
    public void refreshTooltip(){
        List<Component> components = new ArrayList<>();
        components.add(Component.translatable("tooltip.travelerscompass.settings."
                + data.id()).withStyle(ChatFormatting.WHITE));
        Component shift = ClientUtils.coloredComponent(
                Component.translatable("tooltip.travelerscompass.settings.modification.shift"), shiftPressed ? ClientUtils.GRAY : ClientUtils.SOFT_GRAY);
        Component ctrl = ClientUtils.coloredComponent(
                Component.translatable("tooltip.travelerscompass.settings.modification.ctrl"), ctrlPressed ? ClientUtils.GRAY : ClientUtils.SOFT_GRAY);
        components.add(Component.translatable("tooltip.travelerscompass.settings.modification.hold_to_decrease", shift).withStyle(ChatFormatting.GRAY));
        components.add(Component.translatable("tooltip.travelerscompass.settings.modification.hold_to_change_faster", ctrl).withStyle(ChatFormatting.GRAY));
        if (shiftPressed){
            Component desc = Component.empty().append(ClientUtils.DESC_ARROW)
                    .append(Component.translatable("tooltip.travelerscompass.settings." + data.id() + ".desc").withStyle(ChatFormatting.GRAY));
            components.add(desc);
        }
        MutableComponent value = Component.literal(String.valueOf(cached));
        ClientUtils.setColor(value, ClientUtils.SOFT_GRAY);
        components.add(Component.translatable("tooltip.travelerscompass.settings." + data.id() +".value", value).withStyle(ChatFormatting.GRAY));
        this.setTooltip(Tooltip.create(ClientUtils.buildTooltip(components)));
    }

}
