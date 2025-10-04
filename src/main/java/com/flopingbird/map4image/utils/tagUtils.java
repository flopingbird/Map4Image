package com.flopingbird.map4image.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public class tagUtils {
    public static CompoundTag blockPosToTag(BlockPos pos) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("x", pos.getX());
        tag.putInt("y", pos.getY());
        tag.putInt("z", pos.getZ());
        return tag;
    }

    public static BlockPos tagToBlockPos(CompoundTag tag) {
        return new BlockPos(tag.getInt("x").get(), tag.getInt("y").get(), tag.getInt("z").get());
    }

}
