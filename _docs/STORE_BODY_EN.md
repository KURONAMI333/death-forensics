# Death Forensics

> The vanilla death message says "you blew up." This says **what** blew you up, **where**, from **which direction**, with how many hostiles around — automatically, the instant you die.

"I died and have no idea what hit me" is one of the most common Minecraft frustrations — admins on r/admincraft chasing mystery deaths, players on r/feedthebeast killed by something off-screen with nothing to learn from. The vanilla message is too vague to act on. Death Forensics turns each death into a short, plain-language post-mortem you can actually use.

- 🔎 **Auto-posted on death** — no command needed; a color-coded report lands in chat
- 🧭 Exact coords + dimension + in-world day, the killer's type, distance and 8-wind direction
- 🌐 Cause text reuses Minecraft's own death message, so it shows in **each player's own language** automatically
- ✅ Only certain vanilla facts — it never guesses

## What it does / Usage

When you die, this is posted to chat automatically:

```
Death Forensics — your last death:
 ┃ Cause: Slain by Zombie
 ┃ Died at (210, 64, -88) in overworld, on day 12.
 ┃ Zombie was about 8 blocks to the Northeast.
 ┃ 14 hostile mob(s) were within 16 blocks at the time.
```

`/howdididie` (alias `/deathreport`) re-shows your last death any time. `/howdididie <player>` is op-only.

## Supported loaders / versions

| Minecraft | NeoForge | Forge | Fabric |
|---|:---:|:---:|:---:|
| 1.21.1 | ✅ | planned | planned |

Forge / Fabric / 1.20.1 ports are planned; this release is NeoForge 1.21.1.

## Dependencies

None.

## Compatibility & scope

Server-side, read-only: it only listens to vanilla `LivingDeathEvent` and reports. No mixin, no registered blocks/items, no passive tick task — it cannot conflict with other mods. Works with any mod's damage sources (cause text comes from vanilla's own death message).

## Known limitations

v0.1 keeps only your **last** death in memory (cleared on server stop). The persistent multi-death history is a separate mod (Death Log) by design, so the two don't overlap.

## Install

1. Install NeoForge for Minecraft 1.21.1.
2. Drop `deathforensics-0.1.0.jar` into `mods/`. Server-side (clients don't need it).

- Minecraft 1.21.1 · NeoForge · JDK 21

## Languages

Output localized in 9 languages (machine-baseline; native-speaker PRs welcome).

## License

MIT — modpack inclusion welcome, no credit required.

Author: KURONAMI
