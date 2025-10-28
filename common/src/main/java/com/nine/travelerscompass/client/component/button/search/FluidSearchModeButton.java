package com.nine.travelerscompass.client.component.button.search;

import com.nine.travelerscompass.client.component.button.settings.ButtonSearchModeSettings;
import com.nine.travelerscompass.client.utils.ClientUtils;
import com.nine.travelerscompass.client.utils.Icon;
import com.nine.travelerscompass.client.utils.IconLayer;
import net.minecraft.client.gui.GuiGraphics;

public class FluidSearchModeButton extends SearchModeButton {
	
	private int frameIndex = 0;
	private int tickCount = 0;
	final int framesPerTick = 2;
	final int maxFrames = 31;
	
	public FluidSearchModeButton(ButtonSearchModeSettings settings) {
		super(settings);
	}
	
	@Override
	public void tick() {
		tickCount++;
		if (tickCount % framesPerTick == 0) {
			frameIndex = (frameIndex + 1) % maxFrames;
		}
	}
	
	@Override
	public void renderIcon(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		Icon icon = isToggled() ? iconActive : iconInactive;
		int yOff = 8 * frameIndex;
		var textureData = icon.layers().getFirst().textureData().toBuilder().uv(0, yOff).build();
		IconLayer layer = IconLayer.withProperties(icon.layers().getFirst(), textureData);
		ClientUtils.renderIconLayer(graphics, layer, this.getX(), this.getY());
	}
	
}
