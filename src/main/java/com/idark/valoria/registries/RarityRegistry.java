package com.idark.valoria.registries;

import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;
import net.neoforged.fml.*;
import net.neoforged.fml.common.asm.enumextension.*;

import java.util.function.*;

import static com.idark.valoria.util.Styles.*;

/**
 * PORT NOTE: Forge's {@code Rarity.create(name, styleModifier)} runtime enum extension is gone. NeoForge injects enum
 * constants at class-load time from {@code META-INF/enumextensions.json}; the constructor arguments are supplied by the
 * {@link EnumProxy} fields in {@link Proxies}. The constant names carry the mod-id prefix as NeoForge requires
 * ({@code VALORIA_HALLOWEEN} etc.), the id argument is {@code -1} so the ordinal is assigned automatically, and the
 * serialized names are {@code valoria:<name>}. The public {@code Rarity} fields keep their original names.
 */
public class RarityRegistry{
    public static final class Proxies{
        public static final EnumProxy<Rarity> HALLOWEEN = proxy("halloween", apply(halloween));
        public static final EnumProxy<Rarity> LUNAR = proxy("lunar", apply(lunar));
        public static final EnumProxy<Rarity> BLOODY = proxy("bloody", apply(bloody));
        public static final EnumProxy<Rarity> MARSH = proxy("marsh", apply(marsh));
        public static final EnumProxy<Rarity> SPIDER = proxy("spider", apply(spider));
        public static final EnumProxy<Rarity> PYRATITE = proxy("pyratite", apply(arcaneGold));
        public static final EnumProxy<Rarity> INFERNAL = proxy("infernal", apply(infernal));
        public static final EnumProxy<Rarity> AQUARIUS = proxy("aquarius", apply(aquarius));
        public static final EnumProxy<Rarity> SOUL = proxy("soul", apply(soul));
        public static final EnumProxy<Rarity> ETHEREAL = proxy("ethereal", apply(ethereal));
        public static final EnumProxy<Rarity> NATURE = proxy("nature", apply(nature));
        public static final EnumProxy<Rarity> VOID = proxy("void", apply(nihility));
        // Evaluated per call so the Item Borders check happens once mods are known (the old code checked eagerly at class-init).
        public static final EnumProxy<Rarity> ELEMENTAL = proxy("elemental", style -> ModList.get().isLoaded("itemborders") ? white : elemental);
        public static final EnumProxy<Rarity> PHANTASM = proxy("phantasm", apply(phantasm));

        private static EnumProxy<Rarity> proxy(String name, UnaryOperator<Style> styleModifier){
            return new EnumProxy<>(Rarity.class, -1, "valoria:" + name, styleModifier);
        }
    }

    public static final Rarity
    HALLOWEEN = Proxies.HALLOWEEN.getValue(),
    LUNAR = Proxies.LUNAR.getValue(),
    BLOODY = Proxies.BLOODY.getValue(),
    MARSH = Proxies.MARSH.getValue(),
    SPIDER = Proxies.SPIDER.getValue(),
    PYRATITE = Proxies.PYRATITE.getValue(),
    INFERNAL = Proxies.INFERNAL.getValue(),
    AQUARIUS = Proxies.AQUARIUS.getValue(),
    SOUL = Proxies.SOUL.getValue(),
    ETHEREAL = Proxies.ETHEREAL.getValue(),
    NATURE = Proxies.NATURE.getValue(),
    VOID = Proxies.VOID.getValue(),
    ELEMENTAL = Proxies.ELEMENTAL.getValue(),
    PHANTASM = Proxies.PHANTASM.getValue();

}
