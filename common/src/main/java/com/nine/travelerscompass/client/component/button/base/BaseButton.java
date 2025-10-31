package com.nine.travelerscompass.client.component.button.base;

import com.nine.travelerscompass.client.component.button.settings.ButtonGenericSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class BaseButton extends Button {
	
	protected boolean shiftPressed = false;
	protected boolean ctrlPressed = false;
	protected boolean hoverFlag = false;
	
	protected final Supplier<ItemStack> stackSupplier;
	
	private final Consumer<BaseButton> onLeftClick;
	private final Consumer<BaseButton> onRightClick;
	private final Consumer<BaseButton> afterInteraction;
	
	protected final UUID uuid;
	
	public BaseButton(ButtonGenericSettings settings) {
		super(settings.x, settings.y, settings.width, settings.height, Component.empty(), b -> {
		}, DEFAULT_NARRATION);
		this.stackSupplier = settings.supplier;
		this.onLeftClick = settings.afterLeftClick;
		this.onRightClick = settings.afterRightClick;
		this.afterInteraction = settings.afterInteraction;
		this.uuid = settings.uuid;
	}
	
	public ItemStack stack() {
		return stackSupplier.get();
	}
	
	public Supplier<ItemStack> stackSupplier() {
		return stackSupplier;
	}
	
	protected void onKeyModifierChanged() {
		refreshTooltip();
	}
	
	public void refreshTooltip() {
	}
	
	public void updateState() {
	}
	
	protected void onEnter() {
		updateState();
		refreshTooltip();
	}
	
	protected void onExit() {
	}
	
	public void tick() {
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
		if (!active || !visible || !isMouseOver(mouseX, mouseY)) {
			return false;
		}
		boolean handled = onMouseScroll(mouseX, mouseY, scrollX, scrollY);
		if (handled) {
			playMouseScrollSound(Minecraft.getInstance().getSoundManager(), scrollX, scrollY);
			updateState();
			refreshTooltip();
			if (afterInteraction != null) afterInteraction.accept(this);
			return true;
		}
		return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
	}
	
	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean bl) {
		double mouseX = event.x();
		double mouseY = event.y();
		if (!active || !visible || !isMouseOver(mouseX, mouseY)) {
			return false;
		}
		boolean handled = false;
		if (event.button() == 0) {
			handled = onLeftClick(mouseX, mouseY);
			if (onLeftClick != null) {
				onLeftClick.accept(this);
				handled = true;
			}
		} else if (event.button() == 1) {
			handled = onRightClick(mouseX, mouseY);
			if (onRightClick != null) {
				onRightClick.accept(this);
				handled = true;
			}
		}
		if (handled) {
			playDownSound(Minecraft.getInstance().getSoundManager());
			updateState();
			refreshTooltip();
			if (afterInteraction != null) afterInteraction.accept(this);
			return true;
		}
		return false;
	}
	
	public void playMouseScrollSound(SoundManager soundManager, double scrollX, double scrollY) {
		soundManager.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
	}
	
	public boolean onMouseScroll(double mouseX, double mouseY, double scrollX, double scrollY) {
		return false;
	}
	
	public boolean onRightClick(double mouseX, double mouseY) {
		return false;
	}
	
	public boolean onLeftClick(double mouseX, double mouseY) {
		return false;
	}
	
	
	@Override
	protected boolean isValidClickButton(MouseButtonInfo info) {
		return info.button() == 0 || info.button() == 1;
	}
	
	@Override
	public final void onClick(MouseButtonEvent event, boolean bl) {
		onLeftClick(event.x(), event.y());
	}
	
	@Override
	public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		boolean shiftDown = Minecraft.getInstance().hasShiftDown();
		boolean ctrlDown = Minecraft.getInstance().hasControlDown();
		if (shiftDown != shiftPressed || ctrlDown != ctrlPressed) {
			ctrlPressed = ctrlDown;
			shiftPressed = shiftDown;
			onKeyModifierChanged();
		}
		if (isHovered()) {
			if (!hoverFlag) {
				hoverFlag = true;
				refreshTooltip();
				onEnter();
			}
		} else {
			if (hoverFlag) {
				hoverFlag = false;
				onExit();
			}
		}
	}
	
}
