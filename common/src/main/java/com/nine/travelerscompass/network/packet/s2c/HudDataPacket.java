package com.nine.travelerscompass.network.packet.s2c;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.client.ClientData;
import com.nine.travelerscompass.common.search.location.ILocationObject;
import com.nine.travelerscompass.common.search.location.codec.LocationCodecs;
import com.nine.travelerscompass.config.ConfigSyncManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public record HudDataPacket(ILocationObject object, UUID uuid) implements S2CPacket {

    public static final Type<HudDataPacket> ID =
            new Type<>(ResourceLocation.fromNamespaceAndPath(TCCommon.MODID, "hud_data_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, HudDataPacket> CODEC =
            StreamCodec.ofMember(HudDataPacket::encode, HudDataPacket::decode);

    public void encode(RegistryFriendlyByteBuf buf) {
        boolean valid = object != null;
        buf.writeBoolean(valid);
        if (valid){
            LocationCodecs.encode(buf, object);
        }
        buf.writeUUID(uuid);
    }

    public static HudDataPacket decode(RegistryFriendlyByteBuf buf) {
        boolean valid = buf.readBoolean();
        ILocationObject object = valid ? LocationCodecs.read(buf) : null;
        UUID uuid = buf.readUUID();
        return new HudDataPacket(object, uuid);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }

    @Override
    public void handle() {
        if (ClientData.HUD_DATA_CACHE.containsKey(uuid)){
            ClientData.HUD_DATA_CACHE.get(uuid).setLocationObject(object);
        }
    }
}