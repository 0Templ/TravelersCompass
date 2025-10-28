package com.nine.travelerscompass.compat.modmenu;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.TCFabric;
import com.terraformersmc.modmenu.api.UpdateChannel;
import com.terraformersmc.modmenu.api.UpdateChecker;
import com.terraformersmc.modmenu.api.UpdateInfo;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class TCUpdateChecker implements UpdateChecker {
	
	@Override
	public UpdateInfo checkForUpdates() {
		JsonObject updateInfo = tryGetUrlInfo();
		if (updateInfo == null) {
			return TCUpdateInfo.NONE;
		}
		UpdateChannel preference = UpdateChannel.getUserPreference();
		var updateVersions = getUpdateVersions(SharedConstants.getCurrentVersion().name(), updateInfo);
		VersionUpdateData update = null;
		var tempVersion = TCCommon.MOD_VERSION;
		for (var ver : updateVersions) {
			if (compareVersions(tempVersion, ver.version()) >= 0) continue;
			if (ver.updateChannel() == UpdateChannel.RELEASE) {
				tempVersion = ver.version();
				update = ver;
			}
			if (ver.updateChannel() == preference) {
				tempVersion = ver.version();
				update = ver;
			}
		}
		return update == null ? TCUpdateInfo.NONE :
				new TCUpdateInfo(true, Component.literal(update.version()), update.url(), update.updateChannel());
	}
	
	public static JsonObject tryGetUrlInfo() {
		try {
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(TCFabric.UPDATE_JSON_URL))
					.timeout(Duration.ofSeconds(10))
					.GET()
					.build();
			
			HttpClient client = HttpClient.newBuilder()
					.connectTimeout(Duration.ofSeconds(10))
					.build();
			
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			if (response.statusCode() == 200) {
				JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
				return json;
			}
		} catch (Exception e) {
			TCCommon.LOGGER.debug("Couldn't fetch update info{}", e.getMessage());
		}
		return null;
	}
	
	public static List<VersionUpdateData> getUpdateVersions(String mcVersion, JsonObject json) {
		List<VersionUpdateData> ret = new ArrayList<>();
		if (json.has(mcVersion)) {
			JsonObject versions = json.get(mcVersion).getAsJsonObject();
			try {
				
				for (var el : versions.entrySet()) {
					JsonObject data = el.getValue().getAsJsonObject();
					String url = data.get("url").getAsString();
					String version = data.get("version").getAsString();
					ret.add(new VersionUpdateData(UpdateChannel.valueOf(el.getKey().toUpperCase()), url, version));
				}
			} catch (IllegalArgumentException ignored) {
			}
		}
		return ret;
	}
	
	public static int compareVersions(String v1, String v2) {
		var splitV1 = v1.split("[.-]");
		var splitV2 = v2.split("[.-]");
		int len = Math.max(splitV1.length, splitV2.length);
		
		for (int i = 0; i < len; i++) {
			var partV1 = i < splitV1.length ? splitV1[i] : "0";
			var partV2 = i < splitV2.length ? splitV2[i] : "0";
			boolean digitV1 = partV1.matches("\\d+");
			boolean digitV2 = partV2.matches("\\d+");
			int compared;
			if (digitV1 && digitV2) {
				compared = Integer.compare(Integer.parseInt(partV1), Integer.parseInt(partV2));
			} else {
				compared = compareChars(partV1, partV2);
			}
			if (compared != 0) return compared;
		}
		return 0;
	}
	
	
	public static int compareChars(String partV1, String partV2) {
		char[] charsV1 = partV1.toLowerCase().toCharArray();
		char[] charsV2 = partV2.toLowerCase().toCharArray();
		int charsLen = Math.max(charsV1.length, charsV2.length);
		for (int j = 0; j < charsLen; j++) {
			char chV1 = j < charsV1.length ? charsV1[j] : '0';
			char chV2 = j < charsV2.length ? charsV2[j] : '0';
			int c1 = charToInt(chV1);
			int c2 = charToInt(chV2);
			if (c1 != c2) {
				return c1 > c2 ? 1 : -1;
			}
		}
		return 0;
	}
	
	public static int charToInt(char c) {
		int ret = c;
		if (c >= '0' && c <= '9') {
			ret -= 48;
		}
		return ret;
	}
	
}
