package com.nine.travelerscompass.platform;

import com.nine.travelerscompass.client.utils.SearchProgress;
import com.nine.travelerscompass.common.data.DataStorage;
import com.nine.travelerscompass.common.search.location.ILocationObject;
import com.nine.travelerscompass.compat.NEI;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public interface IPlatformNetworkHelper {

    void sendC2SDataPacket(DataStorage<?> data, CompoundTag tag);

    void sendC2SPausePacket(UUID uuid);

    void sendC2SWideSearchPacket(UUID uuid);

    void sendC2SGhostTargetPacket(int slotIndex, ItemStack stack, NEI nei);


    void sendS2CSearchProgressPacket(ServerPlayer player, SearchProgress progress, UUID uuid);

    void sendS2CDataPacket(ServerPlayer player, DataStorage<?> data, CompoundTag tag, UUID uuid);

    void sendS2CHudLocationDataPacket(ServerPlayer player, ILocationObject object, UUID uuid);

    void sendS2ConfigSyncPacket(ServerPlayer player);

    void sendS2CHandshakePacket(ServerPlayer player);

    void sendS2CCompletePacket(ServerPlayer player);

}
