package com.theunwritten;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(TheUnwritten.MODID)
public class TheUnwritten {
    public static final String MODID = "theunwritten";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TheUnwritten(IEventBus modEventBus) {
        LOGGER.info("The Unwritten loaded.");
    }
}