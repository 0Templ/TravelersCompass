package com.nine.travelerscompass.client.render;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import com.nine.travelerscompass.common.utils.HeightAttitude;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class HeightMarkerRenderer {
	
	public static void render(GuiGraphics graphics, ItemStack stack, int x, int y) {
		if (stack.getItem() instanceof TravelersCompassItem) {
			if (CompassProperties.HEIGHT_MARKER.get(stack)) {
				
				HeightAttitude heightAttitude = CompassProperties.get(stack, CompassProperties.TARGET_HEIGHT);
				if (heightAttitude == HeightAttitude.NONE) {
					return;
				}
				String attitudeId = heightAttitude.getKey();
				String priority = CompassProperties.get(stack, CompassProperties.PRIORITY_ITEM_FOUND) ? "_priority" : "";
				graphics.blit(RenderPipelines.GUI_TEXTURED, ResourceLocation.fromNamespaceAndPath(TCCommon.MODID, "textures/gui/marker/altitude_" + attitudeId + priority + ".png"),
						x, y, 0, 0, 5, 5, 5, 5);
			}
		}
		
	}
	
}
