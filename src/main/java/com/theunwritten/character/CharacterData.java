package com.theunwritten.character;

import com.theunwritten.knowledge.CharacterKnowledge;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class CharacterData implements INBTSerializable<CompoundTag> {
    private static final String ATTRIBUTES_TAG = "attributes";
    private static final String KNOWLEDGE_TAG = "knowledge";

    private final Attributes attributes;
    private final CharacterKnowledge knowledge;
    private final List<StatModifier> statModifiers = new ArrayList<>();

    public CharacterData() {
        this.attributes = new Attributes();
        this.knowledge = new CharacterKnowledge();
    }

    public Attributes attributes() {
        return attributes;
    }

    public CharacterKnowledge knowledge() {
        return knowledge;
    }

    public Map<StatType, Double> stats() {
        return CharacterStats.calculate(attributes, statModifiers);
    }

    public void addStatModifier(StatModifier modifier) {
        statModifiers.removeIf(existing ->
                existing.id().equals(modifier.id()) && existing.stat() == modifier.stat());
        statModifiers.add(modifier);
    }

    public void removeStatModifier(String id, StatType stat) {
        statModifiers.removeIf(existing ->
                existing.id().equals(id) && existing.stat() == stat);
    }

    public List<StatModifier> statModifiers() {
        return List.copyOf(statModifiers);
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.put(ATTRIBUTES_TAG, attributes.serializeNBT(provider));
        tag.put(KNOWLEDGE_TAG, knowledge.serializeNBT(provider));
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        if (tag.contains(ATTRIBUTES_TAG)) {
            attributes.deserializeNBT(provider, tag.getCompound(ATTRIBUTES_TAG));
        }
        if (tag.contains(KNOWLEDGE_TAG)) {
            knowledge.deserializeNBT(provider, tag.getCompound(KNOWLEDGE_TAG));
        }
    }
}
