package com.github.quiram.buildhotspots.clients.jenkins.beans;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class BuildTest {

    private Build build;

    @Before
    public void setUp() throws Exception {
        build = new Build("build");
    }

    @Test
    public void identicallyCreatedBuildsAreEqual() {
        Build build2 = new Build("build");
        assertEquals(build, build2);
        assertEquals(build.hashCode(), build2.hashCode());
    }

    @Test
    public void buildsWithDifferentNamesAreNotEqual() {
        Build build2 = new Build("build2");
        assertFalse(build.equals(build2));
        assertFalse(build.hashCode() == build2.hashCode());
    }

    @Test
    public void buildNotEqualToString() {
        //noinspection EqualsBetweenInconvertibleTypes
        assertFalse(build.equals(""));
    }
}