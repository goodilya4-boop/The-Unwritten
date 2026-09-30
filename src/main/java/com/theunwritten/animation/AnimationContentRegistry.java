package com.theunwritten.animation;

public final class AnimationContentRegistry {
    public static final AnimationRegistry REGISTRY = create();

    private AnimationContentRegistry() {
    }

    private static AnimationRegistry create() {
        AnimationRegistry registry = new AnimationRegistry();
        registry.register(AnimationContent.IMPERIAL_IDLE);
        registry.register(AnimationContent.IMPERIAL_STANCE);
        registry.register(AnimationContent.IMPERIAL_LIGHT_ATTACK);
        registry.register(AnimationContent.IMPERIAL_BLOCK);
        registry.register(AnimationContent.IMPERIAL_PARRY);
        return registry;
    }
}
