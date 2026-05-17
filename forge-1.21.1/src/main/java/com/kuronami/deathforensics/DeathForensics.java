package com.kuronami.deathforensics;

import com.kuronami.deathforensics.command.DeathForensicsCommand;
import com.kuronami.deathforensics.death.DeathListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Death Forensics — entry point (Forge 1.21.1).
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

    public DeathForensics(FMLJavaModLoadingContext context) {
        LOGGER.info("Death Forensics ready — run /howdididie after a death.");
        MinecraftForge.EVENT_BUS.register(new DeathListener());
        MinecraftForge.EVENT_BUS.register(new DeathForensicsCommand());
    }
}
