package com.nine.travelerscompass.network.c2s;

import com.nine.travelerscompass.TCCommon;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class HandshakeResponsePacket extends FriendlyByteBuf {

    public static final ResourceLocation ID = new ResourceLocation(TCCommon.MODID, "responce_packet");

    public HandshakeResponsePacket(int clientProtocolVersion, String clientModVersion) {
        super(Unpooled.buffer());
        writeInt(clientProtocolVersion);
        writeUtf(clientModVersion);
    }

    public static void onMessage(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl serverGamePacketListener, FriendlyByteBuf buf, PacketSender packetSender) {
        int clientProtocolVersion = buf.readInt();
        String clientModVersion = buf.readUtf();
        int serverProtocolVersion = TCCommon.NETWORK_PROTOCOL_VERSION;
        server.execute(() -> {
            if (clientProtocolVersion != serverProtocolVersion){
                serverGamePacketListener.disconnect(Component.literal("Invalid protocol version! Server \"Traveler's Compass\" version: " + TCCommon.MOD_VERSION + ", client version: " + clientModVersion));
            }
        });
    }

}
