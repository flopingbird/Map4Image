package com.flopingbird.map4image.mixin;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapId;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MapItem.class)
public interface MapMixin {
    @Invoker("createNewSavedData")
    static MapId invokeCreateNewSavedData(ServerLevel level, int x, int z, int scale, boolean trackingPosition, boolean unlimitedTracking, ResourceKey<Level> dimension) {
        throw new AssertionError();
    }
    @Invoker("lockMap")
    static void lockMap(ItemStack stack, ServerLevel level) {
        throw new AssertionError();
    }
}