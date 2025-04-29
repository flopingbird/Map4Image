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
        source.sendSystemMessage(Component.literal("friend!"));
        return 1;
    }

}
