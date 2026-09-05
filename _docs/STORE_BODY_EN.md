The vanilla death message says "you blew up." This says what blew you up, where, from which direction, and how many hostiles were nearby — automatically, the instant you die.

When you die, a short post-mortem is posted to your chat: the cause (it reuses Minecraft's own death message, so it shows in each player's language), the exact coordinates, dimension and in-world day, the killer's type with distance and 8-wind direction, and how many hostile mobs were within 16 blocks at the time.

```
Death Forensics — your last death:
 ┃ Cause: Slain by Zombie
 ┃ Died at (210, 64, -88) in overworld, on day 12.
 ┃ Zombie was about 8 blocks to the Northeast.
 ┃ 14 hostile mob(s) were within 16 blocks at the time.
```

`/howdididie` (alias `/deathreport`) re-shows your last death at any time. `/howdididie <player>` is op-only and resolves players who are currently online.

It only reports facts Minecraft already knows — it never guesses. It listens to the vanilla death event and writes nothing to the world, so it can't conflict with other mods or damage sources. It keeps your last death in memory (cleared on server stop); for a persistent multi-death history, see the sibling mod Death Log. Output is localized in 9 languages.

Bugs and questions: comment on the CurseForge page, or DM @kuronami333 on X.

All Rights Reserved. Modpack inclusion is allowed without permission or credit. Source: https://github.com/KURONAMI333/death-forensics
