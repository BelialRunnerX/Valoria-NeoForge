package com.idark.valoria.registries.block.types;

import net.minecraft.core.registries.*;
enum WorkbenchPart implements net.minecraft.util.StringRepresentable {
    BOTTOM_LEFT,
    BOTTOM_RIGHT,
    TOP_LEFT,
    TOP_RIGHT;

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase();
    }
}
