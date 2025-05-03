package com.flopingbird.map4image.commands;

import com.flopingbird.map4image.MapGenerationUtils;
import com.flopingbird.map4image.component.ModDataComponentType;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.saveddata.maps.MapId;

import java.awt.image.BufferedImage;

import static com.flopingbird.map4image.GenerateMapArt.*;

public class CreateCommand {
    public final static int MAX_WIDTH = 128*10;
    public final static int MAX_HEIGHT = 128*10;

    public CreateCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("map4image")
                .then(Commands.literal("create").then(Commands.argument("link", StringArgumentType.string()).then(Commands.literal("floydsteinberg").then(Commands.argument("width", IntegerArgumentType.integer(1, MAX_WIDTH)).then(Commands.argument("height", IntegerArgumentType.integer(1, MAX_HEIGHT)).executes((command) -> {
            return create(command, MapGenerationUtils.DitherType.FLOYDSTEINBERG);
        })))))));
        dispatcher.register(
                Commands.literal("map4image")
                .then(Commands.literal("create").then(Commands.argument("link", StringArgumentType.string()).then(Commands.literal("none").then(Commands.argument("width", IntegerArgumentType.integer(1, MAX_WIDTH)).then(Commands.argument("height", IntegerArgumentType.integer(1, MAX_HEIGHT)).executes((command) -> {
                    return create(command, MapGenerationUtils.DitherType.NONE);
        })))))));
        dispatcher.register(
                Commands.literal("map4image")
                .then(Commands.literal("create").then(Commands.argument("link", StringArgumentType.string()).then(Commands.literal("minimizedAverageError").then(Commands.argument("width", IntegerArgumentType.integer(1, MAX_WIDTH)).then(Commands.argument("height", IntegerArgumentType.integer(1, MAX_HEIGHT)).executes((command) -> {
                    return create(command, MapGenerationUtils.DitherType.MINIMIZED_AVERAGE_ERROR);
        })))))));

    }

    private int create(CommandContext<CommandSourceStack> command, MapGenerationUtils.DitherType dither) throws CommandSyntaxException {

        CommandSourceStack source = command.getSource();
        ServerPlayer player = source.getPlayer();
        BufferedImage image = getBufferedImageFromLink(StringArgumentType.getString(command, "link"));
        int[][] maps = generateMapArt(image, player.serverLevel(), dither, IntegerArgumentType.getInteger(command, "width"), IntegerArgumentType.getInteger(command, "height"));

        if (maps.length == 1 && maps[0].length == 1) {
            //preferable to MapItem.create as that will burn through a MapID
            ItemStack mapItem = new ItemStack(Items.FILLED_MAP);
            mapItem.set(DataComponents.MAP_ID, new MapId(maps[0][0]));
            player.addItem(mapItem);
        }
        else {
            int previewMap = generateMapArt(image, player.serverLevel(), dither, 128, 128)[0][0];
            player.addItem(createPreviewMap(previewMap, maps[0].length, maps.length));
        }

        return 1;
    }
}
