package com.nine.travelerscompass.network.s2c;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.data.DataStorage;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class SyncDataPacket extends FriendlyByteBuf {

    public static final ResourceLocation ID = new ResourceLocation(TCCommon.MODID, "s2c_sync_packet");

    public SyncDataPacket(DataStorage<?> data, CompoundTag tag, UUID uuid) {
        super(Unpooled.buffer());
        writeResourceLocation(data.getId());
        writeNbt(tag);
        writeUUID(uuid);
    }


    public static void onMessage(Minecraft minecraft, ClientPacketListener clientPacketListener, FriendlyByteBuf buf, PacketSender packetSender) {
        DataStorage<?> data = CompassProperties.REGISTRY.get(buf.readResourceLocation());
        CompoundTag tag = buf.readNbt();
        UUID uuid = buf.readUUID();
        minecraft.executeIfPossible(() -> {
            handleClientPacket(data, tag, uuid);
        });

    }


    @Environment(EnvType.CLIENT)
    private static void handleClientPacket(DataStorage<?> data, CompoundTag tag, UUID uuid) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        ItemStack stack = null;
        for (ItemStack slotStack : player.getInventory().items) {
            if (slotStack.getItem() instanceof TravelersCompassItem &&
                    CompassProperties.COMPASS_UUID.get(slotStack).equals(uuid)) {
                stack = slotStack;
                break;
            }
        }

        if (stack != null && tag != null) {
            Object value = CompassProperties.get(tag, data);
            @SuppressWarnings("unchecked")
            DataStorage<Object> finalDataStorage = (DataStorage<Object>) data;
            CompassProperties.putWithValidation(stack, finalDataStorage, value);
        }
    }

}
