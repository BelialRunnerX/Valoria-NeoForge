# Valoria — NeoForge 1.21.1 port

This repository is a **community port of [Valoria](https://github.com/IriDark/Valoria)** (by IriDark and contributors) from Forge 1.20.1 to **NeoForge 1.21.1**.

## Please read before opening an issue

- This port was made **with the permission of the original author**.
- **Do not contact the original author with concerns about this port.** The maintainer of *this* repository is responsible for it. Bugs, crashes, or questions about the NeoForge 1.21.1 build belong in this repository's issue tracker, not in the upstream Valoria repository, its Discord, or its wiki.
- You may use this port on the same terms as the original mod: whatever **licensing and permissions the original Valoria requires still apply here**. The repository ships the original `LICENSE` file unchanged (**code: GNU GPL-3.0; assets: Creative Commons Attribution-NonCommercial 4.0 International**). If you redistribute or build on this port, you must keep complying with those licenses and with any conditions the original author has set.

## Status

- `./gradlew build` produces `Valoria-1.21.1-1.0.4.2.jar`, and the data generator boots the complete mod set (NeoForge, Tridot, Curios, GeckoLib, JEI, Jade, JER, KubeJS, Dummmmmmy) through registration.
- In-game testing is in progress: world creation, joining, the starter bundle and codex work; a dedicated server boots with a clean log. Treat the builds as beta.
- **[CHANGELOG.md](CHANGELOG.md)** lists every port fix by commit, **[KNOWN_ISSUES.md](KNOWN_ISSUES.md)** lists open, upstream-inherited and not-yet-verified items, and [PORTING.md](PORTING.md) documents each rewrite. Please check the known-issues list before reporting.

## What changed

Every non-trivial rewrite (registration, events, networking, item NBT → data components, capabilities → attachments, enchantments → data, portal, rendering, mixins, the data pack) is documented in [PORTING.md](PORTING.md), and each code site carries a `// PORT NOTE:` comment explaining the change. The mod was ported in full — no feature was stubbed out or removed.

Things players and pack makers need to know:

- **Dependencies:** the [Tridot NeoForge 1.21.1 port](https://github.com/BelialRunnerX/Tridot-NeoForge) (`1.21.1-1.0.169` or newer), Curios 9.x and GeckoLib 4.7+ (built against 4.9.3) for NeoForge 1.21.1. The 1.20.1 Tridot from CurseForge/Modrinth will **not** work.
- **Integrations:** JEI, Jade, JER, KubeJS, JEED, Enchantment Descriptions, Better Combat, Catalogue and Obscure Tooltips are ported. The Tetra material data is still shipped but inert until Tetra releases for 1.21.
- **Worlds from 1.20.1:** item NBT is migrated by vanilla into `minecraft:custom_data` and read back from there; player data (codex pages, nihility, magma charge, abilities) keeps its ids. Blocks/items that were *renamed* before 1.0.4 are no longer remapped (NeoForge has no `MissingMappingsEvent`). Back up worlds first.
- **Enchantments** (Bleeding, Accuracy, Explosive Flame) are data-driven JSON; Valoria's tools and armour use the `minecraft:enchantable/*` tags.
- **Data packs / KubeJS:** folders follow 1.21 naming (`recipe`, `loot_table`, `tags/item`...), conventional tags use the `c:` namespace, and the KubeJS recipe schemas keep the same script keys as 1.20.1.
- **Config files** are unchanged: `valoria/client.toml`, `valoria/common.toml`, `valoria/server.toml`.

## For developers

Requirements: Java 21, NeoForge 21.1.x, Minecraft 1.21.1.

The Tridot port is not on a public Maven yet, so build it first:

```bash
git clone https://github.com/BelialRunnerX/Tridot-NeoForge
cd Tridot-NeoForge && ./gradlew publishToMavenLocal
```

Then in this repository:

```bash
./gradlew build        # jar in build/libs
./gradlew runClient    # dev client
./gradlew runData      # regenerate src/generated/resources
```

---

# ABOUT THIS MOD
**An ancient cataclysm—the Elemental Collapse—tore reality asunder. From the ashes, a new world was born: a realm of dark myth and terrifying ruin.**

*Valoria* transforms Minecraft into an epic, dark-fantasy RPG experience. Journey through desolate landscapes haunted by monstrous creatures, piece together the fractured history of a fallen world, and discover if hope can be rekindled from the embers.
### Features
* **⚔️ Epic Encounters:** Face new mobs and terrifying bosses using a huge arsenal of weaponry. Includes loads of new building blocks and items, all maintaining a vanilla-friendly aesthetic.
* **🏰 Uncharted Lands:** Explore intricate structures, bizarre biomes, and a completely new Dimension fraught with secrets and danger.
* **📖 Deep Lore:** Uncover a rich, dark-fantasy storyline rooted in mythology and designed to reward exploration.
* **🎵 Immersive Audio:** Experience a haunting Original Soundtrack composed by [DuUaader](https://youtube.com/playlist?list=OLAK5uy_kTIzlCKrHm_RyFxoZPmnKZNccdCT6XL-c&si=0-No5jkq0tsd3neC).
* **🔧 Highly Configurable:** Modpack developers have full control. Move or hide HUD elements (`valoria/client.toml`), customize boss stats (`valoria/common.toml`), or toggle core mechanics like Nihility, Food Rot, and Codex progression (`valoria/server.toml`).

---

### 🏛️ UNEARTH FORGOTTEN RUINS
Valoria's landscape is scattered with unique, hand-crafted structures waiting to be discovered. From crumbling ruins to massive containment facilities hiding specific entities—tread carefully, for deadly traps and ancient guardians still protect what lies within.

![structure.png](src/main/resources/assets/valoria/textures/banners/structure.png)
![fortress_angle.png](src/main/resources/assets/valoria/textures/banners/fortress_angle.png)
![forge.png](src/main/resources/assets/valoria/textures/banners/forge.png)

### 🌿 SURVIVE THE TWISTED FLORA
The Collapse didn’t destroy the world; it mutated it. Journey through vibrant, alien biomes where nature struggles to reclaim the ruins. The old flora has given way to strange new life forms that produce their own light. In this eternal twilight, luminescent plants may be your only beacon of hope.

![shadeforest.png](src/main/resources/assets/valoria/textures/banners/shadeforest.png)
![river.png](src/main/resources/assets/valoria/textures/banners/river.png)
![lush.png](src/main/resources/assets/valoria/textures/banners/lush.png)
![ecotone.png](src/main/resources/assets/valoria/textures/banners/ecotone.png)

### 💀 SCAVENGE THE ASHEN BARRENS
Traverse the open graves of entire ecosystems. Haunted by skeletal forests and choked with dust, the Barrens are a scarred landscape slowly being eroded by a relentless, sorrowful wind.

![dread_wood.png](src/main/resources/assets/valoria/textures/banners/dread_wood.png)

### 🐺 FACE TERRIFYING ABERRATIONS
The fauna of the old world was not spared. Corruption forced creatures to adapt or perish, turning the survivors into fiercely territorial hunters. They stalk the shadows of this broken world, and to them—you are nothing but prey.

![scorpion.png](src/main/resources/assets/valoria/textures/banners/scorpion.png)
![corrupted.png](src/main/resources/assets/valoria/textures/banners/corrupted.png)

### 🩸 UNRAVEL THE CELESTIAL PLAGUE
The sky itself is shattered. A malevolent red planet now hangs in the night, its very gaze acting as a poison upon the land. This celestial body fuels a creeping "bloodborne corruption"—a plague that continues to consume the world. Follow its trail and you will discover colossal, infected remains known only as the 'Monstrosities'.

![monstrosity.png](src/main/resources/assets/valoria/textures/banners/monstrosity.png)
![remains.png](src/main/resources/assets/valoria/textures/banners/remains.png)
![skull.png](src/main/resources/assets/valoria/textures/banners/skull.png)

---

### 🛠️ INTEGRATIONS & COMPATIBILITIES

* **Built-in Support (ported):** JADE, JER, JEI, KubeJS, JEED, Enchantment Descriptions, Better Combat, Catalogue, Obscure Tooltips.
* **Shipped but inert on 1.21.1:** Tetra (no 1.21 release yet).
* **Upstream-only (1.20.1):** the Epic Fight and Dynamic Trees add-ons target the Forge 1.20.1 version and have not been ported here.

---

### 🌐 SUPPORTED LANGUAGES

* ![flag_en_us.png](src/main/resources/assets/valoria/textures/banners/flag_en_us.png) **English**
* ![flag_ua.png](src/main/resources/assets/valoria/textures/banners/flag_ua.png) **Ukrainian** - by IriDark, Foxplane
* ![flag_pl.png](src/main/resources/assets/valoria/textures/banners/flag_pl.png) **Polish** *(WIP)* - by 𝙆𝘼𝙈𝙀𝙃𝙇𝙇𝙄𝙉𝙆
* ![flag_ru.png](src/main/resources/assets/valoria/textures/banners/flag_ru.png) **Russian** - by Ruthenium, TerraPrime, Kerdo
* ![flag_fr.png](src/main/resources/assets/valoria/textures/banners/flag_fr.png) **French** *(WIP)* - by 𝙆𝘼𝙈𝙀𝙃𝙇𝙇𝙄𝙉𝙆
* ![flag_cn.png](src/main/resources/assets/valoria/textures/banners/flag_cn.png) **Chinese** - by 蓝龙奈特萨玛 (Azurmardraco BlueDragon)

*Translation fixes for the port are welcome as pull requests here (see [CONTRIBUTING.md](CONTRIBUTING.md)); new languages belong upstream so both versions benefit.*

---

### 📜 CREDITS & CONTRIBUTORS

All credit for Valoria itself goes to its authors — **IriDark, Sunday, Kerdo, DuUaader** and the contributors below. The NeoForge 1.21.1 port is maintained separately by [BelialRunnerX](https://github.com/BelialRunnerX).

* **AstemirDev** - Code
* **Auriny** - Code, Testing
* **Skoow** - Code
* **Sunday** - Models
* **Kerdo** - Animations, SFX, Ideas, Contributions
* **LowTransient-34** - Music

**Translators:** Ruthenium, TerraPrime, 𝙆𝘼𝙈𝙀𝙃𝙇𝙇𝙄𝙉𝙆, 蓝龙奈特萨玛 (Azurmardraco BlueDragon)

**Special Thanks & Assets:**
* *Pandafolk_Ravio_Li, Rofoo, KoteykaTheCat, GraFik, Wosaj, Rainach, .sweetberries, Feimos, Avacuoss*
* **Calamity Mod Team** - For amazing sound design inspiration and sound assets.
* **Tyro.net** - Space background in Codex ([Source](https://tools.wwwtyro.net/space-3d/index.html))

---

Original project: https://github.com/IriDark/Valoria — wiki, Discord and Patreon links for the *original* mod are on that page.
