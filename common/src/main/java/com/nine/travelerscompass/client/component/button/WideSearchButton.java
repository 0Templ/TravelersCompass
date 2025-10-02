package com.nine.travelerscompass.client.component.button;

import com.nine.travelerscompass.client.ClientData;
import com.nine.travelerscompass.client.utils.ClientUtils;
import com.nine.travelerscompass.client.utils.SearchProgress;
import com.nine.travelerscompass.client.utils.TextureData;
import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.utils.SearchState;
import com.nine.travelerscompass.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class WideSearchButton extends BaseButton {

    private final Supplier<ItemStack> supplier;
    private final UUID uuid;

    public WideSearchButton(int x, int y, Supplier<ItemStack> supplier) {
        super(x, y, 14, 14);
        this.supplier = supplier;
        this.uuid = CompassProperties.COMPASS_UUID.get(supplier.get());
        refreshTooltip();
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        ItemStack stack = supplier.get();
        if (CompassProperties.SEARCH_STATE.get(stack) == SearchState.WIDE_SEARCHING){
            if (shiftPressed) {
                ClientData.PROGRESS_DATA_CACHE.put(uuid, new SearchProgress(0, 0));
                ClientData.PROGRESS_DATA_CACHE.remove(uuid);
                Platform.PLATFORM_NETWORK.sendC2SPausePacket(uuid);
                CompassProperties.SEARCH_STATE.put(stack, SearchState.IDLE);
                refreshTooltip();
            }
        }
        else {
            refreshTooltip();
            Platform.PLATFORM_NETWORK.sendC2SWideSearchPacket(uuid);
        }
    }

    @Override
    protected void renderIcon(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        TextureData iconLayer = isHovered() ? ClientData.WIDE_SEARCH_HOVERED : ClientData.WIDE_SEARCH_ICON;
        ClientUtils.renderTexture(graphics, iconLayer, this.getX() + 2, this.getY() + 2);
    }

    @Override
    protected void renderMainLayer(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        TextureData baseLayer = ClientData.TOGGLE_BUTTON.get(false, isHovered);
        ClientUtils.renderTexture(graphics, baseLayer, this.getX(), this.getY());
        renderIcon(graphics, mouseX, mouseY, partialTicks);
        SearchProgress searchProgress = ClientData.PROGRESS_DATA_CACHE.get(uuid);
        if (searchProgress != null) {
            ItemStack stack = supplier.get();
            if (CompassProperties.SEARCH_STATE.get(stack) == SearchState.WIDE_SEARCHING) {
                float progress = (float) searchProgress.progress / searchProgress.total;
                drawProgress(graphics, this.getX() + 1, this.getY() + 12, 13, 1, progress,
                        isHovered ? 0xFFFFFFFF : 0xA2FFFFFF);
            }
        }
    }

    private void drawProgress(GuiGraphics g, int x, int y, int w, int h, float p, int fg) {
        float wf = p * w;
        int full = Mth.floor(wf);
        if (full > 0) {
            g.fill(x, y, x + full, y + h, fg);
        }
    }

    @Override
    public void refreshTooltip(){
        List<Component> list = new ArrayList<>();
        String key = "tooltip.travelerscompass.wide_search";
        Component title = Component.translatable(key);
        list.add(title);
        if (shiftPressed){
            Component desc = ClientUtils.DESC_ARROW.copy().append(Component.translatable(key + ".desc").withStyle(ChatFormatting.GRAY));
            list.add(desc);
        }
        if (CompassProperties.SEARCH_STATE.get(supplier.get()) == SearchState.WIDE_SEARCHING){
            SearchProgress searchProgress = ClientData.PROGRESS_DATA_CACHE.get(uuid);
            int percent = 0;
            if (searchProgress != null){
                percent = (searchProgress.progress * 100) / searchProgress.total;
            }
            Component progress = ClientUtils.coloredComponent(Component.literal(percent + "%"), ClientUtils.SOFT_GRAY);
            list.add(Component.translatable("tooltip.travelerscompass.settings.info.status.progress", progress).withStyle(ChatFormatting.GRAY));
            Component shiftRBM = ClientUtils.coloredComponent(
                    Component.translatable("tooltip.travelerscompass.settings.modification.shift_click"), ClientUtils.SOFT_GRAY);
            list.add(Component.translatable("tooltip.travelerscompass.wide_search.cancel", shiftRBM).withStyle(ChatFormatting.GRAY));
        }

        this.setTooltip(Tooltip.create(ClientUtils.buildTooltip(list)));
    }

}
