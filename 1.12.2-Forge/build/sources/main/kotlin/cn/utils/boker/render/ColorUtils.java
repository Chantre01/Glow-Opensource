package cn.utils.boker.render;

import java.awt.*;

public class ColorUtils {
    public static int rainbow(int delay) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / 360f), 0.8f, 0.7f).getRGB();
    }
    public static int colorCode(String substring, int alpha) {
        String lowerCase = substring.toLowerCase();
        switch (lowerCase) {
            case "0":
                return new Color(0, 0, 0, alpha).getRGB();

            case "1":
                return new Color(0, 0, 170, alpha).getRGB();

            case "2":
                return new Color(0, 170, 0, alpha).getRGB();

            case "3":
                return new Color(0, 170, 170, alpha).getRGB();

            case "4":
                return new Color(170, 0, 0, alpha).getRGB();

            case "5":
                return new Color(170, 0, 170, alpha).getRGB();

            case "6":
                return new Color(255, 170, 0, alpha).getRGB();

            case "7":
                return new Color(170, 170, 170, alpha).getRGB();

            case "8":
                return new Color(85, 85, 85, alpha).getRGB();

            case "9":
                return new Color(85, 85, 255, alpha).getRGB();

            case "a":
                return new Color(85, 255, 85, alpha).getRGB();

            case "b":
                return new Color(85, 255, 255, alpha).getRGB();

            case "c":
                return new Color(255, 85, 85, alpha).getRGB();

            case "d":
                return new Color(255, 85, 255, alpha).getRGB();

            case "e":
                return new Color(255, 255, 85, alpha).getRGB();
        }
        return new Color(255, 255, 255, alpha).getRGB();
    }
    public static Color getHealthColor(final float health, final float maxHealth) {
        final float[] fractions = { 0.0f, 0.5f, 1.0f };
        final Color[] colors = { new Color(108, 0, 0), new Color(255, 0, 200),new Color(23, 225, 51)  };
        final float progress = health / maxHealth;
        return blendColors(fractions, colors, progress).brighter();
    }
    public static Double interpolate(double oldValue, double newValue, double interpolationValue){
        return (oldValue + (newValue - oldValue) * interpolationValue);
    }

    public static int interpolateInt(int oldValue, int newValue, double interpolationValue){
        return interpolate(oldValue, newValue, (float) interpolationValue).intValue();
    }
    public static int[] getFractionIndices(final float[] fractions, final float progress) {
        final int[] range = new int[2];
        int startPoint;
        for (startPoint = 0; startPoint < fractions.length && fractions[startPoint] <= progress; ++startPoint) {}
        if (startPoint >= fractions.length) {
            startPoint = fractions.length - 1;
        }
        range[0] = startPoint - 1;
        range[1] = startPoint;
        return range;
    }
    public static Color blend(Color color1, Color color2, double ratio) {
        float r = (float)ratio;
        float ir = 1.0f - r;
        float[] rgb1 = new float[3];
        float[] rgb2 = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
        Color color3 = new Color(rgb1[0] * r + rgb2[0] * ir, rgb1[1] * r + rgb2[1] * ir, rgb1[2] * r + rgb2[2] * ir);
        return color3;
    }
    public static Color blendColors(final float[] fractions, final Color[] colors, final float progress) {
        if (fractions.length == colors.length) {
            final int[] indices = getFractionIndices(fractions, progress);
            final float[] range = { fractions[indices[0]], fractions[indices[1]] };
            final Color[] colorRange = { colors[indices[0]], colors[indices[1]] };
            final float max = range[1] - range[0];
            final float value = progress - range[0];
            final float weight = value / max;
            final Color color = blend(colorRange[0], colorRange[1], 1.0f - weight);
            return color;
        }
        throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
    }
    public static Color interpolateColorC(Color color1, Color color2, float amount) {
        amount = Math.min(1, Math.max(0, amount));
        return new Color(interpolateInt(color1.getRed(), color2.getRed(), amount),
                interpolateInt(color1.getGreen(), color2.getGreen(), amount),
                interpolateInt(color1.getBlue(), color2.getBlue(), amount),
                interpolateInt(color1.getAlpha(), color2.getAlpha(), amount));
    }
    public static int applyOpacity(int color, float opacity) {
        Color old = new Color(color);
        return applyOpacity(old, opacity).getRGB();
    }

    //Opacity value ranges from 0-1
    public static Color applyOpacity(Color color, float opacity) {
        opacity = Math.min(1, Math.max(0, opacity));
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (color.getAlpha() * opacity));
    }
    public static int getColor(Color color) {
        return getColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static int getColor(int brightness) {
        return getColor(brightness, brightness, brightness, 255);
    }

    public static int getColor(int brightness, int alpha) {
        return getColor(brightness, brightness, brightness, alpha);
    }

    public static int getColor(int red, int green, int blue) {
        return getColor(red, green, blue, 255);
    }

    public static int getColor(int red, int green, int blue, int alpha) {
        byte color = 0;
        int color1 = color | alpha << 24;
        color1 |= red << 16;
        color1 |= green << 8;
        color1 |= blue;
        return color1;
    }
}
