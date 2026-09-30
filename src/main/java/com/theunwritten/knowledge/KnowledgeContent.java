package com.theunwritten.knowledge;

import com.theunwritten.character.AttributeType;

import java.util.List;
import java.util.Set;

public final class KnowledgeContent {
    public static final String IMPERIAL_FENCING = "combat.school.imperial_fencing";

    public static final KnowledgeNode IMPERIAL_FENCING_NODE = new KnowledgeNode(
            IMPERIAL_FENCING,
            "Imperial Fencing",
            null,
            1,
            Set.of(),
            List.of(
                    new MinimumAttributeRequirement(AttributeType.DEX, 20.0D),
                    new MinimumAttributeRequirement(AttributeType.PER, 20.0D)
            ),
            List.of(
                    new KnowledgeUnlock("weapon_school:imperial_fencing")
            )
    );

    private KnowledgeContent() {
    }
}
