package com.theunwritten.knowledge;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.util.INBTSerializable;

public final class CharacterKnowledge implements INBTSerializable<CompoundTag> {
    private static final String KNOWLEDGE = "Knowledge";
    private static final String ID = "Id";
    private static final String STATE = "State";

    private final Map<ResourceLocation, KnowledgeState> knowledge = new HashMap<>();

    public boolean knows(ResourceLocation knowledgeId) {
        return knowledge.containsKey(knowledgeId);
    }

    public KnowledgeState state(ResourceLocation knowledgeId) {
        return knowledge.get(knowledgeId);
    }

    public boolean discover(ResourceLocation knowledgeId) {
        requireId(knowledgeId);
        return knowledge.putIfAbsent(knowledgeId, KnowledgeState.DISCOVERED) == null;
    }

    public boolean study(ResourceLocation knowledgeId) {
        requireId(knowledgeId);
        KnowledgeState current = knowledge.get(knowledgeId);
        if (current != KnowledgeState.DISCOVERED) {
            return false;
        }
        knowledge.put(knowledgeId, KnowledgeState.STUDIED);
        return true;
    }

    public boolean master(ResourceLocation knowledgeId) {
        requireId(knowledgeId);
        if (knowledge.get(knowledgeId) != KnowledgeState.STUDIED) {
            return false;
        }
        knowledge.put(knowledgeId, KnowledgeState.MASTERED);
        return true;
    }

    public boolean isStudied(ResourceLocation knowledgeId) {
        return state(knowledgeId) == KnowledgeState.STUDIED;
    }

    public boolean isMastered(ResourceLocation knowledgeId) {
        return state(knowledgeId) == KnowledgeState.MASTERED;
    }

    public int discoveredCount() {
        return knowledge.size();
    }

    public Map<ResourceLocation, KnowledgeState> known() {
        return Collections.unmodifiableMap(knowledge);
    }

    public CharacterKnowledge copy() {
        CharacterKnowledge copy = new CharacterKnowledge();
        copy.knowledge.putAll(knowledge);
        return copy;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();

        for (Map.Entry<ResourceLocation, KnowledgeState> entry : knowledge.entrySet()) {
            CompoundTag knowledgeTag = new CompoundTag();
            knowledgeTag.putString(ID, entry.getKey().toString());
            knowledgeTag.putString(STATE, entry.getValue().name());
            list.add(knowledgeTag);
        }

        tag.put(KNOWLEDGE, list);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        knowledge.clear();
        if (!tag.contains(KNOWLEDGE)) {
            return;
        }

        ListTag list = tag.getList(KNOWLEDGE, 10);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag knowledgeTag = list.getCompound(i);
            ResourceLocation id = ResourceLocation.parse(knowledgeTag.getString(ID));
            KnowledgeState state = KnowledgeState.valueOf(knowledgeTag.getString(STATE));
            knowledge.put(id, state);
        }
    }

    private static void requireId(ResourceLocation knowledgeId) {
        if (knowledgeId == null) {
            throw new IllegalArgumentException("Knowledge id cannot be null");
        }
    }
}
