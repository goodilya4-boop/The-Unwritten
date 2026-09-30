package com.theunwritten.character;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.Map;

public final class CharacterAttributeCommand {
    private CharacterAttributeCommand() {
    }

    private static final SuggestionProvider<CommandSourceStack> ATTRIBUTE_SUGGESTIONS =
            (context, builder) -> {
                for (AttributeType type : AttributeType.values()) {
                    builder.suggest(type.name().toLowerCase());
                }
                return builder.buildFuture();
            };

    public static void register(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("attributes")
                .executes(context -> show(context.getSource()))
                .then(Commands.literal("show")
                        .executes(context -> show(context.getSource())))
                .then(Commands.literal("set")
                        .then(Commands.argument("attribute", StringArgumentType.word())
                                .suggests(ATTRIBUTE_SUGGESTIONS)
                                .then(Commands.argument("value", DoubleArgumentType.doubleArg(0.0D))
                                        .executes(context -> set(
                                                context.getSource(),
                                                StringArgumentType.getString(context, "attribute"),
                                                DoubleArgumentType.getDouble(context, "value")
                                        )))))
                .then(Commands.literal("add")
                        .then(Commands.argument("attribute", StringArgumentType.word())
                                .suggests(ATTRIBUTE_SUGGESTIONS)
                                .then(Commands.argument("amount", DoubleArgumentType.doubleArg())
                                        .executes(context -> add(
                                                context.getSource(),
                                                StringArgumentType.getString(context, "attribute"),
                                                DoubleArgumentType.getDouble(context, "amount")
                                        ))));
    }

    private static int show(CommandSourceStack source) {
        ServerPlayer player = source.getPlayer();
        if (player == null) {
            source.sendFailure(Component.literal("This command can only be used by a player."));
            return 0;
        }

        CharacterData data = player.getData(CharacterAttachments.CHARACTER_DATA);
        source.sendSuccess(() -> Component.literal("=== The Unwritten: Character Profile ==="), false);

        for (AttributeType type : AttributeType.values()) {
            double value = data.attributes().get(type);
            source.sendSuccess(() -> Component.literal(
                    "Attribute " + type.name() + ": " + format(value)), false);
        }

        source.sendSuccess(() -> Component.literal("--- Stats ---"), false);
        for (Map.Entry<StatType, Double> entry : data.stats().entrySet()) {
            source.sendSuccess(() -> Component.literal(
                    entry.getKey().name() + ": " + format(entry.getValue())), false);
        }

        return 1;
    }

    private static int set(CommandSourceStack source, String name, double value) {
        ServerPlayer player = source.getPlayer();
        if (player == null) {
            source.sendFailure(Component.literal("This command can only be used by a player."));
            return 0;
        }

        AttributeType type = parseAttribute(source, name);
        if (type == null) {
            return 0;
        }

        CharacterData data = player.getData(CharacterAttachments.CHARACTER_DATA);
        data.attributes().set(type, value);

        source.sendSuccess(() -> Component.literal(
                "Set " + type.name() + " to " + format(value)), true);
        return 1;
    }

    private static int add(CommandSourceStack source, String name, double amount) {
        ServerPlayer player = source.getPlayer();
        if (player == null) {
            source.sendFailure(Component.literal("This command can only be used by a player."));
            return 0;
        }

        AttributeType type = parseAttribute(source, name);
        if (type == null) {
            return 0;
        }

        CharacterData data = player.getData(CharacterAttachments.CHARACTER_DATA);
        data.attributes().add(type, amount);
        double value = data.attributes().get(type);

        source.sendSuccess(() -> Component.literal(
                "Added " + format(amount) + " to " + type.name() + " (now " + format(value) + ")"), true);
        return 1;
    }

    private static AttributeType parseAttribute(CommandSourceStack source, String name) {
        try {
            return AttributeType.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException exception) {
            source.sendFailure(Component.literal(
                    "Unknown attribute: " + name + ". Valid: STR, DEX, END, INT, WIL, PER."));
            return null;
        }
    }

    private static String format(double value) {
        return String.format(java.util.Locale.ROOT, "%.2f", value);
    }
}
