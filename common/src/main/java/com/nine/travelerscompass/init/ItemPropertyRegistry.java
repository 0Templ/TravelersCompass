package com.nine.travelerscompass.init;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.client.render.item.TravelersCompassAngle;
import com.nine.travelerscompass.client.render.item.TravelersCompassPriority;
import com.nine.travelerscompass.client.render.item.TravelersCompassState;
import com.nine.travelerscompass.mixin.client.accessor.ConditionalItemModelPropertiesAccessor;
import com.nine.travelerscompass.mixin.client.accessor.RangeSelectItemModelPropertiesAccessor;
import net.minecraft.resources.ResourceLocation;

public class ItemPropertyRegistry {
	
	private static void SetupItemModelProperties() {
		ConditionalItemModelPropertiesAccessor.travelerscompass$getConditionalIdMapper()
				.put(ResourceLocation.fromNamespaceAndPath(TCCommon.MODID, "priority"),
						TravelersCompassPriority.MAP_CODEC);
		RangeSelectItemModelPropertiesAccessor.travelerscompass$getRangeSelectIdMapper()
				.put(ResourceLocation.fromNamespaceAndPath(TCCommon.MODID, "angle"),
						TravelersCompassAngle.MAP_CODEC);
		RangeSelectItemModelPropertiesAccessor.travelerscompass$getRangeSelectIdMapper()
				.put(ResourceLocation.fromNamespaceAndPath(TCCommon.MODID, "state"),
						TravelersCompassState.MAP_CODEC);
	}
	
	public static void init() {
		SetupItemModelProperties();
	}
	
}
