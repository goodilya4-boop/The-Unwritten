package com.theunwritten.item.data;

import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.Codec;
import com.theunwritten.character.attribute.AttributeModifier;

/**
 * Persistent data stored on an ItemStack describing the attribute modifiers
 * that the item can contribute when it is equipped.
 *
 * The component describes the item; it does not apply the modifiers itself.
 * Activation belongs to the equipment system implemented in the next stage.
 */
public record ItemAttributeModifiers(List<AttributeModifier> modifiers) {
    public static final Codec<ItemAttributeModifiers> CODEC =
            AttributeModifier.CODEC.listOf().xmap(ItemAttributeModifiers::new, ItemAttributeModifiers::modifiers);

    public ItemAttributeModifiers {
        modifiers = List.copyOf(modifiers);
    }

    public static ItemAttributeModifiers empty() {
        return new ItemAttributeModifiers(List.of());
    }

    public ItemAttributeModifiers with(AttributeModifier modifier) {
        List<AttributeModifier> result = new ArrayList<>(modifiers);
        result.add(modifier);
        return new ItemAttributeModifiers(result);
    }

    public ItemAttributeModifiers withoutId(String id) {
        return new ItemAttributeModifiers(modifiers.stream()
                .filter(modifier -> !modifier.id().equals(id))
                .toList());
    }
}
