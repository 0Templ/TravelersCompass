package com.nine.travelerscompass.network.packet.c2s;

import com.nine.travelerscompass.common.container.menu.CompassMenu;
import com.nine.travelerscompass.compat.NEI;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class GhostTargetPacket {

    private final int slotIndex;
    private final ItemStack stack;
    private final NEI nei;

    public GhostTargetPacket(int slotIndex, ItemStack stack, NEI nei) {
        this.slotIndex = slotIndex;
        this.stack = stack;
        this.nei = nei;
    }

    public GhostTargetPacket(FriendlyByteBuf buf) {
        slotIndex = buf.readVarInt();
        stack = buf.readItem();
        nei = buf.readEnum(NEI.class);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(slotIndex);
        buf.writeItem(stack);
        buf.writeEnum(nei);
    }

    public void onMessage(Supplier<NetworkEvent.Context> contextSupplier) {
        Player player = contextSupplier.get().getSender();
        if (player == null) {
            return;
        }
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (player.containerMenu instanceof CompassMenu menu){
                if (nei.allowed()) {
                    menu.slots.get(slotIndex).set(stack);
                }
            }
        });
        context.setPacketHandled(true);
    }

}
