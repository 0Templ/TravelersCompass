package com.nine.travelerscompass.common.network.packet;

import com.nine.travelerscompass.TCConfig;
import com.nine.travelerscompass.common.item.CompassData;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class ConfigButtonPacket {

    private final int ID;
    private final boolean shiftPressed;
    private final boolean controlPressed;

    public ConfigButtonPacket(int value, boolean controlPressed, boolean shiftPressed) {
        this.ID = value;
        this.controlPressed = controlPressed;
        this.shiftPressed = shiftPressed;
    }

    public ConfigButtonPacket(int value) {
        this.ID = value;
        controlPressed = false;
        shiftPressed = false;
    }

    public ConfigButtonPacket(FriendlyByteBuf buf) {
        ID = buf.readVarInt();
        controlPressed = buf.readBoolean();
        shiftPressed = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(ID);
        buf.writeBoolean(controlPressed);
        buf.writeBoolean(shiftPressed);
    }

    public void onMessage(Supplier<NetworkEvent.Context> ctx) {
        Player player = ctx.get().getSender();
        ItemStack stack = Objects.requireNonNull(player).getMainHandItem();
        if (stack.getItem() instanceof TravelersCompassItem travelersCompassItem) {
            boolean controlPressed = this.controlPressed;
            boolean shiftPressed = this.shiftPressed;
            int currentRadius;
            int amount = (shiftPressed ? -1 : 1) * (controlPressed ? 5 : 1);
            switch (this.ID) {
                case (0):
                    travelersCompassItem.writeCompassData(stack, CompassData.SEARCHING_MOBS);
                    break;
                case (1):
                    travelersCompassItem.writeCompassData(stack, CompassData.SEARCHING_BLOCKS);
                    break;
                case (2):
                    travelersCompassItem.writeCompassData(stack, CompassData.SEARCHING_CONTAINERS);
                    break;
                case (3):
                    travelersCompassItem.writeCompassData(stack, CompassData.SEARCHING_FLUIDS);
                    break;
                case (4):
                    travelersCompassItem.writeCompassData(stack, CompassData.SEARCHING_VILLAGERS);
                    break;
                case (5):
                    travelersCompassItem.writeCompassData(stack, CompassData.SEARCHING_SPAWNERS);
                    break;
                case (6):
                    travelersCompassItem.writeCompassData(stack, CompassData.SEARCHING_ENTITIES_INV);
                    break;
                case (7):
                    travelersCompassItem.writeCompassData(stack, CompassData.SEARCHING_DROPPED_ITEMS);
                    break;
                case (8):
                    travelersCompassItem.writeCompassData(stack, CompassData.SEARCHING_ENTITIES_DROP);
                    break;
                case (-1):
                    travelersCompassItem.writeCompassData(stack, CompassData.PAUSED);
                    break;
                case (-3):
                    if (!player.level().isClientSide()) {
                        if (!travelersCompassItem.isPaused(stack)) {
                            travelersCompassItem.writeCompassData(stack, CompassData.PAUSED);
                        }
                        travelersCompassItem.writeCompassData(stack, CompassData.WIDE_SEARCH_SIGNAL);
                    }
                    break;
                case (-5):
                    currentRadius = travelersCompassItem.blockSearchRadius(stack);
                    if (currentRadius + amount <= TCConfig.blockSearchRadius.get() && currentRadius + amount > 0) {
                        travelersCompassItem.setBlockSearchRadius(stack, currentRadius + amount);
                    }
                    break;
                case (-6):
                    currentRadius = travelersCompassItem.entitySearchRadius(stack);
                    if (currentRadius + amount <= TCConfig.entitySearchRadius.get() && currentRadius + amount > 0) {
                        travelersCompassItem.setEntitySearchRadius(stack, currentRadius + amount);
                    }
                    break;
                case (-7):
                    currentRadius = travelersCompassItem.containerSearchRadius(stack);
                    if (currentRadius + amount <= TCConfig.containerSearchRadius.get() && currentRadius + amount > 0) {
                        travelersCompassItem.setContainerSearchRadius(stack, currentRadius + amount);
                    }
                    break;
                case (-8):
                    currentRadius = travelersCompassItem.wideSearchRadius(stack);
                    if (currentRadius + amount <= TCConfig.wideSearchRadius.get() && currentRadius + amount > 0) {
                        travelersCompassItem.setWideSearchRadius(stack, currentRadius + amount);
                    }
                    break;
                case (-9):
                    travelersCompassItem.writeCompassData(stack, CompassData.PRIORITY_MODE);
                    break;
                case (-10):
                    travelersCompassItem.writeCompassData(stack, CompassData.SHOW_LABELS);
                    break;
                case (-11):
                    if (travelersCompassItem.hudMode(stack)) {
                        travelersCompassItem.writeCompassData(stack, CompassData.HUD_SHOW);
                    } else if (travelersCompassItem.hudModeRequiresHeld(stack)) {
                        travelersCompassItem.writeCompassData(stack, CompassData.HUD_SHOW_HAND);
                        travelersCompassItem.writeCompassData(stack, CompassData.HUD_SHOW);
                    } else {
                        travelersCompassItem.writeCompassData(stack, CompassData.HUD_SHOW_HAND);
                    }
                    break;
                case (-12):
                    travelersCompassItem.setContainerSearchRadius(stack, (int) (TCConfig.containerSearchRadius.get() * 0.5F));
                    travelersCompassItem.setEntitySearchRadius(stack, (int) (TCConfig.entitySearchRadius.get() * 0.5F));
                    travelersCompassItem.setBlockSearchRadius(stack, (int) (TCConfig.blockSearchRadius.get() * 0.5F));
                    travelersCompassItem.setWideSearchRadius(stack, (int) (TCConfig.wideSearchRadius.get() * 0.5F));
                    if (travelersCompassItem.showLabels(stack)) {
                        travelersCompassItem.writeCompassData(stack, CompassData.SHOW_LABELS);
                    }
                    if (!travelersCompassItem.priorityMode(stack)) {
                        travelersCompassItem.writeCompassData(stack, CompassData.PRIORITY_MODE);
                    }
                    if (!travelersCompassItem.sound(stack)) {
                        travelersCompassItem.writeCompassData(stack, CompassData.SOUND);
                    }
                    if (travelersCompassItem.hudMode(stack)) {
                        travelersCompassItem.writeCompassData(stack, CompassData.HUD_SHOW);
                    } else if (travelersCompassItem.hudModeRequiresHeld(stack)) {
                        travelersCompassItem.writeCompassData(stack, CompassData.HUD_SHOW_HAND);
                    }
                case (-13):
                    travelersCompassItem.writeCompassData(stack, CompassData.SOUND);
                    break;
                case (-14):
                    travelersCompassItem.writeCompassData(stack, CompassData.LAZY_MODE);
                    break;
                case (401):
                    travelersCompassItem.writeCompassData(stack, CompassData.SEARCHING_VILLAGERS_GOODS);
                    break;
                case (402):
                    travelersCompassItem.writeCompassData(stack, CompassData.SEARCHING_VILLAGERS_COST);
                    break;
                case (601):
                    travelersCompassItem.writeCompassData(stack, CompassData.SEARCHING_PLAYERS_INV);
                    break;
                case (602):
                    travelersCompassItem.writeCompassData(stack, CompassData.SEARCHING_MOBS_INV);
                    break;
                case (603):
                    travelersCompassItem.writeCompassData(stack, CompassData.SEARCHING_MINECARTS_INV);
                    break;
            }
        }
    }
}

