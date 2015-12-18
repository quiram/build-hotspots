package com.github.quiram.buildhotspots.datetofrequencytransformertest;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;

public class OneElementInDateToFrequencyTransformerTest extends DateToFrequencyTransformerTest {
    private LocalDateTime addedDateTime;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        addedDateTime = now();
        transformer.add(addedDateTime);
    }

    @Test
    public void oldestDateIsTheOneAdded() {
        assertEquals(addedDateTime, transformer.getOldestDate());
    }

    @Test
    public void mostRecentDateIsTheOneAdded() {
        assertEquals(addedDateTime, transformer.getMostRecentDate());
    }

    @Test
    public void cannotGetFrequencyFromOnlyOneDate() {
        onBadState.expect(IllegalStateException.class);
        onBadState.expectMessage(containsString("one element"));

        transformer.getFrequencyFor(addedDateTime);
    }
}
