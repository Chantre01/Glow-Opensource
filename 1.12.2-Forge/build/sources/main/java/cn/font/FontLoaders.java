/*
 * Decompiled with CFR 0_132.
 */
package cn.font;

import java.awt.Font;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public abstract class FontLoaders {
    //NL
    public static CFontRenderer NL12 = new CFontRenderer(FontLoaders.getNL(12), true, true);
    public static CFontRenderer NL13 = new CFontRenderer(FontLoaders.getNL(13), true, true);
    public static CFontRenderer NL14 = new CFontRenderer(FontLoaders.getNL(14), true, true);
    public static CFontRenderer NL15 = new CFontRenderer(FontLoaders.getNL(15), true, true);
    public static CFontRenderer NL17 = new CFontRenderer(FontLoaders.getNL(17), true, true);

    public static CFontRenderer NL24 = new CFontRenderer(FontLoaders.getNL(24), true, true);
    public static CFontRenderer NL22 = new CFontRenderer(FontLoaders.getNL(22), true, true);
    public static CFontRenderer NL20 = new CFontRenderer(FontLoaders.getNL(20), true, true);
    public static CFontRenderer NL18 = new CFontRenderer(FontLoaders.getNL(18), true, true);
    public static CFontRenderer NL16 = new CFontRenderer(FontLoaders.getNL(16), true, true);
    public static CFontRenderer NLLogo20 = new CFontRenderer(FontLoaders.getNL2(20), true, true);
    public static CFontRenderer NLLogo22 = new CFontRenderer(FontLoaders.getNL2(22), true, true);
    public static CFontRenderer NL45 = new CFontRenderer(FontLoaders.getNL2(45), true, true);
    public static CFontRenderer NL40 = new CFontRenderer(FontLoaders.getNL2(40), true, true);
    public static CFontRenderer Misans14 = new CFontRenderer(FontLoaders.getMisans(14), true, true);
    public static CFontRenderer Misans16 = new CFontRenderer(FontLoaders.getMisans(16), true, true);
    public static CFontRenderer Misans18 = new CFontRenderer(FontLoaders.getMisans(18), true, true);
    public static CFontRenderer Misans20 = new CFontRenderer(FontLoaders.getMisans(20), true, true);
    public static CFontRenderer Misans24 = new CFontRenderer(FontLoaders.getMisans(24), true, true);
    public static CFontRenderer Misans28 = new CFontRenderer(FontLoaders.getMisans(28), true, true);
    public static CFontRenderer Misans32 = new CFontRenderer(FontLoaders.getMisans(32), true, true);
    public static CFontRenderer NL35 = new CFontRenderer(FontLoaders.getNL2(35), true, true);
    public static CFontRenderer novoIcon = new CFontRenderer(FontLoaders.getIcon(25), true, true);


    public static CFontRenderer NIcon24 = new CFontRenderer(FontLoaders.getCIcon(24), true, true);
    public static CFontRenderer N2Icon24 = new CFontRenderer(FontLoaders.getCIcon2(24), true, true);
    public static CFontRenderer Check24 = new CFontRenderer(FontLoaders.getCheck(24), true, true);

    //jello
    public static CFontRenderer jelloFontBig = new CFontRenderer(FontLoaders.getJelloFont(41, true), true, true);
    public static CFontRenderer jellolightBig = new CFontRenderer(FontLoaders.getJelloFont(45, false), true, true);

    public static CFontRenderer jelloFont30 = new CFontRenderer(FontLoaders.getJelloFont(30, true), true, true);
    public static CFontRenderer jellolight22 = new CFontRenderer(FontLoaders.getJelloFont(22, false), true, true);
    public static CFontRenderer jellolight20 = new CFontRenderer(FontLoaders.getJelloFont(20, false), true, true);
    public static CFontRenderer jellolight30 = new CFontRenderer(FontLoaders.getJelloFont(30, false), true, true);
    public static CFontRenderer jelloFontMedium20 = new CFontRenderer(FontLoaders.getJelloFont(20, true), true, true);

    public static CFontRenderer jelloFontMarker = new CFontRenderer(FontLoaders.getJelloFont(19, false), true, true);
    public static CFontRenderer jellolight18 = new CFontRenderer(FontLoaders.getJelloFont(18, false), true, true);
    public static CFontRenderer jellomedium18 = new CFontRenderer(FontLoaders.getJelloFont(18, true), true, true);

    public static CFontRenderer jelloFontMedium = new CFontRenderer(FontLoaders.getJelloFont(25, false), true, true);
    public static CFontRenderer jelloFontMedium24 = new CFontRenderer(FontLoaders.getJelloFont(24, true), true, true);
    public static CFontRenderer jelloFontMedium25 = new CFontRenderer(FontLoaders.getJelloFont(25, true), true, true);
    public static CFontRenderer jelloFontMedium19 = new CFontRenderer(FontLoaders.getJelloFont(19, true), true, true);
    public static CFontRenderer jelloFontMedium13 = new CFontRenderer(FontLoaders.getJelloFont(13, true), true, true);
    public static CFontRenderer jelloFontMedium14 = new CFontRenderer(FontLoaders.getJelloFont(14, true), true, true);
    public static CFontRenderer jelloFontMedium12 = new CFontRenderer(FontLoaders.getJelloFont(12, true), true, true);
    public static CFontRenderer jelloFontMedium16 = new CFontRenderer(FontLoaders.getJelloFont(16, true), true, true);
    public static CFontRenderer jelloFontMedium15 = new CFontRenderer(FontLoaders.getJelloFont(15, true), true, true);
    public static CFontRenderer jelloFontMedium17 = new CFontRenderer(FontLoaders.getJelloFont(17, true), true, true);

    public static CFontRenderer sfui20 = new CFontRenderer(FontLoaders.getSFUI(20), true, true);
    public static CFontRenderer sfbold20 = new CFontRenderer(FontLoaders.getSFUIBold(20), true, true);


    private static Font getSFUI(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("liquidbounce/font/sf.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }

    private static Font getSFUIBold(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("liquidbounce/font/sfbold.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    private static Font getJelloFont(float size, boolean bold) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(bold ? "liquidbounce/font/jellomedium.ttf": "liquidbounce/font/jellolight.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, +10);
        }
        return font;
    }

    private static Font getIcon(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("liquidbounce/font/stylesicons.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    public static Font getMisans(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("liquidbounce/font/misans.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            System.out.println("Load Default font");
            font = new Font("default", 0, size);
        }
        return font;
    }


    public static Font getNL(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("liquidbounce/font/tahoma.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            System.out.println("Load Default font");
            font = new Font("default", 0, size);
        }
        return font;
    }

    public static Font getCheck(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("liquidbounce/font/check.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            System.out.println("Load Default font");
            font = new Font("default", 0, size);
        }
        return font;
    }

    public static Font getNL2(int size) {

        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("liquidbounce/font/sf.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            System.out.println("Load Default font");
            font = new Font("default", 0, size);
        }
        return font;
    }

    public static Font getCIcon(int size) {

        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("liquidbounce/font/icons.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            System.out.println("Load Default font");
            font = new Font("default", 0, size);
        }
        return font;
    }

    public static Font getCIcon2(int size) {

        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("liquidbounce/font/icon2.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            System.out.println("Load Default font");
            font = new Font("default", 0, size);
        }
        return font;
    }

}

