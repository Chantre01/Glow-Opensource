package cn.utils.drawHelpers;

import cn.utils.drawHelpers.shaderHelper.ShaderShell;
import net.ccbluex.liquidbounce.api.enums.WDefaultVertexFormats;
import net.ccbluex.liquidbounce.api.minecraft.client.render.ITessellator;
import net.ccbluex.liquidbounce.api.minecraft.client.render.IWorldRenderer;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;

import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class DrawHelper extends MinecraftInstance {

    public static void drawGlowRoundedRect(float startX, float startY, float endX, float endY, int color, float radius, float force) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3008);
        float alpha = (float)(color >> 24 & 255) / 255.0F;
        float red = (float)(color >> 16 & 255) / 255.0F;
        float green = (float)(color >> 8 & 255) / 255.0F;
        float blue = (float)(color & 255) / 255.0F;
        ShaderShell.ROUNDED_RECT.attach();
        ShaderShell.ROUNDED_RECT.set4F("color", red, green, blue, alpha);
        ShaderShell.ROUNDED_RECT.set2F("resolution", (float) Minecraft.getMinecraft().displayWidth, (float)Minecraft.getMinecraft().displayHeight);
        ShaderShell.ROUNDED_RECT.set2F("center", (startX + (endX - startX) / 2.0F) * 2.0F, (startY + (endY - startY) / 2.0F) * 2.0F);
        ShaderShell.ROUNDED_RECT.set2F("dst", (endX - startX - radius) * 2.0F, (endY - startY - radius) * 2.0F);
        ShaderShell.ROUNDED_RECT.set1F("radius", radius);
        ShaderShell.ROUNDED_RECT.set1F("force", force);
        GL11.glBegin(7);
        GL11.glVertex2d((double)endX, (double)startY);
        GL11.glVertex2d((double)startX, (double)startY);
        GL11.glVertex2d((double)startX, (double)endY);
        GL11.glVertex2d((double)endX, (double)endY);
        GL11.glEnd();
        ShaderShell.ROUNDED_RECT.detach();
        GL11.glEnable(3008);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }


    public static void prepareScissorBox(float x, float y, float x2, float y2) {
        ScaledResolution scale = new ScaledResolution(Minecraft.getMinecraft());
        int factor = scale.getScaleFactor();
        GL11.glScissor((int) (x * factor), (int) ((scale.getScaledHeight() - y2) * factor), (int) ((x2 - x) * factor), (int) ((y2 - y) * factor));
    }

    public static final void drawSmoothRect(float left, float top, float right, float bottom, int color) {
        GL11.glEnable(3042);
        GL11.glEnable(2848);
        drawRect((double)left, (double)top, (double)right, (double)bottom, color);
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        drawRect((double)(left * 2.0F - 1.0F), (double)(top * 2.0F), (double)(left * 2.0F), (double)(bottom * 2.0F - 1.0F), color);
        drawRect((double)(left * 2.0F), (double)(top * 2.0F - 1.0F), (double)(right * 2.0F), (double)(top * 2.0F), color);
        drawRect((double)(right * 2.0F), (double)(top * 2.0F), (double)(right * 2.0F + 1.0F), (double)(bottom * 2.0F - 1.0F), color);
        drawRect((double)(left * 2.0F), (double)(bottom * 2.0F - 1.0F), (double)(right * 2.0F), (double)(bottom * 2.0F), color);
        GL11.glDisable(3042);
        GL11.glScalef(2.0F, 2.0F, 2.0F);
    }

    public static void drawSmoothRect(double left, double top, double right, double bottom, int color) {
        GL11.glEnable(3042);
        GL11.glEnable(2848);
        drawRect(left, top, right, bottom, color);
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        drawRect(left * 2.0D - 1.0D, top * 2.0D, left * 2.0D, bottom * 2.0D - 1.0D, color);
        drawRect(left * 2.0D, top * 2.0D - 1.0D, right * 2.0D, top * 2.0D, color);
        drawRect(right * 2.0D, top * 2.0D, right * 2.0D + 1.0D, bottom * 2.0D - 1.0D, color);
        drawRect(left * 2.0D, bottom * 2.0D - 1.0D, right * 2.0D, bottom * 2.0D, color);
        GL11.glDisable(3042);
        GL11.glScalef(2.0F, 2.0F, 2.0F);
    }

    public static void drawRectWithGlow(double X, double Y, double Width, double Height, double GlowRange, double GlowMultiplier, Color color) {
        for (float i = 1.0F; (double) i < GlowRange; i += 0.5F) {
            drawRoundedRect99(X - (GlowRange - (double) i), Y - (GlowRange - (double) i), Width + (GlowRange - (double) i), Height + (GlowRange - (double) i), injectAlpha(color, (int) Math.round((double) i * GlowMultiplier)).getRGB());
        }
    }

    public static void startSmooth() {
        GL11.glEnable(2848);
        GL11.glEnable(2881);
        GL11.glEnable(2832);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glHint(3153, 4354);
    }


    public static int setColor(int colorHex) {
        float alpha = (colorHex >> 24 & 0xFF) / 255.0F;
        float red = (colorHex >> 16 & 0xFF) / 255.0F;
        float green = (colorHex >> 8 & 0xFF) / 255.0F;
        float blue = (colorHex & 0xFF) / 255.0F;
        GL11.glColor4f(red, green, blue, (alpha == 0.0F) ? 1.0F : alpha);
        return colorHex;
    }

    public static void drawImage(ResourceLocation image, int x, int y, int width, int height, int color) {
        glPushMatrix();
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        DrawHelper.setColor(color);
        mc2.getTextureManager().bindTexture(image);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
        glDepthMask(true);
        glEnable(GL_DEPTH_TEST);
        glPopMatrix();
    }

    public static void endSmooth() {
        GL11.glDisable(2848);
        GL11.glDisable(2881);
        GL11.glEnable(2832);
    }

    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }

    public static Color injectAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static void drawRoundedRect99(double x, double y, double x1, double y1, int insideC) {
        drawRect(x + 0.5D, y, x1 - 0.5D, y + 0.5D, insideC);
        drawRect(x + 0.5D, y1 - 0.5D, x1 - 0.5D, y1, insideC);
        drawRect(x, y + 0.5D, x1, y1 - 0.5D, insideC);
    }


    public static void drawRect(double left, double top, double right, double bottom, int color) {
        double j;
        if (left < right) {
            j = left;
            left = right;
            right = j;
        }

        if (top < bottom) {
            j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        ITessellator tessellator = classProvider.getTessellatorInstance();
        IWorldRenderer bufferbuilder = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        bufferbuilder.begin(7, classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION));
        bufferbuilder.pos(left, bottom, 0.0D).endVertex();
        bufferbuilder.pos(right, bottom, 0.0D).endVertex();
        bufferbuilder.pos(right, top, 0.0D).endVertex();
        bufferbuilder.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }


}
