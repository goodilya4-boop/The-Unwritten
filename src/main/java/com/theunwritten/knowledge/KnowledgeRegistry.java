package com.theunwritten.knowledge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import net.minecraft.resources.ResourceLocation;

/**
 * Registry of knowledge definitions that exist in the game world.
 *
 * <p>This registry describes available knowledge. It does not grant any
 * knowledge to a character.</p>
 */
public final class KnowledgeRegistry {
    private final Map<ResourceLocation, KnowledgeDefinition> definitions = new LinkedHashMap<>();

    public KnowledgeDefinition register(KnowledgeDefinition definition) {
        if (definition == null) {
            throw new IllegalArgumentException("Knowledge definition cannot be null");
        }
        if (definitions.containsKey(definition.id())) {
            throw new IllegalArgumentException(
                    "Knowledge is already registered: " + definition.id());
        }
        if (definition.parent() != null && !definitions.containsKey(definition.parent())) {
            throw new IllegalArgumentException(
                    "Knowledge parent is not registered: " + definition.parent());
        }

        definitions.put(definition.id(), definition);
        return definition;
    }

    public Optional<KnowledgeDefinition> get(ResourceLocation id) {
        return Optional.ofNullable(definitions.get(id));
    }

    public boolean contains(ResourceLocation id) {
        return definitions.containsKey(id);
    }

    public int size() {
        return definitions.size();
    }

    public List<KnowledgeDefinition> all() {
        return Collections.unmodifiableList(new ArrayList<>(definitions.values()));
    }

    public List<KnowledgeDefinition> byDomain(KnowledgeDomain domain) {
        return definitions.values().stream()
                .filter(definition -> definition.domain() == domain)
                .toList();
    }

    public List<KnowledgeDefinition> byTier(KnowledgeTier tier) {
        return definitions.values().stream()
                .filter(definition -> definition.tier() == tier)
                .toList();
    }

    public static KnowledgeRegistry createDefault() {
        KnowledgeRegistry registry = new KnowledgeRegistry();

        registerFoundation(registry);
        registerMagic(registry);
        registerCombat(registry);
        registerEngineering(registry);

        return registry;
    }

    private static void registerFoundation(KnowledgeRegistry registry) {
        registry.register(new KnowledgeDefinition(
                KnowledgeIds.Magic.FOUNDATION, KnowledgeDomain.MAGIC,
                KnowledgeTier.FOUNDATION, null, false, false, false));
        registry.register(new KnowledgeDefinition(
                KnowledgeIds.Combat.FOUNDATION, KnowledgeDomain.COMBAT,
                KnowledgeTier.FOUNDATION, null, false, false, false));
        registry.register(new KnowledgeDefinition(
                KnowledgeIds.Engineering.MATERIAL, KnowledgeDomain.ENGINEERING,
                KnowledgeTier.SCHOOL, null, false, false, false));
    }

    private static void registerMagic(KnowledgeRegistry registry) {
        ResourceLocation parent = KnowledgeIds.Magic.FOUNDATION;
        registry.register(new KnowledgeDefinition(
                KnowledgeIds.Magic.ELEMENTAL, KnowledgeDomain.MAGIC,
                KnowledgeTier.SCHOOL, parent, false, false, false));
        registry.register(new KnowledgeDefinition(
                KnowledgeIds.Magic.LIGHT, KnowledgeDomain.MAGIC,
                KnowledgeTier.SCHOOL, parent, false, false, false));
        registry.register(new KnowledgeDefinition(
                KnowledgeIds.Magic.DARKNESS, KnowledgeDomain.MAGIC,
                KnowledgeTier.SCHOOL, parent, false, false, false));
        registry.register(new KnowledgeDefinition(
                KnowledgeIds.Magic.LIFE, KnowledgeDomain.MAGIC,
                KnowledgeTier.SCHOOL, parent, false, false, false));
        registry.register(new KnowledgeDefinition(
                KnowledgeIds.Magic.SPIRIT, KnowledgeDomain.MAGIC,
                KnowledgeTier.SCHOOL, parent, false, false, false));
        registry.register(new KnowledgeDefinition(
                KnowledgeIds.Magic.SPACE, KnowledgeDomain.MAGIC,
                KnowledgeTier.SCHOOL, parent, false, false, false));
        registry.register(new KnowledgeDefinition(
                KnowledgeIds.Magic.TIME, KnowledgeDomain.MAGIC,
                KnowledgeTier.SCHOOL, parent, true, false, true));
        registry.register(new KnowledgeDefinition(
                KnowledgeIds.Magic.VOID, KnowledgeDomain.MAGIC,
                KnowledgeTier.SCHOOL, parent, true, false, true));
    }

    private static void registerCombat(KnowledgeRegistry registry) {
        ResourceLocation parent = KnowledgeIds.Combat.FOUNDATION;
        registry.register(new KnowledgeDefinition(
                KnowledgeIds.Combat.SLASHING, KnowledgeDomain.COMBAT,
                KnowledgeTier.SCHOOL, parent, false, false, false));
        registry.register(new KnowledgeDefinition(
                KnowledgeIds.Combat.PIERCING, KnowledgeDomain.COMBAT,
                KnowledgeTier.SCHOOL, parent, false, false, false));
        registry.register(new KnowledgeDefinition(
                KnowledgeIds.Combat.BLUNT, KnowledgeDomain.COMBAT,
                KnowledgeTier.SCHOOL, parent, false, false, false));
        registry.register(new KnowledgeDefinition(
                KnowledgeIds.Combat.RANGED, KnowledgeDomain.COMBAT,
                KnowledgeTier.SCHOOL, parent, false, false, false));
        registry.register(new KnowledgeDefinition(
                KnowledgeIds.Combat.UNARMED, KnowledgeDomain.COMBAT,
                KnowledgeTier.SCHOOL, parent, false, false, false));
        registry.register(new KnowledgeDefinition(
                KnowledgeIds.Combat.IDEAL_MOVEMENT, KnowledgeDomain.COMBAT,
                KnowledgeTier.SCHOOL, parent, true, false, true));
        registry.register(new KnowledgeDefinition(
                KnowledgeIds.Combat.DESTRUCTION, KnowledgeDomain.COMBAT,
                KnowledgeTier.SCHOOL, parent, true, false, true));
        registry.register(new KnowledgeDefinition(
                KnowledgeIds.Combat.INNER_STRENGTH, KnowledgeDomain.COMBAT,
                KnowledgeTier.SCHOOL, parent, true, false, true));
        registry.register(new KnowledgeDefinition(
                KnowledgeIds.Combat.EMPTY_BLADE, KnowledgeDomain.COMBAT,
                KnowledgeTier.SCHOOL, parent, true, false, true));
    }

    private static void registerEngineering(KnowledgeRegistry registry) {
        ResourceLocation parent = KnowledgeIds.Engineering.MATERIAL;
        registry.register(new KnowledgeDefinition(
                KnowledgeIds.Engineering.MECHANICAL, KnowledgeDomain.ENGINEERING,
                KnowledgeTier.SCHOOL, parent, false, false, false));
        registry.register(new KnowledgeDefinition(
                KnowledgeIds.Engineering.ALCHEMICAL, KnowledgeDomain.ENGINEERING,
                KnowledgeTier.SCHOOL, parent, false, false, false));
        registry.register(new KnowledgeDefinition(
                KnowledgeIds.Engineering.ENERGY, KnowledgeDomain.ENGINEERING,
                KnowledgeTier.SCHOOL, parent, false, false, false));
        registry.register(new KnowledgeDefinition(
                KnowledgeIds.Engineering.ARTIFACT, KnowledgeDomain.ENGINEERING,
                KnowledgeTier.SCHOOL, parent, false, false, false));
        registry.register(new KnowledgeDefinition(
                KnowledgeIds.Engineering.CONSTRUCTION, KnowledgeDomain.ENGINEERING,
                KnowledgeTier.SCHOOL, parent, false, false, false));
        registry.register(new KnowledgeDefinition(
                KnowledgeIds.Engineering.ARCANE_ENGINEERING, KnowledgeDomain.ENGINEERING,
                KnowledgeTier.SCHOOL, parent, true, false, true));
        registry.register(new KnowledgeDefinition(
                KnowledgeIds.Engineering.LIVING_CONSTRUCTION, KnowledgeDomain.ENGINEERING,
                KnowledgeTier.SCHOOL, parent, true, false, true));
        registry.register(new KnowledgeDefinition(
                KnowledgeIds.Engineering.SPATIAL_ENGINEERING, KnowledgeDomain.ENGINEERING,
                KnowledgeTier.SCHOOL, parent, true, false, true));
        registry.register(new KnowledgeDefinition(
                KnowledgeIds.Engineering.AUTONOMOUS_CONSTRUCTION, KnowledgeDomain.ENGINEERING,
                KnowledgeTier.SCHOOL, parent, true, false, true));
    }
}
