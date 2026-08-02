package com.kuronami.deathforensics.death;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
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
 *
 * <p>{@code /howdididie} is documented (README / STORE_BODY) to re-show a
 * death "at any time", i.e. across logout/login within the same server
 * run — so entries are <em>not</em> evicted on logout. Instead the map is
 * capped at {@link #MAX_TRACKED_PLAYERS} and evicts the
 * least-recently-looked-up player once full, bounding memory on
 * long-running servers without breaking that contract for anyone who
 * died recently.
 */
public class DeathListener {

    private static final int MAX_TRACKED_PLAYERS = 2000;

    private static final Map<UUID, DeathRecord> LAST_DEATHS =
        Collections.synchronizedMap(new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<UUID, DeathRecord> eldest) {
                return size() > MAX_TRACKED_PLAYERS;
            }
        });

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
