package com.theunwritten.command;

import com.mojang.brigadier.CommandDispatcher;
import com.theunwritten.character.data.AttributeType;
import com.theunwritten.character.data.CharacterData;
import com.theunwritten.character.registry.CharacterAttachments;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public final class CharacterAttributeCommand {
    private CharacterAttributeCommand() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("attributes")
                .executes(context -> show(context.getSource())));
    }

    private static int show(CommandSourceStack source) {
        ServerPlayer player = source.getPlayer();
        CharacterData data = player.getData(CharacterAttachments.CHARACTER_DATA);

        source.sendSuccess(() -> Component.literal("=== Character Attributes ==="), false);
        for (AttributeType type : AttributeType.values()) {
            double base = data.attributeAccess().getBase(type);
            double modified = data.attributeAccess().get(type);
            double modifier = modified - base;
            source.sendSuccess(() -> Component.literal(String.format(
                    "%s  Base: %.2f  Modifier: %+.2f  Total: %.2f",
                    type.name(), base, modifier, modified)), false);
        }
        return 1;
    }
}
