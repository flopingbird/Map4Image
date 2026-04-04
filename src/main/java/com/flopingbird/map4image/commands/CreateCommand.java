package com.flopingbird.map4image.commands;

import com.flopingbird.map4image.Config;
import com.flopingbird.map4image.utils.MapGenerationUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class CreateCommand {
    public final static int MAX_WIDTH = 128*Config.maxWidth;
    public final static int MAX_HEIGHT = 128*Config.maxHeight;

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
        if (command.getSource().getPlayer() == null) return 0;
        try {
            CreateCommandThread createCommandThread = new CreateCommandThread(command, dither);
            createCommandThread.start();
            command.getSource().sendSystemMessage(Component.literal("Generating map art..."));
        } catch (Exception e) {
            command.getSource().sendSystemMessage(Component.literal("Something went wrong while getting your image - see server log for more details"));
            System.out.println("Map4Image ran into error when creating map: " + e);
        }
        //create new thread
        return 1;
    }



}
