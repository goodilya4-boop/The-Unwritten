package com.theunwritten.character.data;

import com.theunwritten.character.api.AttributeAccess;
import com.theunwritten.character.api.CharacterAttributeAccess;
import com.theunwritten.knowledge.CharacterKnowledge;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

/**
 * Persistent foundation data for a character.
 *
 * Runtime attribute modifiers are intentionally not serialized here. Equipment
 * and other transient sources rebuild their modifiers from authoritative state.
 */
public final class CharacterData implements INBTSerializable<CompoundTag> {
    private final Attributes attributes;
    private final AttributeAccess attributeAccess;
    private final Potential potential;
    private final CharacterResources resources;
    private final CharacterKnowledge knowledge;

    public CharacterData() {
        this.attributes = new Attributes(20.0D);
        this.attributeAccess = new CharacterAttributeAccess(attributes);
        this.potential = new Potential(100.0D);
        this.resources = new CharacterResources();
        this.knowledge = new CharacterKnowledge();
        refreshResourceMaximums();
        restoreAllResources();
    }

    public Attributes attributes() {
        return attributes;
    }

    public AttributeAccess attributeAccess() {
        return attributeAccess;
    }

    public Potential potential() {
        return potential;
    }

    public CharacterResources resources() {
        return resources;
    }

    public CharacterKnowledge knowledge() {
        return knowledge;
    }

    public CharacterStats calculateStats() {
        return CharacterStats.calculate(attributeAccess);
    }

    public void refreshResourceMaximums() {
        resources.applyStats(calculateStats());
    }

    public void restoreAllResources() {
        for (CharacterResources.Type type : CharacterResources.Type.values()) {
            resources.restoreToMaximum(type);
        }
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.put("Attributes", attributes.serializeNBT(provider));
        tag.put("Potential", potential.serializeNBT(provider));
        tag.put("Resources", resources.serializeNBT(provider));
        tag.put("Knowledge", knowledge.serializeNBT(provider));
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        if (tag.contains("Attributes")) {
            attributes.deserializeNBT(provider, tag.getCompound("Attributes"));
        }
        if (tag.contains("Potential")) {
            potential.deserializeNBT(provider, tag.getCompound("Potential"));
        }
        if (tag.contains("Resources")) {
            resources.deserializeNBT(provider, tag.getCompound("Resources"));
        } else {
            refreshResourceMaximums();
            restoreAllResources();
        }
        if (tag.contains("Knowledge")) {
            knowledge.deserializeNBT(provider, tag.getCompound("Knowledge"));
        }
    }
}
