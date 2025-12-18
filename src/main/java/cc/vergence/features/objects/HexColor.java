package cc.vergence.features.objects;

import lombok.Getter;

import java.awt.*;

@Getter
public final class HexColor {
    private String value;

    public HexColor(String hex) {
        if (hex == null || !hex.matches("^#[0-9A-Fa-f]{8}$")) {
            throw new IllegalArgumentException("HexColor must be #RRGGBBAA");
        }
        this.value = hex.toUpperCase();
    }

    public boolean setValue(String hex) {
        if (hex == null || !hex.matches("^#[0-9A-Fa-f]{8}$")) {
            return false;
        }
        this.value = hex;
        return true;
    }

    public int translateToInt() {
        String hex = value.substring(1);
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);
        int a = Integer.parseInt(hex.substring(6, 8), 16);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public Color translateToAwt() {
        String hex = value.substring(1);
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);
        int a = Integer.parseInt(hex.substring(6, 8), 16);
        return new Color(r / 255f, g / 255f, b / 255f, a / 255f);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof HexColor && ((HexColor) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}