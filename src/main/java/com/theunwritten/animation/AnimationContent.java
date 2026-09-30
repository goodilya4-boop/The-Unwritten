package com.theunwritten.animation;

public final class AnimationContent {
    public static final AnimationDefinition IMPERIAL_IDLE =
            AnimationApi.definition("imperial_fencing/idle", 40, true, AnimationLayer.MOVEMENT);

    public static final AnimationDefinition IMPERIAL_STANCE =
            AnimationApi.definition("imperial_fencing/stance", 30, true, AnimationLayer.COMBAT, 10, true);

    public static final AnimationDefinition IMPERIAL_LIGHT_ATTACK =
            AnimationApi.definition("imperial_fencing/light_attack", 12, false, AnimationLayer.ACTION, 50, false);

    public static final AnimationDefinition IMPERIAL_BLOCK =
            AnimationApi.definition("imperial_fencing/block", 10, true, AnimationLayer.COMBAT, 40, true);

    public static final AnimationDefinition IMPERIAL_PARRY =
            AnimationApi.definition("imperial_fencing/parry", 8, false, AnimationLayer.ACTION, 60, false);

    public static final AnimationSet IMPERIAL_FENCING_SET = createSet();

    private AnimationContent() {
    }

    private static AnimationSet createSet() {
        AnimationSet set = AnimationApi.set("imperial_fencing");

        set.put("idle", IMPERIAL_IDLE.id());
        set.put("stance", IMPERIAL_STANCE.id());
        set.put("light_attack", IMPERIAL_LIGHT_ATTACK.id());
        set.put("block", IMPERIAL_BLOCK.id());
        set.put("parry", IMPERIAL_PARRY.id());

        return set;
    }
}
