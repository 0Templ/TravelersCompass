package com.nine.travelerscompass.client.component.button;

import com.nine.travelerscompass.client.ClientData;
import com.nine.travelerscompass.client.component.element.NewPopupElement;
import com.nine.travelerscompass.client.component.element.NewRangePopupElement;
import com.nine.travelerscompass.client.hud.HudRenderer;
import com.nine.travelerscompass.client.hud.HudSettings;
import com.nine.travelerscompass.client.utils.ClientUtils;
import com.nine.travelerscompass.client.utils.IconTexture;
import com.nine.travelerscompass.client.utils.RangeUtils;
import com.nine.travelerscompass.client.utils.TextureData;
import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.data.DataStorage;
import com.nine.travelerscompass.common.utils.Alignment;
import com.nine.travelerscompass.common.utils.HudRenderMode;
import com.nine.travelerscompass.common.utils.HudType;
import com.nine.travelerscompass.config.TCConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class HudButton extends PopupButton {

    public static final Component HUD_ENABLED = Component.translatable("tooltip.travelerscompass.settings.hud.enabled").withStyle(ChatFormatting.GRAY);
    public static final Component HUD_REQUIRES_HAND = Component.translatable("tooltip.travelerscompass.settings.hud.requires_hand").withStyle(ChatFormatting.GRAY);

    private final Supplier<ItemStack> supplier;
    private final DataStorage<HudRenderMode> data = CompassProperties.HUD_RENDER_MODE;
    private HudRenderMode cachedRenderMode;
    private final UUID uuid;

    public HudButton(int x, int y, Supplier<ItemStack> supplier) {
        super(x, y, 14, 14,
                ClientData.CONNECTOR_VERTICAL,
                ClientData.POPUP_3X3,
                new Rect2i(0, 0, 13, 13),
                new Rect2i(0, 0, 42, 40));
        this.supplier = supplier;
        ItemStack stack = supplier.get();
        this.uuid = CompassProperties.get(stack, CompassProperties.COMPASS_UUID);
        this.cachedRenderMode = CompassProperties.get(stack, CompassProperties.HUD_RENDER_MODE);
        HudType hudType = CompassProperties.HUD_TYPE.get(stack);

        NewRangePopupElement<Integer> widthElement = new NewRangePopupElement<>(9, 9,
                hudType.minWidth(), hudType.maxWidth(), 1, 5,
                RangeUtils.INTEGER,
                new IconTexture(ClientData.HUD_WIDTH, 2, 2),
                CompassProperties.HUD_WIDTH,
                supplier) {

            private HudType cachedType;

            @Override
            public void updateState(){
                super.updateState();
                this.cachedType = CompassProperties.HUD_TYPE.get(supplier.get());
                ClientData.HUD_DATA_CACHE.computeIfPresent(uuid, (id, hudData) -> {
                    HudSettings settings = hudData.getSettings();
                    hudData.updateSettings(settings.toBuilder().width(cached).build());
                    return hudData;
                });
            }

            @Override
            public Integer max(){
                return cachedType.maxWidth();
            }

            @Override
            public Integer min(){
                return cachedType.minWidth();
            }

            @Override
            public void set(Integer value){
                super.set(value);
            }
        };

        NewRangePopupElement<Integer> heightElement = new NewRangePopupElement<>(9, 9,
                hudType.minHeight(), hudType.maxHeight(), 1, 5,
                RangeUtils.INTEGER,
                new IconTexture(ClientData.HUD_HEIGHT, 3, 2),
                CompassProperties.HUD_HEIGHT,
                supplier) {

            private HudType cachedType;

            @Override
            public void updateState(){
                super.updateState();
                this.cachedType = CompassProperties.HUD_TYPE.get(supplier.get());
                ClientData.HUD_DATA_CACHE.computeIfPresent(uuid, (id, hudData) -> {
                    HudSettings settings = hudData.getSettings();
                    hudData.updateSettings(settings.toBuilder().height(cached).build());
                    return hudData;
                });
            }

            @Override
            public Integer max(){
                return cachedType.maxHeight();
            }

            @Override
            public Integer min(){
                return cachedType.minHeight();
            }

            @Override
            public void set(Integer value){
                super.set(value);
            }
        };

        NewRangePopupElement<Float> scaleElement = new NewRangePopupElement<>(9, 9,
                0.2F, 3F, 0.01F, 0.1F,
                RangeUtils.FLOAT,
                new IconTexture(ClientData.HUD_SCALE, 2, 2),
                CompassProperties.HUD_SCALE,
                supplier) {

            @Override
            public void updateState(){
                super.updateState();
                ClientData.HUD_DATA_CACHE.computeIfPresent(uuid, (id, hudData) -> {
                    HudSettings settings = hudData.getSettings();
                    hudData.updateSettings(settings.toBuilder().scale(cached).build());
                    return hudData;
                });
            }

            @Override
            public void set(Float value){
                super.set(value);
            }
        };

        NewRangePopupElement<Integer> xPosElement = new NewRangePopupElement<Integer>(9, 9,
                0, -1, 1, 10,
                RangeUtils.INTEGER,
                new IconTexture(ClientData.HUD_X_POS, 2, 2),
                CompassProperties.HUD_X_POS,
                supplier) {

            @Override
            public void updateState(){
                super.updateState();
                ClientData.HUD_DATA_CACHE.computeIfPresent(uuid, (id, hudData) -> {
                    HudSettings settings = hudData.getSettings();
                    hudData.updateSettings(settings.toBuilder().xPos(cached).build());
                    return hudData;
                });
            }

            @Override
            public void refreshTooltip(){
                setTooltip(getHudAxisPosTooltip(supplier.get(), cached, "x"));
            }

            @Override
            public void set(Integer value){
                super.set(value);
            }

            @Override
            public Integer max(){
                return Minecraft.getInstance().getWindow().getGuiScaledWidth() - HudSettings.WIDTH_PADDING * 2;
            }
        };

        NewRangePopupElement<Integer> yPosElement = new NewRangePopupElement<Integer>(9, 9,
                0, -1, 1, 10,
                RangeUtils.INTEGER,
                new IconTexture(ClientData.HUD_Y_POS, 2, 2),
                CompassProperties.HUD_Y_POS,
                supplier) {

            @Override
            public void updateState(){
                super.updateState();
                ClientData.HUD_DATA_CACHE.computeIfPresent(uuid, (id, hudData) -> {
                    HudSettings settings = hudData.getSettings();
                    hudData.updateSettings(settings.toBuilder().yPos(cached).build());
                    return hudData;
                });
            }

            @Override
            public void refreshTooltip(){
                setTooltip(getHudAxisPosTooltip(supplier.get(), cached, "y"));
            }

            @Override
            public void set(Integer value){
                super.set(value);
            }

            @Override
            public Integer max(){
                return Minecraft.getInstance().getWindow().getGuiScaledHeight() - HudSettings.HEIGHT_PADDING * 2;
            }
        };

        NewPopupElement hudTypeElement = new NewPopupElement(9, 9) {
            {
                updateState();
            }

            private HudType cached;

            @Override
            public void updateState(){
                this.cached = CompassProperties.HUD_TYPE.get(supplier.get());
                refreshTooltip();
                ClientData.HUD_DATA_CACHE.computeIfPresent(uuid, (id, hudData) -> {
                    hudData.updateSettings(hudData.getSettings().toBuilder()
                            .hudType(cached).build());
                    return hudData;
                });
            }

            @Override
            public void renderElement(GuiGraphics graphics, int xPos, int yPos) {
                TextureData baseLayer = ClientData.SMALL_TOGGLE_BUTTON.get(false, this.isHovered, true);
                IconTexture iconTexture = switch (cached){
                    case EXTENDED -> this.isHovered ? ClientData.HUD_TYPE_EXTENDED_HOVERED.icon(2, 2) : ClientData.HUD_TYPE_EXTENDED.icon(2, 2);
                    case COMPACT -> this.isHovered ? ClientData.HUD_TYPE_COMPACT_HOVERED.icon(2, 2) : ClientData.HUD_TYPE_COMPACT.icon(2, 2);
                };
                ClientUtils.renderTexture(graphics, baseLayer, xPos, yPos);
                ClientUtils.renderTexture(graphics, iconTexture, xPos, yPos);
            }

            @Override
            public void onClick(int button){
                CompassProperties.toggleFromClient(supplier.get(), CompassProperties.HUD_TYPE, button == 1);
                updateState();
                widthElement.updateState();
                heightElement.updateState();
            }

            @Override
            public void refreshTooltip(){
                List<Component> ret = new ArrayList<>();
                String key = "tooltip.travelerscompass.settings.hud.type";
                Component title = Component.translatable(key);
                Component state = Component.translatable(key + "." + cached.name().toLowerCase()).withStyle(ChatFormatting.GRAY);
                ret.add(title);
                if (shiftPressed){
                    ret.add(ClientUtils.DESC_ARROW.copy().append(Component.translatable(key + ".desc").withStyle(ChatFormatting.GRAY)));
                }
                ret.add(state);
                setTooltip(ret);
            }
        };

        NewPopupElement hucChatElement = new NewPopupElement(9, 9) {
            {
                updateState();
            }

            private boolean cached;

            @Override
            public void updateState(){
                this.cached = CompassProperties.HUD_WITH_CHAT.get(supplier.get());
                refreshTooltip();
                ClientData.HUD_DATA_CACHE.computeIfPresent(uuid, (id, hudData) -> {
                    hudData.updateSettings(hudData.getSettings().toBuilder()
                            .hudWithChat(cached).build());
                    return hudData;
                });
            }

            @Override
            public void renderElement(GuiGraphics graphics, int xPos, int yPos) {
                TextureData baseLayer = ClientData.SMALL_TOGGLE_BUTTON.get(false, this.isHovered, true);
                IconTexture iconTexture;
                if (cached){
                    iconTexture = this.isHovered ? ClientData.HUD_CHAT_ACTIVE_HOVERED.icon(1, 1) : ClientData.HUD_CHAT_ACTIVE.icon(1, 1);
                }
                else {
                    iconTexture = this.isHovered ? ClientData.HUD_CHAT_INACTIVE_HOVERED.icon(1, 1) : ClientData.HUD_CHAT_INACTIVE.icon(1, 1);
                }
                ClientUtils.renderTexture(graphics, baseLayer, xPos, yPos);
                ClientUtils.renderTexture(graphics, iconTexture, xPos, yPos);
            }

            @Override
            public void onClick(int button){
                CompassProperties.toggleFromClient(supplier.get(), CompassProperties.HUD_WITH_CHAT, button == 1);
                updateState();
            }

            @Override
            public void refreshTooltip(){
                List<Component> ret = new ArrayList<>();
                String key = "tooltip.travelerscompass.settings.hud_with_chat";
                Component title = Component.translatable(key);
                Component state = cached ? Toggleable.ENABLED : Toggleable.DISABLED;
                ret.add(title);
                if (shiftPressed){
                    ret.add(ClientUtils.DESC_ARROW.copy().append(Component.translatable(key + ".desc").withStyle(ChatFormatting.GRAY)));
                }
                ret.add(state);
                setTooltip(ret);
            }
        };


        NewPopupElement hudAlignmentButton = new NewPopupElement(9, 9) {
            {
                updateState();
            }

            private Alignment cached;

            @Override
            public void updateState(){
                this.cached = CompassProperties.HUD_ALIGNMENT.get(supplier.get());
                refreshTooltip();
                ClientData.HUD_DATA_CACHE.computeIfPresent(uuid, (id, hudData) -> {
                    hudData.updateSettings(hudData.getSettings().toBuilder()
                            .alignment(cached).build());
                    return hudData;
                });
            }

            @Override
            public void renderElement(GuiGraphics graphics, int xPos, int yPos) {
                TextureData baseLayer = ClientData.SMALL_TOGGLE_BUTTON.get(false, this.isHovered, true);
                IconTexture iconTexture = switch (cached){
                    case CENTER -> this.isHovered ? ClientData.HUD_ALIGNMENT_CENTER_HOVERED.icon(3, 1) : ClientData.HUD_ALIGNMENT_CENTER.icon(3, 1);
                    case LEFT -> this.isHovered ? ClientData.HUD_ALIGNMENT_LEFT_HOVERED.icon(1, 1) : ClientData.HUD_ALIGNMENT_LEFT.icon(1, 1);
                    case RIGHT -> this.isHovered ? ClientData.HUD_ALIGNMENT_RIGHT_HOVERED.icon(5, 1) : ClientData.HUD_ALIGNMENT_RIGHT.icon(5, 1);
                };
                ClientUtils.renderTexture(graphics, baseLayer, xPos, yPos);
                ClientUtils.renderTexture(graphics, iconTexture, xPos, yPos);
            }

            @Override
            public void onClick(int button){
                CompassProperties.toggleFromClient(supplier.get(), CompassProperties.HUD_ALIGNMENT, button == 1);
                updateState();
            }

            @Override
            public void refreshTooltip() {
                List<Component> ret = new ArrayList<>();
                String key = "tooltip.travelerscompass.settings.hud.alignment";
                Component title = Component.translatable(key);
                Component state = Component.translatable(key + "." + cached.name().toLowerCase()).withStyle(ChatFormatting.GRAY);
                ret.add(title);
                if (shiftPressed) {
                    List<Component> alignedList = new ArrayList<>();
                    for (Alignment alignment : Alignment.values()) {
                        MutableComponent alignComponent = Component.translatable(key + "." + alignment.name().toLowerCase());
                        if (alignment == cached) {
                            ClientUtils.setColor(alignComponent, ClientUtils.SOFT_GRAY);
                        } else {
                            alignComponent = alignComponent.withStyle(ChatFormatting.GRAY);
                        }
                        alignedList.add(alignComponent);
                    }
                    MutableComponent aligns = Component.empty();
                    for (int i = 0; i < alignedList.size(); i++) {
                        aligns.append(alignedList.get(i));
                        if (i < alignedList.size() - 1) {
                            aligns.append(Component.literal("/"));
                        }
                    }
                    Component desc = ClientUtils.DESC_ARROW.copy().append(
                                    Component.translatable("tooltip.travelerscompass.settings.hud.alignment.desc", aligns)
                                            .withStyle(ChatFormatting.GRAY));
                    ret.add(desc);
                }
                ret.add(state);
                setTooltip(ret);
            }
        };

        NewPopupElement resetElement = new NewPopupElement(9, 9) {

            @Override
            public void onClick(int button){
                resetSettings(supplier.get());
            }

            @Override
            public void renderElement(GuiGraphics graphics, int xPos, int yPos) {
                TextureData baseLayer = ClientData.SMALL_TOGGLE_BUTTON.get(false, this.isHovered, true);
                IconTexture iconTexture = this.isHovered ? ClientData.HUD_RESET_HOVERED.icon(3, 2) : ClientData.HUD_RESET.icon(3, 2);
                ClientUtils.renderTexture(graphics, baseLayer, xPos, yPos);
                ClientUtils.renderTexture(graphics, iconTexture, xPos, yPos);
            }


            @Override
            public void refreshTooltip(){
                List<Component> ret = new ArrayList<>();
                String key = "tooltip.travelerscompass.settings.hud.reset";
                Component title = Component.translatable(key);
                ret.add(title);
                if (shiftPressed){
                    ret.add(ClientUtils.DESC_ARROW.copy().append(Component.translatable(key + ".desc").withStyle(ChatFormatting.GRAY)));
                }
                setTooltip(ret);
            }


        };

        widthElement.init(-37, 2);
        heightElement.init(-26, 2);
        scaleElement.init(-37, 13);
        xPosElement.init(-37, -9);
        yPosElement.init(-26, -9);
        hudAlignmentButton.init(-15, -9);
        hudTypeElement.init(-15, 2);
        resetElement.init(-15, 13);
        hucChatElement.init(-26, 13);

        popupElements.add(widthElement);
        popupElements.add(heightElement);
        popupElements.add(scaleElement);
        popupElements.add(xPosElement);
        popupElements.add(yPosElement);
        popupElements.add(hudAlignmentButton);
        popupElements.add(resetElement);
        popupElements.add(hudTypeElement);
        popupElements.add(hucChatElement);

        updateState();
    }

    private List<Component> getHudAxisPosTooltip(ItemStack stack, int pos, String axis) {
        List<Component> components = new ArrayList<>();
        Component title = Component.translatable("tooltip.travelerscompass.settings.hud.position." + axis).withStyle(ChatFormatting.WHITE);
        MutableComponent value = Component.literal(String.valueOf(pos));
        ClientUtils.setColor(value, ClientUtils.SOFT_GRAY);
        Component offset = Component.translatable("tooltip.travelerscompass.settings.hud.position.value",
                value).withStyle(ChatFormatting.GRAY);
        Component shift = ClientUtils.coloredComponent(
                Component.translatable("tooltip.travelerscompass.settings.modification.shift"), shiftPressed ? ClientUtils.GRAY : ClientUtils.SOFT_GRAY);
        Component ctrl = ClientUtils.coloredComponent(
                Component.translatable("tooltip.travelerscompass.settings.modification.ctrl"), ctrlPressed ? ClientUtils.GRAY : ClientUtils.SOFT_GRAY);
        components.add(title);
        components.add(Component.translatable("tooltip.travelerscompass.settings.modification.hold_to_decrease", shift).withStyle(ChatFormatting.GRAY));
        components.add(Component.translatable("tooltip.travelerscompass.settings.modification.hold_to_change_faster", ctrl).withStyle(ChatFormatting.GRAY));
        if (shiftPressed){
            String alignmentString = CompassProperties.get(stack, CompassProperties.HUD_ALIGNMENT).toString().toLowerCase();
            Component alignment = ClientUtils.coloredComponent(Component.translatable(
                    "tooltip.travelerscompass.settings.hud.position.alignment." + alignmentString), ClientUtils.SOFT_GRAY);
            Component axisComponent = ClientUtils.coloredComponent(Component.literal(axis.toUpperCase()), ClientUtils.SOFT_GRAY);
            Component desc = ClientUtils.coloredComponent(Component.literal("▶"), ClientUtils.SOFT_GRAY)
                    .append((Component.translatable("tooltip.travelerscompass.settings.hud.position.desc", axisComponent, alignment).withStyle(ChatFormatting.GRAY)));
            components.add(desc);
        }
        components.add(offset);
        return components;
    }

    public void resetSettings(ItemStack stack){
        CompassProperties.writeDefaultFromClient(stack, CompassProperties.HUD_X_POS);
        CompassProperties.writeDefaultFromClient(stack, CompassProperties.HUD_Y_POS);
        CompassProperties.writeDefaultFromClient(stack, CompassProperties.HUD_SCALE);
        CompassProperties.writeDefaultFromClient(stack, CompassProperties.HUD_ALIGNMENT);
        CompassProperties.writeDefaultFromClient(stack, CompassProperties.HUD_WITH_CHAT);
        for (HudType hudType : HudType.values()){
            CompassProperties.putFromClient(stack, CompassProperties.HUD_TYPE, hudType);
            CompassProperties.writeDefaultFromClient(stack, CompassProperties.HUD_WIDTH);
            CompassProperties.writeDefaultFromClient(stack, CompassProperties.HUD_HEIGHT);
        }
        CompassProperties.writeDefaultFromClient(stack, CompassProperties.HUD_TYPE);
        for (var el : popupElements){
            el.updateState();
        }
    }


    public void updateState(){
        ItemStack stack = supplier.get();
        this.cachedRenderMode = data.get(stack);
        refreshTooltip();
        ClientData.HUD_DATA_CACHE.computeIfPresent(uuid, (id, hudData) -> {
            hudData.updateSettings(hudData.getSettings().toBuilder().renderMode(cachedRenderMode).build());
            return hudData;
        });
    }

    @Override
    protected boolean isEnabled(){
        return TCConfig.ENABLE_HUD.get();
    }

    @Override
    protected int getPopupXOffset() {
        return -41;
    }

    @Override
    protected int getPopupYOffset() {
        return -13;
    }

    @Override
    protected int getConnectorXOffset() {
        return -1;
    }

    @Override
    protected int getConnectorYOffset() {
        return 1;
    }

    @Override
    protected boolean isPopupVisible(){
        return super.isPopupVisible() && isEnabled() && cachedRenderMode != HudRenderMode.OFF;
    }

    @Override
    protected void onMainButtonClick(int button) {
        ItemStack stack = supplier.get();
        CompassProperties.toggleFromClient(stack, data, true);
        updateState();
    }

    @Override
    protected void renderMainLayer(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        TextureData baseLayer = ClientData.TOGGLE_BUTTON.get(false, isHovered, isEnabled());
        ClientUtils.renderTexture(graphics, baseLayer, this.getX(), this.getY());
    }

    @Override
    protected void renderIcon(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        if (this.isEnabled()){
            IconTexture iconTexture = switch (cachedRenderMode) {
                case OFF -> isHovered() ?
                        new IconTexture(ClientData.HUD_OFF_HOVERED, 2, 2)
                        : new IconTexture(ClientData.HUD_OFF, 2, 2);
                case ALWAYS -> isHovered() ? new IconTexture(ClientData.HUD_ALWAYS_HOVERED, 2, 2)
                        : new IconTexture(ClientData.HUD_ALWAYS, 2, 2);
                case HAND_ONLY -> isHovered() ? new IconTexture(ClientData.HUD_HAND_HOVERED, 2, 2)
                        : new IconTexture(ClientData.HUD_HAND, 2, 2);
            };
            ClientUtils.renderTexture(graphics, iconTexture, this.getX(), this.getY());
            if (isPopupVisible()){
                HudRenderer.forceRender(graphics, partialTicks, uuid);
            }
        }
        else {
            ClientUtils.renderTexture(graphics, new IconTexture(ClientData.LOCK, 3, 2), this.getX(), this.getY());
        }
    }


    @Override
    public void refreshTooltip() {
        String key = "tooltip.travelerscompass.settings.hud";
        Component title = Component.translatable(key);
        List<Component> list = new ArrayList<>();
        list.add(title);
        Component state;
        if (isEnabled()){
            state = switch (cachedRenderMode){
                case OFF -> Toggleable.DISABLED;
                case HAND_ONLY -> HUD_REQUIRES_HAND;
                case ALWAYS -> HUD_ENABLED;
            };
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

}
