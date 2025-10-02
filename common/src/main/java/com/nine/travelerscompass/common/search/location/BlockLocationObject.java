package com.nine.travelerscompass.common.search.location;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.common.search.location.codec.LocationCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public record BlockLocationObject(BlockPos blockPos, int slotIndex, boolean priority, String descriptionId, ResourceLocation blockId) implements ILocationObject {
    
    public static ResourceLocation TYPE = new ResourceLocation(TCCommon.MODID, "block_location_codec");

    public static final LocationCodec<BlockLocationObject> CODEC = new LocationCodec<>() {

        @Override
        public void write(FriendlyByteBuf buf, BlockLocationObject obj) {
            this.writePosSafe(buf, obj.blockPos);
            buf.writeInt(obj.slotIndex);
            buf.writeBoolean(obj.priority);
            buf.writeUtf(obj.descriptionId);
            buf.writeResourceLocation(obj.blockId);
        }

        @Override
        public BlockLocationObject read(FriendlyByteBuf buf) {
            BlockPos blockPos = this.readPosSafe(buf);
            int slotIndex = buf.readInt();
            boolean priority = buf.readBoolean();
            String descriptionId = buf.readUtf();
            ResourceLocation blockId = buf.readResourceLocation();
            return new BlockLocationObject(blockPos, slotIndex, priority, descriptionId, blockId);
        }
    };

    @Override
    public ResourceLocation type() {
        return TYPE;
    }

    @Override
    public ILocationObject copy(
            BlockPos blockPos,
            int slotIndex,
            boolean priority,
            String descriptionId
    ){
        return new BlockLocationObject(blockPos, slotIndex, priority, descriptionId, blockId);
    }
    
    @Override
    public boolean isValid(ServerLevel level) {
        boolean loaded = level.isLoaded(blockPos);
        if (!loaded){
            return true;
        }
        Block block = level.getBlockState(blockPos).getBlock();
        return BuiltInRegistries.BLOCK.get(blockId).equals(block);
    }

    @Override
    public Optional<ILocationObject> update(ServerLevel level) {
        return Optional.of(this);
    }

    @Override
    public String toString() {
        return "Block Location: ["+descriptionId + "], " + blockPos + (priority ? " [priority]" : "");
    }

}
