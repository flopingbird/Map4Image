package com.flopingbird.map4image;

import com.flopingbird.map4image.component.ModDataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.apache.logging.log4j.core.selector.NamedContextSelector;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static com.flopingbird.map4image.MapGenerationUtils.*;

public class GenerateMapArt {
    //returns 2d array of mapIds in [y][x] format, first element of first array is the top left.
    public static int[][] generateMapArt(BufferedImage image, ServerLevel level, DitherType ditherType, int width, int height) {
        //TODO keep map IDs in different serverLevel or try to get rid of having to pass it
        ServerLevel serverLevel = level;
        Image rescaledImage = image.getScaledInstance(width, height, Image.SCALE_REPLICATE);
        image = new BufferedImage(width, height, image.getType());
        Graphics2D graphics = image.createGraphics();
        graphics.drawImage(rescaledImage, 0, 0, null);
        graphics.dispose();

        byte[][] colorMap = imageToMinecraftMapColors(image, ditherType);
        int colorMapHeight = colorMap.length, colorMapWidth = colorMap[0].length;
        //compensate for missing space vertically by filling rows with transparent tiles
        if (colorMapHeight % 128 != 0) {
            //find # to round up to nearest map size
            int heightDiff = 128 - (colorMapHeight % 128);
            byte[][] newColorMap = new byte[colorMapHeight + heightDiff][colorMapWidth];
            byte[] newRow = new byte[colorMapWidth];
            //change all elements to byte zero, byte zero on the minecraft map color chart is transparent
            for (int i = 0; i < newRow.length; i++)
                newRow[i] = 0;
            int oldMapScanIndex = 0;
            for (int y = 0; y < newColorMap.length; y++) {
                if (y < heightDiff/2 || y > heightDiff/2-1+colorMapHeight)
                    newColorMap[y] = newRow;
                else {
                    newColorMap[y] = colorMap[oldMapScanIndex];
                    oldMapScanIndex++;
                }

            }
            colorMap = newColorMap;
            colorMapHeight = colorMap.length;
        }
        //compensate for missing space horizontally by filling columns with transparent tiles
        if (colorMapWidth % 128 != 0) {
            //find # to round up to nearest map size
            int heightDiff = 128 - (colorMapWidth % 128);
            byte[][] newColorMap = new byte[colorMapHeight][colorMapWidth + heightDiff];
            for (int y = 0; y < colorMapHeight; y++) {
                int oldMapScanIndex = 0;
                for (int x = 0; x < newColorMap[0].length; x++) {
                    if (x < heightDiff/2 || x > heightDiff/2-1+colorMapWidth)
                        newColorMap[y][x] = 0;
                    else {
                        newColorMap[y][x] = colorMap[y][oldMapScanIndex];
                        oldMapScanIndex++;
                    }
                }
            }
            colorMap = newColorMap;
            colorMapWidth = colorMap[0].length;
        }


        int[][] maps = new int[colorMapHeight/128][colorMapWidth/128];
        for (int y = 0; y < colorMapHeight; y+=128)
            for (int x = 0; x < colorMapWidth; x+=128){
                byte[][] subColorMap = copySubrange(colorMap, x, y, 128, 128);
                MapItemSavedData mapData = MapItemSavedData.createFresh(0.0, 0.0, (byte)1, false, false, serverLevel.dimension());
                for (int j = 0; j < 128; j++) {
                    for (int i = 0; i < 128; i++) {
                        mapData.setColor(i, j, subColorMap[j][i]);
                    }
                }
                mapData = mapData.locked();
                MapId newMapID = level.getFreeMapId();
                level.getLevel().setMapData(newMapID, mapData);
                maps[y/128][x/128] = newMapID.id();
            }
        return maps;
    }

    public static ItemStack createPreviewMap(int previewMapID, int width, int height) {
        ItemStack previewMapItem = new ItemStack(Items.FILLED_MAP);

        previewMapItem.set(DataComponents.MAP_ID, new MapId(previewMapID));
        previewMapItem.set(ModDataComponentType.WIDTH, height);
        previewMapItem.set(ModDataComponentType.HEIGHT, width);
        previewMapItem.set(DataComponents.ITEM_NAME, Component.literal("Map Art"));
        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.literal("Place map at top left of item frame grid of width " + width + " and height " + height));
        previewMapItem.set(DataComponents.LORE, new ItemLore(lore));
        previewMapItem.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);

        return previewMapItem;
    }

    //taken directly from https://stackoverflow.com/questions/27343663/how-to-obtain-a-part-of-a-2d-array, removed uncessary checks
    private static byte[][] copySubrange(byte[][] source, int x, int y, int width, int height) {
        byte[][] dest = new byte[height][width];
        for (int destY = 0; destY < height; destY++) {
            byte[] srcRow = source[(y + destY)];
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
