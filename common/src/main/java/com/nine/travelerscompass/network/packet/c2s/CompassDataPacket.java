package com.nine.travelerscompass.network.packet.c2s;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.data.DataStorage;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;
import java.util.UUID;

public record CompassDataPacket<T>(UUID compassUUID, DataStorage<T> dataStorage, T value) implements C2SPacket {

    @SuppressWarnings("rawtypes")
    public static final Type<CompassDataPacket> ID = new Type<>(
            ResourceLocation.fromNamespaceAndPath(TCCommon.MODID,"compass_data_packet"));

    @SuppressWarnings("rawtypes")
    public static final StreamCodec<RegistryFriendlyByteBuf, CompassDataPacket> PACKET_CODEC = StreamCodec.ofMember(
            CompassDataPacket::encode, CompassDataPacket::decode);

    public void encode(RegistryFriendlyByteBuf buf) {
        buf.writeByte(dataStorage.networkId());
        if (CompassProperties.CLIENT_EDITABLE.containsKey(dataStorage.networkId())) {
            buf.writeUUID(compassUUID);
            dataStorage.dataComponent().streamCodec().encode(buf, value);
        }
    }

    public static <T> CompassDataPacket<?> decode(RegistryFriendlyByteBuf buf) {
        byte networkId = buf.readByte();
        DataStorage<T> dataStorage = CompassProperties.getById(networkId);
        UUID uuid = null;
        T value = null;
        if (CompassProperties.CLIENT_EDITABLE.containsKey(dataStorage.networkId())) {
            uuid = buf.readUUID();
            value = dataStorage.dataComponent().streamCodec().decode(buf);
        }
        return new CompassDataPacket<>(uuid, dataStorage, value);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }

    @Override
    public void handle(ServerPlayer player) {
        if (CompassProperties.CLIENT_EDITABLE.containsKey(dataStorage.networkId())) {
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() instanceof TravelersCompassItem) {
                dataStorage.set(stack, value);
            }
        }
    }
}

