package com.theunwritten.item.registry;

import java.util.function.Supplier;

import com.theunwritten.TheUnwritten;
import com.theunwritten.item.data.ItemAttributeModifiers;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Data component types owned by The Unwritten item system. */
public final class ItemDataComponents {
    private ItemDataComponents() {
    }

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, TheUnwritten.MODID);

    public static final Supplier<DataComponentType<ItemAttributeModifiers>> ATTRIBUTE_MODIFIERS =
            DATA_COMPONENT_TYPES.register("attribute_modifiers", () ->
                    DataComponentType.<ItemAttributeModifiers>builder()
                            .persistent(ItemAttributeModifiers.CODEC)
                            .build());
}
