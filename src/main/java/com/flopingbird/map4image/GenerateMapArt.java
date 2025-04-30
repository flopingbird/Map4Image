package com.flopingbird.map4image;

import com.flopingbird.map4image.mixin.MapMixin;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import static com.flopingbird.map4image.MapGenerationUtils.*;

public class GenerateMapArt {
    public static ItemStack[] generateMapArt(BufferedImage image, ServerLevel level, DitherType ditherType, int width, int height) {
        //TODO keep map IDs in different serverLevel or try to get rid of having to pass it
        ServerLevel serverLevel = level;
        Image rescaledImage = image.getScaledInstance(width, height, Image.SCALE_REPLICATE);
        image = new BufferedImage(width, height, image.getType());
        Graphics2D graphics = image.createGraphics();
        graphics.drawImage(rescaledImage, 0, 0, null);
        graphics.dispose();

        byte[][] colorMap = imageToMinecraftMapColors(image, ditherType);
        //TODO processing to fill non 128 divisible map size with clear tiles or something like that
        ItemStack[] maps = new ItemStack[(colorMap[0].length / 128) * (colorMap.length / 128)];
        int mapsIndex = 0;
        for (int y = 0; y < colorMap.length; y+=128)
            for (int x = 0; x < colorMap[0].length; x+=128){
                byte[][] subColorMap = copySubrange(colorMap, x, y, 128, 128);
                //TODO fix this process as it burns through 1 map ID and dedicates another to the image, which while not likely a save runs out of 2 billion IDs, it is sloppy
                MapItemSavedData mapData = MapItemSavedData.createFresh(0.0, 0.0, (byte)1, false, false, serverLevel.dimension());
                for (int j = 0; j < 128; j++) {
                    for (int i = 0; i < 128; i++) {
                        mapData.setColor(i, j, subColorMap[j][i]);
                    }
                }

                //TODO maybe increase scale to prevent players from putting map into cartographer table
                ItemStack map = MapItem.create(serverLevel, 0, 0, (byte)(1), false, false);
                level.getLevel().setMapData(map.get(DataComponents.MAP_ID), mapData);

                //TODO try to find a way to lock without using mixin (might be possible to just make a throw away ServerLevel, that would fuck with save files though so idk)
                MapMixin.lockMap(map, serverLevel);
                maps[mapsIndex] = map;
                mapsIndex++;
            }
        return maps;
    }

    //taken directly from https://stackoverflow.com/questions/27343663/how-to-obtain-a-part-of-a-2d-array
    private static byte[][] copySubrange(byte[][] source, int x, int y, int width, int height) {
        if (source == null) {
            return null;
        }
        if (source.length == 0) {
            return new byte[0][0];
        }
        if (height < 0) {
            throw new IllegalArgumentException("height must be positive");
        }
        if (width < 0) {
            throw new IllegalArgumentException("width must be positive");
        }
        if ((y + height) > source.length) {
            throw new IllegalArgumentException("subrange too high");
        }
        byte[][] dest = new byte[height][width];
        for (int destY = 0; destY < height; destY++) {
            byte[] srcRow = source[(y + destY)];
            if ((x + width) > srcRow.length) {
                throw new IllegalArgumentException("subrange too wide");
            }
            System.arraycopy(srcRow, x, dest[destY], 0, width);
        }
        return dest;
    }

    public static BufferedImage getBufferedImageFromLink(String link) {
        try {
            return ImageIO.read(new URL(link).openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
