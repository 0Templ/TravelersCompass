package com.nine.travelerscompass.compat.modmenu;


import com.terraformersmc.modmenu.api.UpdateChannel;
import com.terraformersmc.modmenu.api.UpdateInfo;
import net.minecraft.network.chat.Component;

public class TCUpdateInfo implements UpdateInfo {

    public static TCUpdateInfo NONE = new TCUpdateInfo(false, Component.empty(), "", UpdateChannel.RELEASE);

    private final boolean updateAvailable;
    private final Component updateMessage;
    private final String url;
    private final UpdateChannel channel;

    public TCUpdateInfo(boolean updateAvailable, Component updateMessage, String url, UpdateChannel channel) {
        this.updateAvailable = updateAvailable;
        this.updateMessage = updateMessage;
        this.url = url;
        this.channel = channel;
    }

    @Override
    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    @Override
    public Component getUpdateMessage() {
        return updateMessage;
    }

    @Override
    public String getDownloadLink() {
        return url;
    }

    @Override
    public UpdateChannel getUpdateChannel() {
        return channel;
    }
}