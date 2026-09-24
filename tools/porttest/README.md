# porttest — automated in-game smoke test for the NeoForge 1.21.1 port

A KubeJS server script that exercises the mod's gameplay systems inside a real singleplayer world and logs
`[PORTTEST] PASS/FAIL` lines. It needs no mouse or keyboard: the game is started straight into the test world
and the script drives everything through commands and the game's Java API.

## Running it

1. Create (once) a creative singleplayer world called `PortTest` in the dev client (`gradlew runClient`).
2. Copy `porttest.js` to `run/kubejs/server_scripts/porttest.js` (KubeJS is on the dev classpath).
3. `gradlew runClientAuto` (the `clientAuto` run configuration passes `--quickPlaySingleplayer PortTest`;
   `-PquickPlayWorld=<name>` picks another world).
4. Read `run/logs/kubejs/server.log`; the run ends with `[PORTTEST] DONE <pass>/<total>` after roughly two minutes.
   Check `run/logs/latest.log` for exceptions as well.

`FOCUS = true` at the top of the script runs only the curio / nihility / natural-crypt sections for quick iteration.

## What it checks (last full run 2026-09-24: 56/56)

| Area | Check |
|---|---|
| Kiln | items inserted through the container API, block lights up, `void_stone` → `void_stone_brick` after 100 ticks (vanilla furnace as control) |
| Dispenser | `nature_arrow` dispensed as a `valoria:nature_arrow` projectile |
| Music disc | `jukebox_playable` component present, jukebox starts the song (`ticks_since_song_started` advances) |
| Enchantments | `/enchant` applies bleeding (swords), explosive_flame (`#valoria:enchantable/blaze`), accuracy (`#valoria:enchantable/accuracy`); explosive_flame refused with Fire Aspect (exclusive set) and on unsupported items |
| Curios | `iron_necklace_ruby` +1 max health applied and removed through the right-click path (`setStackInSlot`) and the API path (`setEquippedCurio`); `iron_necklace_health` +5 %; eye necklace state-dependent modifiers with no leak |
| Paintings | a Valoria painting variant summons |
| Portal | a 5×5 inward-facing frame ring fills with portal blocks, a creative player travels to `valoria:the_valoria`, a return portal is generated, the trip back lands in the Overworld |
| Nihility | 60 % nihility damages a survival player periodically without killing |
| Entities | all 22 living entity types exist 40 ticks after `/summon` (re-tested alone if missing from the shared box) |
| Bosses | Necromancer, Dryador, Firron, Wicked Crystal each fight a survival player for 15 s, then die to a player-credited kill and drop their treasure bag |
| Structures | `/place structure valoria:crypt` produces thousands of Valoria blocks and loot containers; a naturally generated crypt start is resolved and standing inside it unlocks the `valoria:crypt` codex page through the dungeon-visit handler |
| Stone crusher | `CrusherBlock.interact` stores an amber gem, a pickaxe (`#valoria:stone_crusher_tool`) crushes it and drops the `gem_crashing` loot; using it unlocks the `valoria:crushables` codex page |
| Bleeding | a Bleeding III sword applies the `valoria:bleeding` effect within 40 `Player.attack` hits |
| Data maps | `neoforge:strippables` maps `shade_log` → `stripped_shade_log` |
| Item codex unlock | holding a portal frame shard unlocks `valoria:valoria_portal` via the inventory scan |
| Max nihility | 97 % nihility kills a survival player (default `maxNihilityAction = KILL`); with `TELEPORT` in `config/valoria/server.toml` the check expects a teleport to the respawn point and a nihility reset instead; the harness revives the player afterwards |
| Stations | jewelry table (empty_gazer + amber_gem → amber_golden_gazer), keg (sugar_cane + bottle → coke_bottle), soul infuser (charged soul collector fills a void_crystal), all through the block entities' item handlers |
| KubeJS | `porttest_recipes.js` adds a kiln, a jewelry and a crusher recipe through the mod's schemas; the harness checks they exist in the recipe manager and that the kiln, jewelry table and crusher execute them |

Run `porttest_recipes.js` alongside `porttest.js` (same folder) for the KubeJS rows.

Informational lines (not counted): `/curios replace` from a script, structure references at a `/place`-d structure.

## Gotchas learned while writing it

- A dead singleplayer host is saved inside `level.dat` and reloads dead; `PlayerList.respawn` did not take from a script,
  reviving in place (`deathTime = 0`, `setHealth`) does. Dead players are excluded from `@a` and stop ticking, which
  silently breaks every later check — the script revives at start and before the boss section.
- Items placed into a furnace-like block by `setblock` NBT never cook (`CookTimeTotal` stays 0); insert them with
  `/item replace block … container.N` instead.
- `/place structure` does not register a `StructureStart`, so structure-based logic cannot see it.
- Rhino cannot pick between overloads such as `StructureManager.getStructureWithPieceAt`; call unambiguous methods
  (`getAllStructuresAt`, `getStructureAt`) or the mod's own wrappers.
- PowerShell variables are case-insensitive if you script around this from a shell.
