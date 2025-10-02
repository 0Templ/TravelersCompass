package com.nine.travelerscompass.network.packet.s2c;

import com.nine.travelerscompass.client.ClientData;
import com.nine.travelerscompass.client.utils.SearchProgress;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class SearchProgressSyncPacket {
    
    private final SearchProgress progress;
    private final UUID uuid ;

    public SearchProgressSyncPacket(SearchProgress progress, UUID uuid) {
        this.progress = progress;
        this.uuid = uuid;
    }

    public SearchProgressSyncPacket(FriendlyByteBuf buf) {
        int v1 = buf.readVarInt();
        int v2 = buf.readVarInt();
        uuid = buf.readUUID();
        progress = new SearchProgress(v1, v2);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(progress.progress);
        buf.writeVarInt(progress.total);
        buf.writeUUID(uuid);
    }

    public static void onMessage(SearchProgressSyncPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                    if (packet.progress.progress < 0) {
                        ClientData.PROGRESS_DATA_CACHE.remove(packet.uuid);
                    } else {
                        ClientData.PROGRESS_DATA_CACHE.put(packet.uuid, packet.progress);
                    }
                })
        );
        ctx.get().setPacketHandled(true);
    }
}
