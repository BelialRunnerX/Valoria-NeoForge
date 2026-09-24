package com.idark.valoria.client.ui.widget;

import net.minecraft.client.gui.*;
import net.minecraft.client.gui.components.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;

/**
 * PORT NOTE: the 1.20.1 {@code ImageButton(x, y, w, h, xTex, yTex, yDiffTex, texture, texW, texH, onPress)} constructor
 * that blits a region of a plain texture (hover state = yDiffTex rows lower) was replaced by sprite-based
 * {@code WidgetSprites}. The codex buttons keep their existing 200x80 button sheets, so this widget reproduces the old
 * behaviour on top of {@link Button}.
 */
public class LegacyImageButton extends Button{
    private final ResourceLocation texture;
    private final int xTex;
    private final int yTex;
    private final int yDiffTex;
    private final int textureWidth;
    private final int textureHeight;

    public LegacyImageButton(int x, int y, int width, int height, int xTex, int yTex, int yDiffTex, ResourceLocation texture, int textureWidth, int textureHeight, OnPress onPress){
        super(x, y, width, height, CommonComponents.EMPTY, onPress, DEFAULT_NARRATION);
        this.texture = texture;
        this.xTex = xTex;
        this.yTex = yTex;
        this.yDiffTex = yDiffTex;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick){
        int v = this.yTex;
        if(!this.isActive()){
            v = this.yTex + this.yDiffTex * 2;
        }else if(this.isHoveredOrFocused()){
            v = this.yTex + this.yDiffTex;
        }

        guiGraphics.blit(this.texture, this.getX(), this.getY(), this.xTex, v, this.width, this.height, this.textureWidth, this.textureHeight);
    }
}
