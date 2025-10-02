package com.nine.travelerscompass.network.c2s;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.common.container.menu.CompassMenu;
import com.nine.travelerscompass.compat.NEI;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.ItemStack;

public class GhostTargetPacket extends FriendlyByteBuf {

    public static final ResourceLocation ID = new ResourceLocation(TCCommon.MODID, "ghost_target_packet");

    public GhostTargetPacket(int slotIndex, ItemStack stack, NEI nei) {
        super(Unpooled.buffer());
        writeVarInt(slotIndex);
        writeItem(stack);
        writeEnum(nei);
    }

    public static void onMessage(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl serverGamePacketListener, FriendlyByteBuf buf, PacketSender packetSender) {
        int slotIndex = buf.readVarInt();
        ItemStack stack = buf.readItem();
        NEI nei = buf.readEnum(NEI.class);
        server.execute(() -> {
            if (player.containerMenu instanceof CompassMenu menu){
                if (nei.allowed()) {
                    menu.slots.get(slotIndex).set(stack);
                }
            }
        });
    }


}
