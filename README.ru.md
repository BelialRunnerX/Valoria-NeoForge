# Valoria — порт на NeoForge 1.21.1

> 🇬🇧 Основная документация ведётся на английском языке: [README.md](README.md). Этот перевод сделан в знак уважения к автору оригинала и может отставать от английской версии; при расхождениях верна английская.

Этот репозиторий — **сообществом сделанный порт [Valoria](https://github.com/IriDark/Valoria)** (автор IriDark и участники) с Forge 1.20.1 на **NeoForge 1.21.1**.

## Пожалуйста, прочитайте перед тем, как открывать issue

- Порт сделан **с разрешения автора оригинала**.
- **Не обращайтесь к автору оригинала по поводу этого порта.** За него отвечает сопровождающий *этого* репозитория. Ошибки, вылеты и вопросы о сборке для NeoForge 1.21.1 нужно направлять в issue-трекер этого репозитория, а не в репозиторий, Discord или вики оригинальной Valoria.
- Использовать порт можно на тех же условиях, что и оригинальный мод: **лицензия и разрешения оригинальной Valoria действуют и здесь**. В репозитории лежит неизменённый оригинальный файл `LICENSE` (**код: GNU GPL-3.0; ассеты: Creative Commons Attribution-NonCommercial 4.0 International**). Если вы распространяете порт или строите на его основе что-то своё, вы обязаны и дальше соблюдать эти лицензии и все условия, установленные автором оригинала.

## Состояние

- **Рабочая сборка, но не полностью проверенная.** Клиент запускается, создаёт и загружает миры; выделенный сервер запускается с чистым логом. Вручную проверено на данный момент (2026-09-24): Кодекс, курио и индикатор Нихилити, измерение Valoria, призыв четырёх боссов (полоски боссов, музыка, катсцена Фиррона), GUI станций крафта, дробилка камня, способности оружия и Phantasm Bow, размещение структур через `/locate`, JEI/Jade. Бои с боссами, выполнение рецептов, зачарования, путешествие через портал и внутренности структур ещё не тестировались — см. [KNOWN_ISSUES.ru.md](KNOWN_ISSUES.ru.md). Считайте сборки бета-версией и ожидайте ошибок в непроверенных областях.
- `./gradlew build` собирает `Valoria-1.21.1-1.0.4.2.jar`; генератор данных загружает полный набор модов (NeoForge, Tridot, Curios, GeckoLib, JEI, Jade, JER, KubeJS, Dummmmmmy).
- **[CHANGELOG.ru.md](CHANGELOG.ru.md)** перечисляет все исправления порта по коммитам, **[KNOWN_ISSUES.ru.md](KNOWN_ISSUES.ru.md)** — открытые, унаследованные и ещё не проверенные пункты, а [PORTING.ru.md](PORTING.ru.md) документирует каждую переработку. Пожалуйста, сверьтесь со списком известных проблем, прежде чем сообщать об ошибке.

## Что изменилось

Каждая нетривиальная переработка (регистрация, события, сеть, NBT предметов → компоненты данных, capabilities → attachments, зачарования → данные, портал, рендер, миксины, датапак) описана в [PORTING.ru.md](PORTING.ru.md), а каждое место в коде помечено комментарием `// PORT NOTE:`. Мод перенесён полностью — ни одна возможность не заглушена и не удалена.

## Требования и проверенные версии

Порт собран и протестирован именно с этими версиями. Более новые патч-релизы обычно работают, но гарантированно проверены только эти.

| Компонент | Версия | Где взять |
|---|---|---|
| Minecraft | 1.21.1 | — |
| Java | 21 (использовался Temurin 21.0.12) | https://adoptium.net/ |
| **NeoForge** | **21.1.251** (допустимый диапазон `[21.1,)`) | https://neoforged.net/ · https://projects.neoforged.net/neoforged/neoforge |
| **Tridot (порт на NeoForge)** | **1.21.1-1.0.169** (диапазон `[1.21.1-1.0.169,)`) | https://github.com/BelialRunnerX/Tridot-NeoForge — обязателен; Tridot для Forge 1.20.1 с CurseForge/Modrinth **не подойдёт** |
| **Curios API** | **9.5.1+1.21.1** (диапазон `[9,)`) | https://modrinth.com/mod/curios · https://www.curseforge.com/minecraft/mc-mods/curios |
| **GeckoLib** | **4.9.3** для NeoForge 1.21.1 (диапазон `[4.7,)`) | https://modrinth.com/mod/geckolib · https://www.curseforge.com/minecraft/mc-mods/geckolib |

Необязательные интеграции, скомпилированные и проверенные с версиями:

| Мод | Версия | Ссылка |
|---|---|---|
| JEI | 19.57.0.447 | https://modrinth.com/mod/jei |
| Jade | 15.10.6+neoforge | https://modrinth.com/mod/jade |
| Just Enough Resources (JER) | 1.6.0.17 (сборка для NeoForge) | https://modrinth.com/mod/just-enough-resources-jer |
| KubeJS / Rhino / Architectury | 2101.7.2-build.377 / 2101.2.8-build.91 / 13.0.11 | https://modrinth.com/mod/kubejs |
| Dummmmmmy + Moonlight Lib | 1.21-2.1.2 (NeoForge) + 1.21.1-3.6.8 (NeoForge) | https://modrinth.com/mod/mmmmmmmmmmmm · https://modrinth.com/mod/moonlight |
| Better Combat, Obscure Tooltips, JEED, Enchantment Descriptions, Catalogue | любая сборка для 1.21.1 (только данные/локализация) | — |
| Tetra | релиза для 1.21 пока нет — интеграция неактивна | — |

Что нужно знать игрокам и авторам сборок:

- **Зависимости:** см. таблицу выше. Устанавливайте сборки Curios и GeckoLib для NeoForge, а не для Forge или Fabric.
- **Интеграции:** JEI, Jade, JER, KubeJS, JEED, Enchantment Descriptions, Better Combat, Catalogue и Obscure Tooltips перенесены. Данные материалов Tetra всё ещё поставляются, но неактивны, пока Tetra не выйдет для 1.21.
- **Миры из 1.20.1:** NBT предметов ванилла переносит в `minecraft:custom_data`, откуда он и читается; данные игрока (страницы кодекса, Нихилити, заряд магмы, способности) сохраняют идентификаторы. Блоки/предметы, *переименованные* до версии 1.0.4, больше не перемапливаются (в NeoForge нет `MissingMappingsEvent`). Сначала сделайте резервную копию мира.
- **Зачарования** (Bleeding, Accuracy, Explosive Flame) описаны JSON-данными; инструменты и броня Valoria используют теги `minecraft:enchantable/*`.
- **Датапаки / KubeJS:** папки названы по правилам 1.21 (`recipe`, `loot_table`, `tags/item`...), общие теги — в пространстве имён `c:`, схемы рецептов KubeJS сохраняют те же ключи скриптов, что в 1.20.1.
- **Файлы конфигурации** не изменились: `valoria/client.toml`, `valoria/common.toml`, `valoria/server.toml`.

## Разработчикам

Требования: Java 21, NeoForge 21.1.x, Minecraft 1.21.1.

Порт Tridot пока не опубликован в публичном Maven, поэтому сначала соберите его:

```bash
git clone https://github.com/BelialRunnerX/Tridot-NeoForge
cd Tridot-NeoForge && ./gradlew publishToMavenLocal
```

Затем в этом репозитории:

```bash
./gradlew build        # jar в build/libs
./gradlew runClient    # dev-клиент
./gradlew runData      # перегенерировать src/generated/resources
```

---

# О МОДЕ

Описание, скриншоты, список интеграций, поддерживаемые языки и благодарности см. в английском [README.md](README.md) — этот раздел там воспроизведён из оригинального описания мода. Все заслуги за саму Valoria принадлежат её авторам — **IriDark, Sunday, Kerdo, DuUaader** и участникам, перечисленным там же. Порт на NeoForge 1.21.1 сопровождается отдельно пользователем [BelialRunnerX](https://github.com/BelialRunnerX).

Оригинальный проект: https://github.com/IriDark/Valoria — ссылки на вики, Discord и Patreon *оригинального* мода находятся на той странице.
