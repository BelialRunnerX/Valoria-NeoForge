# Known issues — Valoria NeoForge 1.21.1 port

Status legend: **Open** (needs a fix), **Upstream** (present in the original Forge 1.20.1 mod too; kept as-is unless it breaks the port), **Unverified** (ported by the book, not yet exercised in game). Fixed items move to [CHANGELOG.md](CHANGELOG.md). Report new ones in this repository's issue tracker, not to the original author.

## Open

_None known at the moment. Current test status: the client reaches the title screen, creates a world, joins it, and the starter bundle/codex work. A dedicated server boots and generates a world with a clean log. Everything past that point is still being play-tested — see "Unverified in game"._

## Upstream (inherited from Valoria 1.0.4.2 / main)

- The `fortress` and `magmatic_halls` structures spawn in `#valoria:all`, a biome tag no data file defines (`Not all defined tags ... are present: valoria:all`), so neither structure ever generates. Same on 1.20.1; left unchanged pending the original author's intent.
- `valoria:item/pixie_pet` has no model (texture `item/pixie_pet.png` is missing upstream, so datagen skips it); the item renders as the missing-texture cube. Same for `debug_item`, `gaib_root`, `karusakan_root`, `throwable_bomb`, `crab_buckler` which have hand-written models but no texture file.
- `valoria:flame_sword` model references textures that do not exist ("Missing textures in model valoria:flame_sword#inventory").
- The eye-necklace animation metadata (`iron_eye_necklace_closed`, `netherite_eye_necklace_closed`) lists frames 1 and 2 on a single-frame 16×16 texture ("Invalid frame index").
- Sound events `valoria:item.halloween_slice_legacy.use` and `valoria:mob.haunted_merchant.range_attack` are registered but have no entry in `sounds.json`.
- The dev run configuration `client1` logs in with the original author's username (supporter cape test); development-only.
- `build.gradle` still carries the upstream CurseForge/Modrinth/Maven publishing targets; they are inert without the upstream tokens and must be changed before the port is published anywhere.

## Changed behaviour versus 1.20.1 (intentional, documented in PORTING.md)

- Handlers that were silently dead on 1.20.1 are now active: `CapabilityEvents.onServerTick` (dungeon-visit codex unlock) and Tridot's dungeon music tick.
- Blocks/items renamed before 1.0.4 are no longer remapped in old worlds (NeoForge has no `MissingMappingsEvent`; the table is kept in `core/LegacyIdRemap` but not wired).
- Typos in upstream data that 1.20.1 silently turned into air are now the intended blocks (`crystal_disk`, `mod_nether_vegetation`, `processor_list/valoria_misc`).
- Enchantments (Bleeding, Accuracy, Explosive Flame) are data-driven; `enchant_with_levels` loot without the old `treasure: true` flag uses vanilla's `on_random_loot` set.
- Attachments are also copied on non-death clones (end return), where 1.20.1 recreated them.
- The `FillBucketEvent` handler (stun blocks bucket use) has no NeoForge equivalent and was removed.

## Unverified in game

- Portal travel Overworld ⇄ Valoria (new `Portal`/`DimensionTransition` path), portal re-use and spawn placement.
- Bosses: Necromancer (all spells, music disc, boss bar), Dryador, Firron, Wicked Crystal, King Crab; stat amplification per difficulty.
- Nihility system (meter, damage scaling, max-nihility action, teleport variant) and magma charge HUD.
- Codex: page unlocks from advancements, dungeon visits and kills; toasts; drag/scroll.
- Crafting stations with JEI: kiln, heavy workbench, alchemy station and upgrade, crusher, jewelry, keg, manipulator, soul infuser.
- Enchanting table/anvil availability of the three custom enchantments, Bleeding proc rate, Explosive Flame exclusivity.
- Tool tiers: the six "level 2" tiers (bronze, pearlium, holiday, halloween, lunar, samurai) sit below iron exactly as the 1.20.1 sort order did.
- Curios: rings, necklaces, jewelry bag key bind, glove dyeing, immunity accessories.
- Items from 1.20.1 worlds (soul collectors, magma charge, poisoned weapons, rotten food, summon books) migrating through `custom_data`.
- Supporter cloaks (`getSkin()` mixin), KubeJS recipe schemas, Jade/JER pages, HUD layer ordering, dispenser behaviours for throwables, strippable logs data map, painting variants.
