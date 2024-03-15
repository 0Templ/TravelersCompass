package com.nine.travelerscompass.common.network.packet;

import com.nine.travelerscompass.common.container.menu.CompassMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class GhostTargetPacket {

    private int slotIndex;
    private ItemStack stack;
    public GhostTargetPacket(int slot, ItemStack stack) {
        this.slotIndex = slot;
        this.stack = stack;
    }

    public GhostTargetPacket(FriendlyByteBuf buf) {
        slotIndex = buf.readVarInt();
        stack = buf.readItem();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(slotIndex);
        buf.writeItemStack(stack, false);
    }

    public void onMessage(Supplier<NetworkEvent.Context> ctx) {
        Player player = ctx.get().getSender();
        if (player == null){
            return;
        }
        if (player.containerMenu instanceof CompassMenu menu)
            menu.slots.get(slotIndex).set(stack);
    }
}
