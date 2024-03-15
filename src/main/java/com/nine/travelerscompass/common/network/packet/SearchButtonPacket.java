package com.nine.travelerscompass.common.network.packet;

import com.nine.travelerscompass.common.item.CompassMode;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class SearchButtonPacket {
    private final int ID;

    public SearchButtonPacket(int value) {
        this.ID = value;
    }

    public SearchButtonPacket(FriendlyByteBuf buf) {
        ID = buf.readVarInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(ID);
    }

    public void onMessage(Supplier<NetworkEvent.Context> ctx) {
        Player player = ctx.get().getSender();
        ItemStack stack = Objects.requireNonNull(player).getMainHandItem();
        if (stack.getItem() instanceof TravelersCompassItem travelersCompassItem){
            switch (this.ID) {
                case (1):
                    travelersCompassItem.writeCompassData(stack, CompassMode.SEARCHING_MOBS);
                    break;
                case (2):
                    travelersCompassItem.writeCompassData(stack, CompassMode.SEARCHING_CONTAINERS);
                    break;
                case (3):
                    travelersCompassItem.writeCompassData(stack, CompassMode.SEARCHING_BLOCKS);
                    break;
                case (4):
                    travelersCompassItem.setConfigMode(stack,true);
                    break;
                case (5):
                    travelersCompassItem.setConfigMode(stack,false);
                    break;
            }
        }
    }
}

