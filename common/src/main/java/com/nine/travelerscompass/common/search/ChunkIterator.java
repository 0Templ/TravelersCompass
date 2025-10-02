package com.nine.travelerscompass.common.search;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;

public class ChunkIterator {

    private final int radius;

    private int layer = 1;
    private int dx = 1, dz = 0;
    private int dirSteps = 0;
    private int steps = 1;
    private int turns = 0;

    private int x, z;

    private int scannedChunks = 0;
    private final int totalChunks;

    private boolean first = true;

    public ChunkIterator(BlockPos center, int radius) {
        this.radius = radius;
        this.x = center.getX() >> 4;
        this.z = center.getZ() >> 4;
        totalChunks = (int) Math.pow((1 + (radius * 2)), 2);
    }


    public ChunkPos next() {
        if (first) {
            first = false;
            return new ChunkPos(x, z);
        }

        x += dx;
        z += dz;
        dirSteps++;
        scannedChunks++;

        if (dirSteps == steps) {
            dirSteps = 0;
            turns++;

            int tmp = dx;
            dx = -dz;
            dz = tmp;

            if (turns % 2 == 0) {
                steps++;
                layer++;
            }
        }
        ChunkPos pos = new ChunkPos(x, z);
        return pos;
    }


    public boolean hasNext() {
        return scannedChunks < totalChunks;
    }

}
