package com.theunwritten.knowledge;

import com.theunwritten.character.CharacterData;

import java.util.Objects;

public final class KnowledgeProgression {
    private KnowledgeProgression() {
    }

    public static boolean canLearn(CharacterData character, KnowledgeRegistry registry, String nodeId) {
        Objects.requireNonNull(character, "character");
        Objects.requireNonNull(registry, "registry");
        Objects.requireNonNull(nodeId, "nodeId");

        KnowledgeNode node = registry.get(nodeId);
        if (node == null || character.knowledge().level(nodeId) >= node.maxLevel()) {
            return false;
        }

        for (String prerequisite : node.prerequisites()) {
            if (!character.knowledge().has(prerequisite)) {
                return false;
            }
        }

        for (KnowledgeRequirement requirement : node.requirements()) {
            if (!requirement.isMet(character)) {
                return false;
            }
        }

        return true;
    }

    public static boolean learn(CharacterData character, KnowledgeRegistry registry, String nodeId) {
        if (!canLearn(character, registry, nodeId)) {
            return false;
        }

        character.knowledge().addLevel(nodeId, 1);
        return true;
    }
}
