package com.nine.travelerscompass.network.c2s;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.data.DataStorage;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.ItemStack;

public class CompassDataPacket extends FriendlyByteBuf {

    public static final ResourceLocation ID = new ResourceLocation(TCCommon.MODID, "compass_data_packet");

    public CompassDataPacket(DataStorage<?> data, CompoundTag tag) {
        super(Unpooled.buffer());
        writeResourceLocation(data.getId());
        if (CompassProperties.CLIENT_EDITABLE.containsKey(data.getId())) {
            writeNbt(tag);
        }
    }

    public static void onMessage(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl serverGamePacketListener, FriendlyByteBuf buf, PacketSender packetSender) {
        var id = buf.readResourceLocation();
        if (!CompassProperties.CLIENT_EDITABLE.containsKey(id)) return;
        DataStorage<?> dataStorage = CompassProperties.REGISTRY.get(id);
        CompoundTag tag = buf.readNbt();
        server.execute(() -> {
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() instanceof TravelersCompassItem && tag != null){
                Object value = CompassProperties.get(tag, dataStorage);
                @SuppressWarnings("unchecked")
                DataStorage<Object> finalDataStorage = (DataStorage<Object>) dataStorage;
                CompassProperties.putWithValidation(stack, finalDataStorage, value);
            }
        });
    }

}
