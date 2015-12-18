package com.github.quiram.buildhotspots.visualisation;

import javafx.scene.paint.Color;

public class ColorGradient {
    public Color getColourPoint(int gradientPoint) {
        int red = 255 * gradientPoint / 100;
        int blue = 255 - red;

        return Color.rgb(red, 0, blue);
    }
}
