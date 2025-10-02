package com.nine.travelerscompass.network.packet.s2c;

import com.nine.travelerscompass.client.ClientData;
import com.nine.travelerscompass.common.search.location.ILocationObject;
import com.nine.travelerscompass.common.search.location.codec.LocationCodecs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class HudLocationDataPacket {

    private final ILocationObject object;
    private final UUID uuid;
    private final boolean valid;

    public HudLocationDataPacket(ILocationObject object, UUID uuid) {
        this.valid = object != null;
        this.object = object;
        this.uuid = uuid;
    }

    public HudLocationDataPacket(FriendlyByteBuf buf) {
        valid = buf.readBoolean();
        object = valid ? LocationCodecs.read(buf) : null;
        uuid = buf.readUUID();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(valid);
        if (valid){
            LocationCodecs.write(buf, object);
        }
        buf.writeUUID(uuid);
    }

    public static void onMessage(HudLocationDataPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                    UUID uuid = packet.uuid;
                    ILocationObject object = packet.object;
                    if (ClientData.HUD_DATA_CACHE.containsKey(uuid)){
                        ClientData.HUD_DATA_CACHE.get(uuid).setLocationObject(object);
                    }
                })
        );
        ctx.get().setPacketHandled(true);
    }
}
