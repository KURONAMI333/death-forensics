package com.kuronami.deathforensics.death;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

/**
 * On death: build the forensic record, store it (so {@code /howdididie}
 * can re-show it or an op can look it up), and <em>immediately post the
 * full styled report to the player's chat</em>. No command needed to
 * learn what just killed you — the report finds you.
 *
 * <p>v0.1 keeps only the last death per player, in memory, cleared on
 * server stop (the persistent multi-death journal is a separate mod).
 * Read-only against the world.
 */
public class DeathListener {

    private static final Map<UUID, DeathRecord> LAST_DEATHS =
        new ConcurrentHashMap<>();

    public static DeathRecord lastDeath(UUID uuid) {
        return LAST_DEATHS.get(uuid);
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        DeathRecord record = DeathAnalyzer.analyze(player, event.getSource());
        LAST_DEATHS.put(player.getUUID(), record);

        // Auto-post the full styled report. It lands in chat and is
        // there when the death screen closes / they respawn.
        for (Component line : DeathReport.render(record)) {
            player.sendSystemMessage(line);
        }
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        LAST_DEATHS.clear();
    }
}
