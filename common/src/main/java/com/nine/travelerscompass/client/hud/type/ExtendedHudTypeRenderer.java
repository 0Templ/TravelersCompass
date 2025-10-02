package com.nine.travelerscompass.client.hud.type;

import com.nine.travelerscompass.client.hud.HudData;
import com.nine.travelerscompass.client.hud.HudSettings;
import com.nine.travelerscompass.client.utils.ClientUtils;
import com.nine.travelerscompass.common.search.location.ILocationObject;
import com.nine.travelerscompass.common.utils.Alignment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class ExtendedHudTypeRenderer implements IHudTypeRenderer{

    public static final int MAX_LINES = 8;

    public void render(GuiGraphics graphics, Player player, Font font, HudData hudData){
        if (hudData.isDirty()) {
            hudData.updateCachedData(font, 0);
            hudData.clearDirty();
        }
        HudSettings settings = hudData.getSettings();
        int xPos = settings.xPos() + HudSettings.WIDTH_PADDING;
        int yPos = settings.yPos() + HudSettings.HEIGHT_PADDING;
        int width = settings.width();
        int height = settings.height();

        ILocationObject locationObject = hudData.getLocationObject();

        boolean valid = locationObject != null;
        List<Component> components = new ArrayList<>();

        Component stateTitle = Component.translatable("hud.travelerscompass.extended.state").withStyle(ChatFormatting.WHITE);
        Component state = hudData.getState();
        components.add(stateTitle);
        components.add(state);
        if (valid){
            Component targetTitle = Component.translatable("hud.travelerscompass.extended.target").withStyle(ChatFormatting.WHITE);
            Component target = hudData.getTargetName();


            BlockPos blockPos = locationObject.blockPos();

            components.add(targetTitle);
            components.add(target);

            if (blockPos != null){
                Component distance = ClientUtils.getDistance(player.blockPosition(), blockPos);
                Component position = formatCoordsExtended(blockPos, font, width);

                Component distanceTitle = Component.translatable("hud.travelerscompass.extended.distance").withStyle(ChatFormatting.WHITE);
                Component positionTitle = Component.translatable("hud.travelerscompass.extended.position").withStyle(ChatFormatting.WHITE);

                components.add(positionTitle);
                components.add(position);
                components.add(distanceTitle);
                components.add(distance);
            }
        }

        drawExtendedLines(graphics, font, xPos, yPos, width, height, settings.alignment(), MAX_LINES, components);
    }

    public static void drawExtendedLines(GuiGraphics graphics, Font font,
                                         int xPos, int yPos,
                                         int width, int height,
                                         Alignment alignment,
                                         int maxLines,
                                         List<Component> components) {
        if (components.isEmpty()) return;
        maxLines = 4;
        int baseGap = (int) (font.lineHeight * 0.2);

        int extraGaps = (maxLines - 1) / 2;
        int extraGap = (int) (font.lineHeight * 0.4);
        int extraGapsHeight = extraGap * extraGaps;
        final int linesHeight = maxLines * (font.lineHeight * 2 + baseGap) + extraGapsHeight;

        final int gap = components.size() > 1
                ? (height - linesHeight) / (maxLines - 1)
                : 0;
        int currentY = yPos;
        int currentX;
        int count = 0;
        for (Component component : components) {
            count++;
            currentX = xPos;
            switch (alignment){
                case LEFT -> {
                }
                case RIGHT -> currentX += width - font.width(component);
                case CENTER -> currentX += (width - font.width(component)) / 2;
            }
            graphics.drawString(font, component, currentX, currentY, 0xAAAAAA);
            currentY += font.lineHeight + baseGap;
            if (count % 2 == 0){
                currentY += gap;
            }
        }
    }

    public static Component formatCoordsExtended(BlockPos pos, Font font, int maxWidth) {
        Component coords = Component.literal(pos.getX() + ", " + pos.getY() + ", " + pos.getZ())
                .withStyle(ChatFormatting.GRAY);
        if (font.width(coords) <= maxWidth) {
            return coords;
        }
        coords = Component.literal(pos.getX() + ", " + pos.getZ()).withStyle(ChatFormatting.GRAY);
        if (font.width(coords) <= maxWidth) {
            return coords;
        }
        coords = Component.literal(ClientUtils.compressNumber(pos.getX()) + ", " + ClientUtils.compressNumber(pos.getZ()))
                .withStyle(ChatFormatting.GRAY);
        if (font.width(coords) <= maxWidth) {
            return coords;
        }

        return Component.literal("...").withStyle(ChatFormatting.GRAY);
    }

}
