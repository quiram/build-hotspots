package com.github.quiram.buildhotspots.visualisation;

import javafx.scene.paint.Color;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static javafx.scene.paint.Color.BLUE;
import static javafx.scene.paint.Color.RED;
import static org.junit.Assert.assertEquals;

public class ColorGradientTest {

    private ColorGradient colorGradient;

    @Rule
    public ExpectedException onBadInput = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        colorGradient = new ColorGradient();
    }

    @Test
    public void blueIsLowestColor() {
        final Color obtained = colorGradient.getColourPoint(0);
        assertEquals(BLUE, obtained);
    }

    @Test
    public void redIsHighestColor() {
        final Color obtained = colorGradient.getColourPoint(100);
        assertEquals(RED, obtained);
    }

    @Test
    public void midFrequencyWouldProvideMiddleColor() {
        final Color expected = Color.rgb(127, 0, 128);
        final Color obtained = colorGradient.getColourPoint(50);

        assertEquals(expected, obtained);
    }
}
