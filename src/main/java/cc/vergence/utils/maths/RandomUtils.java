package cc.vergence.utils.maths;

import cc.vergence.features.objects.HexColor;

import java.util.concurrent.ThreadLocalRandom;

public final class RandomUtils {
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SYMBOLS = "~!+=_-@#$%^&*()[]{}|\\:;,.<>/?";

    public static int getInt() {
        return ThreadLocalRandom.current().nextInt();
    }

    public static float getFloat() {
        return ThreadLocalRandom.current().nextFloat();
    }

    public static double getDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }

    public static boolean getBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    public static int getInt(int min, int max) {
        if (min == max) return min;
        if (min > max) { int t = min; min = max; max = t; }
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static float getFloat(float min, float max) {
        if (min == max) {
            return min;
        }
        if (min > max) {
            float t = min; min = max; max = t;
        }
        return min + ThreadLocalRandom.current().nextFloat() * (max - min);
    }

    public static double getDouble(double min, double max) {
        if (min == max) {
            return min;
        }
        if (min > max) {
            double t = min; min = max; max = t;
        }
        return min + ThreadLocalRandom.current().nextDouble() * (max - min);
    }

    public static String getLowerString(int len) {
        return len <= 0 ? "" : randomString(LOWER, len);
    }

    public static String getUpperString(int len) {
        return len <= 0 ? "" : randomString(UPPER, len);
    }

    public static String getSymbols(int len) {
        return len <= 0 ? "" : randomString(SYMBOLS, len);
    }

    public static String getString(int len) {
        return len <= 0 ? "" : randomString(LOWER + UPPER + DIGITS + SYMBOLS, len);
    }

    public static HexColor getHexColor() {
        ThreadLocalRandom r = ThreadLocalRandom.current();
        return new HexColor("#" + intToHex(r.nextInt(256)) +
                intToHex(r.nextInt(256)) +
                intToHex(r.nextInt(256)) +
                intToHex(r.nextInt(256)));
    }

    private static String randomString(String src, int len) {
        StringBuilder sb = new StringBuilder(len);
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        for (int i = 0; i < len; i++) sb.append(src.charAt(rnd.nextInt(src.length())));
        return sb.toString();
    }

    private static String intToHex(int v) {
        return String.format("%02X", v & 0xFF);
    }
}