package com.nine.travelerscompass.network.s2c;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.client.ClientData;
import com.nine.travelerscompass.common.search.location.ILocationObject;
import com.nine.travelerscompass.common.search.location.codec.LocationCodecs;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public class HudDataPacket extends FriendlyByteBuf {

    public static final ResourceLocation ID = new ResourceLocation(TCCommon.MODID, "hud_sync_packet");

    public HudDataPacket(ILocationObject object, UUID uuid) {
        super(Unpooled.buffer());
        boolean valid = object != null;
        writeBoolean(valid);
        if (valid){
            LocationCodecs.write(this, object);
        }

        writeUUID(uuid);
    }

    public static void onMessage(Minecraft minecraft, ClientPacketListener clientPacketListener, FriendlyByteBuf buf, PacketSender packetSender) {
        boolean valid = buf.readBoolean();
        ILocationObject object = valid ? LocationCodecs.read(buf) : null;
        UUID uuid = buf.readUUID();
        minecraft.executeIfPossible(() -> {
            if (ClientData.HUD_DATA_CACHE.containsKey(uuid)){
                ClientData.HUD_DATA_CACHE.get(uuid).setLocationObject(object);
            }
        });

    }
}