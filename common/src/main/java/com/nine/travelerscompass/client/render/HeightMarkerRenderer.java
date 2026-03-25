package com.nine.travelerscompass.client.render;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.common.data.CompassComponents;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import com.nine.travelerscompass.common.utils.HeightAttitude;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public class HeightMarkerRenderer {
	
	public static void render(GuiGraphicsExtractor graphics, ItemStack stack, int x, int y) {
		if (stack.getItem() instanceof TravelersCompassItem) {
			if (CompassComponents.HEIGHT_MARKER.get(stack)) {
				
				HeightAttitude heightAttitude = CompassComponents.get(stack, CompassComponents.TARGET_HEIGHT);
				if (heightAttitude == HeightAttitude.NONE) {
					return;
				}
				String attitudeId = heightAttitude.getKey();
				String priority = CompassComponents.get(stack, CompassComponents.PRIORITY_ITEM_FOUND) ? "_priority" : "";
				graphics.blit(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(TCCommon.MODID, "textures/gui/marker/altitude_" + attitudeId + priority + ".png"),
						x, y, 0, 0, 5, 5, 5, 5);
			}
		}
		
	}
	
}
