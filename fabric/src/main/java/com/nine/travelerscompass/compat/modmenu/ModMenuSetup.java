package com.nine.travelerscompass.compat.modmenu;

import com.nine.travelerscompass.TCCommon;
import com.terraformersmc.modmenu.api.ModMenuApi;
import com.terraformersmc.modmenu.api.UpdateChecker;

import java.util.Map;

public class ModMenuSetup implements ModMenuApi {
	
	@Override
	public Map<String, UpdateChecker> getProvidedUpdateCheckers() {
		return java.util.Map.of(
				TCCommon.MODID, new TCUpdateChecker()
		);
	}
	
}
