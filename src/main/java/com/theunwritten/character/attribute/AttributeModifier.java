package com.theunwritten.character.attribute;

import java.util.Objects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.theunwritten.character.data.AttributeType;

import net.minecraft.resources.ResourceLocation;

/**
 * A single, named modification to a character attribute.
 *
 * The identifier is stable within the source that owns the modifier.
 */
public record AttributeModifier(
        String id,
        AttributeType attribute,
        double amount,
        AttributeModifierOperation operation,
        ResourceLocation source) {

    public static final Codec<AttributeModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("id").forGetter(AttributeModifier::id),
            Codec.STRING.xmap(AttributeType::valueOf, Enum::name)
                    .fieldOf("attribute").forGetter(AttributeModifier::attribute),
            Codec.DOUBLE.fieldOf("amount").forGetter(AttributeModifier::amount),
            Codec.STRING.xmap(AttributeModifierOperation::valueOf, Enum::name)
                    .fieldOf("operation").forGetter(AttributeModifier::operation),
            ResourceLocation.CODEC.fieldOf("source").forGetter(AttributeModifier::source)
    ).apply(instance, AttributeModifier::new));

    public AttributeModifier {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(attribute, "attribute");
        Objects.requireNonNull(operation, "operation");
        Objects.requireNonNull(source, "source");

        if (id.isBlank()) {
            throw new IllegalArgumentException("Modifier id must not be blank");
        }
        if (!Double.isFinite(amount)) {
            throw new IllegalArgumentException("Modifier amount must be finite");
        }
    }

    public static AttributeModifier addition(
            String id,
            AttributeType attribute,
            double amount,
            ResourceLocation source) {
        return new AttributeModifier(id, attribute, amount, AttributeModifierOperation.ADDITION, source);
    }

    public AttributeModifier withSource(ResourceLocation newSource) {
        return new AttributeModifier(id, attribute, amount, operation, newSource);
    }
}
