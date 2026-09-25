package com.theunwritten.item.registry;

import com.theunwritten.item.KnowledgeBookItem;
import com.theunwritten.knowledge.KnowledgeDiscoveryOffer;
import com.theunwritten.knowledge.KnowledgeIds;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

import static com.theunwritten.TheUnwritten.ITEMS;

/**
 * Concrete knowledge sources used to test book-based discovery.
 */
public final class KnowledgeBooks {
    private KnowledgeBooks() {
    }

    public static void init() {
        // Forces class initialization so the DeferredItems are registered.
    }

    public static final DeferredItem<Item> ELEMENTAL_TREATISE = ITEMS.register(
            "elemental_treatise",
            ignored -> new KnowledgeBookItem(
                    new Item.Properties().stacksTo(1),
                    KnowledgeIds.Magic.ELEMENTAL,
                    new KnowledgeDiscoveryOffer(
                            KnowledgeIds.Magic.ELEMENTAL,
                            java.util.List.of())));
}
