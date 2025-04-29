package com.flopingbird.map4image;

import com.flopingbird.map4image.mixin.MapMixin;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import static com.flopingbird.map4image.MapGenerationUtils.*;

public class GenerateMap {
    public static ItemStack generateMap(BufferedImage image, ServerLevel level, DitherType ditherType) {
        //TODO keep map IDs in different serverLevel or try to get rid of having to pass it
        ServerLevel serverLevel = level;
        image = resizeBufferedImage(image, 128, 128);

        byte[][] colorMap = ditherImageToMinecraftMapColors(image, ditherType);

        //TODO fix this process as it burns through 1 map ID and dedicates another to the image, which while not likely a save runs out of 2 billion IDs, it is sloppy
        MapItemSavedData mapData = MapItemSavedData.createFresh(0.0, 0.0, (byte)1, false, false, serverLevel.dimension());
        for (int j = 0; j < 128; j++) {
            for (int i = 0; i < 128; i++) {
                mapData.setColor(i, j, colorMap[i][j]);
            }
        }

        //TODO maybe increase scale to prevent players from putting map into cartographer table
        ItemStack map = MapItem.create(serverLevel, 0, 0, (byte)(1), false, false);
        level.getLevel().setMapData(map.get(DataComponents.MAP_ID), mapData);

        //TODO try to find a way to lock without using mixin (might be possible to just make a throw away ServerLevel, that would fuck with save files though so idk)
        MapMixin.lockMap(map, serverLevel);
        return map;
    }

    public static BufferedImage getBufferedImageFromLink(String link) {
        try {
            return ImageIO.read(new URL(link).openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
