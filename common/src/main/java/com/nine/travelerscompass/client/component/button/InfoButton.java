package com.nine.travelerscompass.client.component.button;

import com.nine.travelerscompass.client.ClientData;
import com.nine.travelerscompass.client.hud.HudData;
import com.nine.travelerscompass.client.utils.ClientUtils;
import com.nine.travelerscompass.client.utils.SearchProgress;
import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.search.location.ILocationObject;
import com.nine.travelerscompass.common.utils.FoundBlockPos;
import com.nine.travelerscompass.common.utils.SearchState;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class InfoButton extends BaseButton {

    private final Supplier<ItemStack> supplier;
    private final UUID uuid;

    public InfoButton(int x, int y, int width, int height, Supplier<ItemStack> supplier) {
        super(x, y, width, height);
        this.supplier = supplier;
        this.uuid = CompassProperties.COMPASS_UUID.get(supplier.get());
        refreshTooltip();
    }

    @Override
    public void refreshTooltip(){
        Component title = Component.translatable("tooltip.travelerscompass.settings.info");
        List<Component> components = new ArrayList<>();
        Component status;
        ItemStack stack = supplier.get();
        FoundBlockPos pos = CompassProperties.FOUND_BLOCK_POS.get(stack);
        SearchState state = CompassProperties.SEARCH_STATE.get(stack);
        if (CompassProperties.PAUSE.get(stack)){
            status = Component.translatable("tooltip.travelerscompass.settings.info.status.paused").withStyle(ChatFormatting.GRAY);
        } else if (state == SearchState.SEARCHING ) {
            SearchProgress searchProgress = ClientData.PROGRESS_DATA_CACHE.get(uuid);
            int percent = 0;
            if (searchProgress != null){
                percent = (searchProgress.progress * 100) / searchProgress.total;
            }
            Component progress = ClientUtils.coloredComponent(Component.literal(percent + "%"), ClientUtils.SOFT_GRAY);
            if (pos.isValid()){
                status = Component.translatable("tooltip.travelerscompass.settings.info.status.scanning", progress).withStyle(ChatFormatting.GRAY);
            } else {
                status = Component.translatable("tooltip.travelerscompass.settings.info.status.searching", progress).withStyle(ChatFormatting.GRAY);
            }
        }
        else {
            status = Component.translatable("tooltip.travelerscompass.settings.info.status.idle").withStyle(ChatFormatting.GRAY);
        }

        Component statusFull = Component.translatable("tooltip.travelerscompass.settings.info.status", status);

        components.add(title);
        components.add(statusFull);

        HudData data = ClientData.HUD_DATA_CACHE.get(CompassProperties.COMPASS_UUID.get(stack));
        ILocationObject locationObject = data.getLocationObject();

        if (pos.isValid() && locationObject != null){
            Component target = Component.translatable("tooltip.travelerscompass.settings.info.target",
                            Component.translatable(locationObject.descriptionId())
                                    .withStyle(ChatFormatting.GRAY))
                    .withStyle(ChatFormatting.WHITE
                    );
            components.add(target);
            if (locationObject.blockPos() != null){
                BlockPos blockPos = pos.blockPos();
                Component position = Component.translatable("tooltip.travelerscompass.settings.info.position",
                                Component.literal(blockPos.getX() + ", " + blockPos.getY() + ", " + blockPos.getZ())
                                        .withStyle(ChatFormatting.GRAY))
                        .withStyle(ChatFormatting.WHITE
                        );
                components.add(position);
            }
        }

        this.setTooltip(Tooltip.create(ClientUtils.buildTooltip(components)));
    }

    @Override
    protected void renderMainLayer(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        ClientUtils.renderTexture(graphics, ClientData.TOGGLE_BUTTON.get(false, isHovered()), this.getX(), this.getY());
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.renderWidget(graphics, mouseX, mouseY, partialTicks);
        ClientUtils.renderTexture(graphics, isHovered() ? ClientData.INFO_HOVERED : ClientData.INFO, this.getX() + 4, this.getY() + 3);
    }
}
