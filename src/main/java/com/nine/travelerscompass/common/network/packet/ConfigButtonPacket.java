package com.nine.travelerscompass.common.network.packet;

import com.nine.travelerscompass.common.item.CompassMode;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class ConfigButtonPacket {
    private final int ID;


    public ConfigButtonPacket(int value) {
        this.ID = value;
    }

    public ConfigButtonPacket(FriendlyByteBuf buf) {
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
                    travelersCompassItem.writeCompassData(stack, CompassMode.SEARCHING_VILLAGERS);
                    break;
                case (2):
                    travelersCompassItem.writeCompassData(stack, CompassMode.SEARCHING_DROPPED_ITEMS);
                    break;
                case (3):
                    travelersCompassItem.writeCompassData(stack, CompassMode.SEARCHING_FLUIDS);
                    break;
                case (4):
                    travelersCompassItem.writeCompassData(stack, CompassMode.SEARCHING_SPAWNERS);
                    break;
                case (5):
                    travelersCompassItem.writeCompassData(stack, CompassMode.SEARCHING_MOBS_INV);
                    break;
                case (6):
                    travelersCompassItem.writeCompassData(stack, CompassMode.SEARCHING_MOB_DROP);
                    break;
                case (7):
                    travelersCompassItem.writeCompassData(stack, CompassMode.PAUSED);
                    break;
            }
        }
    }
}

