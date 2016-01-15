package com.github.quiram.buildhotspots;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.SECONDS;

/**
 * Assuming dates represent the oldest available build for a build configuration, this class will help convert those
 * dates to a normalised frequency. Frequency would normally be calculated by getting all the builds in a fixed period
 * of time, but sometimes the build history is not fully available. In those cases we take the date of the oldest build
 * and infer the frequency from it: the oldest the build is, the less frequency the build configuration is rebuilt.
 */
public class DateToFrequencyTransformer {
    private LocalDateTime oldest;
    private LocalDateTime mostRecent;


    public LocalDateTime getOldestDate() {
        return oldest;
    }

    public void add(LocalDateTime dateTime) {
        if (this.oldest == null || dateTime.isBefore(this.oldest)) {
            this.oldest = dateTime;
        }

        if (this.mostRecent == null || dateTime.isAfter(this.mostRecent)) {
            mostRecent = dateTime;
        }
    }

    public LocalDateTime getMostRecentDate() {
        return mostRecent;
    }

    public long getFrequencyFor(LocalDateTime dateTime) {
        if (oldest == null) {
            throw new IllegalStateException("Transformer is empty.");
        }

        if (oldest == mostRecent) {
            throw new IllegalStateException("Transformer only has one element.");
        }

        if (dateTime.isBefore(oldest) || dateTime.isAfter(mostRecent)) {
            throw new IllegalArgumentException("Date to calculate frequency from must be within range of dates previously inserted in the transformer.");
        }

        long totalRange = oldest.until(mostRecent, SECONDS);
        long datePosition = oldest.until(dateTime, SECONDS);

        return datePosition * 100 / totalRange;
    }
}
