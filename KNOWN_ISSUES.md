# Known issues — Valoria NeoForge 1.21.1 port

Status legend: **Open** (needs a fix), **Upstream** (present in the original Forge 1.20.1 mod too; kept as-is unless it breaks the port), **Changed** (intentional difference from 1.20.1, documented in [PORTING.md](PORTING.md)), **Unverified** (ported by the book, not yet exercised in game). Fixed items move to [CHANGELOG.md](CHANGELOG.md). Report new ones in this repository's issue tracker, not to the original author.

## Open

- **Cosmetic:** every launch logs `Configuration file …\valoria\client.toml is not correct. Correcting` (and the same for `common.toml`) although the rewritten file is identical. Harmless; cause not yet found.
- **Cosmetic:** creating or first opening a world shows vanilla's one-time "These settings are using experimental features" confirmation. Vanilla flags any world with a dimension beyond the three built-in ones (`WorldDimensions.bake`); NeoForge only remembers the answer per world. Not a Valoria bug; the world is fine.
- **Cosmetic:** JEI warns "3 duplicate items were found in 'Valoria - Tools' creative tab" — the tab is filled by the same tag predicates as upstream; duplicates are de-duplicated by JEI.
- **Intermittent (harness):** in one of eleven scripted Necromancer kills (seven full runs plus four repeats) the player-credited kill dropped no `necromancer_treasure_bag` (Dryador, Firron and Wicked Crystal always dropped theirs). Not reproduced by hand yet; the harness now logs the entities present at the kill to narrow it down.

**Test status (2026-09-24):** working build, not fully validated. Verified in a creative test world by the maintainer: world creation/loading; Codex GUI and pages; curios equipping (right-click and Curios menu) and the nihility meter rising in survival inside Valoria; teleporting into the Valoria dimension (sky, fog, ambient particles, terrain); summoning Necromancer, Wicked Crystal, Dryador and Firron (Tridot boss bars with per-boss styling, Necromancer music toast, Firron spawn cutscene, GeckoLib model); GUIs of kiln, jewelry table, heavy workbench (including 2×2 placement from the item), soul infuser, elemental manipulator and keg; stone crusher item in/out; firework tube; katana dash and scythe blade abilities; Phantasm Bow; `/locate` finds crypt, medium crypt, necromancer crypt, sand ruin and crystallized deep ruins in the Overworld and flesh altar, fractured skull, giant ribs, monstrosity skull/spine, taint spike and corrupted well in Valoria; JEI indexing and Jade block tooltips. A dedicated server boots with a clean log. Everything else is still listed under "Unverified in game".

## Missing or inert on 1.21.1

- **Tetra integration** — Tetra has no 1.21 release. The material/replacement data under `data/tetra/` is still shipped (attribute keys updated to `minecraft:entity_interaction_range` / `block_interaction_range`) but does nothing until Tetra ships for 1.21.
- **Epic Fight add-on and Dynamic Trees add-on** — third-party add-ons for the Forge 1.20.1 version; not part of this repository and not ported.
- **Old-world id remapping** — blocks/items that were *renamed* in Valoria before 1.0.4 are no longer remapped when loading a 1.20.1 world (NeoForge has no `MissingMappingsEvent`). The table is preserved in `core/LegacyIdRemap` but not wired; such blocks/items vanish. Back up worlds before converting.
- **Stun blocking bucket use** — the `FillBucketEvent` handler (stunned players could not fill/empty buckets) has no NeoForge equivalent and was removed; stun still blocks item use, block placing and breaking through other events.
- **Fortress and Magmatic Halls structures never generate** — they spawn in `#valoria:all`, a biome tag no data file defines. Same on 1.20.1 (upstream), so left unchanged pending the original author's intent; the fortress locator maps therefore find nothing.

## Upstream (inherited from Valoria 1.0.4.2 / main)

- **Code base is upstream `main`, not the 1.0.4.2 release jar.** The upstream `CHANGELOG.md` already opens with unreleased *1.1.0* entries (sarcophagus rework, data-driven sarcophagus gear, crypt height fix); those work-in-progress changes are in this port even though the version string says `1.0.4.2`.
- `valoria:item/pixie_pet` has no model (texture `item/pixie_pet.png` is missing upstream, so datagen skips it) and renders as the missing-texture cube. Same for `debug_item`, `gaib_root`, `karusakan_root`, `throwable_bomb`, `crab_buckler`, which have hand-written models but no texture file.
- `valoria:flame_sword` model references textures that do not exist ("Missing textures in model valoria:flame_sword#inventory").
- The eye-necklace animation metadata (`iron_eye_necklace_closed`, `netherite_eye_necklace_closed`) lists frames 1 and 2 on a single-frame 16×16 texture ("Invalid frame index").
- Sound events `valoria:item.halloween_slice_legacy.use` and `valoria:mob.haunted_merchant.range_attack` are registered but have no entry in `sounds.json`.
- `TagsRegistry.NEEDS_NATURE_TOOL` points at `needs_pearlium_tool` (upstream typo). Kept, because the tier tags are derived from it exactly as 1.20.1's tier sorting did; only `needs_cobalt_tool` has content anyway.
- The in-game Patreon links (codex button, "new version" chat message) and the runtime patron list (`PatreonManager` fetches `patrons.json` from the upstream repository) still belong to the original author. Intentional: they are part of the mod, not of the port.
- The dev run configuration `client1` logs in with the original author's username (supporter cape test); development-only.
- `build.gradle` still carries the upstream CurseForge/Modrinth/Maven publishing targets; they are inert without the upstream tokens and must be changed before the port is published anywhere.

## Changed behaviour versus 1.20.1 (intentional, see PORTING.md)

- **Previously dead handlers are active**: `CapabilityEvents.onServerTick` (dungeon-visit codex unlock every 100 ticks) and Tridot's dungeon music tick were `static` methods that Forge silently never called. NeoForge refuses that, so they were made instance methods and now run. Remove their `@SubscribeEvent` to restore the old behaviour.
- **Upstream data typos that 1.20.1 turned into air now place the intended block**: `crystal_disk` fallback (`minecraft:gravel`), `mod_nether_vegetation` (`valoria:soulroot`), `processor_list/valoria_misc` (`valoria:void_stone`, `void_stone_wall`, `void_taint`, `void_brick`). Structures using that processor no longer have air holes where those blocks were meant.
- **Enchantments are data-driven** (`data/valoria/enchantment/*.json`). Loot `enchant_with_levels` entries that had `treasure: true` now draw from vanilla's `on_random_loot` set (close, not identical); `treasure: false` maps to `#minecraft:non_treasure`.
- **Summon books in crypt/monstrosity chests**: the two loot entries with an empty `EntityTag` (meaning "default variant") were dropped because the 1.21 `entity_data` component requires an id; the book behaves identically without the component. Coloured/typed variants are unchanged.
- **Explosion knockback** (golem stomp/ground punch, Necromancer knockback spell) uses the `explosion_knockback_resistance` attribute (fed by Blast Protection) instead of the removed `ProtectionEnchantment` dampener — the same formula vanilla applies now.
- **Player data attachments** (codex pages, nihility, magma charge, abilities) are also copied on non-death clones such as returning from the End; 1.20.1 recreated them there.
- **Music disc** is a plain item with the `jukebox_playable` component and is no longer in the `minecraft:music_discs` tag (that tag no longer exists).
- **Data pack / KubeJS**: 1.21 folder names (`recipe`, `loot_table`, `tags/item`, …), conventional tags in the `c:` namespace, `forge:stained_glass` replaced by `valoria:stained_glass`, `neoforge:conditions` instead of `forge:conditional`; KubeJS script keys are unchanged.
- **Tool tiers**: `incorrect_for_<tier>_tool` tags reproduce the 1.20.1 tier sort order exactly, so bronze, pearlium, holiday, halloween, lunar and samurai tools cannot mine iron-level blocks (they were sorted below iron in 1.20.1 as well).
- **Config options whose effect may differ** (details in PORTING.md → "Config behaviour changes"): `maxNihilityAction = TELEPORT` now goes through vanilla's respawn logic and can consume a respawn anchor charge; timer-based options tick on `PlayerTickEvent.Post`/`ServerTickEvent.Post`; `PatreonRewards` capes come from the `getSkin()` mixin and override a Mojang cape; `damageIndicator` reads the pre-armour damage from `LivingIncomingDamageEvent`.
- **Repository metadata** (issue tracker URL, update JSON, homepage, in-game update link, contributing guide) points at this port, not the original author.
- **KubeJS integration is active for the first time.** Upstream shipped the plugin class and schemas but not the `kubejs.plugins.txt` marker, so `event.recipes.valoria.*` never existed on 1.20.1; the port ships the marker. Argument order follows the schema keys (see CHANGELOG 2026-09-24).

## Unverified in game

**Verified by the automated harness (`tools/porttest`, 2026-09-24, 58 checks, last full run 57/58 (one intermittent Necromancer treasure-bag miss, see Open)):** jewelry table, keg and soul infuser recipe execution; KubeJS-added kiln/jewelry/crusher recipes registering and executing; the max-nihility TELEPORT action; stone crusher insert/crush with a pickaxe and its loot; Bleeding III applying `valoria:bleeding` in melee; the `neoforge:strippables` data map; the item-based codex unlock (portal frame shard) and the `crushables` unlock; the max-nihility KILL action at 97 %; portal ring formation, Overworld → Valoria travel through a real portal, return-portal generation and the trip back; kiln recipe execution (100 ticks, vanilla furnace as control); `/enchant` availability of bleeding / explosive_flame / accuracy on their supported items, exclusivity with Fire Aspect and refusal on unsupported items; Curios attribute bonuses applied and removed through the right-click and API paths (ruby, health and state-dependent eye necklaces); nihility damage at 60 %; dispenser behaviour for Valoria arrows; the music disc playing in a jukebox; painting variants; all 22 living entity types spawning; 15-second boss fights against a survival player and treasure-bag drops for Necromancer, Dryador, Firron and Wicked Crystal; crypt generation with loot containers; the `valoria:crypt` codex page unlocking when standing in a naturally generated crypt (dungeon-visit handler); boss-kill and dimension-visit codex unlocks.

Still not exercised:

- Boss music disc drop chance, King Crab, stat amplification per difficulty; boss spells were only observed not to crash for 15 s each.
- Magma charge HUD, damage-indicator numbers versus 1.20.1.
- Codex toasts and drag/scroll (unlocks from kills, dimension visit, dungeon visit and held items are verified).
- JEI recipe categories, alchemy station and its upgrade, elemental manipulator recipe execution (needs charged cores).
- Enchanting-table roll rates and anvil combining (only `/enchant` was used).
- Entity eye heights were compared statically with upstream's `getStandingEyeHeight` values (all nine match, the three `height - 0.28125` cases resolve to the same numbers); riding offsets were not.
- Curios: jewelry bag key bind, glove dyeing, immunity accessories; `/curios replace` issued from a script did nothing (Curios debug command, informational).
- Items from 1.20.1 worlds (soul collectors, magma charge, poisoned weapons, rotten food, summon books) migrating through `custom_data`.
- Supporter cloaks (`getSkin()` mixin), Jade/JER pages, HUD layer ordering, entity riding offsets moved to the type builders. (All six KubeJS schemas register recipes; kiln, jewelry, crusher, keg and manipulator script recipes were also executed by their blocks — the heavy workbench needs the GUI.)
