package com.nine.travelerscompass.network.packet.c2s;

import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import com.nine.travelerscompass.common.search.SearchManager;
import com.nine.travelerscompass.common.utils.SearchState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

public class PausePacket {

    private final UUID uuid;

    public PausePacket(UUID uuid) {
        this.uuid = uuid;
    }

    public PausePacket(FriendlyByteBuf buf) {
        uuid = buf.readUUID();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(uuid);
    }

    public void onMessage(Supplier<NetworkEvent.Context> contextSupplier) {
        Player player = contextSupplier.get().getSender();
        if (player == null) {
            return;
        }
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() instanceof TravelersCompassItem
                    && Objects.equals(CompassProperties.COMPASS_UUID.get(stack), uuid)
                    && player instanceof ServerPlayer serverPlayer
            ){
                SearchManager.stopScan(uuid);
                SearchManager.removeWatcher(uuid, serverPlayer);
                CompassProperties.SEARCH_STATE.put(stack, SearchState.IDLE);
            }
        });
        context.setPacketHandled(true);
    }
}
