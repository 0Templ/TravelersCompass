package com.nine.travelerscompass.compat.modmenu;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nine.travelerscompass.TCCommon;
import com.terraformersmc.modmenu.api.ModMenuApi;
import com.terraformersmc.modmenu.api.UpdateChannel;
import com.terraformersmc.modmenu.api.UpdateChecker;
import com.terraformersmc.modmenu.api.UpdateInfo;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModMenuSetup implements ModMenuApi {

    @Override
    public Map<String, UpdateChecker> getProvidedUpdateCheckers() {
        return java.util.Map.of(
                TCCommon.MODID, new TCUpdateChecker()
        );
    }

}
