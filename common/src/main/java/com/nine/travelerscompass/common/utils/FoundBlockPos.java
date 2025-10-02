package com.nine.travelerscompass.common.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public class FoundBlockPos {

    public static final String X_TAG = "found_position_x";
    public static final String Y_TAG = "found_position_y";
    public static final String Z_TAG = "found_position_z";
    public static final String VALID_TAG = "position_is_valid";

    private final int x;
    private final int y;
    private final int z;
    private final boolean valid;

    public FoundBlockPos(int x, int y, int z, boolean valid) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.valid = valid;
    }

    public FoundBlockPos() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.valid = false;
    }

    public FoundBlockPos(BlockPos blockPos, boolean valid) {
        this.x = blockPos.getX();
        this.y = blockPos.getY();
        this.z = blockPos.getZ();
        this.valid = valid;
    }

    public FoundBlockPos(boolean valid) {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.valid = valid;
    }

    public BlockPos blockPos(){
        return valid ? new BlockPos(x, y, z) : null;
    }

    public boolean isValid(){
        return valid;
    }

    public void writeData(CompoundTag tag){
        tag.putInt(X_TAG, x);
        tag.putInt(Y_TAG, y);
        tag.putInt(Z_TAG, z);
        tag.putBoolean(VALID_TAG, valid);
    }

    public static FoundBlockPos readData(CompoundTag tag) {

        return new FoundBlockPos(tag.getInt(X_TAG), tag.getInt(Y_TAG), tag.getInt(Z_TAG), tag.getBoolean(VALID_TAG));
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        FoundBlockPos pos = (FoundBlockPos) other;
        return x == pos.x && y == pos.y && z == pos.z && valid == pos.valid;
    }


}
