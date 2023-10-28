package net.ccbluex.liquidbounce.feng;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class FontLoaders {
    public static FontDrawer C12;
    public static FontDrawer C14;
    public static FontDrawer C16;
    public static FontDrawer C18;
    public static FontDrawer C20;
    public static FontDrawer C22;
    public static FontDrawer C24;
    public static FontDrawer F18;
    public static FontDrawer Check;
    public static List<FontDrawer> fonts;

    public static FontDrawer getFontRender(int size) {
        return fonts.get(size - 10);
    }

    public static void initFonts() {
        F18 = new FontDrawer(FontLoaders.getComfortaa(18), true);
        C22 = new FontDrawer(FontLoaders.getComfortaa(22), true);
        C24 = new FontDrawer(FontLoaders.getComfortaa(24), true);
        C20 = new FontDrawer(FontLoaders.getComfortaa(20), true);
        C18 = new FontDrawer(FontLoaders.getComfortaa(18), true);
        C16 = new FontDrawer(FontLoaders.getComfortaa(16), true);
        C14 = new FontDrawer(FontLoaders.getComfortaa(14), true);
        C12 = new FontDrawer(FontLoaders.getComfortaa(12), true);
        fonts = new ArrayList<>();
    }

    public static Font getComfortaa(int size) {
        Font font;
        try {
            font = Font.createFont(0, Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("liquidbounce/font/pingfang.ttf")).getInputStream()).deriveFont(0, (float) size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
}
