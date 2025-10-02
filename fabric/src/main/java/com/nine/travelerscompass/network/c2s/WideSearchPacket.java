package com.nine.travelerscompass.network.c2s;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import com.nine.travelerscompass.common.search.SearchManager;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;
import java.util.UUID;

public class WideSearchPacket extends FriendlyByteBuf {

    public static final ResourceLocation ID = new ResourceLocation(TCCommon.MODID, "wide_search_packet");

    public WideSearchPacket(UUID uuid) {
        super(Unpooled.buffer());
        writeUUID(uuid);
    }

    public static void onMessage(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl serverGamePacketListener, FriendlyByteBuf buf, PacketSender packetSender) {
        UUID uuid = buf.readUUID();
        server.execute(() -> {
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() instanceof TravelersCompassItem &&
                    Objects.equals(CompassProperties.COMPASS_UUID.get(stack), uuid)){
                SearchManager.startWideSearch(stack, player, CompassContainer.container(stack));
            }
        });
    }

}
