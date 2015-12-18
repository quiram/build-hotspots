package com.github.quiram.buildhotspots.datetofrequencytransformertest;

import com.github.quiram.buildhotspots.DateToFrequencyTransformer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

abstract public class DateToFrequencyTransformerTest {
    @Rule
    public ExpectedException onBadState = ExpectedException.none();
    protected DateToFrequencyTransformer transformer;

    @Before
    public void setUp() {
        transformer = new DateToFrequencyTransformer();
    }
}
