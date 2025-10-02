package com.nine.travelerscompass.network.s2c;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.client.ClientData;
import com.nine.travelerscompass.client.utils.SearchProgress;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public class SearchProgressSyncPacket extends FriendlyByteBuf {

    public static final ResourceLocation ID = new ResourceLocation(TCCommon.MODID, "search_progress_sync_packet");

    public SearchProgressSyncPacket(SearchProgress progress, UUID uuid) {
        super(Unpooled.buffer());
        writeVarInt(progress.progress);
        writeVarInt(progress.total);
        writeUUID(uuid);
    }

    public static void onMessage(Minecraft minecraft, ClientPacketListener clientPacketListener, FriendlyByteBuf buf, PacketSender packetSender) {
        int v1 = buf.readVarInt();
        int v2 = buf.readVarInt();
        UUID uuid = buf.readUUID();
        SearchProgress progress = new SearchProgress(v1, v2);
        minecraft.executeIfPossible(() -> {
            if (progress.progress < 0) {
                ClientData.PROGRESS_DATA_CACHE.remove(uuid);
            } else {
                ClientData.PROGRESS_DATA_CACHE.put(uuid, progress);
            }
        });

    }
}