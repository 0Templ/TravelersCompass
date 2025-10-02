package com.nine.travelerscompass.network.s2c;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.network.c2s.HandshakeResponsePacket;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class HandshakeRequestPacket extends FriendlyByteBuf {

    public static final ResourceLocation ID = new ResourceLocation(TCCommon.MODID, "request_packet");

    public HandshakeRequestPacket() {
        super(Unpooled.buffer());
    }

    public static void onMessage(Minecraft minecraft, ClientPacketListener clientPacketListener, FriendlyByteBuf buf, PacketSender packetSender) {
        int clientProtocolVersion = TCCommon.NETWORK_PROTOCOL_VERSION;
        minecraft.executeIfPossible(() -> {
            ClientPlayNetworking.send(HandshakeResponsePacket.ID, new HandshakeResponsePacket(clientProtocolVersion, TCCommon.MOD_VERSION));
        });

    }
}

