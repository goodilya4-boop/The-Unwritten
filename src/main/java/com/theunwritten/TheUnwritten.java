package com.theunwritten;

import com.mojang.logging.LogUtils;
import com.theunwritten.character.CharacterAttachments;
import com.theunwritten.character.CharacterAttributeCommand;
import com.theunwritten.character.CharacterRuntimeEvents;
import com.theunwritten.knowledge.KnowledgeCommand;
import org.slf4j.Logger;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(TheUnwritten.MODID)
public class TheUnwritten {
    public static final String MODID = "theunwritten";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TheUnwritten(IEventBus modEventBus) {
        CharacterAttachments.ATTACHMENT_TYPES.register(modEventBus);
        NeoForge.EVENT_BUS.addListener(CharacterAttributeCommand::register);
        NeoForge.EVENT_BUS.addListener(KnowledgeCommand::register);
        NeoForge.EVENT_BUS.addListener(CharacterRuntimeEvents::onPlayerTick);
        NeoForge.EVENT_BUS.addListener(CharacterRuntimeEvents::onPlayerLoggedIn);
        NeoForge.EVENT_BUS.addListener(CharacterRuntimeEvents::onPlayerRespawn);

        LOGGER.info("The Unwritten loaded.");
    }
}
