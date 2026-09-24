# Character Foundation v0.1

## Implemented

The first implementation separates the character into three persisted groups:

- `Attributes`: STR, DEX, END, INT, WIL, PER.
- `Potential`: soft development limits for each attribute.
- `Resources`: current/max Health, Stamina, Mana and Focus.

Derived statistics are calculated by `CharacterStats` and are not persisted as independent source-of-truth values.

## Persistence

`CharacterData` is registered as the `the_unwritten:character_data` NeoForge entity attachment. The attachment is serialized and configured with `copyOnDeath`, so character foundation data survives normal player death/respawn.

## Current formulas

```text
MaxHealth = 100 + END * 9 + STR * 2
MaxStamina = 100 + END * 5 + DEX * 2
MaxMana = 100 + WIL * 5 + INT * 3 + MagicalProgression
MaxFocus = 100 + WIL * 4 + INT * 2
PhysicalPower = STR + END * 0.25 + Equipment + Technique
MovementMultiplier = 1 + DEX / 200
Accuracy = DEX * 0.6 + PER * 0.4
Control = DEX * 0.35 + WIL * 0.35 + INT * 0.30
```

`MagicalProgression`, equipment and technique contributions are intentionally supplied as external inputs to the calculation rather than stored in the foundation.

## Not implemented yet

Skills, Knowledge/Proficiency/Mastery, Affinity, Schools, Techniques, Hybrid Interactions, Traits and Progression History remain separate systems for later stages.

## Equipment attribute foundation

The first equipment integration layer defines `AttributeModifier` as a named
change to one fundamental attribute. A modifier contains:

- a stable identifier;
- the target attribute;
- the numeric amount;
- an operation (`ADDITION`, `MULTIPLY_BASE`, or `MULTIPLY_TOTAL`);
- the source identifier.

`ItemAttributeModifiers` is persisted as an item-stack Data Component. It
only describes what an item can contribute. It does not activate modifiers;
the equipment lifecycle will activate and deactivate them when an item enters
or leaves an applicable equipment slot.

This keeps item data, equipment state, attribute calculation and derived stats
as separate responsibilities.
