package com.theunwritten.character.equipment;

import com.theunwritten.TheUnwritten;
import com.theunwritten.character.api.CharacterAPI;
import com.theunwritten.character.data.CharacterData;
import com.theunwritten.character.attribute.AttributeModifier;
import com.theunwritten.item.data.ItemAttributeModifiers;
import com.theunwritten.item.registry.ItemDataComponents;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;

/**
 * Reconciles item-declared attribute modifiers with the character's runtime
 * attribute state.
 *
 * Equipment is authoritative. Modifiers are removed and rebuilt for the
 * affected slot whenever NeoForge reports an equipment change. The same
 * mechanism also handles initial equipment state and entity cloning because
 * LivingEquipmentChangeEvent covers those lifecycle transitions.
 */
@EventBusSubscriber(modid = TheUnwritten.MODID)
public final class EquipmentAttributeLifecycle {
    private EquipmentAttributeLifecycle() {
    }

    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        reconcileSlot(event.getEntity(), event.getSlot(), event.getTo());
    }

    public static void reconcileSlot(LivingEntity entity, EquipmentSlot slot) {
        reconcileSlot(entity, slot, entity.getItemBySlot(slot));
    }

    private static void reconcileSlot(LivingEntity entity, EquipmentSlot slot, ItemStack stack) {
        if (entity.level().isClientSide()) {
            return;
        }

        CharacterData character = CharacterAPI.getData(entity);
        ResourceLocation source = sourceFor(slot);

        ItemAttributeModifiers declared =
                stack.getOrDefault(ItemDataComponents.ATTRIBUTE_MODIFIERS.get(), ItemAttributeModifiers.empty());

        applyDeclaredModifiers(character, declared, source);
        character.refreshResourceMaximums();
    }

    static void applyDeclaredModifiers(
            CharacterData character,
            ItemAttributeModifiers declared,
            ResourceLocation source) {
        character.attributeAccess().removeModifiersFromSource(source);

        for (AttributeModifier modifier : declared.modifiers()) {
            character.attributeAccess().addModifier(modifier.withSource(source));
        }
    }

    static ResourceLocation sourceFor(EquipmentSlot slot) {
        return ResourceLocation.fromNamespaceAndPath(
                TheUnwritten.MODID,
                "equipment/" + slot.getName());
    }
}
