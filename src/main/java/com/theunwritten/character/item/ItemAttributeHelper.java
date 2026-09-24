package com.theunwritten.character.item;

import com.theunwritten.character.api.AttributeModifier;
import com.theunwritten.character.api.AttributeAccess;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/** Bridge between equipped items and the character Attribute API. */
public final class ItemAttributeHelper {
    private ItemAttributeHelper() {}

    /**
     * Applies the item's declared modifiers. The caller is responsible for invoking this
     * when equipment changes; this keeps the attribute system independent from inventory code.
     */
    public static void apply(ItemStack stack, AttributeAccess attributes, List<ItemAttributeModifier> modifiers) {
        String source = stack.getItem().getDescriptionId();
        attributes.removeModifiersFromSource(source);
        for (ItemAttributeModifier modifier : modifiers) {
            AttributeModifier characterModifier = modifier.asCharacterModifier(source);
            attributes.addModifier(characterModifier);
        }
    }

    public static void remove(ItemStack stack, AttributeAccess attributes) {
        attributes.removeModifiersFromSource(stack.getItem().getDescriptionId());
    }
}
