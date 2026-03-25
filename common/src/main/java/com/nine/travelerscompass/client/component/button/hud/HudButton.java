package com.nine.travelerscompass.client.component.button.hud;

import com.nine.travelerscompass.client.ui.constant.TCColors;
import com.nine.travelerscompass.client.ui.constant.TCComponents;
import com.nine.travelerscompass.client.ui.constant.TCTextures;
import com.nine.travelerscompass.client.ui.constant.TCIcons;

import com.nine.travelerscompass.client.ClientCache;
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
import com.nine.travelerscompass.common.data.CompassComponents;
import com.nine.travelerscompass.config.TCConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class HudButton extends PopupButton {
	
	private final boolean configEnabled = TCConfig.ENABLE_HUD.get();
	
	private HudRenderMode cached;
	
	public HudButton(ButtonGenericSettings settings) {
		super(
				settings,
				TCTextures.Popup.POPUP_3X3.layer(-41, -13),
				TCTextures.Popup.CONNECTOR_VERTICAL.layer(-1, 1)
		);
		updateState();
		var builder = ButtonSearchModeSettings.builder()
				.position(getX(), getY())
				.size(10, 10)
				.uuid(uuid)
				.lockIcon(TCIcons.Common.SMALL_LOCK)
				.mainLayerSet(TCTextures.Buttons.SMALL_TOGGLE_BUTTON)
				.stackSup(stackSupplier());
		
		ButtonRangeSettings.Builder rangeBuilder = ButtonRangeSettings
				.builder(builder)
				.minusIcons(TCIcons.Common.MINUS_SMALL, TCIcons.Common.MINUS_SMALL_HOVERED, TCIcons.Common.MINUS_SMALL_INACTIVE)
				.plusIcons(TCIcons.Common.PLUS_SMALL, TCIcons.Common.PLUS_SMALL_HOVERED, TCIcons.Common.PLUS_SMALL_INACTIVE);
		
		
		ButtonGrid grid = new ButtonGrid(getX() - 37, getY() - 9, 10, 10, 1, 1);
		
		var hudWithChatButton = SettingsButton.create(builder.copy().position(grid.x(1), grid.y(2))
						.afterInteraction(b -> ClientCache.updateHudSettings(uuid,
								modifier -> modifier.hudWithChat((Boolean) ((SettingsButton<?>) b).cached)))
						.build(),
				CompassComponents.HUD_WITH_CHAT,
				(cached, hovered) -> {
					if (cached) {
						return hovered ? TCIcons.Settings.HUD_CHAT_ACTIVE_HOVERED : TCIcons.Settings.HUD_CHAT_ACTIVE;
					}
					return hovered ? TCIcons.Settings.HUD_CHAT_INACTIVE_HOVERED : TCIcons.Settings.HUD_CHAT_INACTIVE;
				});
		
		var hudAlignmentButton = new SettingsButton<>(builder.copy().position(grid.x(2), grid.y(1))
				.afterInteraction(
						b -> {
							var alignment = (Alignment) ((SettingsButton<?>) b).cached;
							ClientCache.updateHudSettings(uuid, (modifier) -> modifier.alignment(alignment));
						})
				.build(),
				CompassComponents.HUD_ALIGNMENT,
				(cached, hovered) -> switch (cached) {
					case LEFT ->
							hovered ? TCIcons.Settings.HUD_ALIGNMENT_LEFT_HOVERED : TCIcons.Settings.HUD_ALIGNMENT_LEFT;
					case CENTER ->
							hovered ? TCIcons.Settings.HUD_ALIGNMENT_CENTER_HOVERED : TCIcons.Settings.HUD_ALIGNMENT_CENTER;
					case RIGHT ->
							hovered ? TCIcons.Settings.HUD_ALIGNMENT_RIGHT_HOVERED : TCIcons.Settings.HUD_ALIGNMENT_RIGHT;
				},
				(cached) -> Component.translatable("tooltip.travelerscompass.settings.hud_alignment." + cached.name().toLowerCase()).withStyle(ChatFormatting.GRAY));
		
		var xHudPosButton = new HudPosButton(rangeBuilder.copy().position(grid.x(0), grid.y(0))
				.icon(TCIcons.Settings.HUD_X_POS)
				.afterInteraction(b -> {
					ClientCache.updateHudSettings(uuid,
							modifier -> modifier.xPos(((HudPosButton) b).cached));
					ClientCache.updateHudSettings(uuid,
							modifier -> modifier.anchorX((HudAnchorX) ((HudPosButton) b).cachedAnchor));
				})
				.build(),
				0, Minecraft.getInstance().getWindow().getGuiScaledWidth(),
				Component.literal("X").withColor(TCColors.SOFT_GRAY),
				CompassComponents.HUD_X_ANCHOR,
				CompassComponents.HUD_X_POS);
		
		var yHudPosButton = new HudPosButton(rangeBuilder.copy().position(grid.x(1), grid.y(0))
				.icon(TCIcons.Settings.HUD_Y_POS)
				.afterInteraction(b -> {
					ClientCache.updateHudSettings(uuid,
							modifier -> modifier.yPos(((HudPosButton) b).cached));
					ClientCache.updateHudSettings(uuid,
							modifier -> modifier.anchorY((HudAnchorY) ((HudPosButton) b).cachedAnchor));
				})
				.build(),
				0, Minecraft.getInstance().getWindow().getGuiScaledHeight(),
				Component.literal("Y").withColor(TCColors.SOFT_GRAY),
				CompassComponents.HUD_Y_ANCHOR,
				CompassComponents.HUD_Y_POS);
		
		var hudWidthButton = new HudSizeButton(rangeBuilder.copy().position(grid.x(0), grid.y(1))
				.icon(TCIcons.Settings.HUD_WIDTH)
				.afterInteraction(b -> ClientCache.updateHudSettings(uuid, (modifier) -> modifier.width(((HudSizeButton) b).cached)))
				.build(), true);
		
		var hudHeightButton = new HudSizeButton(rangeBuilder.copy().position(grid.x(1), grid.y(1))
				.icon(TCIcons.Settings.HUD_HEIGHT)
				.afterInteraction(b -> ClientCache.updateHudSettings(uuid, (modifier) -> modifier.height(((HudSizeButton) b).cached)))
				.build(), false);
		
		var scaleRangeButton = new FloatRangeButton(rangeBuilder.copy().position(grid.x(0), grid.y(2))
				.icon(TCIcons.Settings.HUD_SCALE)
				.afterInteraction(b -> ClientCache.updateHudSettings(uuid, (modifier) -> modifier.scale(((FloatRangeButton) b).cached)))
				.build(),
				0.1F, 3F, 0.01F, 0.1F,
				CompassComponents.HUD_SCALE);
		
		var hudTypeButton = new SettingsButton<>(builder.copy().position(grid.x(2), grid.y(0))
				.afterInteraction(
						b -> {
							var type = (HudType) ((SettingsButton<?>) b).cached;
							var size = CompassComponents.HUD_SIZE.get(stack());
							ClientCache.updateHudSettings(uuid, (modifier) -> modifier.hudType(type));
							ClientCache.updateHudSettings(uuid, (modifier) -> modifier.width(size.getWidth(type)));
							ClientCache.updateHudSettings(uuid, (modifier) -> modifier.height(size.getHeight(type)));
						})
				.build(),
				CompassComponents.HUD_TYPE,
				(cached, hovered) -> switch (cached) {
					case COMPACT ->
							hovered ? TCIcons.Settings.HUD_TYPE_COMPACT_HOVERED : TCIcons.Settings.HUD_TYPE_COMPACT;
					case EXTENDED ->
							hovered ? TCIcons.Settings.HUD_TYPE_EXTENDED_HOVERED : TCIcons.Settings.HUD_TYPE_EXTENDED;
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
		CompassComponents.putDefaultToServer(stack, CompassComponents.HUD_X_POS);
		CompassComponents.putDefaultToServer(stack, CompassComponents.HUD_Y_POS);
		CompassComponents.putDefaultToServer(stack, CompassComponents.HUD_SIZE);
		CompassComponents.putDefaultToServer(stack, CompassComponents.HUD_SCALE);
		CompassComponents.putDefaultToServer(stack, CompassComponents.HUD_TYPE);
		CompassComponents.putDefaultToServer(stack, CompassComponents.HUD_ALIGNMENT);
		CompassComponents.putDefaultToServer(stack, CompassComponents.HUD_WITH_CHAT);
		CompassComponents.putDefaultToServer(stack, CompassComponents.HUD_Y_ANCHOR);
		CompassComponents.putDefaultToServer(stack, CompassComponents.HUD_X_ANCHOR);
	}
	
	@Override
	public void updateState() {
		cached = CompassComponents.HUD_RENDER_MODE.get(stack());
		refreshTooltip();
		ClientCache.updateHudSettings(uuid, modifier -> modifier.renderMode(cached));
	}
	
	@Override
	public boolean onLeftClick(double mouseX, double mouseY) {
		CompassComponents.toggleToServer(stack(), CompassComponents.HUD_RENDER_MODE, true);
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
						isHovered ? TCIcons.Settings.HUD_OFF_HOVERED : TCIcons.Settings.HUD_OFF;
				case ALWAYS ->
						isHovered ? TCIcons.Settings.HUD_ALWAYS_HOVERED : TCIcons.Settings.HUD_ALWAYS;
				case HAND_ONLY ->
						isHovered ? TCIcons.Settings.HUD_HAND_HOVERED : TCIcons.Settings.HUD_HAND;
			};
		} else {
			iconTexture = TCIcons.Common.LOCK;
		}
		return iconTexture;
	}
	
	@Override
	public boolean isPopupVisible() {
		return super.isPopupVisible() && configEnabled && cached != HudRenderMode.OFF;
	}
	
	@Override
	protected void renderIcon(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
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
				case OFF -> TCComponents.DISABLED;
				case HAND_ONLY -> TCComponents.HUD_REQUIRES_HAND;
				case ALWAYS -> TCComponents.HUD_ENABLED;
			};
			if (shiftPressed) {
				builder.desc(key + ".desc");
			}
		} else {
			state = TCComponents.CONFIG_DISABLED;
		}
		builder.state(state);
		this.setTooltip(builder.buildAsTooltip());
	}
	
}
