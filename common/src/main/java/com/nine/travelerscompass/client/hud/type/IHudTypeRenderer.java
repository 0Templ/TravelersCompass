package com.nine.travelerscompass.client.hud.type;

import com.nine.travelerscompass.client.hud.HudData;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.player.Player;

public interface IHudTypeRenderer {
	
	void render(GuiGraphicsExtractor graphics, Player player, Font font, HudData hudData, int x, int y, int width, int height);
	
}
