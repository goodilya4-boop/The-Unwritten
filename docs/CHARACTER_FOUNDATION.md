# Character Foundation v0.2

## Implemented

The character foundation separates persistent state, runtime attribute access and derived statistics:

- Attributes: STR, DEX, END, INT, WIL, PER.
- Potential: soft development limits for each attribute.
- Resources: current/max Health, Stamina, Mana and Focus.
- AttributeAccess: effective attribute values including runtime modifiers.
- CharacterStats: derived values calculated from AttributeAccess.

## Persistence

CharacterData is registered as the the_unwritten:character_data NeoForge entity attachment. The attachment is serialized and configured with copyOnDeath.

Runtime attribute modifiers are not persisted independently. Equipment is authoritative for equipment-provided modifiers and rebuilds them when NeoForge reports an equipment lifecycle change.

## Current formulas

MaxHealth = 100 + END * 9 + STR * 2
MaxStamina = 100 + END * 5 + DEX * 2
MaxMana = 100 + WIL * 5 + INT * 3 + MagicalProgression
MaxFocus = 100 + WIL * 4 + INT * 2
PhysicalPower = STR + END * 0.25 + Equipment + Technique
MovementMultiplier = 1 + DEX / 200
Accuracy = DEX * 0.6 + PER * 0.4
Control = DEX * 0.35 + WIL * 0.35 + INT * 0.30

## Attribute modifier model

AttributeModifier is the only modifier model. A modifier contains a stable identifier, target attribute, amount, operation and source.

The runtime access layer treats (source, id) as the modifier identity. Adding the same identity again replaces the previous modifier.

## Equipment lifecycle

ItemAttributeModifiers is persisted as an item-stack Data Component. It only describes what an item can contribute.

EquipmentAttributeLifecycle activates that declaration when an item is reported as equipped and removes the previous declaration when the slot changes.

Each equipment slot owns a dedicated runtime source:

the_unwritten:equipment/<slot>

This makes replacement, unequipping, login/load and player cloning converge on the same idempotent reconciliation operation.

## Not implemented yet

Skills, Knowledge/Proficiency/Mastery, Affinity, Schools, Techniques, Hybrid Interactions, Traits and Progression History remain separate systems for later stages.
