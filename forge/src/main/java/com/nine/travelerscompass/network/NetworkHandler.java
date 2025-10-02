package com.nine.travelerscompass.network;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.network.packet.c2s.CompassDataPacket;
import com.nine.travelerscompass.network.packet.c2s.GhostTargetPacket;
import com.nine.travelerscompass.network.packet.c2s.PausePacket;
import com.nine.travelerscompass.network.packet.c2s.WideSearchPacket;
import com.nine.travelerscompass.network.packet.s2c.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {

    private static final String PROTOCOL_VERSION = String.valueOf(TCCommon.NETWORK_PROTOCOL_VERSION);

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(TCCommon.MODID, "network"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int packetId = 0;
        CHANNEL.registerMessage(packetId++, CompassDataPacket.class, CompassDataPacket::encode, CompassDataPacket::new, CompassDataPacket::onMessage);
        CHANNEL.registerMessage(packetId++, PausePacket.class, PausePacket::encode, PausePacket::new, PausePacket::onMessage);
        CHANNEL.registerMessage(packetId++, WideSearchPacket.class, WideSearchPacket::encode, WideSearchPacket::new, WideSearchPacket::onMessage);
        CHANNEL.registerMessage(packetId++, GhostTargetPacket.class, GhostTargetPacket::encode, GhostTargetPacket::new, GhostTargetPacket::onMessage);

        CHANNEL.registerMessage(packetId++, HudLocationDataPacket.class, HudLocationDataPacket::encode, HudLocationDataPacket::new, HudLocationDataPacket::onMessage);

        CHANNEL.registerMessage(packetId++, SyncDataPacket.class, SyncDataPacket::encode, SyncDataPacket::new, SyncDataPacket::onMessage);

        CHANNEL.registerMessage(packetId++, SearchProgressSyncPacket.class, SearchProgressSyncPacket::encode, SearchProgressSyncPacket::new, SearchProgressSyncPacket::onMessage);
        CHANNEL.registerMessage(packetId++, ConfigSyncPacket.class, ConfigSyncPacket::encode, ConfigSyncPacket::new, ConfigSyncPacket::onMessage);
        CHANNEL.registerMessage(packetId++, CompletePacket.class, CompletePacket::encode, CompletePacket::new, CompletePacket::onMessage);
    }
}
