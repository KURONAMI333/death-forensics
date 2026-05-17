package com.kuronami.deathforensics;

import com.kuronami.deathforensics.command.DeathForensicsCommand;
import com.kuronami.deathforensics.death.DeathListener;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Death Forensics — entry point (Fabric 1.21.1).
 *
 * <p>The vanilla death message says "you blew up". This says <em>what</em>
 * blew you up, exactly where, from which direction and how far, with how
 * many hostiles around, on what day — so you can avoid it next time.
 *
 * <p>Three Fabric API hooks, nothing else: {@code AFTER_DEATH} (the
 * forensic record + auto-posted report), {@code CommandRegistrationCallback}
 * (the {@code /howdididie} command), {@code SERVER_STOPPING} (clear the
 * in-memory record). No mixin, no config, no registered game object, no
 * passive tick task.
 */
public class DeathForensicsFabric implements ModInitializer {

    public static final String MOD_ID = "deathforensics";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Death Forensics ready — run /howdididie after a death.");

        ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
            if (entity instanceof ServerPlayer player) {
                DeathListener.record(player, source);
            }
        });

        CommandRegistrationCallback.EVENT.register(
            (dispatcher, registryAccess, environment) ->
                DeathForensicsCommand.register(dispatcher));

        ServerLifecycleEvents.SERVER_STOPPING.register(
            server -> DeathListener.clear());
    }
}
