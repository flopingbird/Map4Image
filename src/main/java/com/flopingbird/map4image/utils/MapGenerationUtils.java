package com.flopingbird.map4image.utils;

import java.awt.image.BufferedImage;

public class MapGenerationUtils {
    //see https://minecraft.wiki/w/Map_item_format "Full color tables" for reference, ID needs to be shifted by 4 to compensate for transparent colors
    public static final int[][] mapColors = {
            {89, 125, 39},
            {109, 153, 48},
            {127, 178, 56},
            {67, 94, 29},
            {174, 164, 115},
            {213, 201, 140},
            {247, 233, 163},
            {130, 123, 86},
            {140, 140, 140},
            {171, 171, 171},
            {199, 199, 199},
            {105, 105, 105},
            {180, 0, 0},
            {220, 0, 0},
            {255, 0, 0},
            {135, 0, 0},
            {112, 112, 180},
            {138, 138, 220},
            {160, 160, 255},
            {84, 84, 135},
            {117, 117, 117},
            {144, 144, 144},
            {167, 167, 167},
            {88, 88, 88},
            {0, 87, 0},
            {0, 106, 0},
            {0, 124, 0},
            {0, 65, 0},
            {180, 180, 180},
            {220, 220, 220},
            {255, 255, 255},
            {135, 135, 135},
            {115, 118, 129},
            {141, 144, 158},
            {164, 168, 184},
            {86, 88, 97},
            {106, 76, 54},
            {130, 94, 66},
            {151, 109, 77},
            {79, 57, 40},
            {79, 79, 79},
            {96, 96, 96},
            {112, 112, 112},
            {59, 59, 59},
            {45, 45, 180},
            {55, 55, 220},
            {64, 64, 255},
            {33, 33, 135},
            {100, 84, 50},
            {123, 102, 62},
            {143, 119, 72},
            {75, 63, 38},
            {180, 177, 172},
            {220, 217, 211},
            {255, 252, 245},
            {135, 133, 129},
            {152, 89, 36},
            {186, 109, 44},
            {216, 127, 51},
            {114, 67, 27},
            {125, 53, 152},
            {153, 65, 186},
            {178, 76, 216},
            {94, 40, 114},
            {72, 108, 152},
            {88, 132, 186},
            {102, 153, 216},
            {54, 81, 114},
            {161, 161, 36},
            {197, 197, 44},
            {229, 229, 51},
            {121, 121, 27},
            {89, 144, 17},
            {109, 176, 21},
            {127, 204, 25},
            {67, 108, 13},
            {170, 89, 116},
            {208, 109, 142},
            {242, 127, 165},
            {128, 67, 87},
            {53, 53, 53},
            {65, 65, 65},
            {76, 76, 76},
            {40, 40, 40},
            {108, 108, 108},
            {132, 132, 132},
            {153, 153, 153},
            {81, 81, 81},
            {53, 89, 108},
            {65, 109, 132},
            {76, 127, 153},
            {40, 67, 81},
            {89, 44, 125},
            {109, 54, 153},
            {127, 63, 178},
            {67, 33, 94},
            {36, 53, 125},
            {44, 65, 153},
            {51, 76, 178},
            {27, 40, 94},
            {72, 53, 36},
            {88, 65, 44},
            {102, 76, 51},
            {54, 40, 27},
            {72, 89, 36},
            {88, 109, 44},
            {102, 127, 51},
            {54, 67, 27},
            {108, 36, 36},
            {132, 44, 44},
            {153, 51, 51},
            {81, 27, 27},
            {17, 17, 17},
            {21, 21, 21},
            {25, 25, 25},
            {13, 13, 13},
            {176, 168, 54},
            {215, 205, 66},
            {250, 238, 77},
            {132, 126, 40},
            {64, 154, 150},
            {79, 188, 183},
            {92, 219, 213},
            {48, 115, 112},
            {52, 90, 180},
            {63, 110, 220},
            {74, 128, 255},
            {39, 67, 135},
            {0, 153, 40},
            {0, 187, 50},
            {0, 217, 58},
            {0, 114, 30},
            {91, 60, 34},
            {111, 74, 42},
            {129, 86, 49},
            {68, 45, 25},
            {79, 1, 0},
            {96, 1, 0},
            {112, 2, 0},
            {59, 1, 0},
            {147, 124, 113},
            {180, 152, 138},
            {209, 177, 161},
            {110, 93, 85},
            {112, 57, 25},
            {137, 70, 31},
            {159, 82, 36},
            {84, 43, 19},
            {105, 61, 76},
            {128, 75, 93},
            {149, 87, 108},
            {78, 46, 57},
            {79, 76, 97},
            {96, 93, 119},
            {112, 108, 138},
            {59, 57, 73},
            {131, 93, 25},
            {160, 114, 31},
            {186, 133, 36},
            {98, 70, 19},
            {72, 82, 37},
            {88, 100, 45},
            {103, 117, 53},
            {54, 61, 28},
            {112, 54, 55},
            {138, 66, 67},
            {160, 77, 78},
            {84, 40, 41},
            {40, 28, 24},
            {49, 35, 30},
            {57, 41, 35},
            {30, 21, 18},
            {95, 75, 69},
            {116, 92, 84},
            {135, 107, 98},
            {71, 56, 51},
            {61, 64, 64},
            {75, 79, 79},
            {87, 92, 92},
            {46, 48, 48},
            {86, 51, 62},
            {105, 62, 75},
            {122, 73, 88},
            {64, 38, 46},
            {53, 43, 64},
            {65, 53, 79},
            {76, 62, 92},
            {40, 32, 48},
            {53, 35, 24},
            {65, 43, 30},
            {76, 50, 35},
            {40, 26, 18},
            {53, 57, 29},
            {65, 70, 36},
            {76, 82, 42},
            {40, 43, 22},
            {100, 42, 32},
            {122, 51, 39},
            {142, 60, 46},
            {75, 31, 24},
            {26, 15, 11},
            {31, 18, 13},
            {37, 22, 16},
            {19, 11, 8}
    };

    //returns int array first value being the coresponding map color byte and the second value being the coresponding rgb int value
    public static int[] findClosetMapColorByte (int[] rgb) {
        //TODO add transparency
        int minDistValue = Integer.MAX_VALUE;
        int minDistIndex = -1;
        int[] rgbValue = new int[0];
        for (int i = 0; i < mapColors.length; i++) {
            int dist = distanceBetweenRgbValues(rgb, mapColors[i]);
            if (dist < minDistValue) {
                rgbValue = mapColors[i];
                minDistValue = dist;
                minDistIndex = i;
            }
        }
        //shift by 4 to account for transparency IDs discussed above
        return new int[]{(minDistIndex + 4), rgbArrayToRgbInt(rgbValue)};
    }

    public static int[] rgbIntToRgbArray(int rgb) {
        return new int[]{(rgb & 0xff0000) >> 16, (rgb & 0xff00) >> 8, rgb & 0xff};
    }
    public static int rgbArrayToRgbInt(int[] rgb) {
        return (((rgb[0] << 16) | (rgb[1] << 8) | rgb[2]));
    }

    public static int distanceBetweenRgbValues(int rgb1, int rgb2) {
        return distanceBetweenRgbValues(rgbIntToRgbArray(rgb1), rgbIntToRgbArray(rgb2));
    }
    public static int distanceBetweenRgbValues(int[] rgb1, int[] rgb2) {
        //TODO this is taking up bulk of processing time since something something nerd shit O(n) maybe i should go look at other ways people have done this
        return (int) (Math.pow(rgb1[0]-rgb2[0], 2) + Math.pow(rgb1[1]-rgb2[1], 2) + Math.pow(rgb1[2]-rgb2[2], 2));
    }

    public static int addRgbValues(int rgb1, int rgb2) {
        return rgbArrayToRgbInt(addRgbValues(rgbIntToRgbArray(rgb1),  rgbIntToRgbArray(rgb2)));
    }
    public static int[] addRgbValues(int[] rgb1, int[] rgb2) {
        return new int[]{rgb1[0]+rgb2[0], rgb1[1]+rgb2[1], rgb1[2]+rgb2[2]};
    }

    public static int subRgbValues(int rgb1, int rgb2) {
        return rgbArrayToRgbInt(subRgbValues(rgbIntToRgbArray(rgb1),  rgbIntToRgbArray(rgb2)));
    }
    public static int[] subRgbValues(int[] rgb1, int[] rgb2) {
        return new int[]{rgb1[0]-rgb2[0], rgb1[1]-rgb2[1], rgb1[2]-rgb2[2]};
    }

    public static int scaleRgbValue(int rgb, double scale) {
        return rgbArrayToRgbInt(scaleRgbValue(rgbIntToRgbArray(rgb), scale));
    }
    public static int[] scaleRgbValue(int[] rgb, double scale) {
        return new int[]{(int)(rgb[0]*scale), (int)(rgb[1]*scale), (int)(rgb[2]*scale)};
    }

    public static int clampRgbValue(int rgb) {
        return rgbArrayToRgbInt(clampRgbValue(rgbIntToRgbArray(rgb)));
    }
    public static int[] clampRgbValue(int[] rgb) {
        return new int[]{Math.clamp(rgb[0], 0, 255), Math.clamp(rgb[1], 0, 255), Math.clamp(rgb[2], 0, 255)};
    }

    //returns colorMap with minecraft color bytes for each color
    public static byte[][] imageToMinecraftMapColors(BufferedImage image, DitherType dither) {
        int height = image.getHeight(), width = image.getWidth();
        byte[][] colorMap = new byte[height][width];
        for (int y = 0; y < colorMap.length; y++) {
            for (int x = 0; x < colorMap[0].length; x++) {
                int rgb = image.getRGB(x, y);
                int[] closestColor = findClosetMapColorByte(rgbIntToRgbArray(rgb));
                colorMap[y][x] = (byte) closestColor[0];
                //TODO change computation surrounding rgb values, objects will be cleaner but idk if they will cause performance shennigans
                int[] quantizationError = subRgbValues(rgbIntToRgbArray(rgb), rgbIntToRgbArray(closestColor[1]));
                int[][] offsets;
                int[] scale;

                switch(dither) {
                    case DitherType.NONE:
                        break;
                    case DitherType.FLOYDSTEINBERG:
                        offsets = new int[][]{       {1, 0},
                                            {-1,-1}, {0, -1}, {1, -1}};
                        scale = new int[]{7,
                                    3, 5, 1};
                        for (int i = 0; i < offsets.length; i++) {
                            //y offset subtracted since the image increases its y value as it goes down, same for the colorMap returned
                            int targetXValue = x+offsets[i][0], targetYValue = y-offsets[i][1];
                            if ((targetXValue >= 0 && targetXValue < width) && (targetYValue >= 0 && targetYValue < height))
                                image.setRGB(targetXValue, targetYValue, rgbArrayToRgbInt(clampRgbValue(addRgbValues(rgbIntToRgbArray(image.getRGB(targetXValue, targetYValue)), scaleRgbValue(quantizationError, scale[i] / 16.0)))));
                        }
                        break;
                    case DitherType.MINIMIZED_AVERAGE_ERROR:
                        offsets = new int[][]{           {1,0}, {2,0},
                                {-2,-1}, {-1,-1}, {0,-1}, {1,-1}, {2,-1},
                                {-2,-2}, {-1,-2}, {0,-2}, {1,-2}, {2,-2}};
                        scale = new int[]{7, 5,
                                 3, 5, 7, 5, 3,
                                 1, 3, 5, 3, 1};
                        for (int i = 0; i < offsets.length; i++) {
                            //y offset subtracted since the image increases its y value as it goes down, same for the colorMap returned
                            int targetXValue = x+offsets[i][0], targetYValue = y-offsets[i][1];
                            if ((targetXValue >= 0 && targetXValue < width) && (targetYValue >= 0 && targetYValue < height))
                                image.setRGB(targetXValue, targetYValue, rgbArrayToRgbInt(clampRgbValue(addRgbValues(rgbIntToRgbArray(image.getRGB(targetXValue, targetYValue)), scaleRgbValue(quantizationError, scale[i] / 48.0)))));
                        }
                        break;

                }

            }
        }
        return colorMap;
    }

    public enum DitherType{FLOYDSTEINBERG, MINIMIZED_AVERAGE_ERROR, NONE}
}
