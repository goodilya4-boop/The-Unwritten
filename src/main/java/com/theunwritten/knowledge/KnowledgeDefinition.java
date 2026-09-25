package com.theunwritten.knowledge;

import net.minecraft.resources.ResourceLocation;

public record KnowledgeDefinition(
        ResourceLocation id,
        KnowledgeDomain domain,
        KnowledgeTier tier,
        ResourceLocation parent,
        boolean rare,
        boolean forbidden,
        boolean hiddenByDefault) {

    public KnowledgeDefinition {
        if (id == null) throw new IllegalArgumentException("Knowledge id cannot be null");
        if (domain == null) throw new IllegalArgumentException("Knowledge domain cannot be null");
        if (tier == null) throw new IllegalArgumentException("Knowledge tier cannot be null");
        if (parent != null && parent.equals(id)) {
            throw new IllegalArgumentException("Knowledge cannot be its own parent");
        }
    }
}
