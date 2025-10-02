package com.nine.travelerscompass.network.packet.s2c;

import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.data.DataStorage;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class SyncDataPacket {

    private final UUID uuid;
    private final DataStorage<?> data;
    private final CompoundTag tag;

    public SyncDataPacket(DataStorage<?> data, CompoundTag tag, UUID uuid) {
        this.data = data;
        this.tag = tag;
        this.uuid = uuid;
    }

    public SyncDataPacket(FriendlyByteBuf buf) {
        data = CompassProperties.REGISTRY.get(buf.readResourceLocation());
        tag = buf.readNbt();
        uuid = buf.readUUID();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(data.getId());
        buf.writeNbt(tag);
        buf.writeUUID(uuid);
    }

    public static void onMessage(SyncDataPacket packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        if (context.getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
            context.enqueueWork(() -> {
                handleClientPacket(packet);
            });
        }
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClientPacket(SyncDataPacket packet) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        ItemStack stack = null;
        ItemStack handStack = player.getMainHandItem();
        if (handStack.getItem() instanceof TravelersCompassItem compassItem){
            if (CompassProperties.COMPASS_UUID.get(handStack).equals(packet.uuid)){
                stack = handStack;
            }
        }
        if (stack == null) {
            for (ItemStack slotStack : player.getInventory().items) {
                if (slotStack.getItem() instanceof TravelersCompassItem &&
                        CompassProperties.COMPASS_UUID.get(slotStack).equals(packet.uuid)) {
                    stack = slotStack;
                    break;
                }
            }
        }

        if (stack != null && packet.tag != null) {
            Object value = CompassProperties.get(packet.tag, packet.data);
            @SuppressWarnings("unchecked")
            DataStorage<Object> finalDataStorage = (DataStorage<Object>) packet.data;
            CompassProperties.putWithValidation(stack, finalDataStorage, value);
        }
    }
}
