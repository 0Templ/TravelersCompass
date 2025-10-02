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
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class CompactHudTypeRenderer implements IHudTypeRenderer {

    public static final int MAX_LINES = 4;

    public void render(GuiGraphics graphics, Player player, Font font, HudData hudData){
        if (hudData.isDirty()) {
            int prefixWidth = font.width(Component.translatable("hud.travelerscompass.compact.target", ""));
            hudData.updateCachedData(font, prefixWidth);
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

        //Sate
        Component state = Component.translatable("hud.travelerscompass.compact.state", hudData.getState())
                .withStyle(ChatFormatting.WHITE);
        components.add(state);
        if (valid){
            //Name
            Component finalName = Component.translatable("hud.travelerscompass.compact.target", hudData.getTargetName())
                    .withStyle(ChatFormatting.WHITE);

            components.add(finalName);

            BlockPos blockPos = locationObject.blockPos();
            if (blockPos != null){
                //Coordinates
                Component position = formatCoordsCompact(blockPos, font, width);
                //Distance
                Component distance = Component.translatable("hud.travelerscompass.compact.distance",
                                ClientUtils.getDistance(player.blockPosition(), blockPos))
                        .withStyle(ChatFormatting.WHITE);
                components.add(position);
                components.add(distance);
            }
        }



        drawCompactLines(graphics, font, xPos, yPos, width, height, settings.alignment(), MAX_LINES, components);
    }

    public static void drawCompactLines(GuiGraphics graphics, Font font,
                                         int xPos, int yPos,
                                         int width, int height,
                                         Alignment alignment,
                                         int maxLines,
                                         List<Component> components) {
        if (components.isEmpty()) return;
        final int linesHeight = maxLines * font.lineHeight;
        final int gap = components.size() > 1
                ? (height - linesHeight) / (maxLines - 1)
                : 0;
        int currentY = yPos;
        int currentX;
        for (Component component : components) {
            currentX = xPos;
            switch (alignment){
                case LEFT -> {
                }
                case RIGHT -> currentX += width - font.width(component);
                case CENTER -> currentX += (width - font.width(component)) / 2;
            }
            graphics.drawString(font, component, currentX, currentY, 0xAAAAAA);
            currentY += font.lineHeight + gap;
        }
    }


    public static Component formatCoordsCompact(BlockPos pos, Font font, int maxWidth) {
        Component coords = Component.literal(pos.getX() + ", " + pos.getY() + ", " + pos.getZ()).withStyle(ChatFormatting.GRAY);
        MutableComponent full = Component.translatable("hud.travelerscompass.compact.position",
                coords
        ).withStyle(ChatFormatting.WHITE);
        if (font.width(full) <= maxWidth) {
            return full;
        }
        coords = Component.literal(pos.getX() + ", " + pos.getZ()).withStyle(ChatFormatting.GRAY);
        full = Component.translatable("hud.travelerscompass.compact.position",
                coords).withStyle(ChatFormatting.WHITE);
        if (font.width(full) <= maxWidth) {
            return full;
        }
        coords = Component.literal(pos.getX() + ", " + pos.getZ()).withStyle(ChatFormatting.GRAY);
        full = Component.translatable("hud.travelerscompass.compact.position_short",
                coords).withStyle(ChatFormatting.WHITE);
        if (font.width(full) <= maxWidth) {
            return full;
        }
        coords = Component.literal(ClientUtils.compressNumber(pos.getX()) + ", " + ClientUtils.compressNumber(pos.getZ())).withStyle(ChatFormatting.GRAY);
        full = Component.translatable("hud.travelerscompass.compact.position_short",
                coords).withStyle(ChatFormatting.WHITE);
        if (font.width(full) <= maxWidth) {
            return full;
        }
        return Component.translatable("hud.travelerscompass.compact.position_short",
                Component.literal("...").withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.WHITE);
    }
}
