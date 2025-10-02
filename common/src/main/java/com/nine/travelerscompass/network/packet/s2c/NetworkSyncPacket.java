package com.nine.travelerscompass.network.packet.s2c;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.data.DataStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public record NetworkSyncPacket(Map<Byte, String> map) implements S2CPacket {

    public static final Type<NetworkSyncPacket> ID =
            new Type<>(ResourceLocation.fromNamespaceAndPath(TCCommon.MODID, "network_sync_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, NetworkSyncPacket> CODEC =
            StreamCodec.ofMember(NetworkSyncPacket::encode, NetworkSyncPacket::decode);

    public void encode(RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(map.size());
        for (var el : map.entrySet()){
            buf.writeByte(el.getKey());
            buf.writeUtf(el.getValue());
        }
    }

    public static NetworkSyncPacket decode(RegistryFriendlyByteBuf buf) {
        Map<Byte, String > map = new HashMap<>();
        int size = buf.readVarInt();
        for (int i = 0; i < size; i++){
            map.put(buf.readByte(), buf.readUtf());
        }
        return new NetworkSyncPacket(map);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }

    @Override
    public void handle() {
        for (var el : map.entrySet()){
            byte id = el.getKey();
            String value = el.getValue();
            DataStorage<?> current = CompassProperties.NETWORK_REGISTRY.get(id);
            DataStorage<?> correct = CompassProperties.REGISTRY.get(value);

            if (current == null || correct == null) continue;

            if (!Objects.equals(current.id(), value)){
                CompassProperties.NETWORK_REGISTRY.put(id, correct);
            }
        }
    }
}