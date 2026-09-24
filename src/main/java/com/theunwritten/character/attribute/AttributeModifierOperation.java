package com.theunwritten.character.attribute;

/**
 * Defines how an attribute modifier is applied to an attribute value.
 *
 * ADDITION is the only operation used by the first equipment prototype. The
 * other operations are reserved so the modifier model does not have to be
 * redesigned when percentage-based effects are introduced.
 */
public enum AttributeModifierOperation {
    ADDITION,
    MULTIPLY_BASE,
    MULTIPLY_TOTAL
}
