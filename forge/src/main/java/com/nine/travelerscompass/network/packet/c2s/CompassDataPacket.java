package com.nine.travelerscompass.network.packet.c2s;

import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.data.DataStorage;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CompassDataPacket {

    private final ResourceLocation id;
    private final CompoundTag tag;

    public CompassDataPacket(DataStorage<?> data, CompoundTag tag) {
        this.id = data.getId();
        this.tag = tag;
    }

    public CompassDataPacket(FriendlyByteBuf buf) {
        id = buf.readResourceLocation();
        if (CompassProperties.CLIENT_EDITABLE.containsKey(id)) {
            tag = buf.readNbt();
        }
        else {
            tag = null;
        }
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(id);
        if (CompassProperties.CLIENT_EDITABLE.containsKey(id)){
            buf.writeNbt(tag);
        }
    }

    public void onMessage(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        ServerPlayer player = context.getSender();
        if (player == null) {
            return;
        }
        context.enqueueWork(() -> {
            if (!CompassProperties.CLIENT_EDITABLE.containsKey(id)) return;
            ItemStack stack = player.getMainHandItem();
            DataStorage<?> dataStorage = CompassProperties.REGISTRY.get(id);
            if (stack.getItem() instanceof TravelersCompassItem && tag != null){
                Object value = CompassProperties.get(tag, dataStorage);
                @SuppressWarnings("unchecked")
                DataStorage<Object> resDataStorage = (DataStorage<Object>) dataStorage;
                CompassProperties.putWithValidation(stack, resDataStorage, value);
            }
        });
        context.setPacketHandled(true);
    }
}
