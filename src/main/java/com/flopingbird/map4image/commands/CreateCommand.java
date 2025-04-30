package com.flopingbird.map4image.commands;

import com.flopingbird.map4image.MapGenerationUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import java.awt.image.BufferedImage;

import static com.flopingbird.map4image.GenerateMapArt.generateMapArt;
import static com.flopingbird.map4image.GenerateMapArt.getBufferedImageFromLink;

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

    }

    private int create(CommandContext<CommandSourceStack> command, MapGenerationUtils.DitherType dither) throws CommandSyntaxException {

        CommandSourceStack source = command.getSource();
        ServerPlayer player = source.getPlayer();
        BufferedImage image = getBufferedImageFromLink(StringArgumentType.getString(command, "link"));
        ItemStack[] maps = generateMapArt(image, player.serverLevel(), dither, IntegerArgumentType.getInteger(command, "width"), IntegerArgumentType.getInteger(command, "height"));

        //TODO click to add to multiple item frames at once when map size > 1
        for (ItemStack map : maps) {
            if (map != null)
                player.addItem(map);
        }
        return 1;
    }
}
