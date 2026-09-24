package com.theunwritten.character.registry;

import java.util.function.Supplier;

import com.theunwritten.TheUnwritten;
import com.theunwritten.character.data.CharacterData;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

/** Registry for persistent character data attachments. */
public final class CharacterAttachments {
    private CharacterAttachments() {
    }

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, TheUnwritten.MODID);

    public static final Supplier<AttachmentType<CharacterData>> CHARACTER_DATA = ATTACHMENT_TYPES.register(
            "character_data",
            () -> AttachmentType.serializable(CharacterData::new)
                    .copyOnDeath()
                    .build());
}
