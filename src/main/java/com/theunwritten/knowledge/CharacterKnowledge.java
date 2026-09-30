package com.theunwritten.knowledge;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class CharacterKnowledge {
    private final Map<String, Integer> levels = new HashMap<>();

    public int level(String nodeId) {
        return levels.getOrDefault(nodeId, 0);
    }

    public void setLevel(String nodeId, int level) {
        if (nodeId == null || nodeId.isBlank()) {
            throw new IllegalArgumentException("Knowledge node id must not be blank");
        }
        if (level < 0) {
            throw new IllegalArgumentException("Knowledge level must be >= 0");
        }
        if (level == 0) {
            levels.remove(nodeId);
        } else {
            levels.put(nodeId, level);
        }
    }

    public void addLevel(String nodeId, int amount) {
        setLevel(nodeId, level(nodeId) + amount);
    }

    public boolean has(String nodeId) {
        return level(nodeId) > 0;
    }

    public Map<String, Integer> levels() {
        return Collections.unmodifiableMap(levels);
    }
}
