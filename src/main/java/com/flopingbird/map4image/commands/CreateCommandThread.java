package com.flopingbird.map4image.commands;

import com.flopingbird.map4image.MapGenerationUtils;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.saveddata.maps.MapId;

import java.awt.image.BufferedImage;

import static com.flopingbird.map4image.GenerateMapArt.*;

public class CreateCommandThread extends Thread {
    CommandContext<CommandSourceStack> command = null;
    MapGenerationUtils.DitherType dither = null;

    CreateCommandThread(CommandContext<CommandSourceStack> command, MapGenerationUtils.DitherType dither) {
        this.command = command;
        this.dither = dither;
    }

    public void run() {

        CommandSourceStack source = command.getSource();
        ServerPlayer player = source.getPlayer();
        BufferedImage image = getBufferedImageFromLink(StringArgumentType.getString(command, "link"));


        int[][] maps = generateMapArt(image, player.serverLevel(), dither, IntegerArgumentType.getInteger(command, "width"), IntegerArgumentType.getInteger(command, "height"));


        if (maps.length == 1 && maps[0].length == 1) {
            //preferable to MapItem.create as that will burn through a MapID
            ItemStack mapItem = new ItemStack(Items.FILLED_MAP);
            mapItem.set(DataComponents.MAP_ID, new MapId(maps[0][0]));
            player.addItem(mapItem);
        } else {
            int previewMap = generateMapArt(image, player.serverLevel(), dither, 128, 128)[0][0];
            ItemStack finalMapItem = createPreviewMap(previewMap, maps[0].length, maps.length);
            player.addItem(finalMapItem);
            command.getSource().sendSystemMessage(Component.literal("Map art generated!"));
        }
    }
}
