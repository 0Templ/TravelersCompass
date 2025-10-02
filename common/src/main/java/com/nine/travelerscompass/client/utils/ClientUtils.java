package com.nine.travelerscompass.client.utils;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.common.search.location.ILocationObject;
import com.nine.travelerscompass.common.search.location.WithContent;
import com.nine.travelerscompass.config.filter.FilterReason;
import com.nine.travelerscompass.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClientUtils {

    public static final int SOFT_GRAY = 0xc8c8c8;
    public static final int GRAY = 11184810;

    public static final int HONEY = 0xd4b16a;
    public static final int SOFT_HONEY = 0xf6d491;

    public static final int SOFT_GREEN = 0xaee67f;
    public static final int SOFT_RED = 0xb84949;

    public static final float MAX_SCALE = 3F;
    public static final float MIN_SCALE = 0.3F;

    //Custom font provider that allows to fine-tune font indents
    public static final ResourceLocation SPACE_HELPER = ResourceLocation.fromNamespaceAndPath(TCCommon.MODID, "space_helper");
    public static final Component DESC_ARROW_INDENT = Component.literal("\uF820")
            .withStyle(Style.EMPTY.withFont(SPACE_HELPER));

    public static final Component DESC_ARROW = ClientUtils.coloredComponent(Component.literal("▶").append(DESC_ARROW_INDENT), ClientUtils.SOFT_GRAY);
    public static final Component DISABLED = Component.translatable("tooltip.travelerscompass.disabled").withStyle(ChatFormatting.GRAY);
    public static final Component ENABLED = Component.translatable("tooltip.travelerscompass.enabled").withStyle(ChatFormatting.GRAY);

    public static Component buildTooltip(Component title, Component... toAppend) {
        MutableComponent ret = Component.empty().append(title);
        for (Component component : toAppend) {
            ret = ret.append(Component.literal("\n")).append(component);
        }
        return ret;
    }

    public static Component buildSearchModeTooltip(String titleKey, List<String> descKeys, boolean state) {
        return buildToggleTooltip(titleKey, descKeys, ENABLED, DISABLED, state);
    }

    public static Component buildToggleTooltip(String titleKey, List<String> descKeys, Component enabled, Component disabled, boolean state) {
        List<Component> components = buildToggleComponentsList(titleKey, descKeys, state ? enabled : disabled);
        return buildTooltip(components);
    }

    public static Component buildSearchModeTooltip(String titleKey, List<String> descKeys, Component state) {
        List<Component> components = buildToggleComponentsList(titleKey, descKeys, state);
        return buildTooltip(components);
    }

    public static List<Component> buildToggleComponentsList(String titleKey, List<String> descKeys, boolean state) {
        return buildToggleComponentsList(titleKey, descKeys, state ? ENABLED : DISABLED);
    }

    public static List<Component> buildToggleComponentsList(String titleKey, List<String> descKeys, Component enabled, Component disabled, boolean state) {
        return buildToggleComponentsList(titleKey, descKeys, state ? enabled : disabled);
    }

    public static List<Component> buildToggleComponentsList(String titleKey, List<String> descKeys, Component state) {
        List<Component> ret = new ArrayList<>();
        ret.add(Component.translatable(titleKey).withStyle(ChatFormatting.WHITE));
        if (descKeys != null) {
            for (String descKey : descKeys) {
                ret.add(Component.empty()
                        .append(ClientUtils.DESC_ARROW)
                        .append(Component.translatable(descKey).withStyle(ChatFormatting.GRAY)));
            }
        }
        ret.add(state);
        return ret;
    }

    public static Component buildTooltip(List<Component> components) {
        if (components.isEmpty()) {
            return Component.empty();
        }
        Iterator<Component> iterator = components.iterator();
        MutableComponent ret = Component.empty().append(iterator.next());
        while (iterator.hasNext()) {
            ret = ret.append(Component.literal("\n")).append(iterator.next());
        }
        return ret;
    }

    public static MutableComponent coloredComponent(MutableComponent start, int color) {
        return start.withStyle(start.getStyle().withColor(color));
    }

    public static MutableComponent setColor(MutableComponent component, int color) {
        return component.withStyle(component.getStyle().withColor(color));
    }

    public static Component getDistance(BlockPos playerPos, BlockPos targetPos) {
        return Component.literal(String.valueOf((int) Math.sqrt(playerPos.distSqr(targetPos)))).withStyle(ChatFormatting.GRAY);
    }

    public static Component getLocationObjectTargetName(ILocationObject object){
        boolean priority = object.priority();
        final int defaultColor = priority ? ClientUtils.HONEY : ClientUtils.GRAY;
        final int softColor = priority ? ClientUtils.SOFT_HONEY : ClientUtils.SOFT_GRAY;
        if (object instanceof WithContent withContent){
            MutableComponent ret = Component.empty();
            ret.append(ClientUtils.coloredComponent(
                    Component.translatable(object.descriptionId()), defaultColor));
            ret.append(ClientUtils.coloredComponent(Component.literal(" ["), defaultColor));
            ret.append(ClientUtils.coloredComponent(
                    Component.translatable(withContent.contentId()), softColor));
            ret.append(ClientUtils.coloredComponent(Component.literal("]"), defaultColor));
            return ret;
        }
        return ClientUtils.coloredComponent(
                Component.translatable(object.descriptionId()), defaultColor);
    }

    public static Component cutComponent(Font font, Component component, int availableWidth){
        MutableComponent ret = Component.empty();
        MutableComponent dots = Component.literal("...");
        int dotsWidth = font.width(dots);
        if (availableWidth <= font.width(dots)) {
            return dots;
        }
        int componentWidth = font.width(component);

        if (componentWidth <= availableWidth) {
            return component;
        }
        int widthUsed = 0;
        for (Component content : component.toFlatList()){
            int contentWidth = font.width(content);
            if (widthUsed + contentWidth + dotsWidth <= availableWidth){
                ret.append(content);
                widthUsed+=contentWidth;
            }
            else {
                String s = content.getString();
                if (widthUsed + dotsWidth >= availableWidth){
                    ret.append(dots.withStyle(content.getStyle()));
                } else {
                    Component formated = Component.literal(
                                    font.plainSubstrByWidth(s, availableWidth - widthUsed - dotsWidth))
                            .withStyle(content.getStyle()).append(dots.withStyle(content.getStyle()));
                    ret.append(formated);
                }
                break;
            }
        }

        return ret;
    }

    public static String compressNumber(int val) {
        int abs = Math.abs(val);
        if (abs >= 1_000_000) return (val / 1_000_000)
                + Component.translatable("hud.travelerscompass.number.mega").getString();
        if (abs >= 1_000) return (val / 1_000)
                + Component.translatable("hud.travelerscompass.number.kilo").getString();
        return String.valueOf(val);
    }

    public static Component formatReason(FilterReason reason) {
        if (reason instanceof FilterReason.ByModId byMod) {
            return Component.translatable("tooltip.travelerscompass.config.filter_reason.mod",
                            Platform.PLATFORM.getModName(byMod.modId()))
                    .withStyle(ChatFormatting.RED);

        } else if (reason instanceof FilterReason.ByItemId) {
            return Component.translatable("tooltip.travelerscompass.config.filter_reason.item")
                    .withStyle(ChatFormatting.RED);

        } else if (reason instanceof FilterReason.ByTag byTag) {
            return Component.translatable("tooltip.travelerscompass.config.filter_reason.tag", byTag.tag().location().toString())
                    .withStyle(ChatFormatting.RED);

        } else if (reason instanceof FilterReason.ByEntityType byEntityType) {
            Component entityName = byEntityType.type().getDescription();
            return Component.translatable("tooltip.travelerscompass.config.filter_reason.entity", entityName)
                    .withStyle(ChatFormatting.RED);
        }
        return Component.empty();
    };

    public static void renderTexture(GuiGraphics graphics, IconTexture iconTexture, int x, int y){
        renderTexture(graphics, iconTexture.textureData(), x + iconTexture.xOffset(), y + iconTexture.yOffset());
    }

    public static void renderTexture(GuiGraphics graphics, TextureData data, int x, int y){
        graphics.blit(data.getLocation(), x, y,
                data.getRenderWidth(), data.getRenderHeight(),
                data.getU(), data.getV(),
                data.getWidth(), data.getHeight(), data.getTextureWidth(), data.getTextureHeight()
        );
    }


}
