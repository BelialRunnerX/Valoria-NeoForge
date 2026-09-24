package com.idark.valoria.registries.item.types.builders;

import com.google.common.collect.*;
import com.idark.valoria.*;
import net.minecraft.core.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.Item.Properties;

/**
 * PORT NOTE: attributes are {@code Holder<Attribute>}s and modifiers are identified by a {@link net.minecraft.resources.ResourceLocation}
 * in 1.21. The 1.20.1 builder generated a random UUID per modifier; the id is now derived from the attribute name and
 * the insertion index ({@code valoria:talisman/<attribute>/<n>}) so it is stable across launches.
 */
public abstract class AbstractTalismanBuilder<T extends Item>{
    public Properties properties;
    public Multimap<Holder<Attribute>, AttributeModifier> attributes = LinkedHashMultimap.create();

    public AbstractTalismanBuilder(Properties pProperties){
        this.properties = pProperties;
    }

    public AbstractTalismanBuilder<T> put(Holder<Attribute> attribute, double value){
        return put(attribute, AttributeModifier.Operation.ADD_VALUE, value);
    }

    public AbstractTalismanBuilder<T> put(Holder<Attribute> attribute, AttributeModifier.Operation operation, double value){
        String name = attribute.getRegisteredName().replace(':', '/').replace('.', '_');
        this.attributes.put(attribute, new AttributeModifier(Valoria.loc("talisman/" + name + "/" + attributes.size()), value, operation));
        return this;
    }

    public abstract T build();
}
