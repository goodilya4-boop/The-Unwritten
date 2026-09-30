package com.theunwritten.knowledge;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class KnowledgeRegistry {
    private final Map<String, KnowledgeNode> nodes = new LinkedHashMap<>();

    public void register(KnowledgeNode node) {
        Objects.requireNonNull(node, "node");
        if (nodes.putIfAbsent(node.id(), node) != null) {
            throw new IllegalArgumentException("Duplicate knowledge node: " + node.id());
        }
    }

    public KnowledgeNode get(String id) {
        return nodes.get(id);
    }

    public boolean contains(String id) {
        return nodes.containsKey(id);
    }

    public Collection<KnowledgeNode> all() {
        return Collections.unmodifiableCollection(nodes.values());
    }

    public void validate() {
        for (KnowledgeNode node : nodes.values()) {
            if (node.parentId() != null && !contains(node.parentId())) {
                throw new IllegalStateException("Unknown parent '" + node.parentId() + "' for node '" + node.id() + "'");
            }
            for (String prerequisite : node.prerequisites()) {
                if (!contains(prerequisite)) {
                    throw new IllegalStateException("Unknown prerequisite '" + prerequisite + "' for node '" + node.id() + "'");
                }
            }
            for (KnowledgeRequirement requirement : node.requirements()) {
                if (requirement instanceof MinimumKnowledgeRequirement minimum
                        && !contains(minimum.nodeId())) {
                    throw new IllegalStateException("Unknown requirement node '" + minimum.nodeId()
                            + "' for node '" + node.id() + "'");
                }
            }
        }
    }
}
