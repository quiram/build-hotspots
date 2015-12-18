package com.github.quiram.buildhotspots.datetofrequencytransformertest;

import org.junit.Test;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertNull;

public class EmptyDateToFrequencyTransformerTest extends DateToFrequencyTransformerTest {

    @Test
    public void oldestDateIsNull() {
        LocalDateTime date = transformer.getOldestDate();
        assertNull(date);
    }

    @Test
    public void mostRecentDateIsNull() {
        LocalDateTime date = transformer.getMostRecentDate();
        assertNull(date);
    }

    @Test
    public void getFrequencyFromEmptyTransformerWillFail() {
        onBadState.expect(IllegalStateException.class);
        onBadState.expectMessage(containsString("empty"));

        transformer.getFrequencyFor(now());
    }
}
