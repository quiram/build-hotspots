package com.github.quiram.buildhotspots.clients.jenkins.beans;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class JobTest {

    private Job build;

    @Before
    public void setUp() throws Exception {
        build = new Job("build");
    }

    @Test
    public void identicallyCreatedBuildsAreEqual() {
        Job build2 = new Job("build");
        assertEquals(build, build2);
        assertEquals(build.hashCode(), build2.hashCode());
    }

    @Test
    public void buildsWithDifferentNamesAreNotEqual() {
        Job build2 = new Job("build2");
        assertFalse(build.equals(build2));
        assertFalse(build.hashCode() == build2.hashCode());
    }

    @Test
    public void buildNotEqualToString() {
        //noinspection EqualsBetweenInconvertibleTypes
        assertFalse(build.equals(""));
    }
}