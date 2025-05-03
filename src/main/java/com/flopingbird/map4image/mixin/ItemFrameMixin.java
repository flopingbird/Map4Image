package com.flopingbird.map4image.mixin;

import com.flopingbird.map4image.component.ModDataComponentType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Interaction;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static com.flopingbird.map4image.GenerateMapArt.createPreviewMap;

@Mixin(ItemFrame.class)
public abstract class ItemFrameMixin {

    @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
    private void itemFrameFiller(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack item = player.getItemInHand(hand);
        if (item.getItem() != Items.FILLED_MAP || item.get(ModDataComponentType.WIDTH) == null) return;
        int width = item.get(ModDataComponentType.WIDTH);
        int height = item.get(ModDataComponentType.HEIGHT);
        int[][] mapIds = new int[height][width];
        int currentMapID = item.get(DataComponents.MAP_ID).id() - height*width;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                mapIds[y][x] = currentMapID;
                currentMapID++;
            }
        }

        //surely theres a less silly way to getPos without casting shennigans right
        ItemFrame interactedItemFrame = (ItemFrame) (Object) this;
        BlockPos topLeftItemFramePosition = interactedItemFrame.getPos();
        Vec3i direction = interactedItemFrame.getDirection().getUnitVec3i();
        //TODO add ability to place on floor
        Vec3i widthDirection = new Vec3i(direction.getZ(), 0, -direction.getX());
        Vec3i heightDirection = new Vec3i(0, -1, 0);
        ItemFrame[][] itemFrames = new ItemFrame[height][width];




        boolean failed = false;
        blockCheck:
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                BlockPos currentItemFrameBlockPos = topLeftItemFramePosition.offset(widthDirection.multiply(x).offset(heightDirection.multiply(y)));
                List<ItemFrame> itemFramesAtBlock = interactedItemFrame.level().getEntitiesOfClass(ItemFrame.class, new AABB(currentItemFrameBlockPos));
                if (itemFramesAtBlock.isEmpty()) {failed = true; break blockCheck;}
                for (ItemFrame itemFrame : itemFramesAtBlock)
                    if (itemFrame.getDirection().getUnitVec3i().equals(direction) && itemFrame.getItem().is(Items.AIR)) { itemFrames[y][x] = itemFrame; break; }
                if (itemFrames[y][x] == null) {failed = true; break blockCheck;}
            }
        }

        if (failed) {
            player.displayClientMessage(Component.literal("The map you are currently trying to place does not have valid placement. (" + width + "x" + height + ")"), true);
            cir.setReturnValue(InteractionResult.FAIL);
            return;
        }


        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                ItemStack mapItem = new ItemStack(Items.FILLED_MAP);
                mapItem.set(DataComponents.MAP_ID, new MapId(mapIds[y][x]));
                mapItem.set(ModDataComponentType.PLACED_PARENT, topLeftItemFramePosition);
                if (x == 0 && y == 0) {
                    mapItem.set(ModDataComponentType.WIDTH, width);
                    mapItem.set(ModDataComponentType.HEIGHT, height);
                }
                itemFrames[y][x].setItem(mapItem, false);
            }
        cir.setReturnValue(InteractionResult.SUCCESS);
        item.consume(1, player);
    }

    //ok... this one......... i failed my calc test so i mightve been a little out of it
    @Inject(method = "dropItem(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Entity;Z)V", at = @At("HEAD"))
    private void itemFrameFilledRemover(ServerLevel level, Entity entity, boolean dropItem, CallbackInfo ci) {
        ItemFrame interactedItemFrame = (ItemFrame) (Object) this;
        if (interactedItemFrame.getItem().get(ModDataComponentType.PLACED_PARENT) == null) return;
        BlockPos blockPosOfParent = interactedItemFrame.getItem().get(ModDataComponentType.PLACED_PARENT);
        Vec3i direction = interactedItemFrame.getDirection().getUnitVec3i();
        Vec3i widthDirection = new Vec3i(direction.getZ(), 0, -direction.getX());
        Vec3i heightDirection = new Vec3i(0, -1, 0);

        List<ItemFrame> itemFramesAtParentBlocks = interactedItemFrame.level().getEntitiesOfClass(ItemFrame.class, new AABB(blockPosOfParent));
        if (itemFramesAtParentBlocks.isEmpty()) return;
        ItemFrame parentItemFrame = null;
        for (ItemFrame itemFrame : itemFramesAtParentBlocks)
                if (itemFrame.getDirection().getUnitVec3i().equals(direction)) { parentItemFrame = itemFrame; break; }
        if (parentItemFrame == null || parentItemFrame.getItem().get(ModDataComponentType.WIDTH) == null) return;
        int width = parentItemFrame.getItem().get(ModDataComponentType.WIDTH), height = parentItemFrame.getItem().get(ModDataComponentType.HEIGHT);
        ItemFrame[][] itemFrames = new ItemFrame[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                BlockPos currentItemFrameBlockPos = blockPosOfParent.offset(widthDirection.multiply(x).offset(heightDirection.multiply(y)));
                List<ItemFrame> itemFramesAtBlock = interactedItemFrame.level().getEntitiesOfClass(ItemFrame.class, new AABB(currentItemFrameBlockPos));
                if (itemFramesAtBlock.isEmpty()) {return;}
                for (ItemFrame itemFrame : itemFramesAtBlock)
                    if (itemFrame.getDirection().getUnitVec3i().equals(direction) && itemFrame.getItem().get(ModDataComponentType.PLACED_PARENT) != null) { itemFrames[y][x] = itemFrame; break; }
                if (itemFrames[y][x] == null) {return;}
            }
        }

        ItemStack itemDropped = createPreviewMap(width*height + parentItemFrame.getItem().get(DataComponents.MAP_ID).id(), width, height);

        for (ItemFrame[] itemFrameRow : itemFrames)
            for (ItemFrame itemFrame : itemFrameRow)
                itemFrame.setItem(ItemStack.EMPTY);

        interactedItemFrame.spawnAtLocation(level, itemDropped);
    }
}
