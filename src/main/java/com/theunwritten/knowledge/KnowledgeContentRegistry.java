package com.theunwritten.knowledge;

public final class KnowledgeContentRegistry {
    public static final KnowledgeRegistry REGISTRY = create();

    private KnowledgeContentRegistry() {
    }

    private static KnowledgeRegistry create() {
        KnowledgeRegistry registry = new KnowledgeRegistry();
        registry.register(KnowledgeContent.IMPERIAL_FENCING_NODE);
        registry.validate();
        return registry;
    }
}
