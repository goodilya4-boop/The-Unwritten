package com.theunwritten.character.api;

import com.theunwritten.character.data.CharacterData;
import com.theunwritten.character.registry.CharacterAttachments;
import net.minecraft.world.entity.LivingEntity;

/** Stable public facade for systems that need access to The Unwritten character foundation. */
public final class CharacterAPI {
    private CharacterAPI() {}

    public static CharacterData getData(LivingEntity entity) {
        return entity.getData(CharacterAttachments.CHARACTER_DATA);
    }

    public static AttributeAccess attributes(LivingEntity entity) {
        return getData(entity).attributeAccess();
    }
}
