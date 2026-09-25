package com.theunwritten.command;

import com.mojang.brigadier.CommandDispatcher;
import com.theunwritten.character.data.AttributeType;
import com.theunwritten.character.data.CharacterData;
import com.theunwritten.character.registry.CharacterAttachments;
import com.theunwritten.knowledge.CharacterKnowledge;
import com.theunwritten.knowledge.KnowledgeDiscoveryContext;
import com.theunwritten.knowledge.KnowledgeDiscoveryOffer;
import com.theunwritten.knowledge.KnowledgeDiscoveryResult;
import com.theunwritten.knowledge.KnowledgeDiscoveryService;
import com.theunwritten.knowledge.KnowledgeDiscoverySource;
import com.theunwritten.knowledge.KnowledgeIds;
import com.theunwritten.knowledge.KnowledgeRegistry;
import com.theunwritten.knowledge.KnowledgeRequirement;
import com.theunwritten.knowledge.KnowledgeState;
import com.theunwritten.knowledge.KnowledgeTeacher;

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
                                        .executes(context -> showAttributes(context.getSource())))
                                .then(Commands.literal("knowledge")
                                        .executes(context -> showKnowledge(context.getSource()))
                                        .then(Commands.literal("teacher")
                                                .executes(context -> testTeacher(context.getSource()))))));
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

    private static int showKnowledge(CommandSourceStack source) {
        ServerPlayer player = source.getPlayer();
        if (player == null) {
            return 0;
        }

        CharacterKnowledge knowledge =
                player.getData(CharacterAttachments.CHARACTER_DATA).knowledge();

        source.sendSuccess(() -> Component.literal("=== The Unwritten: Knowledge ==="), false);
        if (knowledge.known().isEmpty()) {
            source.sendSuccess(() -> Component.literal("No known knowledge."), false);
            return 1;
        }

        knowledge.known().forEach((id, state) ->
                source.sendSuccess(
                        () -> Component.literal(id + " -> " + state.name()),
                        false));

        return 1;
    }

    private static int testTeacher(CommandSourceStack source) {
        ServerPlayer player = source.getPlayer();
        if (player == null) {
            return 0;
        }

        CharacterKnowledge characterKnowledge =
                player.getData(CharacterAttachments.CHARACTER_DATA).knowledge();
        KnowledgeRegistry registry = KnowledgeRegistry.createDefault();
        KnowledgeDiscoveryService discoveryService = new KnowledgeDiscoveryService(registry);

        KnowledgeDiscoveryOffer elementalOffer =
                new KnowledgeDiscoveryOffer(KnowledgeIds.Magic.ELEMENTAL, java.util.List.of());
        KnowledgeDiscoveryOffer lightOffer =
                new KnowledgeDiscoveryOffer(
                        KnowledgeIds.Magic.LIGHT,
                        java.util.List.of(
                                new KnowledgeRequirement(
                                        KnowledgeIds.Magic.ELEMENTAL,
                                        KnowledgeState.STUDIED)));

        KnowledgeTeacher teacher = new KnowledgeTeacher(
                KnowledgeIds.id("teacher/debug"),
                java.util.List.of(elementalOffer, lightOffer));

        source.sendSuccess(
                () -> Component.literal("=== The Unwritten: Teacher Discovery Test ==="),
                false);
        source.sendSuccess(
                () -> Component.literal("Teacher: " + teacher.id()),
                false);

        KnowledgeDiscoveryResult elementalResult = discoveryService.discover(
                new KnowledgeDiscoveryContext(
                        characterKnowledge,
                        elementalOffer.knowledgeId(),
                        KnowledgeDiscoverySource.TEACHER),
                elementalOffer);

        source.sendSuccess(
                () -> Component.literal("Elemental discovery: " + elementalResult.name()),
                false);

        KnowledgeDiscoveryResult blockedLightResult = discoveryService.discover(
                new KnowledgeDiscoveryContext(
                        characterKnowledge,
                        lightOffer.knowledgeId(),
                        KnowledgeDiscoverySource.TEACHER),
                lightOffer);

        source.sendSuccess(
                () -> Component.literal(
                        "Light before studying Elemental: " + blockedLightResult.name()),
                false);

        boolean studied = characterKnowledge.study(KnowledgeIds.Magic.ELEMENTAL);
        source.sendSuccess(
                () -> Component.literal("Study Elemental: " + studied),
                false);

        KnowledgeDiscoveryResult lightResult = discoveryService.discover(
                new KnowledgeDiscoveryContext(
                        characterKnowledge,
                        lightOffer.knowledgeId(),
                        KnowledgeDiscoverySource.TEACHER),
                lightOffer);

        source.sendSuccess(
                () -> Component.literal("Light after studying Elemental: " + lightResult.name()),
                false);

        return 1;
    }
}
