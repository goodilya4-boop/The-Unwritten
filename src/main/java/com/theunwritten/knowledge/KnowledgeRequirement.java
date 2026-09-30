package com.theunwritten.knowledge;

import com.theunwritten.character.CharacterData;

public interface KnowledgeRequirement {
    boolean isMet(CharacterData character);

    String description();
}
