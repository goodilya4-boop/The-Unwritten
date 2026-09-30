package com.theunwritten.knowledge;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class CharacterKnowledge implements INBTSerializable<CompoundTag> {
    private static final String LEVELS_TAG = "levels";
    private static final String NODE_ID_TAG = "node";
    private static final String LEVEL_TAG = "level";

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

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();

        levels.forEach((nodeId, level) -> {
            CompoundTag entry = new CompoundTag();
            entry.putString(NODE_ID_TAG, nodeId);
            entry.putInt(LEVEL_TAG, level);
            list.add(entry);
        });

        tag.put(LEVELS_TAG, list);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        levels.clear();

        if (!tag.contains(LEVELS_TAG)) {
            return;
        }

        ListTag list = tag.getList(LEVELS_TAG, 10);
        for (int index = 0; index < list.size(); index++) {
            CompoundTag entry = list.getCompound(index);
            if (entry.contains(NODE_ID_TAG) && entry.contains(LEVEL_TAG)) {
                setLevel(entry.getString(NODE_ID_TAG), entry.getInt(LEVEL_TAG));
            }
        }
    }
}
