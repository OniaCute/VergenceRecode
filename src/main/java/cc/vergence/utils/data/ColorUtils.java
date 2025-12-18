package cc.vergence.utils.data;

import cc.vergence.features.objects.HexColor;

import java.awt.*;

public class ColorUtils {
    private ColorUtils() {}

    public static Color setRed(Color c, int red) {
        return new Color(clamp(red), c.getGreen(), c.getBlue(), c.getAlpha());
    }

    public static Color setGreen(Color c, int green) {
        return new Color(c.getRed(), clamp(green), c.getBlue(), c.getAlpha());
    }

    public static Color setBlue(Color c, int blue) {
        return new Color(c.getRed(), c.getGreen(), clamp(blue), c.getAlpha());
    }

    public static Color setAlpha(Color c, int alpha) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), clamp(alpha));
    }

    public static HexColor translateIntToHex(int argb) {
        int a = (argb >>> 24) & 0xFF;
        int r = (argb >>> 16) & 0xFF;
        int g = (argb >>> 8) & 0xFF;
        int b = argb & 0xFF;
        String hex = String.format("#%02X%02X%02X%02X", r, g, b, a);
        return new HexColor(hex);
    }

    public static HexColor translateAwtToHex(Color c) {
        String hex = String.format("#%02X%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
        return new HexColor(hex);
    }

    public static int translateAwtToInt(Color c) {
        return (c.getAlpha() << 24) | (c.getRed()   << 16) | (c.getGreen() << 8) | c.getBlue();
    }

    private static int clamp(int v) {
        return Math.max(0, Math.min(255, v));
    }
}
