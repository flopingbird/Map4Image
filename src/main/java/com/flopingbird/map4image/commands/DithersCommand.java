package com.flopingbird.map4image.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class DithersCommand{
    public DithersCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("map4image").then(Commands.literal("dithers").executes((command) -> {
            return help(command.getSource());
        })));
    }

    private int help(CommandSourceStack source) throws CommandSyntaxException {
        source.sendSystemMessage(Component.literal("none:floydsteinberg:minimizedAverageError"));
        return 1;
    }
}