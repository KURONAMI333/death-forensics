package com.kuronami.deathforensics.death;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;

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
 *
 * <p>Fabric variant: static helpers driven by the {@code AFTER_DEATH}
 * and {@code SERVER_STOPPING} hooks wired in {@code DeathForensicsFabric}.
 */
public final class DeathListener {

    private static final int MAX_TRACKED_PLAYERS = 2000;

    private static final Map<UUID, DeathRecord> LAST_DEATHS =
        Collections.synchronizedMap(new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<UUID, DeathRecord> eldest) {
                return size() > MAX_TRACKED_PLAYERS;
            }
        });

    private DeathListener() {
    }

    public static DeathRecord lastDeath(UUID uuid) {
        return LAST_DEATHS.get(uuid);
    }

    public static void clear() {
        LAST_DEATHS.clear();
    }

    public static void record(ServerPlayer player, DamageSource source) {
        DeathRecord r = DeathAnalyzer.analyze(player, source);
        LAST_DEATHS.put(player.getUUID(), r);

        // Auto-post the full styled report. It lands in chat and is
        // there when the death screen closes / they respawn.
        for (Component line : DeathReport.render(r)) {
            player.sendSystemMessage(line);
        }
    }
}
