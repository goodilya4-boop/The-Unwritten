package com.theunwritten.character.data;

import com.theunwritten.character.api.AttributeAccess;
import com.theunwritten.character.api.CharacterAttributeAccess;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

/**
 * Persistent foundation data for a character.
 *
 * This class deliberately contains only the first foundation layer. Skills,
 * schools, progression history and hybrid interactions will be added as
 * independent systems rather than being folded into attributes.
 */
public final class CharacterData implements INBTSerializable<CompoundTag> {
    private final Attributes attributes;
    private final AttributeAccess attributeAccess;
    private final Potential potential;
    private final CharacterResources resources;

    public CharacterData() {
        this.attributes = new Attributes(20.0D);
        this.attributeAccess = new CharacterAttributeAccess(attributes);
        this.potential = new Potential(100.0D);
        this.resources = new CharacterResources();
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

    public CharacterStats calculateStats() {
        return CharacterStats.calculate(attributes);
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
    }
}