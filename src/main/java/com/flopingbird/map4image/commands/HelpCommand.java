package com.flopingbird.map4image.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class HelpCommand {
    public HelpCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("map4image").executes((command) ->
                help(command.getSource())));
        dispatcher.register(Commands.literal("map4image").then(Commands.literal("help").executes((command) ->
                help(command.getSource()))));
    }

    private int help(CommandSourceStack source) {
        source.sendSystemMessage(Component.literal(
                """
                        /map4image create '<link>' <dithertype> <width> <height>
                        Keep in mind a map is 128 pixels in length and the width and height parameters are in pixels. The recommended dither type is floydsteinberg, see more dither types with /map4image dithers
                        Make sure to include the 's.
                        For more information you can visit the modrinth page here: https://www.modrinth.com/mod/map4image"""));
        return 1;
    }

}
