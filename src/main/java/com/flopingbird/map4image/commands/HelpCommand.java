package com.flopingbird.map4image.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class HelpCommand {
    public HelpCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("map4image").executes((command) -> {
            return help(command.getSource());
        }));
        dispatcher.register(Commands.literal("map4image").then(Commands.literal("help").executes((command) -> {
            return help(command.getSource());
        })));
    }

    private int help(CommandSourceStack source) throws CommandSyntaxException {
        source.sendSystemMessage(Component.literal("Currently the only command (beside this one), is the create command.\n" +
                "/map4image create <link> <dithertype> <width> <height>\n" +
                "Keep in mind a map is 128 pixels in length. The recommended dither type is floydsteinberg\n" +
                "For more information you can visit the modrinth page here: https://www.modrinth.com/mod/map4image"));
        return 1;
    }

}
