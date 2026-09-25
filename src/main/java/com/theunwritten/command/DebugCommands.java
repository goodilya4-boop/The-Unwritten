package com.theunwritten.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.theunwritten.character.data.AttributeType;
import com.theunwritten.character.data.CharacterData;
import com.theunwritten.character.registry.CharacterAttachments;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public final class DebugCommands {
    private DebugCommands() {
    }

    public static void register(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(
                Commands.literal("uw")
                        .then(Commands.literal("debug")
                                .then(Commands.literal("attributes")
                                        .executes(context -> showAttributes(context.getSource())))));
    }

    private static int showAttributes(CommandSourceStack source) {
        ServerPlayer player = source.getPlayer();
        if (player == null) {
            return 0;
        }
        CharacterData data = player.getData(CharacterAttachments.CHARACTER_DATA);

        source.sendSuccess(() -> Component.literal("=== The Unwritten: Attributes ==="), false);
        for (AttributeType type : AttributeType.values()) {
            double base = data.attributeAccess().getBase(type);
            double effective = data.attributeAccess().get(type);
            double modifier = data.attributeAccess().getModifierTotal(type);

            source.sendSuccess(
                    () -> Component.literal(String.format(
                            java.util.Locale.ROOT,
                            "%s: %.2f (base %.2f, modifiers %.2f)",
                            type.name(),
                            effective,
                            base,
                            modifier)),
                    false);
        }

        return 1;
    }
}
