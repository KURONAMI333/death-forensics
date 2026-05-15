package com.kuronami.deathforensics;

import com.kuronami.deathforensics.command.DeathForensicsCommand;
import com.kuronami.deathforensics.death.DeathListener;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Death Forensics — entry point.
 *
 * <p>The vanilla death message says "you blew up". This says <em>what</em>
 * blew you up, exactly where, from which direction and how far, with how
 * many hostiles around, on what day — so you can avoid it next time.
 *
 * <p>Two registrations, nothing else: a {@link DeathListener} (vanilla
 * {@code LivingDeathEvent}) and the {@code /howdididie} command. No
 * mixin, no config, no registered game object, no passive tick task —
 * it does nothing until someone dies and then asks.
 */
@Mod(DeathForensics.MOD_ID)
public class DeathForensics {

    public static final String MOD_ID = "deathforensics";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public DeathForensics(IEventBus modBus, ModContainer container) {
        LOGGER.info("Death Forensics ready — run /howdididie after a death.");
        NeoForge.EVENT_BUS.register(new DeathListener());
        NeoForge.EVENT_BUS.register(new DeathForensicsCommand());
    }
}
