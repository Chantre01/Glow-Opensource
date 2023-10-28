/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.ui.font;

import com.google.gson.*;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.HttpUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;
import java.util.List;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Fonts extends MinecraftInstance {

    @FontDetails(fontName = "Minecraft Font")
    public static final IFontRenderer minecraftFont = mc.getFontRendererObj();
    private static final HashMap<FontInfo, IFontRenderer> CUSTOM_FONT_RENDERERS = new HashMap<>();
    @FontDetails(fontName = "flux", fontSize = 35)
    public static IFontRenderer flux;
    @FontDetails(fontName = "flux", fontSize = 45)
    public static IFontRenderer flux45;

    @FontDetails(fontName = "Bold", fontSize = 30)
        public static IFontRenderer bold30;
    @FontDetails(fontName = "Bold", fontSize = 40)
    public static IFontRenderer bold40;
    @FontDetails(fontName = "Bold", fontSize = 35)
    public static IFontRenderer bold35;
    @FontDetails(fontName = "nico", fontSize = 80)
    public static IFontRenderer nico80;
    @FontDetails(fontName = "SimpleNotificatonsIcons", fontSize = 40)
    public static IFontRenderer SimpleNotifications;



    @FontDetails(fontName = "Roboto Medium", fontSize = 35)
    public static IFontRenderer font35;
    @FontDetails(fontName = "Roboto Medium", fontSize = 30)
    public static IFontRenderer font30;
    @FontDetails(fontName = "Roboto Medium", fontSize = 25)
    public static IFontRenderer font25;
    @FontDetails(fontName = "Roboto Medium", fontSize = 40)
    public static IFontRenderer font40;
    @FontDetails(fontName = "Roboto Medium", fontSize = 80)
    public static IFontRenderer font80;
    @FontDetails(fontName = "Pixelation", fontSize = 45)
    public static IFontRenderer pix45;

    @FontDetails(fontName = "SFUI Regular", fontSize = 35)
    public static IFontRenderer fontSFUI35;
    @FontDetails(fontName = "SFUI Regular", fontSize = 45)
    public static IFontRenderer fontSFUI45;
    @FontDetails(fontName = "SFUI Regular", fontSize = 55)
    public static IFontRenderer fontSFUI30;
    @FontDetails(fontName = "SFUI Regular", fontSize = 40)
    public static IFontRenderer fontSFUI40;

    @FontDetails(fontName = "SFUI Regular", fontSize = 120)
    public static IFontRenderer fontSFUI120;

    @FontDetails(fontName = "comfortaa", fontSize = 96)
    public static IFontRenderer com96;
    @FontDetails(fontName = "comfortaa", fontSize = 35)
    public static IFontRenderer com35;
    @FontDetails(fontName = "csicon", fontSize = 40)
    public static IFontRenderer cs40;
    @FontDetails(fontName = "jello35", fontSize = 35)
    public static IFontRenderer jello35;

    @FontDetails(fontName = "jello50", fontSize = 45)
    public static IFontRenderer jello50;
    @FontDetails(fontName = "jello55", fontSize = 55)
    public static IFontRenderer jello55;
    @FontDetails(fontName = "sfbold25", fontSize = 45)
    public static IFontRenderer sfbold25;
    @FontDetails(fontName = "sfbold30", fontSize = 55)
    public static IFontRenderer sfbold30;
    @FontDetails(fontName = "sfbold35", fontSize = 35)
    public static IFontRenderer sfbold35;
    @FontDetails(fontName = "sfbold40", fontSize = 35)
    public static IFontRenderer sfbold40;
    @FontDetails(fontName = "sfbold45", fontSize = 45)
    public static IFontRenderer sfbold45;

    @FontDetails(fontName = "sfbold50", fontSize = 45)
    public static IFontRenderer sfbold50;
    @FontDetails(fontName = "sfbold55", fontSize = 55)
    public static IFontRenderer sfbold55;
    @FontDetails(fontName = "sfbold65", fontSize = 75)
    public static IFontRenderer sfbold65;
    @FontDetails(fontName = "prod30", fontSize = 30)
    public static IFontRenderer prod30;
    @FontDetails(fontName = "prod35", fontSize = 35)
    public static IFontRenderer prod35;
    @FontDetails(fontName = "prod40", fontSize = 40)
    public static IFontRenderer prod40;
    @FontDetails(fontName = "prod200", fontSize = 250)
    public static IFontRenderer prod200;


    public static void loadFonts() {
        long l = System.currentTimeMillis();

        ClientUtils.getLogger().info("Loading Fonts.");

//        downloadFonts();
        flux = classProvider.wrapFontRenderer(new GameFontRenderer(getFlux(30)));
        flux45= classProvider.wrapFontRenderer(new GameFontRenderer(getFlux(45)));



        font35 = classProvider.wrapFontRenderer(new GameFontRenderer(getSFUI(35)));
        font25 = classProvider.wrapFontRenderer(new GameFontRenderer(getSFUI(25)));
        font40 = classProvider.wrapFontRenderer(new GameFontRenderer(getSFUI(40)));
        font30 = classProvider.wrapFontRenderer(new GameFontRenderer(getSFUI(30)));
        font80 = classProvider.wrapFontRenderer(new GameFontRenderer(getSFUI(80)));
        SimpleNotifications = classProvider.wrapFontRenderer(new GameFontRenderer(getnotif(40)));
        pix45 = classProvider.wrapFontRenderer(new GameFontRenderer(getpix(45)));

        cs40 = classProvider.wrapFontRenderer(new GameFontRenderer(getcsicon(40)));
//        fontSFUI18 = classProvider.wrapFontRenderer(new GameFontRenderer(getSFUI(18)));
        fontSFUI30 = classProvider.wrapFontRenderer(new GameFontRenderer(getSFUI(30)));

        fontSFUI35 = classProvider.wrapFontRenderer(new GameFontRenderer(getSFUI(35)));
        fontSFUI40 = classProvider.wrapFontRenderer(new GameFontRenderer(getSFUI(40)));
        fontSFUI45 = classProvider.wrapFontRenderer(new GameFontRenderer(getSFUI(45)));
//        fontSFUI56 = classProvider.wrapFontRenderer(new GameFontRenderer(getSFUI(56)));
        fontSFUI120 = classProvider.wrapFontRenderer(new GameFontRenderer(getSFUI(120)));


        bold35 = classProvider.wrapFontRenderer(new GameFontRenderer(getBold(35)));
        bold40 = classProvider.wrapFontRenderer(new GameFontRenderer(getBold(40)));
//        bold45 = classProvider.wrapFontRenderer(new GameFontRenderer(getBold(45)));
        bold30 = classProvider.wrapFontRenderer(new GameFontRenderer(getBold(30)));

        nico80 = classProvider.wrapFontRenderer(new GameFontRenderer(getnico(80)));



        jello35 = classProvider.wrapFontRenderer(new GameFontRenderer(getJello(35)));

        jello50 = classProvider.wrapFontRenderer(new GameFontRenderer(getJello(50)));
        jello55 = classProvider.wrapFontRenderer(new GameFontRenderer(getJello(55)));
        sfbold25 = classProvider.wrapFontRenderer(new GameFontRenderer(getSFBold(25)));
        sfbold30 = classProvider.wrapFontRenderer(new GameFontRenderer(getSFBold(30)));
        sfbold35 = classProvider.wrapFontRenderer(new GameFontRenderer(getSFBold(35)));
        sfbold40 = classProvider.wrapFontRenderer(new GameFontRenderer(getSFBold(40)));
        sfbold45 = classProvider.wrapFontRenderer(new GameFontRenderer(getSFBold(45)));
        sfbold50 = classProvider.wrapFontRenderer(new GameFontRenderer(getSFBold(50)));
        sfbold55 = classProvider.wrapFontRenderer(new GameFontRenderer(getSFBold(55)));
        sfbold65 = classProvider.wrapFontRenderer(new GameFontRenderer(getSFBold(65)));
        prod30 = classProvider.wrapFontRenderer(new GameFontRenderer(getProd(30)));
        prod35 = classProvider.wrapFontRenderer(new GameFontRenderer(getProd(35)));
        prod40 = classProvider.wrapFontRenderer(new GameFontRenderer(getProd(40)));
        prod200 = classProvider.wrapFontRenderer(new GameFontRenderer(getProd(200)));
        net.ccbluex.liquidbounce.feng.FontLoaders.initFonts();

        try {
            CUSTOM_FONT_RENDERERS.clear();

            final File fontsFile = new File(LiquidBounce.fileManager.fontsDir, "fonts.json");

            if (fontsFile.exists()) {
                final JsonElement jsonElement = new JsonParser().parse(new BufferedReader(new FileReader(fontsFile)));

                if (jsonElement instanceof JsonNull)
                    return;

                final JsonArray jsonArray = (JsonArray) jsonElement;

                for (final JsonElement element : jsonArray) {
                    if (element instanceof JsonNull)
                        return;

                    final JsonObject fontObject = (JsonObject) element;

                    Font font = getFont(fontObject.get("fontFile").getAsString(), fontObject.get("fontSize").getAsInt());

                    CUSTOM_FONT_RENDERERS.put(new FontInfo(font), classProvider.wrapFontRenderer(new GameFontRenderer(font)));
                }
            } else {
                fontsFile.createNewFile();

                final PrintWriter printWriter = new PrintWriter(new FileWriter(fontsFile));
                printWriter.println(new GsonBuilder().setPrettyPrinting().create().toJson(new JsonArray()));
                printWriter.close();
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }

        ClientUtils.getLogger().info("Loaded Fonts. (" + (System.currentTimeMillis() - l) + "ms)");
    }

    private static void downloadFonts() {
        try {
            final File outputFile = new File(LiquidBounce.fileManager.fontsDir, "roboto.zip");

            if (!outputFile.exists()) {
                ClientUtils.getLogger().info("Downloading fonts...");
                HttpUtils.download(LiquidBounce.CLIENT_CLOUD + "/fonts/Roboto.zip", outputFile);
                ClientUtils.getLogger().info("Extract fonts...");
                extractZip(outputFile.getPath(), LiquidBounce.fileManager.fontsDir.getPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static IFontRenderer getFontRenderer(final String name, final int size) {
        for (final Field field : Fonts.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);

                Object o = field.get(null);

                if (o instanceof IFontRenderer) {
                    FontDetails fontDetails = field.getAnnotation(FontDetails.class);

                    if (fontDetails.fontName().equals(name) && fontDetails.fontSize() == size)
                        return (IFontRenderer) o;
                }
            } catch (final IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return CUSTOM_FONT_RENDERERS.getOrDefault(new FontInfo(name, size), minecraftFont);
    }

    public static FontInfo getFontDetails(final IFontRenderer fontRenderer) {
        for (final Field field : Fonts.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);

                final Object o = field.get(null);

                if (o.equals(fontRenderer)) {
                    final FontDetails fontDetails = field.getAnnotation(FontDetails.class);

                    return new FontInfo(fontDetails.fontName(), fontDetails.fontSize());
                }
            } catch (final IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        for (Map.Entry<FontInfo, IFontRenderer> entry : CUSTOM_FONT_RENDERERS.entrySet()) {
            if (entry.getValue() == fontRenderer)
                return entry.getKey();
        }

        return null;
    }

    private static Font getJello(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("liquidbounce/font/jello.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    private static Font getpix(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("liquidbounce/font/pixel.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    private static Font getcsicon(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("liquidbounce/font/neverlose.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    private static Font getnotif(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("liquidbounce/font/3.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    private static Font getProd(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("liquidbounce/font/prodmedium.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }

    private static Font getSFBold(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("liquidbounce/font/sfbold.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }

    private static Font getnico(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("liquidbounce/font/flux.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }

    private static Font getBold(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("liquidbounce/font/bold.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }

    public float getCharWidth(char c) {
        return fontSFUI35.getStringWidth(String.valueOf(c));
    }

    private static Font getSFUI(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("liquidbounce/font/sf.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }


    private static Font getFlux(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("liquidbounce/font/fluxicon.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    public static List<IFontRenderer> getFonts() {
        final List<IFontRenderer> fonts = new ArrayList<>();

        for (final Field fontField : Fonts.class.getDeclaredFields()) {
            try {
                fontField.setAccessible(true);

                final Object fontObj = fontField.get(null);

                if (fontObj instanceof IFontRenderer) fonts.add((IFontRenderer) fontObj);
            } catch (final IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        fonts.addAll(Fonts.CUSTOM_FONT_RENDERERS.values());

        return fonts;
    }

    private static Font getFont(final String fontName, final int size) {
        try {
            final InputStream inputStream = new FileInputStream(new File(LiquidBounce.fileManager.fontsDir, fontName));
            Font awtClientFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            awtClientFont = awtClientFont.deriveFont(Font.PLAIN, size);
            inputStream.close();
            return awtClientFont;
        } catch (final Exception e) {
            e.printStackTrace();

            return new Font("default", Font.PLAIN, size);
        }
    }

    private static void extractZip(final String zipFile, final String outputFolder) {
        final byte[] buffer = new byte[1024];

        try {
            final File folder = new File(outputFolder);

            if (!folder.exists()) folder.mkdir();

            final ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile));

            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                File newFile = new File(outputFolder + File.separator + zipEntry.getName());
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fileOutputStream = new FileOutputStream(newFile);

                int i;
                while ((i = zipInputStream.read(buffer)) > 0)
                    fileOutputStream.write(buffer, 0, i);

                fileOutputStream.close();
                zipEntry = zipInputStream.getNextEntry();
            }

            zipInputStream.closeEntry();
            zipInputStream.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public static class FontInfo {
        private final String name;
        private final int fontSize;

        public FontInfo(String name, int fontSize) {
            this.name = name;
            this.fontSize = fontSize;
        }

        public FontInfo(Font font) {
            this(font.getName(), font.getSize());
        }

        public String getName() {
            return name;
        }

        public int getFontSize() {
            return fontSize;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FontInfo fontInfo = (FontInfo) o;

            if (fontSize != fontInfo.fontSize) return false;
            return Objects.equals(name, fontInfo.name);
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + fontSize;
            return result;
        }
    }

}