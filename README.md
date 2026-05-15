# Death Forensics

> The vanilla death message says "you blew up." This says **what** blew you up, **where**, from **which direction**, with how many hostiles around — automatically, the moment you die.

## What it does

When you die, Death Forensics posts a short, color-coded report to chat — no command needed:

```
Death Forensics — your last death:
 ┃ Cause: Slain by Zombie
 ┃ Died at (210, 64, -88) in overworld, on day 12.
 ┃ Zombie was about 8 blocks to the Northeast.
 ┃ 14 hostile mob(s) were within 16 blocks at the time.
```

- **Cause** reuses Minecraft's own death message, so it shows in each player's own language automatically.
- Coordinates, dimension, in-world day, the responsible entity's distance + 8-wind direction, and the nearby hostile count — everything is straight from vanilla data. It never guesses.

`/howdididie` (alias `/deathreport`) re-shows your last death any time. `/howdididie <player>` is op-only.

## Why

"I died and have no idea why / what hit me" is one of the most common Minecraft frustrations — the vanilla message is too vague to learn from. Death Forensics turns it into something you can act on.

## Install

Drop `deathforensics-<version>.jar` into `mods/`. Server-side (clients don't need it). No dependencies.

- Minecraft 1.21.1 · NeoForge · JDK 21

## Scope

Read-only and on-demand: it only listens to vanilla `LivingDeathEvent` and reports. No mixin, no config, no blocks/items, no passive tick task. Output localized into 9 languages (machine-baseline; native-speaker PRs welcome).

## License

MIT — modpack inclusion welcome, no credit required.

Author: KURONAMI
