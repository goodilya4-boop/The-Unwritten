package com.theunwritten.knowledge;

import net.minecraft.resources.ResourceLocation;

public final class KnowledgeIds {
    private static final String MOD_ID = "the_unwritten";

    private KnowledgeIds() {}

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static final class Magic {
        public static final ResourceLocation FOUNDATION = id("magic/foundation");
        public static final ResourceLocation ELEMENTAL = id("magic/elemental");
        public static final ResourceLocation LIGHT = id("magic/light");
        public static final ResourceLocation DARKNESS = id("magic/darkness");
        public static final ResourceLocation LIFE = id("magic/life");
        public static final ResourceLocation SPIRIT = id("magic/spirit");
        public static final ResourceLocation SPACE = id("magic/space");
        public static final ResourceLocation TIME = id("magic/time");
        public static final ResourceLocation VOID = id("magic/void");
        private Magic() {}
    }

    public static final class Combat {
        public static final ResourceLocation FOUNDATION = id("combat/foundation");
        public static final ResourceLocation SLASHING = id("combat/slashing");
        public static final ResourceLocation PIERCING = id("combat/piercing");
        public static final ResourceLocation BLUNT = id("combat/blunt");
        public static final ResourceLocation RANGED = id("combat/ranged");
        public static final ResourceLocation UNARMED = id("combat/unarmed");
        public static final ResourceLocation IDEAL_MOVEMENT = id("combat/rare/ideal_movement");
        public static final ResourceLocation DESTRUCTION = id("combat/rare/destruction");
        public static final ResourceLocation INNER_STRENGTH = id("combat/rare/inner_strength");
        public static final ResourceLocation EMPTY_BLADE = id("combat/rare/empty_blade");
        private Combat() {}
    }

    public static final class Engineering {
        public static final ResourceLocation FOUNDATION = id("engineering/foundation");
        public static final ResourceLocation MATERIAL = id("engineering/material");
        public static final ResourceLocation MECHANICAL = id("engineering/mechanical");
        public static final ResourceLocation ALCHEMICAL = id("engineering/alchemical");
        public static final ResourceLocation ENERGY = id("engineering/energy");
        public static final ResourceLocation ARTIFACT = id("engineering/artifact");
        public static final ResourceLocation CONSTRUCTION = id("engineering/construction");
        public static final ResourceLocation ARCANE_ENGINEERING = id("engineering/rare/arcane");
        public static final ResourceLocation LIVING_CONSTRUCTION = id("engineering/rare/living_construction");
        public static final ResourceLocation SPATIAL_ENGINEERING = id("engineering/rare/spatial");
        public static final ResourceLocation AUTONOMOUS_CONSTRUCTION = id("engineering/rare/autonomous_construction");
        private Engineering() {}
    }
}
