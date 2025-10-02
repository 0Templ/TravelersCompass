package com.nine.travelerscompass.network.packet.s2c;

import com.nine.travelerscompass.config.ConfigSyncManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ConfigSyncPacket {

    private final Map<String, Object> map;

    public ConfigSyncPacket() {
        this.map = new HashMap<>();
    }

    public ConfigSyncPacket(FriendlyByteBuf buf) {
        map = ConfigSyncManager.read(buf);
    }

    public void encode(FriendlyByteBuf buf) {
        ConfigSyncManager.write(buf);
    }

    public static void onMessage(ConfigSyncPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                    ConfigSyncManager.SYNCED_VALUES.clear();
                    ConfigSyncManager.SYNCED_VALUES.putAll(packet.map);
                })
        );
        ctx.get().setPacketHandled(true);
    }
}
