package com.flopingbird.map4image.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.decoration.BlockAttachedEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockAttachedEntity.class)
public abstract class BlockAttachedEntityMixin extends EntityMixin {
    @Shadow
    public abstract BlockPos getPos();
}
