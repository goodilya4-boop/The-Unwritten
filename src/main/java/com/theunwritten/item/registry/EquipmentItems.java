package com.theunwritten.item.registry;

import com.theunwritten.character.attribute.AttributeModifier;
import com.theunwritten.character.attribute.AttributeModifierOperation;
import com.theunwritten.character.data.AttributeType;
import com.theunwritten.item.data.ItemAttributeModifiers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

import static com.theunwritten.TheUnwritten.ITEMS;

/** Minimal concrete equipment content used to exercise the modifier lifecycle. */
public final class EquipmentItems {
    private EquipmentItems() {
    }

    public static void init() {
        // Forces class initialization so the DeferredItem is registered.
    }

    private static final ResourceLocation TRAINING_CHARM_SOURCE =
            ResourceLocation.fromNamespaceAndPath("the_unwritten", "training_charm");

    public static final DeferredItem<Item> TRAINING_CHARM = ITEMS.register(
            "training_charm",
            registryName -> new Item(new Item.Properties()
                    .setId(registryName)
                    .stacksTo(1)
                    .component(
                            ItemDataComponents.ATTRIBUTE_MODIFIERS.get(),
                            new ItemAttributeModifiers(java.util.List.of(
                                    new AttributeModifier(
                                            "training_charm.wil",
                                            AttributeType.WIL,
                                            5.0D,
                                            AttributeModifierOperation.ADDITION,
                                            TRAINING_CHARM_SOURCE))))));
}
