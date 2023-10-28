package cn.utils.boker.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class Nameplate {
    private final double y;
    private final int width;
    private final String name;
    private final EntityLivingBase owner;
    private final Minecraft mc;
    private final RenderManager rm;
    private final double z;
    private final double x;

    public Nameplate(String Name, double X, double Y, double Z, EntityLivingBase livingBase) {
        mc = Minecraft.getMinecraft();
        name = Name;
        x = X;
        y = Y;
        z = Z;
        owner = livingBase;
        width = mc.fontRenderer.getStringWidth(name) / 2;
        rm = mc.getRenderManager();
    }


}

