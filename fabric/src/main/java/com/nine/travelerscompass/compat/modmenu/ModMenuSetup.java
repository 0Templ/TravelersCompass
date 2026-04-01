package com.nine.travelerscompass.compat.modmenu;

import com.nine.travelerscompass.TCCommon;
import com.terraformersmc.modmenu.api.ModMenuApi;
import com.terraformersmc.modmenu.api.UpdateChecker;

import java.util.Map;

public class ModMenuSetup implements ModMenuApi {
	
	public static final String UPDATE_JSON_URL = "https://raw.githubusercontent.com/0Templ/ModVersions/refs/heads/main/fabric/travelers-compass.json";
	
	@Override
	public Map<String, UpdateChecker> getProvidedUpdateCheckers() {
		return java.util.Map.of(
				TCCommon.MODID, new TCUpdateChecker()
		);
	}
	
}
