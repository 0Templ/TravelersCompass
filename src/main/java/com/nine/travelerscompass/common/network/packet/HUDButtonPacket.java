package com.nine.travelerscompass.common.network.packet;

import com.nine.travelerscompass.common.item.CompassData;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class HUDButtonPacket {

    private final int ID;
    private final boolean shiftPressed;
    private final boolean controlPressed;

    public HUDButtonPacket(int value, boolean controlPressed, boolean shiftPressed) {
        this.ID = value;
        this.controlPressed = controlPressed;
        this.shiftPressed = shiftPressed;
    }

    public HUDButtonPacket(FriendlyByteBuf buf) {
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
        ServerPlayer player = ctx.get().getSender();
        ItemStack stack = Objects.requireNonNull(player).getMainHandItem();
        ctx.get().enqueueWork(() -> {
            if (stack.getItem() instanceof TravelersCompassItem travelersCompassItem) {
                boolean controlPressed = this.controlPressed;
                boolean shiftPressed = this.shiftPressed;
                int amount = (shiftPressed ? -1 : 1) * (controlPressed ? 5 : 1);
                switch (this.ID) {
                    case (1):
                        if (travelersCompassItem.hudMode(stack)) {
                            travelersCompassItem.writeCompassData(stack, CompassData.HUD_SHOW);
                        } else if (travelersCompassItem.hudModeRequiresHeld(stack)) {
                            travelersCompassItem.writeCompassData(stack, CompassData.HUD_SHOW_HAND);
                            travelersCompassItem.writeCompassData(stack, CompassData.HUD_SHOW);
                        } else {
                            travelersCompassItem.writeCompassData(stack, CompassData.HUD_SHOW_HAND);
                        }
                        break;
                    case (2):
                        if (!(travelersCompassItem.getXHudPos(stack) + amount < -5)) {
                            travelersCompassItem.setHudPos(stack, travelersCompassItem.getXHudPos(stack) + amount, travelersCompassItem.getYHudPos(stack));
                        }
                        break;
                    case (3):
                        if (!(travelersCompassItem.getYHudPos(stack) + amount < -5)) {
                            travelersCompassItem.setHudPos(stack, travelersCompassItem.getXHudPos(stack), travelersCompassItem.getYHudPos(stack) + amount);
                        }
                        break;
                    case (4):
                        travelersCompassItem.setHudWithChatMode(stack, !travelersCompassItem.hudWithChatMode(stack));
                        break;
                    case (5):
                        if (!travelersCompassItem.hudWithChatMode(stack)) {
                            travelersCompassItem.setHudWithChatMode(stack, !travelersCompassItem.hudWithChatMode(stack));
                        }
                        travelersCompassItem.setHudPos(stack, 0, 0);
                        travelersCompassItem.setHudAlign(stack, 0);
                        travelersCompassItem.setHudType(stack, 0);
                        break;
                    case (6):
                        int currentPreset = travelersCompassItem.getHudAlign(stack);
                        int preset = currentPreset == 0 || currentPreset == 1 ? currentPreset + 1 : 0;
                        travelersCompassItem.setHudAlign(stack, preset);
                        break;
                    case (7):
                        int currentType = travelersCompassItem.getHudType(stack);
                        int type = currentType == 0 ? currentType + 1 : 0;
                        travelersCompassItem.setHudType(stack, type);
                        break;

                }

            }
        });
    }
}

