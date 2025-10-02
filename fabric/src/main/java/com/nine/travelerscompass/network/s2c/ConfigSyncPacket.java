package com.nine.travelerscompass.network.s2c;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.config.ConfigSyncManager;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class ConfigSyncPacket extends FriendlyByteBuf {

    public static final ResourceLocation ID = new ResourceLocation(TCCommon.MODID, "config_sync_packet");

    public ConfigSyncPacket() {
        super(Unpooled.buffer());
        ConfigSyncManager.write(this);
    }

    public static void onMessage(Minecraft minecraft, ClientPacketListener clientPacketListener, FriendlyByteBuf buf, PacketSender packetSender) {
        var map = ConfigSyncManager.read(buf);
        minecraft.executeIfPossible(() -> {
            ConfigSyncManager.SYNCED_VALUES.clear();
            ConfigSyncManager.SYNCED_VALUES.putAll(map);
        });
    }
}
