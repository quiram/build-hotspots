package com.github.quiram.buildhotspots.datetofrequencytransformertest;

import com.github.quiram.buildhotspots.DateToFrequencyTransformer;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;

public class TwoElementsInDateToFrequencyTransformerTest extends DateToFrequencyTransformerTest {

    private LocalDateTime older;
    private LocalDateTime later;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        older = now();
        later = older.plusDays(1);
        transformer.add(older);
        transformer.add(later);
    }

    @Test
    public void oldestDateWhenFirstIsOlderReturnsFirst() {
        assertEquals(older, transformer.getOldestDate());
    }

    @Test
    public void mostRecentDateWhenSecondIsOlderReturnsFirst() {
        DateToFrequencyTransformer transformer = new DateToFrequencyTransformer();
        transformer.add(later);
        transformer.add(older);
        assertEquals(later, transformer.getMostRecentDate());
    }

    @Test
    public void getFrequencyForOldestReturns100() {
        assertEquals(100, transformer.getFrequencyFor(older));
    }

    @Test
    public void getFrequencyForLatestReturns0() {
        assertEquals(0, transformer.getFrequencyFor(later));
    }

    @Test
    public void getFrequencyForBuildInTheMiddleReturns50() {
        LocalDateTime middle = older.plusHours(12);

        assertEquals(50, transformer.getFrequencyFor(middle));
    }

    @Test
    public void getFrequencyForDateOlderThanRangeFails() {
        onBadState.expect(IllegalArgumentException.class);
        onBadState.expectMessage(containsString("within range"));

        LocalDateTime tooOldDate = older.minusDays(1);
        transformer.getFrequencyFor(tooOldDate);
    }

    @Test
    public void getFrequencyForDateNewerThanRangeFails() {
        onBadState.expect(IllegalArgumentException.class);
        onBadState.expectMessage(containsString("within range"));

        LocalDateTime tooNewDate = later.plusDays(1);
        transformer.getFrequencyFor(tooNewDate);
    }
}
