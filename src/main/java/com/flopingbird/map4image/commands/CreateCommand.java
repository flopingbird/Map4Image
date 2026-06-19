package com.flopingbird.map4image.commands;

import com.flopingbird.map4image.Config;
import com.flopingbird.map4image.utils.MapGenerationUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class CreateCommand {
    public final static int MAX_WIDTH = 128*Config.maxWidth;
    public final static int MAX_HEIGHT = 128*Config.maxHeight;

    public CreateCommand(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(
                Commands.literal("map4image").then(
                        Commands.literal("create").then(
                                Commands.argument("link", StringArgumentType.string())
                                        .then(buildDitherBranch("floydsteinberg", MapGenerationUtils.DitherType.FLOYDSTEINBERG))
                                        .then(buildDitherBranch("none", MapGenerationUtils.DitherType.NONE))
                                        .then(buildDitherBranch("minimizedAverageError", MapGenerationUtils.DitherType.MINIMIZED_AVERAGE_ERROR))
                        )
                )
        );
    }

    private ArgumentBuilder<CommandSourceStack, ?> buildDitherBranch(String ditherName, MapGenerationUtils.DitherType ditherType) {
        return Commands.literal(ditherName)
                .then(Commands.argument("width", IntegerArgumentType.integer(1, MAX_WIDTH))
                        .then(Commands.argument("height", IntegerArgumentType.integer(1, MAX_HEIGHT))

                                .executes(cmd -> create(cmd, ditherType, MapGenerationUtils.FlipType.NONE))

                                .then(Commands.literal("none")
                                        .executes(cmd -> create(cmd, ditherType, MapGenerationUtils.FlipType.NONE)))

                                .then(Commands.literal("horizontal")
                                        .executes(cmd -> create(cmd, ditherType, MapGenerationUtils.FlipType.HORIZONTAL)))

                                .then(Commands.literal("vertical")
                                        .executes(cmd -> create(cmd, ditherType, MapGenerationUtils.FlipType.VERTICAL)))

                                .then(Commands.literal("both")
                                        .executes(cmd -> create(cmd, ditherType, MapGenerationUtils.FlipType.BOTH)))
                        ));
    }

    private int create(CommandContext<CommandSourceStack> command, MapGenerationUtils.DitherType dither, MapGenerationUtils.FlipType flipType) {
        if (command.getSource().getPlayer() == null) return 0;

        try {
            CreateCommandThread createCommandThread = new CreateCommandThread(command, dither, flipType);
            createCommandThread.start();
            command.getSource().sendSystemMessage(Component.literal("Generating map art..."));
        } catch (Exception e) {
            command.getSource().sendSystemMessage(Component.literal("Something went wrong while getting your image - see server log for more details"));
            System.out.println("Map4Image ran into error when creating map: " + e);
        }
        return 1;
    }



}
