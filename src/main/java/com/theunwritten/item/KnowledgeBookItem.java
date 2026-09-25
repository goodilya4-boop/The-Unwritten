package com.theunwritten.item;

import com.theunwritten.character.data.CharacterData;
import com.theunwritten.character.registry.CharacterAttachments;
import com.theunwritten.knowledge.KnowledgeDiscoveryContext;
import com.theunwritten.knowledge.KnowledgeDiscoveryOffer;
import com.theunwritten.knowledge.KnowledgeDiscoveryResult;
import com.theunwritten.knowledge.KnowledgeDiscoveryService;
import com.theunwritten.knowledge.KnowledgeDiscoverySource;
import com.theunwritten.knowledge.KnowledgeRegistry;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * A reusable written source of knowledge.
 *
 * <p>Reading the book attempts to discover its associated knowledge. Discovery
 * is intentionally separate from studying, so a book can reveal a subject
 * without immediately teaching it.</p>
 */
public class KnowledgeBookItem extends Item {
    private final ResourceLocation knowledgeId;
    private final KnowledgeDiscoveryOffer offer;

    public KnowledgeBookItem(
            Properties properties,
            ResourceLocation knowledgeId,
            KnowledgeDiscoveryOffer offer) {
        super(properties);
        if (knowledgeId == null) {
            throw new IllegalArgumentException("Knowledge id cannot be null");
        }
        if (offer == null) {
            throw new IllegalArgumentException("Knowledge discovery offer cannot be null");
        }
        if (!knowledgeId.equals(offer.knowledgeId())) {
            throw new IllegalArgumentException(
                    "Knowledge book id and offer knowledge ids must match");
        }
        this.knowledgeId = knowledgeId;
        this.offer = offer;
    }

    public ResourceLocation knowledgeId() {
        return knowledgeId;
    }

    public KnowledgeDiscoveryOffer offer() {
        return offer;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            CharacterData characterData =
                    player.getData(CharacterAttachments.CHARACTER_DATA);
            KnowledgeDiscoveryService discoveryService =
                    new KnowledgeDiscoveryService(KnowledgeRegistry.createDefault());

            KnowledgeDiscoveryResult result = discoveryService.discover(
                    new KnowledgeDiscoveryContext(
                            characterData.knowledge(),
                            knowledgeId,
                            KnowledgeDiscoverySource.BOOK),
                    offer);

            sendResultMessage(player, result);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    private void sendResultMessage(Player player, KnowledgeDiscoveryResult result) {
        Component message = switch (result) {
            case SUCCESS -> Component.translatable(
                    "message.the_unwritten.knowledge_book.discovered",
                    knowledgeId);
            case ALREADY_KNOWN -> Component.translatable(
                    "message.the_unwritten.knowledge_book.already_known",
                    knowledgeId);
            case REQUIREMENTS_NOT_MET -> Component.translatable(
                    "message.the_unwritten.knowledge_book.requirements_not_met");
            case UNKNOWN_KNOWLEDGE -> Component.translatable(
                    "message.the_unwritten.knowledge_book.unknown");
        };

        player.sendSystemMessage(message);
    }
}
