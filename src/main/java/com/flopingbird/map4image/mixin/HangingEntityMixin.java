package com.flopingbird.map4image.mixin;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.decoration.HangingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(HangingEntity.class)
public abstract class HangingEntityMixin extends BlockAttachedEntityMixin {
    @Shadow
    public abstract Direction getDirection();
}
