package com.theunwritten.knowledge;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.theunwritten.character.CharacterAttachments;
import com.theunwritten.character.CharacterData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public final class KnowledgeCommand {
    private KnowledgeCommand() {
    }

    private static final SuggestionProvider<CommandSourceStack> NODE_SUGGESTIONS =
            (context, builder) -> {
                for (KnowledgeNode node : KnowledgeContentRegistry.REGISTRY.all()) {
                    builder.suggest(node.id());
                }
                return builder.buildFuture();
            };

    public static void register(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("knowledge")
                .then(Commands.literal("list")
                        .executes(context -> list(context.getSource())))
                .then(Commands.literal("unlock")
                        .then(Commands.argument("node", StringArgumentType.word())
                                .suggests(NODE_SUGGESTIONS)
                                .executes(context -> unlock(
                                        context.getSource(),
                                        StringArgumentType.getString(context, "node")
                                )))));
    }

    private static int list(CommandSourceStack source) {
        ServerPlayer player = source.getPlayer();
        if (player == null) {
            source.sendFailure(Component.literal("This command can only be used by a player."));
            return 0;
        }

        CharacterData data = player.getData(CharacterAttachments.CHARACTER_DATA);
        source.sendSuccess(() -> Component.literal("=== Knowledge ==="), false);

        for (KnowledgeNode node : KnowledgeContentRegistry.REGISTRY.all()) {
            int level = data.knowledge().level(node.id());
            source.sendSuccess(() -> Component.literal(
                    node.id() + " [" + level + "/" + node.maxLevel() + "]"
            ), false);
        }

        return 1;
    }

    private static int unlock(CommandSourceStack source, String nodeId) {
        ServerPlayer player = source.getPlayer();
        if (player == null) {
            source.sendFailure(Component.literal("This command can only be used by a player."));
            return 0;
        }

        KnowledgeRegistry registry = KnowledgeContentRegistry.REGISTRY;
        if (!registry.contains(nodeId)) {
            source.sendFailure(Component.literal("Unknown knowledge node: " + nodeId));
            return 0;
        }

        CharacterData data = player.getData(CharacterAttachments.CHARACTER_DATA);
        if (!KnowledgeProgression.learn(data, registry, nodeId)) {
            source.sendFailure(Component.literal(
                    "Cannot unlock " + nodeId + ": prerequisites or requirements are not met."
            ));
            return 0;
        }

        source.sendSuccess(() -> Component.literal(
                "Unlocked knowledge: " + nodeId
        ), true);
        return 1;
    }
}
