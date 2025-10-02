package com.nine.travelerscompass.network.s2c;

import com.nine.travelerscompass.TCCommon;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class CompletePacket extends FriendlyByteBuf {

    public static final ResourceLocation ID = new ResourceLocation(TCCommon.MODID, "complete_packet");

    public CompletePacket() {
        super(Unpooled.buffer());
    }

    public static void onMessage(Minecraft minecraft, ClientPacketListener clientPacketListener, FriendlyByteBuf buf, PacketSender packetSender) {
        minecraft.executeIfPossible(() -> {
                    TCCommon.updateCache();
                }
        );
    }
}
