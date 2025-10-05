package com.flopingbird.map4image.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;

public class TagUtils {
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

    public static CompoundTag directionToTag(Vec3i dir) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("x", dir.getX());
        tag.putInt("y", dir.getY());
        tag.putInt("z", dir.getZ());
        return tag;
    }

    public static Vec3i tagToDirection(CompoundTag tag) {
        return new Vec3i(tag.getInt("x").get(), tag.getInt("y").get(), tag.getInt("z").get());
    }

}
