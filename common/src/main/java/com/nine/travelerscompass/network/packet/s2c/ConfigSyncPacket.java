package com.nine.travelerscompass.network.packet.s2c;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.config.ConfigSyncManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public record ConfigSyncPacket(Map<String, Object> map) implements S2CPacket {

    public static final Type<ConfigSyncPacket> ID =
            new Type<>(ResourceLocation.fromNamespaceAndPath(TCCommon.MODID, "config_sync_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ConfigSyncPacket> CODEC =
            StreamCodec.ofMember(ConfigSyncPacket::encode, ConfigSyncPacket::decode);

    public ConfigSyncPacket() {
        this(new HashMap<>());
    }

    public void encode(RegistryFriendlyByteBuf buf) {
        ConfigSyncManager.encode(buf);
    }

    public static ConfigSyncPacket decode(RegistryFriendlyByteBuf buf) {
        return new ConfigSyncPacket(ConfigSyncManager.decode(buf));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }

    @Override
    public void handle() {
        ConfigSyncManager.SYNCED_VALUES.clear();
        ConfigSyncManager.SYNCED_VALUES.putAll(map);
    }
}