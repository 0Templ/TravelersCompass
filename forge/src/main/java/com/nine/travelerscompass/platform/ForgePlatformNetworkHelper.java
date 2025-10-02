package com.nine.travelerscompass.platform;

import com.nine.travelerscompass.client.utils.SearchProgress;
import com.nine.travelerscompass.common.data.DataStorage;
import com.nine.travelerscompass.common.search.location.ILocationObject;
import com.nine.travelerscompass.compat.NEI;
import com.nine.travelerscompass.network.NetworkHandler;
import com.nine.travelerscompass.network.packet.c2s.CompassDataPacket;
import com.nine.travelerscompass.network.packet.c2s.GhostTargetPacket;
import com.nine.travelerscompass.network.packet.c2s.PausePacket;
import com.nine.travelerscompass.network.packet.c2s.WideSearchPacket;
import com.nine.travelerscompass.network.packet.s2c.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;

public class ForgePlatformNetworkHelper implements IPlatformNetworkHelper {

    @Override
    public void sendC2SDataPacket(DataStorage<?> data, CompoundTag tag) {
        NetworkHandler.CHANNEL.sendToServer(new CompassDataPacket(data, tag));
    }

    @Override
    public void sendC2SPausePacket(UUID uuid) {
        NetworkHandler.CHANNEL.sendToServer(new PausePacket(uuid));
    }

    @Override
    public void sendC2SWideSearchPacket(UUID uuid){
        NetworkHandler.CHANNEL.sendToServer(new WideSearchPacket(uuid));
    }

    @Override
    public void sendC2SGhostTargetPacket(int slotIndex, ItemStack stack, NEI nei){
        NetworkHandler.CHANNEL.sendToServer(new GhostTargetPacket(slotIndex, stack, nei));
    }


    @Override
    public void sendS2CSearchProgressPacket(ServerPlayer player, SearchProgress progress, UUID uuid){
        NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SearchProgressSyncPacket(progress, uuid));
    }

    @Override
    public void sendS2CHudLocationDataPacket(ServerPlayer player, ILocationObject object, UUID uuid) {
        NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new HudLocationDataPacket(object, uuid));
    }

    @Override
    public void sendS2CDataPacket(ServerPlayer player, DataStorage<?> data, CompoundTag tag, UUID uuid){
        NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SyncDataPacket(data, tag, uuid));
    }

    @Override
    public void sendS2ConfigSyncPacket(ServerPlayer player){
        NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new ConfigSyncPacket());
    }

    @Override
    public void sendS2CHandshakePacket(ServerPlayer player) {
        //Forge has a built-in compatibility check between the client and server.
    }

    @Override
    public void sendS2CCompletePacket(ServerPlayer player){
        NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new CompletePacket());
    }
}
