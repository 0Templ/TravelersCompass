package com.nine.travelerscompass.client.component.button.hud;

import com.nine.travelerscompass.client.ClientCache;
import com.nine.travelerscompass.client.CompassUI;
import com.nine.travelerscompass.client.component.button.SettingsButton;
import com.nine.travelerscompass.client.component.button.popup.PopupButton;
import com.nine.travelerscompass.client.component.button.range.FloatRangeButton;
import com.nine.travelerscompass.client.component.button.settings.ButtonGenericSettings;
import com.nine.travelerscompass.client.component.button.settings.ButtonRangeSettings;
import com.nine.travelerscompass.client.component.button.settings.ButtonSearchModeSettings;
import com.nine.travelerscompass.client.hud.Alignment;
import com.nine.travelerscompass.client.hud.HudRenderMode;
import com.nine.travelerscompass.client.hud.HudRenderer;
import com.nine.travelerscompass.client.hud.HudType;
import com.nine.travelerscompass.client.hud.anchor.HudAnchorX;
import com.nine.travelerscompass.client.hud.anchor.HudAnchorY;
import com.nine.travelerscompass.client.utils.ButtonGrid;
import com.nine.travelerscompass.client.utils.Icon;
import com.nine.travelerscompass.client.utils.TextureData;
import com.nine.travelerscompass.client.utils.TooltipBuilder;
import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.config.TCConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class HudButton extends PopupButton {
	
	private final boolean configEnabled = TCConfig.ENABLE_HUD.get();
	
	private HudRenderMode cached;
	
	public HudButton(ButtonGenericSettings settings) {
		super(
				settings,
				CompassUI.PopupTextures.POPUP_3X3.layer(-41, -13),
				CompassUI.PopupTextures.CONNECTOR_VERTICAL.layer(-1, 1)
		);
		updateState();
		var builder = ButtonSearchModeSettings.builder()
				.position(getX(), getY())
				.size(10, 10)
				.uuid(uuid)
				.lockIcon(CompassUI.CommonTextures.SMALL_LOCK_ICON)
				.mainLayerSet(CompassUI.ButtonTextures.SMALL_TOGGLE_BUTTON)
				.stackSup(stackSupplier());
		
		ButtonRangeSettings.Builder rangeBuilder = ButtonRangeSettings
				.builder(builder)
				.minusIcons(CompassUI.CommonTextures.MINUS_SMALL_ICON, CompassUI.CommonTextures.MINUS_SMALL_HOVERED_ICON, CompassUI.CommonTextures.MINUS_SMALL_INACTIVE_ICON)
				.plusIcons(CompassUI.CommonTextures.PLUS_SMALL_ICON, CompassUI.CommonTextures.PLUS_SMALL_HOVERED_ICON, CompassUI.CommonTextures.PLUS_SMALL_INACTIVE_ICON);
		
		
		ButtonGrid grid = new ButtonGrid(getX() - 37, getY() - 9, 10, 10, 1, 1);
		
		var hudWithChatButton = SettingsButton.create(builder.copy().position(grid.x(1), grid.y(2))
						.afterInteraction(b -> ClientCache.updateHudSettings(uuid,
								modifier -> modifier.hudWithChat((Boolean) ((SettingsButton<?>) b).cached)))
						.build(),
				CompassProperties.HUD_WITH_CHAT,
				(cached, hovered) -> {
					if (cached) {
						return hovered ? CompassUI.SettingsTextures.HUD_CHAT_ACTIVE_HOVERED_ICON : CompassUI.SettingsTextures.HUD_CHAT_ACTIVE_ICON;
					}
					return hovered ? CompassUI.SettingsTextures.HUD_CHAT_INACTIVE_HOVERED_ICON : CompassUI.SettingsTextures.HUD_CHAT_INACTIVE_ICON;
				});
		
		var hudAlignmentButton = new SettingsButton<>(builder.copy().position(grid.x(2), grid.y(1))
				.afterInteraction(
						b -> {
							var alignment = (Alignment) ((SettingsButton<?>) b).cached;
							ClientCache.updateHudSettings(uuid, (modifier) -> modifier.alignment(alignment));
						})
				.build(),
				CompassProperties.HUD_ALIGNMENT,
				(cached, hovered) -> switch (cached) {
					case LEFT ->
							hovered ? CompassUI.SettingsTextures.HUD_ALIGNMENT_LEFT_HOVERED_ICON : CompassUI.SettingsTextures.HUD_ALIGNMENT_LEFT_ICON;
					case CENTER ->
							hovered ? CompassUI.SettingsTextures.HUD_ALIGNMENT_CENTER_HOVERED_ICON : CompassUI.SettingsTextures.HUD_ALIGNMENT_CENTER_ICON;
					case RIGHT ->
							hovered ? CompassUI.SettingsTextures.HUD_ALIGNMENT_RIGHT_HOVERED_ICON : CompassUI.SettingsTextures.HUD_ALIGNMENT_RIGHT_ICON;
				},
				(cached) -> Component.translatable("tooltip.travelerscompass.settings.hud_alignment." + cached.name().toLowerCase()).withStyle(ChatFormatting.GRAY));
		
		var xHudPosButton = new HudPosButton(rangeBuilder.copy().position(grid.x(0), grid.y(0))
				.icon(CompassUI.SettingsTextures.HUD_X_POS_ICON)
				.afterInteraction(b -> {
					ClientCache.updateHudSettings(uuid,
							modifier -> modifier.xPos(((HudPosButton) b).cached));
					ClientCache.updateHudSettings(uuid,
							modifier -> modifier.anchorX((HudAnchorX) ((HudPosButton) b).cachedAnchor));
				})
				.build(),
				0, Minecraft.getInstance().getWindow().getGuiScaledWidth(),
				Component.literal("X").withColor(CompassUI.Colors.SOFT_GRAY),
				CompassProperties.HUD_X_ANCHOR,
				CompassProperties.HUD_X_POS);
		
		var yHudPosButton = new HudPosButton(rangeBuilder.copy().position(grid.x(1), grid.y(0))
				.icon(CompassUI.SettingsTextures.HUD_Y_POS_ICON)
				.afterInteraction(b -> {
					ClientCache.updateHudSettings(uuid,
							modifier -> modifier.yPos(((HudPosButton) b).cached));
					ClientCache.updateHudSettings(uuid,
							modifier -> modifier.anchorY((HudAnchorY) ((HudPosButton) b).cachedAnchor));
				})
				.build(),
				0, Minecraft.getInstance().getWindow().getGuiScaledHeight(),
				Component.literal("Y").withColor(CompassUI.Colors.SOFT_GRAY),
				CompassProperties.HUD_Y_ANCHOR,
				CompassProperties.HUD_Y_POS);
		
		var hudWidthButton = new HudSizeButton(rangeBuilder.copy().position(grid.x(0), grid.y(1))
				.icon(CompassUI.SettingsTextures.HUD_WIDTH_ICON)
				.afterInteraction(b -> ClientCache.updateHudSettings(uuid, (modifier) -> modifier.width(((HudSizeButton) b).cached)))
				.build(), true);
		
		var hudHeightButton = new HudSizeButton(rangeBuilder.copy().position(grid.x(1), grid.y(1))
				.icon(CompassUI.SettingsTextures.HUD_HEIGHT_ICON)
				.afterInteraction(b -> ClientCache.updateHudSettings(uuid, (modifier) -> modifier.height(((HudSizeButton) b).cached)))
				.build(), false);
		
		var scaleRangeButton = new FloatRangeButton(rangeBuilder.copy().position(grid.x(0), grid.y(2))
				.icon(CompassUI.SettingsTextures.HUD_SCALE_ICON)
				.afterInteraction(b -> ClientCache.updateHudSettings(uuid, (modifier) -> modifier.scale(((FloatRangeButton) b).cached)))
				.build(),
				0.1F, 3F, 0.01F, 0.1F,
				CompassProperties.HUD_SCALE);
		
		var hudTypeButton = new SettingsButton<>(builder.copy().position(grid.x(2), grid.y(0))
				.afterInteraction(
						b -> {
							var type = (HudType) ((SettingsButton<?>) b).cached;
							var size = CompassProperties.HUD_SIZE.get(stack());
							ClientCache.updateHudSettings(uuid, (modifier) -> modifier.hudType(type));
							ClientCache.updateHudSettings(uuid, (modifier) -> modifier.width(size.getWidth(type)));
							ClientCache.updateHudSettings(uuid, (modifier) -> modifier.height(size.getHeight(type)));
						})
				.build(),
				CompassProperties.HUD_TYPE,
				(cached, hovered) -> switch (cached) {
					case COMPACT ->
							hovered ? CompassUI.SettingsTextures.HUD_TYPE_COMPACT_HOVERED_ICON : CompassUI.SettingsTextures.HUD_TYPE_COMPACT_ICON;
					case EXTENDED ->
							hovered ? CompassUI.SettingsTextures.HUD_TYPE_EXTENDED_HOVERED_ICON : CompassUI.SettingsTextures.HUD_TYPE_EXTENDED_ICON;
				},
				(cached) -> switch (cached) {
					case COMPACT ->
							Component.translatable("tooltip.travelerscompass.settings.hud_type.compact").withStyle(ChatFormatting.GRAY);
					case EXTENDED ->
							Component.translatable("tooltip.travelerscompass.settings.hud_type.extended").withStyle(ChatFormatting.GRAY);
				});
		
		var resetButton = new HudResetButton(builder.copy().position(grid.x(2), grid.y(2))
				.afterLeftClick((button -> {
					resetHudSettings(stack());
					updatePopupButtons();
					ClientCache.updateAllHudSettings(stack(), true);
				}))
				.build());
		
		
		popupButtons.add(xHudPosButton);
		popupButtons.add(yHudPosButton);
		popupButtons.add(hudWidthButton);
		popupButtons.add(hudWithChatButton);
		popupButtons.add(hudHeightButton);
		popupButtons.add(scaleRangeButton);
		popupButtons.add(hudTypeButton);
		popupButtons.add(hudAlignmentButton);
		popupButtons.add(resetButton);
		
	}
	
	public static void resetHudSettings(ItemStack stack) {
		CompassProperties.putDefaultToServer(stack, CompassProperties.HUD_X_POS);
		CompassProperties.putDefaultToServer(stack, CompassProperties.HUD_Y_POS);
		CompassProperties.putDefaultToServer(stack, CompassProperties.HUD_SIZE);
		CompassProperties.putDefaultToServer(stack, CompassProperties.HUD_SCALE);
		CompassProperties.putDefaultToServer(stack, CompassProperties.HUD_TYPE);
		CompassProperties.putDefaultToServer(stack, CompassProperties.HUD_ALIGNMENT);
		CompassProperties.putDefaultToServer(stack, CompassProperties.HUD_WITH_CHAT);
		CompassProperties.putDefaultToServer(stack, CompassProperties.HUD_Y_ANCHOR);
		CompassProperties.putDefaultToServer(stack, CompassProperties.HUD_X_ANCHOR);
	}
	
	@Override
	public void updateState() {
		cached = CompassProperties.HUD_RENDER_MODE.get(stack());
		refreshTooltip();
		ClientCache.updateHudSettings(uuid, modifier -> modifier.renderMode(cached));
	}
	
	@Override
	public boolean onLeftClick(double mouseX, double mouseY) {
		CompassProperties.toggleToServer(stack(), CompassProperties.HUD_RENDER_MODE, true);
		return true;
	}
	
	@Override
	public void afterPopupClick() {
		refreshTooltip();
	}
	
	@Override
	protected TextureData getMainLayerTexture() {
		return buttonTexturesSet().get(false, isHovered, configEnabled);
	}
	
	@Override
	protected Icon getIcon() {
		Icon iconTexture;
		if (configEnabled) {
			iconTexture = switch (cached) {
				case OFF ->
						isHovered ? CompassUI.SettingsTextures.HUD_OFF_HOVERED_ICON : CompassUI.SettingsTextures.HUD_OFF_ICON;
				case ALWAYS ->
						isHovered ? CompassUI.SettingsTextures.HUD_ALWAYS_HOVERED_ICON : CompassUI.SettingsTextures.HUD_ALWAYS_ICON;
				case HAND_ONLY ->
						isHovered ? CompassUI.SettingsTextures.HUD_HAND_HOVERED_ICON : CompassUI.SettingsTextures.HUD_HAND_ICON;
			};
		} else {
			iconTexture = CompassUI.CommonTextures.LOCK_ICON;
		}
		return iconTexture;
	}
	
	@Override
	public boolean isPopupVisible() {
		return super.isPopupVisible() && configEnabled && cached != HudRenderMode.OFF;
	}
	
	@Override
	protected void renderIcon(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		super.renderIcon(graphics, mouseX, mouseY, partialTicks);
		if (isPopupVisible()) {
			HudRenderer.forceRender(graphics, partialTicks, uuid);
		}
	}
	
	@Override
	public void refreshTooltip() {
		var builder = TooltipBuilder.builder();
		String key = "tooltip.travelerscompass.settings.hud";
		builder.state(key);
		Component state;
		if (configEnabled) {
			state = switch (cached) {
				case OFF -> CompassUI.DISABLED;
				case HAND_ONLY -> CompassUI.HUD_REQUIRES_HAND;
				case ALWAYS -> CompassUI.HUD_ENABLED;
			};
			if (shiftPressed) {
				builder.desc(key + ".desc");
			}
		} else {
			state = CompassUI.CONFIG_DISABLED;
		}
		builder.state(state);
		this.setTooltip(builder.buildAsTooltip());
	}
	
}
