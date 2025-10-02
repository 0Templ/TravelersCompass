package com.nine.travelerscompass.network;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.network.packet.PacketHolder;
import com.nine.travelerscompass.network.packet.c2s.C2SPacket;
import com.nine.travelerscompass.network.packet.s2c.S2CPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.SimpleChannel;
import net.minecraftforge.server.ServerLifecycleHooks;

public class ForgeNetworkHandler {

    public static final ResourceLocation PROTOCOL_NAME = ResourceLocation.fromNamespaceAndPath(TCCommon.MODID, "network_packets");

    public static final SimpleChannel CHANNEL = ChannelBuilder.named(PROTOCOL_NAME)
            .networkProtocolVersion(TCCommon.NETWORK_PROTOCOL_VERSION)
            .optionalClient()
            .optionalServer()
            .clientAcceptedVersions(Channel.VersionTest.exact(TCCommon.NETWORK_PROTOCOL_VERSION))
            .simpleChannel();


    public static void init() {
        TCNetworkManager.init();
        for (var holder : TCNetworkManager.C2S_PACKETS) {
            registerC2S(holder);
        }
        for (var holder : TCNetworkManager.S2C_PACKETS) {
            registerS2C(holder);
        }

    }

    private static <T extends S2CPacket> void registerS2C(PacketHolder<T> holder) {
        CHANNEL.messageBuilder(holder.clazz())
                .encoder((msg, buf) -> holder.codec().encode(new RegistryFriendlyByteBuf(buf, getServerRegistryAccess()), msg))
                .decoder(buf -> holder.codec().decode(new RegistryFriendlyByteBuf(buf, getClientRegistryAccess())))
                .consumerMainThread(ForgeNetworkHandler::handle)
                .add();
    }

    private static <T extends C2SPacket> void registerC2S(PacketHolder<T> holder) {
        CHANNEL.messageBuilder(holder.clazz())
                .encoder((msg, buf) -> holder.codec().encode(new RegistryFriendlyByteBuf(buf, getClientRegistryAccess()), msg))
                .decoder(buf -> holder.codec().decode(new RegistryFriendlyByteBuf(buf, getServerRegistryAccess())))
                .consumerMainThread(ForgeNetworkHandler::handle)
                .add();
    }

    private static RegistryAccess getClientRegistryAccess(){
        Minecraft mc = Minecraft.getInstance();
        RegistryAccess registryAccess = null;
        if (mc.level != null){
            registryAccess = mc.level.registryAccess();
        }
        return registryAccess;
    }

    private static RegistryAccess getServerRegistryAccess(){
        RegistryAccess registryAccess = null;
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server != null){
            registryAccess = server.registryAccess();
        }
        return registryAccess;
    }


    public static void handle(S2CPacket packet, CustomPayloadEvent.Context context){
        context.enqueueWork(packet::handle);
        context.setPacketHandled(true);
    }

    public static void handle(C2SPacket packet, CustomPayloadEvent.Context context){
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender != null) {
                packet.handle(sender);
            }
        });
        context.setPacketHandled(true);
    }
}
