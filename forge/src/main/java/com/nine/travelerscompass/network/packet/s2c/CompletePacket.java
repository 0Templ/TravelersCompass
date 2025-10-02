package com.nine.travelerscompass.network.packet.s2c;

import com.nine.travelerscompass.TCCommon;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CompletePacket {

    public CompletePacket() {
    }

    public CompletePacket(FriendlyByteBuf buf) {
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public static void onMessage(CompletePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> TCCommon::updateCache)
        );
        ctx.get().setPacketHandled(true);
    }

}
