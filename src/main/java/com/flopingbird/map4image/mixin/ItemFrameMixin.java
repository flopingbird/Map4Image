package com.flopingbird.map4image.mixin;

import com.flopingbird.map4image.component.ModDataComponentType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemFrame.class)
public abstract class ItemFrameMixin {

    @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
    private void itemFrameFiller(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack item = player.getItemInHand(hand);
        if (item.getItem() == Items.FILLED_MAP && item.get(ModDataComponentType.WIDTH) != null) {
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


            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    BlockPos currentItemFrameBlockPos = topLeftItemFramePosition.offset(widthDirection.multiply(x).offset(heightDirection.multiply(y)));
                    List<ItemFrame> itemFramesAtBlock = interactedItemFrame.level().getEntitiesOfClass(ItemFrame.class, new AABB(currentItemFrameBlockPos));
                    if (itemFramesAtBlock.isEmpty()) {cir.setReturnValue(InteractionResult.SUCCESS); return;}
                    for (ItemFrame itemFrame : itemFramesAtBlock)
                        if (itemFrame.getDirection().getUnitVec3i().equals(direction) && itemFrame.getItem().is(Items.AIR)) { itemFrames[y][x] = itemFrame; break; }
                    if (itemFrames[y][x] == null) {cir.setReturnValue(InteractionResult.SUCCESS); return;}
                }
            }


            for (int y = 0; y < height; y++)
                for (int x = 0; x < width; x++) {
                    ItemStack mapItem = new ItemStack(Items.FILLED_MAP);
                    mapItem.set(DataComponents.MAP_ID, new MapId(mapIds[y][x]));
                    itemFrames[y][x].setItem(mapItem, false);
                }
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}
