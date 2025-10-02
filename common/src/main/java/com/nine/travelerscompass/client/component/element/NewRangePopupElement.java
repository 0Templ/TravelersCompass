package com.nine.travelerscompass.client.component.element;

import com.nine.travelerscompass.client.ClientData;
import com.nine.travelerscompass.client.utils.ClientUtils;
import com.nine.travelerscompass.client.utils.IconTexture;
import com.nine.travelerscompass.client.utils.RangeUtils;
import com.nine.travelerscompass.client.utils.TextureData;
import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.data.DataStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class NewRangePopupElement<T extends Number> extends NewPopupElement implements RangeUtils.RangeButtonHandler<T> {

    public static final IconTexture PLUS = new IconTexture(ClientData.PLUS_SMALL, 2, 2);
    public static final IconTexture PLUS_INACTIVE = new IconTexture(ClientData.PLUS_SMALL_INACTIVE, 2, 2);
    public static final IconTexture PLUS_CTRL = new IconTexture(ClientData.PLUS_SMALL_HOVERED, 2, 2);

    public static final IconTexture MINUS = new IconTexture(ClientData.MINUS_SMALL, 2, 2);
    public static final IconTexture MINUS_INACTIVE = new IconTexture(ClientData.MINUS_SMALL_INACTIVE, 2, 2);
    public static final IconTexture MINUS_CTRL = new IconTexture(ClientData.MINUS_SMALL_HOVERED, 2, 2);

    private final IconTexture icon;
    private final Supplier<ItemStack> supplier;

    private final DataStorage<T> data;
    private final T min, max;
    private final T step, stepCtrl;
    private final RangeUtils.RangeAdapter<T> adapter;

    public T cached;

    public NewRangePopupElement(int width, int height, T minValue, T maxValue, T step, T stepCtrl, RangeUtils.RangeAdapter<T> adapter, IconTexture icon, DataStorage<T> data, Supplier<ItemStack> supplier) {
        super(width, height);
        this.min = minValue;
        this.max = maxValue;
        this.step = step;
        this.stepCtrl = stepCtrl;
        this.adapter = adapter;
        this.data = data;
        this.supplier = supplier;
        this.icon = icon;
        this.cached = data.get(supplier.get());
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
    public RangeUtils.RangeAdapter<T> adapter(){
        return this.adapter;
    }

    @Override
    public void onClick(int button){
        boolean changed = RangeUtils.changeValue(this, shiftPressed, ctrlPressed, step.doubleValue(), stepCtrl.doubleValue());
        if (changed){
            updateState();
        }
    }

    @Override
    public void onMouseScroll(double delta){
        boolean changed = RangeUtils.changeValue(this, delta < 0, ctrlPressed, step.doubleValue(), stepCtrl.doubleValue());
        if (changed){
            updateState();
        }
    }

    @Override
    public void updateState(){
        ItemStack stack = supplier.get();
        cached = data.get(stack);
        refreshTooltip();
    }

    public void renderIcon(GuiGraphics graphics, int xPos, int yPos, boolean outOfBounds) {
        IconTexture toRender = icon;
        if (isHovered){
            if (shiftPressed){
                toRender = outOfBounds ? MINUS_INACTIVE : (ctrlPressed ? MINUS_CTRL : MINUS);
            }
            else {
                toRender = outOfBounds ? PLUS_INACTIVE : (ctrlPressed ? PLUS_CTRL : PLUS);
            }
        }
        ClientUtils.renderTexture(graphics, toRender, xPos, yPos);
    }

    @Override
    public void renderElement(GuiGraphics graphics, int xPos, int yPos) {
        boolean outOfBounds = RangeUtils.isOutOfBounds(this, adapter().add(cached, (ctrlPressed ? stepCtrl.doubleValue() : step.doubleValue()) * (shiftPressed ? -1 : 1)));
        TextureData baseLayer = ClientData.SMALL_TOGGLE_BUTTON.get(false, isHovered, !outOfBounds || !isHovered);
        ClientUtils.renderTexture(graphics, baseLayer, xPos, yPos);
        renderIcon(graphics, xPos, yPos, outOfBounds);
    }

    protected String translationKey(){
        return data.id();
    }

    @Override
    public void refreshTooltip() {
        List<Component> components = new ArrayList<>();
        components.add(Component.translatable("tooltip.travelerscompass.settings."
                + translationKey()).withStyle(ChatFormatting.WHITE));
        Component shift = ClientUtils.coloredComponent(
                Component.translatable("tooltip.travelerscompass.settings.modification.shift"), shiftPressed ? ClientUtils.GRAY : ClientUtils.SOFT_GRAY);
        Component ctrl = ClientUtils.coloredComponent(
                Component.translatable("tooltip.travelerscompass.settings.modification.ctrl"), ctrlPressed ? ClientUtils.GRAY : ClientUtils.SOFT_GRAY);
        components.add(Component.translatable("tooltip.travelerscompass.settings.modification.hold_to_decrease", shift).withStyle(ChatFormatting.GRAY));
        components.add(Component.translatable("tooltip.travelerscompass.settings.modification.hold_to_change_faster", ctrl).withStyle(ChatFormatting.GRAY));
        if (shiftPressed){
            Component desc = Component.empty().append(ClientUtils.DESC_ARROW)
                    .append(Component.translatable("tooltip.travelerscompass.settings." + translationKey() + ".desc").withStyle(ChatFormatting.GRAY));
            components.add(desc);
        }
        Component value = ClientUtils.coloredComponent(adapter.format(cached).copy(), ClientUtils.SOFT_GRAY);
        components.add(Component.translatable("tooltip.travelerscompass.settings." + translationKey() +".value", value).withStyle(ChatFormatting.GRAY));
        this.setTooltip(components);
    }


}
