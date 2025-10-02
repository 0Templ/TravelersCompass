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

public record SpawnerLocationObject(BlockPos blockPos, int slotIndex, boolean priority, String descriptionId, String contentId, ResourceLocation blockId) implements ILocationObject, WithContent{

    public static ResourceLocation TYPE = new ResourceLocation(TCCommon.MODID, "spawner_location_codec");

    public static final LocationCodec<SpawnerLocationObject> CODEC = new LocationCodec<>() {

        @Override
        public void write(FriendlyByteBuf buf, SpawnerLocationObject obj) {
            this.writePosSafe(buf, obj.blockPos);
            buf.writeInt(obj.slotIndex);
            buf.writeBoolean(obj.priority);
            buf.writeUtf(obj.descriptionId);
            buf.writeUtf(obj.contentId);
            buf.writeResourceLocation(obj.blockId);
        }

        @Override
        public SpawnerLocationObject read(FriendlyByteBuf buf) {
            BlockPos blockPos = this.readPosSafe(buf);
            int slotIndex = buf.readInt();
            boolean priority = buf.readBoolean();
            String descriptionId = buf.readUtf();
            String containerId = buf.readUtf();
            ResourceLocation blockId = buf.readResourceLocation();
            return new SpawnerLocationObject(blockPos, slotIndex, priority, descriptionId, containerId, blockId);
        }
    };

    @Override
    public ResourceLocation type() {
        return TYPE;
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
    public ILocationObject copy(
            BlockPos blockPos,
            int slotIndex,
            boolean priority,
            String descriptionId
    ){
        return new SpawnerLocationObject(blockPos, slotIndex, priority, descriptionId, contentId, blockId);
    }

    @Override
    public Optional<ILocationObject> update(ServerLevel level) {
        return Optional.of(this);
    }


    @Override
    public String toString() {
        return "Spawner Location: ["+descriptionId + "--" + contentId + blockPos + (priority ? " [priority]" : "");
    }

}
