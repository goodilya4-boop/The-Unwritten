package com.theunwritten.knowledge;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.util.INBTSerializable;

public final class CharacterKnowledge implements INBTSerializable<CompoundTag> {
    private static final String DISCOVERED = "Discovered";
    private final Set<ResourceLocation> discovered = new HashSet<>();

    public boolean knows(ResourceLocation knowledgeId) {
        return discovered.contains(knowledgeId);
    }

    public boolean discover(ResourceLocation knowledgeId) {
        if (knowledgeId == null) throw new IllegalArgumentException("Knowledge id cannot be null");
        return discovered.add(knowledgeId);
    }

    public int discoveredCount() {
        return discovered.size();
    }

    public Set<ResourceLocation> discovered() {
        return Collections.unmodifiableSet(discovered);
    }

    public CharacterKnowledge copy() {
        CharacterKnowledge copy = new CharacterKnowledge();
        copy.discovered.addAll(discovered);
        return copy;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        for (ResourceLocation id : discovered) {
            list.add(StringTag.valueOf(id.toString()));
        }
        tag.put(DISCOVERED, list);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        discovered.clear();
        if (!tag.contains(DISCOVERED)) return;
        ListTag list = tag.getList(DISCOVERED, 8);
        for (int i = 0; i < list.size(); i++) {
            discovered.add(ResourceLocation.parse(list.getString(i)));
        }
    }
}
