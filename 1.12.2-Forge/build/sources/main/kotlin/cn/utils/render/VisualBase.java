package cn.utils.render;

import cn.utils.PaletteHelper;
import com.jhlabs.image.GaussianFilter;
import cn.utils.render.RenderUtil;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.render.ITessellator;
import net.ccbluex.liquidbounce.api.minecraft.client.render.IWorldRenderer;
import net.ccbluex.liquidbounce.api.minecraft.util.WMathHelper;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;

import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class VisualBase extends MinecraftInstance {
    static RenderManager renderManager;
    private static final HashMap<Integer, Integer> shadowCache = new HashMap<>();
    protected static float zLevel;
    private static ShaderGroup blurShader;
    private static Framebuffer buffer;
    private static int lastScale;
    private static int lastScaleWidth;
    private static int lastScaleHeight;
    private static ResourceLocation shader;


    public double fpsMultiplier() {
        return (RenderUtils.deltaTime / 60.0) * 3;
    }


    public static void pushAttrib()
    {
        GL11.glPushAttrib(8256);
    }

    public static void popAttrib()
    {
        GL11.glPopAttrib();
    }

    public static void pushMatrix()
    {
        GL11.glPushMatrix();
    }

    public static void popMatrix()
    {
        GL11.glPopMatrix();
    }

    public static void pushAttribAndMatrix() {
        pushAttrib();
        pushMatrix();
    }

    public static void popAttribAndMatrix() {
        popAttrib();
        popMatrix();
    }

    public static void circle(double x, double y, double radius, boolean filled, Color color) {
        polygon(x, y, radius, 360.0, filled, color);
    }

    public static void circle(double x, double y, double radius, boolean filled) {
        polygon(x, y, radius, 360, filled);
    }

    public static void circle(double x, double y, double radius, Color color) {
        polygon(x, y, radius, 360, color);
    }


    public static void rescale(double factor) {
        rescale(mc2.displayWidth / factor, mc2.displayHeight / factor);
    }

    public static void rescaleMC() {
        ScaledResolution resolution = new ScaledResolution(mc2);
        rescale(mc2.displayWidth / resolution.getScaleFactor(),mc2.displayHeight / resolution.getScaleFactor());
    }

    public static void rescale(double width, double height) {
        GlStateManager.clear(256);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, width, height, 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -2000.0F);
    }

    public static void glColor(final Color color) {
        final float red = color.getRed() / 255F;
        final float green = color.getGreen() / 255F;
        final float blue = color.getBlue() / 255F;
        final float alpha = color.getAlpha() / 255F;

        GlStateManager.color(red, green, blue, alpha);
    }

    public static void glColor(final Color color, final int alpha) {
        glColor(color, alpha / 255F);
    }

    public static void glColor(final Color color, final float alpha) {
        final float red = color.getRed() / 255F;
        final float green = color.getGreen() / 255F;
        final float blue = color.getBlue() / 255F;

        GlStateManager.color(red, green, blue, alpha);
    }

    public static final void color(double red, double green, double blue, double alpha) {
        GL11.glColor4d(red, green, blue, alpha);
    }

    public static void color(int color) {
        color(color, (float) (color >> 24 & 255) / 255.0F);
    }

    public static void color(int color, float alpha) {
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        GlStateManager.color(r, g, b, alpha);
    }

    public static void glColor(final int hex, final int alpha) {
        final float red = (hex >> 16 & 0xFF) / 255F;
        final float green = (hex >> 8 & 0xFF) / 255F;
        final float blue = (hex & 0xFF) / 255F;

        GlStateManager.color(red, green, blue, alpha / 255F);
    }

    public static void glColor(final int hex, final float alpha) {
        final float red = (hex >> 16 & 0xFF) / 255F;
        final float green = (hex >> 8 & 0xFF) / 255F;
        final float blue = (hex & 0xFF) / 255F;

        GlStateManager.color(red, green, blue, alpha);
    }


    public static void inShaderFBO() {
        try {
            blurShader = new ShaderGroup(mc2.getTextureManager(), mc2.getResourceManager(), mc2.getFramebuffer(), shader);
            blurShader.createBindFramebuffers(mc2.displayWidth, mc2.displayHeight);
            buffer = blurShader.mainFramebuffer;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void twoColorOutline(double x, double y, double width, double height, double radius, float linewidth, int color1, int color2) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        double x1 = x + width;
        double y1 = y + height;
        int colorI = 0;
        GL11.glPushAttrib(0);
        GL11.glScaled(0.5D, 0.5D, 0.5D);
        x *= 2.0D;
        y *= 2.0D;
        x1 *= 2.0D;
        y1 *= 2.0D;
        GL11.glLineWidth(linewidth);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBegin(2);
        int i;
        for (i = 0; i <= 90; i += 3) {
            setColor(ScaleUtils.fadeBetween(color1, color2, 20L * colorI));
            GL11.glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y + radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
            colorI++;
        }
        for (i = 0; i <= y; i += 3) {
            setColor(ScaleUtils.fadeBetween(color1, color2, 20L * colorI));
            GL11.glVertex2d(x, y1 - radius);
            colorI++;
        }
        for (i = 90; i <= 180; i += 3) {
            setColor(ScaleUtils.fadeBetween(color1, color2, 20L * colorI));
            GL11.glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
            colorI++;
        }
        for (i = 90; i <= 180; i += 3) {
            setColor(ScaleUtils.fadeBetween(color1, color2, 20L * colorI));
            GL11.glVertex2d(x + radius, y1);
            colorI++;
        }
        for (i = 0; i <= 90; i += 3) {
            setColor(ScaleUtils.fadeBetween(color1, color2, 20L * colorI));
            GL11.glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius);
            colorI++;
        }
        for (i = 0; i <= 90; i += 3) {
            setColor(ScaleUtils.fadeBetween(color1, color2, 20L * colorI));
            GL11.glVertex2d(x1, y1 - radius);
            colorI++;
        }
        for (i = 90; i <= 180; i += 3) {
            setColor(ScaleUtils.fadeBetween(color1, color2, 20L * colorI));
            GL11.glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y + radius + Math.cos(i * Math.PI / 180.0D) * radius);
            colorI++;
        }
        for (i = 90; i <= 180; i += 3) {
            setColor(ScaleUtils.fadeBetween(color1, color2, 20L * colorI));
            GL11.glVertex2d(x1 - radius, y);
            colorI++;
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glScaled(2.0D, 2.0D, 2.0D);
        GL11.glPopAttrib();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawBlurredShadow2(float x, float y, float width, float height, int blurRadius, Color color) {
        BufferedImage original = null;
        GaussianFilter op = null;
        GL11.glPushMatrix();
        GlStateManager.alphaFunc(516, 0.01f);
        float _X = (x -= (float)blurRadius) - 0.25f;
        float _Y = (y -= (float)blurRadius) + 0.25f;
        int identifier = String.valueOf((width += (float)(blurRadius * 2)) * (height += (float)(blurRadius * 2)) + width + (float)(1000000000 * blurRadius) + (float)blurRadius).hashCode();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2884);
        GL11.glEnable((int)3008);
        GlStateManager.enableBlend();
        int texId = -1;
        if (shadowCache.containsKey(identifier)) {
            texId = shadowCache.get(identifier);
            GlStateManager.bindTexture(texId);
        } else {
            if (width <= 0.0f) {
                width = 1.0f;
            }
            if (height <= 0.0f) {
                height = 1.0f;
            }
            if (original == null) {
                original = new BufferedImage((int)width, (int)height, 3);
            }
            Graphics g = original.getGraphics();
            g.setColor(Color.white);
            g.fillRect(blurRadius, blurRadius, (int)(width - (float)(blurRadius * 2)), (int)(height - (float)(blurRadius * 2)));
            g.dispose();
            if (op == null) {
                op = new GaussianFilter(blurRadius);
            }
            BufferedImage blurred = op.filter(original, null);
            texId = TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), blurred, true, false);
            shadowCache.put(identifier, texId);
        }
        color(color);
        GL11.glBegin((int)7);
        GL11.glTexCoord2f((float)0.0f, (float)0.0f);
        GL11.glVertex2f((float)_X, (float)_Y);
        GL11.glTexCoord2f((float)0.0f, (float)1.0f);
        GL11.glVertex2f((float)_X, (float)(_Y + height));
        GL11.glTexCoord2f((float)1.0f, (float)1.0f);
        GL11.glVertex2f((float)(_X + width), (float)(_Y + height));
        GL11.glTexCoord2f((float)1.0f, (float)0.0f);
        GL11.glVertex2f((float)(_X + width), (float)_Y);
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.resetColor();
        GL11.glEnable((int)2884);
        GL11.glPopMatrix();
    }

    public static void drawBlurredShadow(float x, float y, float width, float height, int blurRadius, Color color) {
        glPushMatrix();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.01f);

        width = width + blurRadius * 2;
        height = height + blurRadius * 2;
        x = x - blurRadius;
        y = y - blurRadius;

        float _X = x - 0.25f;
        float _Y = y + 0.25f;

        int identifier = (int) (width * height + width + color.hashCode() * blurRadius + blurRadius);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GlStateManager.enableBlend();

        int texId = -1;
        if (shadowCache.containsKey(identifier)) {
            texId = shadowCache.get(identifier);

            GlStateManager.bindTexture(texId);
        } else {
            if (width <= 0) width = 1;
            if (height <= 0) height = 1;
            BufferedImage original = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB_PRE);

            Graphics g = original.getGraphics();
            g.setColor(color);
            g.fillRect(blurRadius, blurRadius, (int) (width - blurRadius * 2), (int) (height - blurRadius * 2));
            g.dispose();

            GaussianFilter op = new GaussianFilter(blurRadius);

            BufferedImage blurred = op.filter(original, null);


            texId = TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), blurred, true, false);

            shadowCache.put(identifier, texId);
        }

        GL11.glColor4f(1f, 1f, 1f, 1f);

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0); // top left
        GL11.glVertex2f(_X, _Y);

        GL11.glTexCoord2f(0, 1); // bottom left
        GL11.glVertex2f(_X, _Y + height);

        GL11.glTexCoord2f(1, 1); // bottom right
        GL11.glVertex2f(_X + width, _Y + height);

        GL11.glTexCoord2f(1, 0); // top right
        GL11.glVertex2f(_X + width, _Y);
        GL11.glEnd();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.resetColor();

        glEnable(GL_CULL_FACE);
        glPopMatrix();
    }

    public static void drawGradientRected(float f, float sY, double width, double height, int colour1, int colour2) {
        cn.utils.miku.render.RenderUtil.drawGradientRect(f, sY, f + width, sY + height, colour1, colour2);
    }

    public static void stop() {
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        GL11.glEnable(2884);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        color(Color.white);
    }

    public static void vertex(double x, double y) {
        GL11.glVertex2d(x, y);
    }

    public static void begin(int glMode) {
        GL11.glBegin(glMode);
    }

    public static void end() {
        GL11.glEnd();
    }

    public static void polygon(double x, double y, double sideLength, int amountOfSides, boolean filled) {
        polygon(x, y, sideLength, amountOfSides, filled, null);
    }

    public static void polygon(double x, double y, double sideLength, int amountOfSides, Color color) {
        polygon(x, y, sideLength, amountOfSides, true, color);
    }

    public static void polygon(double x, double y, double sideLength, int amountOfSides) {
        polygon(x, y, sideLength, amountOfSides, true, null);
    }


    public static void polygon(double x, double y, double sideLength, double amountOfSides, boolean filled, Color color) {
        sideLength /= 2.0;
        start();
        if (color != null) {
            setGLColor(color);
        }
        if (!filled) {
            GL11.glLineWidth(2.0f);
        }
        GL11.glEnable(2848);
        begin(filled ? 6 : 3);
        for (double i = 0.0; i <= amountOfSides / 4.0; i += 1.0) {
            double angle = i * 4.0 * (Math.PI * 2) / 360.0;
            vertex(x + sideLength * Math.cos(angle) + sideLength, y + sideLength * Math.sin(angle) + sideLength);
        }
        end();
        GL11.glDisable(2848);
        stop();
    }

    public static void start() {
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
    }

    public static void glColor(int hex) {
        float alpha = (float)(hex >> 24 & 0xFF) / 255.0f;
        float red = (float)(hex >> 16 & 0xFF) / 255.0f;
        float green = (float)(hex >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hex & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
    }

    public static boolean isSliderHovered(float x1, float y1, float x2, float y2, int mouseX, int mouseY) {
        return (float)mouseX >= x1 && (float)mouseX <= x2 && (float)mouseY >= y1 && (float)mouseY <= y2;
    }

    public static Color getColorAlpha(int color, int alpha) {
        Color color2 = new Color(new Color(color).getRed(), new Color(color).getGreen(), new Color(color).getBlue(), alpha);
        return color2;
    }

    public static void scissor(double x, double y, double width, double height) {
        ScaledResolution sr = new ScaledResolution(mc2);
        double scale = sr.getScaleFactor();
        y = (double)sr.getScaledHeight() - y;
        GL11.glScissor((int)(x *= scale), (int)((y *= scale) - (height *= scale)), (int)(width *= scale), (int)height);
    }

    public static int rainbow(int delay) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + (long)delay) / 7L);
        return Color.getHSBColor((float)((rainbowState %= 360.0) / 360.0), 0.9f, 1.0f).getRGB();
    }

    public static Color setAlpha(int color, int alpha) {
        return new Color(new Color(color).getRed(), new Color(color).getGreen(), new Color(color).getBlue(), alpha);
    }

    public static int getColor(int red, int green, int blue, int alpha) {
        int color = WMathHelper.clamp_int(alpha, 0, 255) << 24;
        color |= WMathHelper.clamp_int(red, 0, 255) << 16;
        color |= WMathHelper.clamp_int(green, 0, 255) << 8;
        return color |= WMathHelper.clamp_int(blue, 0, 255);
    }

    public static void drawCircle(double x, double y, double radius, float startAngle, float endAngle, int color, float lineWidth) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)lineWidth);
        GL11.glBegin((int)3);
        for (int i = (int)((double)startAngle / 360.0 * 100.0); i <= (int)((double)endAngle / 360.0 * 100.0); ++i) {
            double angle = Math.PI * 2 * (double)i / 100.0 + Math.toRadians(180.0);
            if (color == 1337) {
                color(PaletteHelper.astolfoColors(i * 5, 1));
            } else {
                color(color);
            }
            GL11.glVertex2d((double)(x + Math.sin(angle) * radius), (double)(y + Math.cos(angle) * radius));
        }
        GL11.glEnd();
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GL11.glDisable((int)2848);
        GlStateManager.popMatrix();
        GlStateManager.resetColor();
    }

    public static void renderBlurredShadow(Color color, double x, double y, double width, double height, int blurRadius) {
        GlStateManager.resetColor();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2884);
        renderBlurredShadow(x, y, width, height, blurRadius, color);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2884);
        GL11.glEnable((int)3008);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)3042);
    }

    public static void renderBlurredShadow(double x, double y, double width, double height, int blurRadius, Color color) {
        GlStateManager.resetColor();
        GlStateManager.alphaFunc(516, 0.01f);
        float _X = (float)((x -= (double)blurRadius) - 0.25);
        float _Y = (float)((y -= (double)blurRadius) + 0.25);
        int identifier = (int)((width += (double)(blurRadius * 2)) * (height += (double)(blurRadius * 2)) + width + (double)(color.hashCode() * blurRadius) + (double)blurRadius);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2884);
        GL11.glEnable((int)3008);
        GL11.glEnable((int)3042);
        int texId = -1;
        if (shadowCache.containsKey(identifier)) {
            texId = shadowCache.get(identifier);
            GlStateManager.bindTexture(texId);
        } else {
            width = WMathHelper.clamp_double(width, 0.01, width);
            height = WMathHelper.clamp_double(height, 0.01, height);
            BufferedImage original = new BufferedImage((int)width, (int)height, 2);
            Graphics g = original.getGraphics();
            g.setColor(color);
            g.fillRect(blurRadius, blurRadius, (int)width - blurRadius * 2, (int)height - blurRadius * 2);
            g.dispose();
            GaussianFilter op = new GaussianFilter(blurRadius);
            BufferedImage blurred = op.filter(original, null);
            texId = TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), blurred, true, false);
            shadowCache.put(identifier, texId);
        }
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glBegin((int)7);
        GL11.glTexCoord2f((float)0.0f, (float)0.0f);
        GL11.glVertex2f((float)_X, (float)_Y);
        GL11.glTexCoord2f((float)0.0f, (float)1.0f);
        GL11.glVertex2f((float)_X, (float)(_Y + (float)((int)height)));
        GL11.glTexCoord2f((float)1.0f, (float)1.0f);
        GL11.glVertex2f((float)(_X + (float)((int)width)), (float)(_Y + (float)((int)height)));
        GL11.glTexCoord2f((float)1.0f, (float)0.0f);
        GL11.glVertex2f((float)(_X + (float)((int)width)), (float)_Y);
        GL11.glEnd();
        GL11.glDisable((int)3553);
    }

    public static void drawTexturedModalRect(ResourceLocation location, double x, double y, int textureX, int textureY, double width, double height) {
        boolean alpha_test = GL11.glIsEnabled((int)3008);
        GL11.glEnable((int)3008);
        Minecraft.getMinecraft().getTextureManager().bindTexture(location);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, y + height, 0.0).tex((float)textureX * 0.00390625f, (float)((double)textureY + height) * 0.00390625f).endVertex();
        bufferbuilder.pos(x + width, y + height, 0.0).tex((float)((double)textureX + width) * 0.00390625f, (float)((double)textureY + height) * 0.00390625f).endVertex();
        bufferbuilder.pos(x + width, y, 0.0).tex((float)((double)textureX + width) * 0.00390625f, (float)textureY * 0.00390625f).endVertex();
        bufferbuilder.pos(x, y, 0.0).tex((float)textureX * 0.00390625f, (float)textureY * 0.00390625f).endVertex();
        tessellator.draw();
        if (alpha_test) {
            GL11.glEnable((int)3008);
        } else {
            GL11.glDisable((int)3008);
        }
    }

    public static void drawTexturedRect(ResourceLocation location, double xStart, double yStart, double width, double height, double scale) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glScaled((double)scale, (double)scale, (double)scale);
        drawTexturedModalRect(location, xStart / scale, yStart / scale, 0, 0, width, height);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static float getRenderHurtTime(EntityLivingBase hurt) {
        return (float)hurt.hurtTime - (hurt.hurtTime != 0 ? Minecraft.getMinecraft().timer.renderPartialTicks : 0.0f);
    }

    public static float getHurtPercent(EntityLivingBase hurt) {
        return getRenderHurtTime(hurt) / 10.0f;
    }

    public static void drawRGBLineHorizontal(double x, double y, double width, float linewidth, float colors, boolean reverse) {
        GlStateManager.shadeModel(7425);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glLineWidth(linewidth);
        GL11.glBegin(3);
        colors = (float)((double)colors * width);
        double steps = width / (double)colors;
        double cX = x;
        double cX2 = x + steps;
        if (reverse) {
            for (float i = colors; i > 0.0f; i -= 1.0f) {
                int argbColor = rainbow((int)(i * 10.0f));
                float a = (float)(argbColor >> 24 & 0xFF) / 255.0f;
                float r = (float)(argbColor >> 16 & 0xFF) / 255.0f;
                float g = (float)(argbColor >> 8 & 0xFF) / 255.0f;
                float b = (float)(argbColor & 0xFF) / 255.0f;
                GlStateManager.color(r, g, b, a);
                GL11.glVertex2d(cX, y);
                GL11.glVertex2d(cX2, y);
                cX = cX2;
                cX2 += steps;
            }
        } else {
            int i = 0;
            while ((float)i < colors) {
                int argbColor = rainbow(i * 10);
                float a = (float)(argbColor >> 24 & 0xFF) / 255.0f;
                float r = (float)(argbColor >> 16 & 0xFF) / 255.0f;
                float g = (float)(argbColor >> 8 & 0xFF) / 255.0f;
                float b = (float)(argbColor & 0xFF) / 255.0f;
                GlStateManager.color(r, g, b, a);
                GL11.glVertex2d(cX, y);
                GL11.glVertex2d(cX2, y);
                cX = cX2;
                cX2 += steps;
                ++i;
            }
        }
        GL11.glEnd();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRoundedRect3(double x, double y, double width, double height, double cornerRadius, boolean leftTop, boolean rightTop, boolean rightBottom, boolean leftBottom, Color color) {
        int i;
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        setGLColor(color);
        GL11.glBegin(9);
        double cornerX = x + width - cornerRadius;
        double cornerY = y + height - cornerRadius;
        if (rightBottom) {
            for (i = 0; i <= 90; i += 30) {
                GL11.glVertex2d(cornerX + Math.sin((double)i * Math.PI / 180.0) * cornerRadius, cornerY + Math.cos((double)i * Math.PI / 180.0) * cornerRadius);
            }
        } else {
            GL11.glVertex2d(x + width, y + height);
        }
        if (rightTop) {
            cornerX = x + width - cornerRadius;
            cornerY = y + cornerRadius;
            for (i = 90; i <= 180; i += 30) {
                GL11.glVertex2d(cornerX + Math.sin((double)i * Math.PI / 180.0) * cornerRadius, cornerY + Math.cos((double)i * Math.PI / 180.0) * cornerRadius);
            }
        } else {
            GL11.glVertex2d(x + width, y);
        }
        if (leftTop) {
            cornerX = x + cornerRadius;
            cornerY = y + cornerRadius;
            for (i = 180; i <= 270; i += 30) {
                GL11.glVertex2d(cornerX + Math.sin((double)i * Math.PI / 180.0) * cornerRadius, cornerY + Math.cos((double)i * Math.PI / 180.0) * cornerRadius);
            }
        } else {
            GL11.glVertex2d(x, y);
        }
        if (leftBottom) {
            cornerX = x + cornerRadius;
            cornerY = y + height - cornerRadius;
            for (i = 270; i <= 360; i += 30) {
                GL11.glVertex2d(cornerX + Math.sin((double)i * Math.PI / 180.0) * cornerRadius, cornerY + Math.cos((double)i * Math.PI / 180.0) * cornerRadius);
            }
        } else {
            GL11.glVertex2d(x, y + height);
        }
        GL11.glEnd();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
        setGLColor(Color.white);
    }

    public static void drawRoundedRect2Alpha(double x, double y, double width, double height, double cornerRadius, Color color) {
        int i;
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        color(color);
        GL11.glBegin(9);
        double cornerX = x + width - cornerRadius;
        double cornerY = y + height - cornerRadius;
        for (i = 0; i <= 90; i += 30) {
            GL11.glVertex2d(cornerX + Math.sin((double)i * Math.PI / 180.0) * cornerRadius, cornerY + Math.cos((double)i * Math.PI / 180.0) * cornerRadius);
        }
        cornerX = x + width - cornerRadius;
        cornerY = y + cornerRadius;
        for (i = 90; i <= 180; i += 30) {
            GL11.glVertex2d(cornerX + Math.sin((double)i * Math.PI / 180.0) * cornerRadius, cornerY + Math.cos((double)i * Math.PI / 180.0) * cornerRadius);
        }
        cornerX = x + cornerRadius;
        cornerY = y + cornerRadius;
        for (i = 180; i <= 270; i += 30) {
            GL11.glVertex2d(cornerX + Math.sin((double)i * Math.PI / 180.0) * cornerRadius, cornerY + Math.cos((double)i * Math.PI / 180.0) * cornerRadius);
        }
        cornerX = x + cornerRadius;
        cornerY = y + height - cornerRadius;
        for (i = 270; i <= 360; i += 30) {
            GL11.glVertex2d(cornerX + Math.sin((double)i * Math.PI / 180.0) * cornerRadius, cornerY + Math.cos((double)i * Math.PI / 180.0) * cornerRadius);
        }
        GL11.glEnd();
        color(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }

    public static void drawRoundedRectSmooth(double x, double y, double width, double height, double cornerRadius, Color color) {
        int i;
        GL11.glPushMatrix();
        GL11.glDisable(3008);
        GL11.glEnable(2881);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        color(color);
        GL11.glBegin(9);
        double cornerX = x + width - cornerRadius;
        double cornerY = y + height - cornerRadius;
        for (i = 0; i <= 90; i += 30) {
            GL11.glVertex2d(cornerX + Math.sin((double)i * Math.PI / 180.0) * cornerRadius, cornerY + Math.cos((double)i * Math.PI / 180.0) * cornerRadius);
        }
        cornerX = x + width - cornerRadius;
        cornerY = y + cornerRadius;
        for (i = 90; i <= 180; i += 30) {
            GL11.glVertex2d(cornerX + Math.sin((double)i * Math.PI / 180.0) * cornerRadius, cornerY + Math.cos((double)i * Math.PI / 180.0) * cornerRadius);
        }
        cornerX = x + cornerRadius;
        cornerY = y + cornerRadius;
        for (i = 180; i <= 270; i += 30) {
            GL11.glVertex2d(cornerX + Math.sin((double)i * Math.PI / 180.0) * cornerRadius, cornerY + Math.cos((double)i * Math.PI / 180.0) * cornerRadius);
        }
        cornerX = x + cornerRadius;
        cornerY = y + height - cornerRadius;
        for (i = 270; i <= 360; i += 30) {
            GL11.glVertex2d(cornerX + Math.sin((double)i * Math.PI / 180.0) * cornerRadius, cornerY + Math.cos((double)i * Math.PI / 180.0) * cornerRadius);
        }
        GL11.glEnd();
        color(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDisable(2881);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }

    public static void drawRoundRectTest(float x1, float y1, float x2, float y2, float radius, int steps, boolean leftTop, boolean rightTop, boolean leftBottom, boolean rightBottom, Color color) {
        int i;
        GL11.glPushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        color(color);
        GL11.glBegin(9);
        radius = Math.min(Math.min(Math.abs(y2 - y1) / 2.0f, Math.abs(x2 - x1) / 2.0f), radius);
        steps = Math.max(1, steps - steps % 3);
        if (rightBottom) {
            for (i = 0; i <= 90; i += steps) {
                GL11.glVertex2d((double)(x2 - radius) + Math.sin(Math.toRadians(i)) * (double)radius, (double)(y2 - radius) + Math.cos(Math.toRadians(i)) * (double)radius);
            }
        } else {
            GL11.glVertex2d(x2, y2);
        }
        if (rightTop) {
            for (i = 90; i <= 180; i += steps) {
                GL11.glVertex2d((double)(x2 - radius) + Math.sin(Math.toRadians(i)) * (double)radius, (double)(y1 + radius) + Math.cos(Math.toRadians(i)) * (double)radius);
            }
        } else {
            GL11.glVertex2d(x2, y1);
        }
        if (leftTop) {
            for (i = 180; i <= 270; i += steps) {
                GL11.glVertex2d((double)(x1 + radius) + Math.sin(Math.toRadians(i)) * (double)radius, (double)(y1 + radius) + Math.cos(Math.toRadians(i)) * (double)radius);
            }
        } else {
            GL11.glVertex2d(x1, y1);
        }
        if (leftBottom) {
            for (i = 270; i <= 360; i += steps) {
                GL11.glVertex2d((double)(x1 + radius) + Math.sin(Math.toRadians(i)) * (double)radius, (double)(y2 - radius) + Math.cos(Math.toRadians(i)) * (double)radius);
            }
        } else {
            GL11.glVertex2d(x1, y2);
        }
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        color(Color.white);
        GL11.glPopMatrix();
    }

    public static void drawTriangleFilled(float x, float y, float width, float height, int color) {
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glEnable(2848);
        GL11.glLineWidth(1.0f);
        GL11.glBegin(1);
        glColor(color);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x + width, y + height);
        GL11.glEnd();
        GL11.glBegin(1);
        GL11.glVertex2d(x + width, y + height);
        GL11.glVertex2d(x + width * 2.0f, y);
        GL11.glEnd();
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
    }

    public static double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    public static void drawImage(int x, int y, int width, int height, ResourceLocation resourceLocation) {
        GlStateManager.enableAlpha();
        mc2.getTextureManager().bindTexture(resourceLocation);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public static void drawImageDarker(int x, int y, int width, int height, ResourceLocation resourceLocation) {
        mc2.getTextureManager().bindTexture(resourceLocation);
        GL11.glColor4f(0.19607843f, 0.19607843f, 0.19607843f, 1.0f);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void drawHorizontalGradientRect(double x, double y, double width, double height, Color right, Color left) {
        pushAttribAndMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glBegin(GL11.GL_QUADS);

        ColorUtils.glColor(right);
        GL11.glVertex2d(x, y + height);

        ColorUtils.glColor(left);
        GL11.glVertex2d(x + width, y + height);

        ColorUtils.glColor(left);
        GL11.glVertex2d(x + width, y);

        ColorUtils.glColor(right);
        GL11.glVertex2d(x, y);

        GL11.glEnd();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        popAttribAndMatrix();
    }

    public void drawVerticalGradientRect(double x, double y, double width, double height, Color top, Color bottom) {
        pushAttribAndMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glBegin(GL11.GL_QUADS);

        ColorUtils.glColor(bottom);
        GL11.glVertex2d(x, y + height);

        ColorUtils.glColor(bottom);
        GL11.glVertex2d(x + width, y + height);

        ColorUtils.glColor(top);
        GL11.glVertex2d(x + width, y);

        ColorUtils.glColor(top);
        GL11.glVertex2d(x, y);

        GL11.glEnd();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        popAttribAndMatrix();
    }

    public void drawRainbowRect(double x, double y, double x2, double y2, int wave, float saturation, double speed) {
        for (int i = 0; i < x2 - x; ++i) {
            drawRect(x + i, y, x + i + 1, y2, ColorUtils.rainbow(i * wave, saturation, speed).getRGB());
        }
    }

    public void renderSkinHead(EntityLivingBase player, double x, double y, int size) {
        renderSkinHead(player, x, y, size, Color.WHITE);
    }

    public void renderSkinHead(EntityLivingBase player, double x, double y, int size, Color color) {
        if (!(player instanceof EntityPlayer))
            return;

        try {
            GL11.glPushMatrix();
            mc2.getTextureManager().bindTexture(((AbstractClientPlayer) player).getLocationSkin());
            GL11.glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);

            Gui.drawScaledCustomSizeModalRect((int) x, (int) y, 8, 8, 8, 8, size, size, 64, 64);
            GL11.glPopMatrix();
        } catch (Exception ignored) {
        }
    }

    public static void drawRect(double x, double y, double x2, double y2, int color) {
        GlStateManager.color(1, 1, 1, 1);
        RenderUtil.drawRect(x, y, x2, y2, color);
    }
    public static void preRenderShade() {
        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GlStateManager.disableCull();
    }

    public static void postRenderShade() {
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }

    public static void startScissorBox() {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
    }

    public static void drawScissorBox(double x, double y, double width, double height) {
        width = Math.max(width, 0.1);

        ScaledResolution sr = new ScaledResolution(mc2);
        double scale = sr.getScaleFactor();

        y = sr.getScaledHeight() - y;

        x *= scale;
        y *= scale;
        width *= scale;
        height *= scale;

        GL11.glScissor((int) x, (int) (y - height), (int) width, (int) height);
    }

    public static void endScissorBox() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();
    }

    public static void drawCircleArc(double x, double y, double width, double height, Color color) {
        drawArc(x + width / 2f, y + height / 2f, width / 2f, 0, 360f, color);
    }

    public static double ticks = 0;
    public static long lastFrame = 0;

    public static void drawCircle(Entity entity, double rad, int color, boolean shade) {
        ticks += .004 * (System.currentTimeMillis() - lastFrame);

        lastFrame = System.currentTimeMillis();

        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glEnable(2832);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glHint(3153, 4354);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        if (shade) GL11.glShadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableCull();

        Color c = new Color(color);

        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX)
                * Minecraft.getMinecraft().timer.renderPartialTicks - renderManager.renderPosX;
        double y = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY)
                * Minecraft.getMinecraft().timer.renderPartialTicks - renderManager.renderPosY) + Math.sin(ticks) + 1;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ)
                * Minecraft.getMinecraft().timer.renderPartialTicks - renderManager.renderPosZ;

        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);

        double TAU = Math.PI * 2.D;
        for (float i = 0; i < TAU; i += TAU / 64.F) {

            double vecX = x + rad * Math.cos(i);
            double vecZ = z + rad * Math.sin(i);

            if (shade) {
                GL11.glColor4f(c.getRed() / 255.F,
                        c.getGreen() / 255.F,
                        c.getBlue() / 255.F,
                        0.05F
                );

                GL11.glVertex3d(vecX, y - Math.sin(ticks + 1) / 2.7f, vecZ);
            }

            GL11.glColor4f(c.getRed() / 255.F,
                    c.getGreen() / 255.F,
                    c.getBlue() / 255.F,
                    0.56F
            );

            GL11.glVertex3d(vecX, y, vecZ);
        }

        GL11.glColor4f(1, 1, 1, 1);
        GL11.glEnd();
        if (shade) GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GlStateManager.enableCull();
        GL11.glDisable(2848);
        GL11.glEnable(2832);
        GL11.glEnable(3553);
        GL11.glPopMatrix();

        pushAttribAndMatrix();
        GL11.glPushMatrix();
        mc2.entityRenderer.disableLightmap();
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2929);
        GL11.glEnable(2848);
        GL11.glDepthMask(false);
        GL11.glPushMatrix();
        GL11.glLineWidth(2);
        ColorUtils.glColor(new Color(color));
        GL11.glBegin(1);
        for (int i = 0; i <= 90; ++i) {
            ColorUtils.glColor(new Color(color));
            GL11.glVertex3d(x + rad * Math.cos((double) i * (Math.PI * 2) / 45), y, z + rad * Math.sin((double) i * (Math.PI * 2) / 45));
        }
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glDepthMask(true);
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        mc2.entityRenderer.enableLightmap();
        GL11.glPopMatrix();
        popAttribAndMatrix();
    }

    public static void drawRoundedRectNew(double x, double y, double width, double height, double cornerRadius, Color color) {
        drawRect(x, y + cornerRadius, x + cornerRadius, y + height - cornerRadius, color.getRGB());
        drawRect(x + cornerRadius, y, x + width - cornerRadius, y + height, color.getRGB());
        drawRect(x + width - cornerRadius, y + cornerRadius, x + width, y + height - cornerRadius, color.getRGB());
        drawArc(x + cornerRadius, y + cornerRadius, cornerRadius, 0, 90, color);
        drawArc(x + width - cornerRadius, y + cornerRadius, cornerRadius, 270, 360, color);
        drawArc(x + width - cornerRadius, y + height - cornerRadius, cornerRadius, 180, 270, color);
        drawArc(x + cornerRadius, y + height - cornerRadius, cornerRadius, 90, 180, color);
    }

    public static void drawRoundedRectOutlineNew(double x, double y, double width, double height, double cornerRadius, Color color) {
        drawRect(x - 0.5, y + cornerRadius, x + 0.5, y + height - cornerRadius, color.getRGB());
        drawRect(x + width - 0.5, y + cornerRadius, x + width + 0.5, y + height - cornerRadius, color.getRGB());

        drawRect(x + cornerRadius, y - 0.5, x + width - cornerRadius, y + 0.5, color.getRGB());
        drawRect(x + cornerRadius, y + height - 0.5, x + width - cornerRadius, y + height + 0.5, color.getRGB());

        drawArcOutline(x + cornerRadius, y + cornerRadius, cornerRadius, 0, 90, 2, color);
        drawArcOutline(x + width - cornerRadius, y + cornerRadius, cornerRadius, 270, 360, 2, color);
        drawArcOutline(x + width - cornerRadius, y + height - cornerRadius, cornerRadius, 180, 270, 2, color);
        drawArcOutline(x + cornerRadius, y + height - cornerRadius, cornerRadius, 90, 180, 2, color);
    }

    public static void drawArc(double x, double y, double radius, double startAngle, double endAngle, Color color) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);

        VertexUtils.start(6);
        VertexUtils.add(x, y, color);

        for (double i = (startAngle / 360.0 * 100); i <= (endAngle / 360.0 * 100); i++) {
            double angle = (Math.PI * 2 * i / 100) + Math.toRadians(180);
            VertexUtils.add(x + Math.sin(angle) * radius, y + Math.cos(angle) * radius, color);
        }

        VertexUtils.end();

        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawCircleOutline(double x, double y, double width, double height, float lineWidth, Color color) {
        drawArcOutline(x + width / 2f, y + height / 2f, width / 2f, 0, 360f, lineWidth, color);
    }

    public static void drawCheck(double x, double y, Color color) {
        GlStateManager.pushMatrix();
        preRenderShade();
        GL11.glLineWidth(2);

        VertexUtils.start(GL11.GL_LINE_STRIP);

        VertexUtils.add(x + 1, y, color);
        VertexUtils.add(x + 3, y + 3.5, color);
        VertexUtils.add(x + 7, y - 2.5, color);

        VertexUtils.end();

        postRenderShade();
        GlStateManager.popMatrix();
    }

    public static void drawArcOutline(double x, double y, double radius, double startAngle, double endAngle, float lineWidth, Color color) {
        GlStateManager.pushMatrix();
        preRenderShade();
        GL11.glLineWidth(lineWidth);

        VertexUtils.start(GL11.GL_LINE_STRIP);

        for (double i = (startAngle / 360.0 * 100); i <= (endAngle / 360.0 * 100); i++) {
            double angle = (Math.PI * 2 * i / 100) + Math.toRadians(180);
            VertexUtils.add(x + Math.sin(angle) * radius, y + Math.cos(angle) * radius, color);
        }

        VertexUtils.end();
        postRenderShade();
        GlStateManager.popMatrix();
    }

    public static void drawBorderedRect(double x, double y,
                                 double x1, double y1, double width, int internalColor, int borderColor) {
        drawRect(x + width, y + width, x1 - width, y1 - width, internalColor);
        drawRect(x + width, y, x1 - width, y + width, borderColor);
        drawRect(x, y, x + width, y1, borderColor);
        drawRect(x1 - width, y, x1, y1, borderColor);
        drawRect(x + width, y1 - width, x1 - width, y1, borderColor);
    }
    public static void bindFrameBuffer(double x, double y, double width, double height, Framebuffer framebuffer) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, framebuffer.framebufferTexture);
        drawQuad(x, y, width, height);
    }

    public static void drawSmoothRect(double left, double top, double right, double bottom, int color) {
        GlStateManager.resetColor();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        drawRect(left, top, right, bottom, color);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        drawRect(left * 2.0f - 1.0f, top * 2.0f, left * 2.0f, bottom * 2.0f - 1.0f, color);
        drawRect(left * 2.0f, top * 2.0f - 1.0f, right * 2.0f, top * 2.0f, color);
        drawRect(right * 2.0f, top * 2.0f, right * 2.0f + 1.0f, bottom * 2.0f - 1.0f, color);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
    }

    public static void drawColorRect(double left, double top, double right, double bottom, Color color1, Color color2, Color color3, Color color4)
    {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glPushMatrix();
        GL11.glBegin(GL11.GL_QUADS);
        glColor(color2);
        GL11.glVertex2d(left, bottom);
        glColor(color3);
        GL11.glVertex2d(right, bottom);
        glColor(color4);
        GL11.glVertex2d(right, top);
        glColor(color1);
        GL11.glVertex2d(left, top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glShadeModel(GL11.GL_FLAT);
        Gui.drawRect(0, 0, 0, 0, 0);
    }

    public static void renderShadowVertical(Color c, float lineWidth, double startAlpha, int size, double posX, double posY1, double posY2, boolean right, boolean edges)
    {
        GlStateManager.resetColor();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_CULL_FACE);
        renderShadowVertical(lineWidth, startAlpha, size, posX, posY1, posY2, right, edges, (float)c.getRed() / 255.0f, (float)c.getGreen() / 255.0f, (float)c.getBlue() / 255.0f);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
    }

    public static void renderShadowVertical(float lineWidth, double startAlpha, int size, double posX, double posY1, double posY2, boolean right, boolean edges, float red, float green, float blue)
    {
        double alpha = startAlpha;
        GlStateManager.alphaFunc(516, 0.0f);
        GL11.glLineWidth(lineWidth);

        if (right)
        {
            for (double x = 0.5; x < (double)size; x += 0.5)
            {
                GL11.glColor4d(red, green, blue, alpha);
                GL11.glBegin(GL11.GL_LINES);
                GL11.glVertex2d(posX + x, posY1 - (edges ? x : 0.0));
                GL11.glVertex2d(posX + x, posY2 + (edges ? x : 0.0));
                GL11.glEnd();
                alpha = startAlpha - x / (double)size;
            }
        }
        else
        {
            for (double x = 0.0; x < (double)size; x += 0.5)
            {
                GL11.glColor4d(red, green, blue, alpha);
                GL11.glBegin(GL11.GL_LINES);
                GL11.glVertex2d(posX - x, posY1 - (edges ? x : 0.0));
                GL11.glVertex2d(posX - x, posY2 + (edges ? x : 0.0));
                GL11.glEnd();
                alpha = startAlpha - x / (double)size;
            }
        }
    }

    public static void drawNewRect(double left, double top, double right, double bottom, int color) {
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }
        float f3 = (float)(color >> 24 & 0xFF) / 255.0f;
        float f = (float)(color >> 16 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f2 = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.color(f, f1, f2, f3);
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(left, bottom, 0.0).endVertex();
        vertexbuffer.pos(right, bottom, 0.0).endVertex();
        vertexbuffer.pos(right, top, 0.0).endVertex();
        vertexbuffer.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawGradientRect(double d, double e, double e2, double g, int startColor, int endColor)
    {
        float f = (float)(startColor >> 24 & 255) / 255.0F;
        float f1 = (float)(startColor >> 16 & 255) / 255.0F;
        float f2 = (float)(startColor >> 8 & 255) / 255.0F;
        float f3 = (float)(startColor & 255) / 255.0F;
        float f4 = (float)(endColor >> 24 & 255) / 255.0F;
        float f5 = (float)(endColor >> 16 & 255) / 255.0F;
        float f6 = (float)(endColor >> 8 & 255) / 255.0F;
        float f7 = (float)(endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double)e2, (double)e, (double)zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double)d, (double)e, (double)zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double)d, (double)g, (double)zLevel).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos((double)e2, (double)g, (double)zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (col1 >> 24 & 255) / 255.0f;
        float f1 = (col1 >> 16 & 255) / 255.0f;
        float f2 = (col1 >> 8 & 255) / 255.0f;
        float f3 = (col1 & 255) / 255.0f;
        float f4 = (col2 >> 24 & 255) / 255.0f;
        float f5 = (col2 >> 16 & 255) / 255.0f;
        float f6 = (col2 >> 8 & 255) / 255.0f;
        float f7 = (col2 & 255) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glVertex2d(left, top);
        GL11.glVertex2d(left, bottom);
        GL11.glColor4f(f, f6, f7, f4);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }

    public static void renderItem(ItemStack itemStack, int x, int y) {
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.enableDepth();
        RenderHelper.enableGUIStandardItemLighting();
        mc2.getRenderItem().renderItemAndEffectIntoGUI(itemStack, x, y);
        mc2.getRenderItem().renderItemOverlays(mc2.fontRenderer, itemStack, x, y);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableDepth();
    }

    public static void drawQuad(double x, double y, double width, double height) {
        GL11.glBegin(GL11.GL_QUADS);

        GL11.glTexCoord2d(0, 1);
        GL11.glVertex2d(x, y);

        GL11.glTexCoord2d(0, 0);
        GL11.glVertex2d(x, y + height);

        GL11.glTexCoord2d(1, 0);
        GL11.glVertex2d(x + width, y + height);

        GL11.glTexCoord2d(1, 1);
        GL11.glVertex2d(x + width, y);

        GL11.glEnd();
    }

    public static Framebuffer createFrameBuffer(Framebuffer framebuffer) {
        if (framebuffer == null || framebuffer.framebufferWidth != mc2.displayWidth
                || framebuffer.framebufferHeight != mc2.displayHeight) {
            if (framebuffer != null) {
                framebuffer.deleteFramebuffer();
            }

            return new Framebuffer(mc2.displayWidth, mc2.displayHeight, true);
        }

        return framebuffer;
    }

    public static void preLight() {
        mc2.entityRenderer.disableLightmap();
    }

    public static void postLight() {
        mc2.entityRenderer.enableLightmap();
    }

    public static void drawRoundedRect2(double x, double y, double width, double height, double cornerRadius, int color) {
        drawRoundedRect2(x, y, width, height, cornerRadius, true, true, true, true, color);
    }

    public static void drawRoundedRect(double x, double y, double width, double height, double cornerRadius, int color) {
        int i;
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        setGLColor(color);
        GL11.glBegin(9);
        double cornerX = x + width - cornerRadius;
        double cornerY = y + height - cornerRadius;
        for (i = 0; i <= 90; i += 30) {
            GL11.glVertex2d(cornerX + Math.sin((double)i * Math.PI / 180.0) * cornerRadius, cornerY + Math.cos((double)i * Math.PI / 180.0) * cornerRadius);
        }
        cornerX = x + width - cornerRadius;
        cornerY = y + cornerRadius;
        for (i = 90; i <= 180; i += 30) {
            GL11.glVertex2d(cornerX + Math.sin((double)i * Math.PI / 180.0) * cornerRadius, cornerY + Math.cos((double)i * Math.PI / 180.0) * cornerRadius);
        }
        cornerX = x + cornerRadius;
        cornerY = y + cornerRadius;
        for (i = 180; i <= 270; i += 30) {
            GL11.glVertex2d(cornerX + Math.sin((double)i * Math.PI / 180.0) * cornerRadius, cornerY + Math.cos((double)i * Math.PI / 180.0) * cornerRadius);
        }
        cornerX = x + cornerRadius;
        cornerY = y + height - cornerRadius;
        for (i = 270; i <= 360; i += 30) {
            GL11.glVertex2d(cornerX + Math.sin((double)i * Math.PI / 180.0) * cornerRadius, cornerY + Math.cos((double)i * Math.PI / 180.0) * cornerRadius);
        }
        GL11.glEnd();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
        setGLColor(Color.white);
    }

    public static void drawRoundedRect2(double x, double y, double width, double height, double cornerRadius, boolean leftTop, boolean rightTop, boolean rightBottom, boolean leftBottom, int color) {
        int i;
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glEnable(3042);
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        setGLColor(color);
        GL11.glBegin(9);
        double cornerX = x + width - cornerRadius;
        double cornerY = y + height - cornerRadius;
        if (rightBottom) {
            for (i = 0; i <= 90; ++i) {
                GL11.glVertex2d(cornerX + Math.sin((double)i * Math.PI / 180.0) * cornerRadius, cornerY + Math.cos((double)i * Math.PI / 180.0) * cornerRadius);
            }
        } else {
            GL11.glVertex2d(x + width, y + height);
        }
        if (rightTop) {
            cornerX = x + width - cornerRadius;
            cornerY = y + cornerRadius;
            for (i = 90; i <= 180; ++i) {
                GL11.glVertex2d(cornerX + Math.sin((double)i * Math.PI / 180.0) * cornerRadius, cornerY + Math.cos((double)i * Math.PI / 180.0) * cornerRadius);
            }
        } else {
            GL11.glVertex2d(x + width, y);
        }
        if (leftTop) {
            cornerX = x + cornerRadius;
            cornerY = y + cornerRadius;
            for (i = 180; i <= 270; ++i) {
                GL11.glVertex2d(cornerX + Math.sin((double)i * Math.PI / 180.0) * cornerRadius, cornerY + Math.cos((double)i * Math.PI / 180.0) * cornerRadius);
            }
        } else {
            GL11.glVertex2d(x, y);
        }
        if (leftBottom) {
            cornerX = x + cornerRadius;
            cornerY = y + height - cornerRadius;
            for (i = 270; i <= 360; ++i) {
                GL11.glVertex2d(cornerX + Math.sin((double)i * Math.PI / 180.0) * cornerRadius, cornerY + Math.cos((double)i * Math.PI / 180.0) * cornerRadius);
            }
        } else {
            GL11.glVertex2d(x, y + height);
        }
        GL11.glEnd();
        setGLColor(new Color(255, 255, 255, 255));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }

    public static void drawRoundedRectGradient(double x, double y, double width, double height, double cornerRadius, Color start, Color end) {
        int i;
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425);
        color(start);
        GL11.glBegin(9);
        double cornerX = x + width - cornerRadius;
        double cornerY = y + height - cornerRadius;
        for (i = 0; i <= 90; i += 30) {
            GL11.glVertex2d(cornerX + Math.sin((double)i * Math.PI / 180.0) * cornerRadius, cornerY + Math.cos((double)i * Math.PI / 180.0) * cornerRadius);
        }
        cornerX = x + width - cornerRadius;
        cornerY = y + cornerRadius;
        for (i = 90; i <= 180; i += 30) {
            GL11.glVertex2d(cornerX + Math.sin((double)i * Math.PI / 180.0) * cornerRadius, cornerY + Math.cos((double)i * Math.PI / 180.0) * cornerRadius);
        }
        color(end);
        cornerX = x + cornerRadius;
        cornerY = y + cornerRadius;
        for (i = 180; i <= 270; i += 30) {
            GL11.glVertex2d(cornerX + Math.sin((double)i * Math.PI / 180.0) * cornerRadius, cornerY + Math.cos((double)i * Math.PI / 180.0) * cornerRadius);
        }
        cornerX = x + cornerRadius;
        cornerY = y + height - cornerRadius;
        for (i = 270; i <= 360; i += 30) {
            GL11.glVertex2d(cornerX + Math.sin((double)i * Math.PI / 180.0) * cornerRadius, cornerY + Math.cos((double)i * Math.PI / 180.0) * cornerRadius);
        }
        GL11.glEnd();
        GL11.glShadeModel(7424);
        color(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }
    public static void bindTexture(int texture) {
        glBindTexture(GL_TEXTURE_2D, texture);
    }

    public static Color injectAlpha(Color color, int alpha) {
        alpha = WMathHelper.clamp_int(alpha, 0, 255);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
    public static void drawBlur(float radius, Runnable data) {
        StencilUtil.initStencilToWrite();
        data.run();
        StencilUtil.readStencilBuffer(1);
        GaussianBlur.renderBlur(radius);
        StencilUtil.uninitStencilBuffer();
    }

    public static void stuffToBlur(boolean bloom) {

        // Gui.drawRect2(40, 40, 400, 40, -1);

    }

    private static Framebuffer bloomFramebuffer = new Framebuffer(1, 1, false);

    public static void drawShadow(float radius, float offset, Runnable data) {
        bloomFramebuffer = VisualBase.createFrameBuffer(bloomFramebuffer);
        bloomFramebuffer.framebufferClear();
        bloomFramebuffer.bindFramebuffer(true);
        data.run();
        stuffToBlur(true);
        bloomFramebuffer.unbindFramebuffer();
        BloomUtil.renderBlur(bloomFramebuffer.framebufferTexture, (int) radius, (int) offset);
    }

    public static void setAlphaLimit(float limit) {
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL_GREATER, (float) (limit * .01));
    }

    public static void drawFilledCircle(float xx, float yy, float radius, Color color) {
        int sections = 50;
        double dAngle = 6.283185307179586D / sections;
        GL11.glPushAttrib(8192);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glBegin(6);
        for (int i = 0; i < sections; i++) {
            float x = (float) (radius * Math.sin(i * dAngle));
            float y = (float) (radius * Math.cos(i * dAngle));
            GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F,
                    color.getAlpha() / 255.0F);
            GL11.glVertex2f(xx + x, yy + y);
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnd();
        GL11.glPopAttrib();
    }

    public static void renderBlur(int x, int y, int width, int height, int blurRadius) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        blurAreaBoarder(x, y, width, height, blurRadius);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2884);
        GL11.glEnable(3008);
        GL11.glEnable(3553);
        GL11.glEnable(3042);
    }
    public static void renderBlur(int x, int y, int width, int height, int blurWidth, int blurHeight, int blurRadius) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        blurAreaBoarder(x, y, width, height, blurRadius, blurWidth, blurHeight);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2884);
        GL11.glEnable(3008);
        GL11.glEnable(3553);
        GL11.glEnable(3042);
    }

    public static void roundedBorder(float x, float y, float x2, float y2, float radius, int color) {
        float left = x;
        float top = y;
        float bottom = y2;
        float right = x2;
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_NICEST);
        setColor(color);
        glLineWidth(2);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex2d(left, top + radius);
        GL11.glVertex2f(left + radius, top);
        GL11.glVertex2f(right - radius, top);
        GL11.glVertex2f(right, top + radius);
        GL11.glVertex2f(right, bottom - radius);
        GL11.glVertex2f(right - radius, bottom);
        GL11.glVertex2f(left + radius, bottom);
        GL11.glVertex2f(left, bottom - radius);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);
        GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_DONT_CARE);
    }

    public static void prepareScissorBox(int factor, float height, float x, float y, float x2, float y2) {

        GL11.glScissor((int) (x * factor), (int) ((height - y2) * factor), (int) ((x2 - x) * factor), (int) ((y2 - y) * factor));

    }
    public static void drawCircle(float x, float y, float start, float end, float radius, boolean filled, Color color) {
        float sin;
        float cos;
        float i;
        GlStateManager.color(0, 0, 0, 0);

        float endOffset;
        if (start > end) {
            endOffset = end;
            end = start;
            start = endOffset;
        }

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        setColor(color.getRGB());
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(2);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        for (i = end; i >= start; i -= 4) {
            cos = (float) (Math.cos(i * Math.PI / 180) * radius * 1);
            sin = (float) (Math.sin(i * Math.PI / 180) * radius * 1);
            GL11.glVertex2f(x + cos, y + sin);
        }
        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);

        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBegin(filled ? GL11.GL_TRIANGLE_FAN : GL11.GL_LINE_STRIP);
        for (i = end; i >= start; i -= 4) {
            cos = (float) Math.cos(i * Math.PI / 180) * radius;
            sin = (float) Math.sin(i * Math.PI / 180) * radius;
            GL11.glVertex2f(x + cos, y + sin);
        }
        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void roundedBorder(float x, float y, float x2, float y2, float radius, float line, int color) {
        float left = x;
        float top = y;
        float bottom = y2;
        float right = x2;
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_NICEST);
        setColor(color);
        glLineWidth(line);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex2d(left, top + radius);
        GL11.glVertex2f(left + radius, top);
        GL11.glVertex2f(right - radius, top);
        GL11.glVertex2f(right, top + radius);
        GL11.glVertex2f(right, bottom - radius);
        GL11.glVertex2f(right - radius, bottom);
        GL11.glVertex2f(left + radius, bottom);
        GL11.glVertex2f(left, bottom - radius);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);
        GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_DONT_CARE);
    }

    public static void drawBlurredShadowCircle(float x, float y, float width, float height, int blurRadius, Color color) {
        glPushMatrix();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.01f);

        width = width + blurRadius * 2;
        height = height + blurRadius * 2;
        x = x - blurRadius;
        y = y - blurRadius;

        float _X = x - 0.25f;
        float _Y = y + 0.25f;

        int identifier = (int) (width * height + width + color.hashCode() * blurRadius + blurRadius);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GlStateManager.enableBlend();

        int texId = -1;
        if (shadowCache.containsKey(identifier)) {
            texId = shadowCache.get(identifier);

            GlStateManager.bindTexture(texId);
        } else {
            BufferedImage original = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB_PRE);

            Graphics g = original.getGraphics();
            g.setColor(color);
            g.fillOval(blurRadius, blurRadius, (int) (width - blurRadius * 2), (int) (height - blurRadius * 2));
            g.dispose();

            GaussianFilter op = new GaussianFilter(blurRadius);

            BufferedImage blurred = op.filter(original, null);


            texId = TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), blurred, true, false);

            shadowCache.put(identifier, texId);
        }

        GL11.glColor4f(1f, 1f, 1f, 1f);

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0); // top left
        GL11.glVertex2f(_X, _Y);

        GL11.glTexCoord2f(0, 1); // bottom left
        GL11.glVertex2f(_X, _Y + height);

        GL11.glTexCoord2f(1, 1); // bottom right
        GL11.glVertex2f(_X + width, _Y + height);

        GL11.glTexCoord2f(1, 0); // top right
        GL11.glVertex2f(_X + width, _Y);
        GL11.glEnd();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        glEnable(GL_CULL_FACE);
        glPopMatrix();
    }

    public static void setColor(int color) {
        GL11.glColor4ub((byte) (color >> 16 & 0xFF), (byte) (color >> 8 & 0xFF), (byte) (color & 0xFF), (byte) (color >> 24 & 0xFF));
    }
    private static ShaderGroup blurShaderW;

    private static void shaderConfigFix(float intensity, float blurWidth, float blurHeight) {
        blurShaderW.listShaders.get(0).getShaderManager().getShaderUniform("Radius").set(intensity);
        blurShaderW.listShaders.get(1).getShaderManager().getShaderUniform("Radius").set(intensity);
        blurShaderW.listShaders.get(0).getShaderManager().getShaderUniform("BlurDir").set(blurWidth, blurHeight);
        blurShaderW.listShaders.get(1).getShaderManager().getShaderUniform("BlurDir").set(blurHeight, blurWidth);
    }

    public static void blurAreaBoarder(float x, float f, float width, float height, float intensity, float blurWidth, float blurHeight) {
        ScaledResolution scale = new ScaledResolution(mc2);
        int factor = scale.getScaleFactor();
        int factor2 = scale.getScaledWidth();
        int factor3 = scale.getScaledHeight();
        if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null || blurShader == null) {
            inShaderFBO();
        }
        lastScale = factor;
        lastScaleWidth = factor2;
        lastScaleHeight = factor3;
        GL11.glScissor((int) (x * (float) factor), (int) ((float) mc2.displayHeight - f * (float) factor - height * (float) factor) + 1, (int) (width * (float) factor), (int) (height * (float) factor));
        GL11.glEnable(3089);
        shaderConfigFix(intensity, blurWidth, blurHeight);
        buffer.bindFramebuffer(true);
        blurShader.render(mc2.timer.renderPartialTicks);
        mc.getFramebuffer().bindFramebuffer(true);
        GL11.glDisable(3089);
    }

    public static void blurAreaBoarder(int x, int y, int width, int height, float intensity) {
        ScaledResolution scale = new ScaledResolution(mc2);
        int factor = scale.getScaleFactor();
        int factor2 = scale.getScaledWidth();
        int factor3 = scale.getScaledHeight();
        if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null || blurShader == null) {
            inShaderFBO();
        }
        lastScale = factor;
        lastScaleWidth = factor2;
        lastScaleHeight = factor3;
        GL11.glScissor(x * factor, mc2.displayHeight - y * factor - height * factor, width * factor, height * factor);
        GL11.glEnable(3089);
        shaderConfigFix(intensity, 1.0f, 0.0f);
        buffer.bindFramebuffer(true);
        blurShader.render(mc2.timer.renderPartialTicks);
        mc.getFramebuffer().bindFramebuffer(true);
        GL11.glDisable(3089);
    }


    public static void color(float red, float green, float blue, float alpha) {
        GL11.glColor4f(red / 255.0f, green / 255.0f, blue / 255.0f, alpha / 255.0f);
    }

    public static void color(Color color) {
        color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }


    public static void setGLColor(Color color) {
        float r = (float)color.getRed() / 255.0f;
        float g = (float)color.getGreen() / 255.0f;
        float b = (float)color.getBlue() / 255.0f;
        float a = (float)color.getAlpha() / 255.0f;
        GL11.glColor4f(r, g, b, a);
    }

    public static void setGLColor(int color) {
        setGLColor(new Color(color));
    }
}
