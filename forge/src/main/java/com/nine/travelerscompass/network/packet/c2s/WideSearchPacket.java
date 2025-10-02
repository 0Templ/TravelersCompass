package com.nine.travelerscompass.network.packet.c2s;

import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import com.nine.travelerscompass.common.search.SearchManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

public class WideSearchPacket {

    private final UUID uuid;

    public WideSearchPacket(UUID uuid) {
        this.uuid = uuid;
    }

    public WideSearchPacket(FriendlyByteBuf buf) {
        uuid = buf.readUUID();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(uuid);
    }

    public void onMessage(Supplier<NetworkEvent.Context> contextSupplier) {
        ServerPlayer player = contextSupplier.get().getSender();
        if (player == null) {
            return;
        }
        ItemStack stack = player.getMainHandItem();
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (stack.getItem() instanceof TravelersCompassItem
                    && Objects.equals(CompassProperties.COMPASS_UUID.get(stack), uuid)) {
                SearchManager.startWideSearch(stack, player, CompassContainer.container(stack));
            }
        });
        context.setPacketHandled(true);
    }
}
