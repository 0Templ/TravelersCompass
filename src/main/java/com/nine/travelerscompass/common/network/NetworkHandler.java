package com.nine.travelerscompass.common.network;

import com.nine.travelerscompass.TravelersCompass;
import com.nine.travelerscompass.common.network.packet.ConfigButtonPacket;
import com.nine.travelerscompass.common.network.packet.GhostTargetPacket;
import com.nine.travelerscompass.common.network.packet.SearchButtonPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {


    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(

            new ResourceLocation(TravelersCompass.MODID, "messages"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    public static void regiser() {
        int packetId  = 0;
        CHANNEL.registerMessage(packetId ++, SearchButtonPacket.class, SearchButtonPacket::encode, SearchButtonPacket::new, SearchButtonPacket::onMessage);
        CHANNEL.registerMessage(packetId ++, ConfigButtonPacket.class, ConfigButtonPacket::encode, ConfigButtonPacket::new, ConfigButtonPacket::onMessage);
        CHANNEL.registerMessage(packetId ++, GhostTargetPacket.class, GhostTargetPacket::encode, GhostTargetPacket::new, GhostTargetPacket::onMessage);

    }
}
