package com.nine.travelerscompass.platform;

import com.nine.travelerscompass.client.utils.SearchProgress;
import com.nine.travelerscompass.common.data.DataStorage;
import com.nine.travelerscompass.common.search.location.ILocationObject;
import com.nine.travelerscompass.compat.NEI;
import com.nine.travelerscompass.network.c2s.CompassDataPacket;
import com.nine.travelerscompass.network.c2s.GhostTargetPacket;
import com.nine.travelerscompass.network.c2s.PausePacket;
import com.nine.travelerscompass.network.c2s.WideSearchPacket;
import com.nine.travelerscompass.network.s2c.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class FabricPlatformNetworkHelper implements IPlatformNetworkHelper {

    @Override
    public void sendC2SDataPacket(DataStorage<?> data, CompoundTag tag) {
       ClientPlayNetworking.send(CompassDataPacket.ID, new CompassDataPacket(data, tag));
    }

    @Override
    public void sendC2SPausePacket(UUID uuid) {
        ClientPlayNetworking.send(PausePacket.ID, new PausePacket(uuid));
    }

    @Override
    public void sendC2SWideSearchPacket(UUID uuid) {
        ClientPlayNetworking.send(WideSearchPacket.ID, new WideSearchPacket(uuid));
    }

    @Override
    public void sendC2SGhostTargetPacket(int slotIndex, ItemStack stack, NEI nei){
        ClientPlayNetworking.send(GhostTargetPacket.ID, new GhostTargetPacket(slotIndex, stack, nei));
    }


    @Override
    public void sendS2CSearchProgressPacket(ServerPlayer player, SearchProgress progress, UUID uuid){
        ServerPlayNetworking.send(player, SearchProgressSyncPacket.ID, new SearchProgressSyncPacket(progress, uuid));
    }

    @Override
    public void sendS2CHudLocationDataPacket(ServerPlayer player, ILocationObject object, UUID uuid) {
        ServerPlayNetworking.send(player, HudDataPacket.ID, new HudDataPacket(object, uuid));
    }

    @Override
    public void sendS2CDataPacket(ServerPlayer player, DataStorage<?> data, CompoundTag tag, UUID uuid){
        ServerPlayNetworking.send(player, SyncDataPacket.ID, new SyncDataPacket(data, tag, uuid));
    }

    @Override
    public void sendS2ConfigSyncPacket(ServerPlayer player){
        ServerPlayNetworking.send(player, ConfigSyncPacket.ID, new ConfigSyncPacket());
    }

    @Override
    public void sendS2CHandshakePacket(ServerPlayer player) {
        ServerPlayNetworking.send(player, HandshakeRequestPacket.ID, new HandshakeRequestPacket());
    }

    @Override
    public void sendS2CCompletePacket(ServerPlayer player) {
        ServerPlayNetworking.send(player, CompletePacket.ID, new CompletePacket());
    }
}
