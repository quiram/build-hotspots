package com.github.quiram.buildhotspots.visualisation;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BuildConfigurationTest {
    @Test
    public void newBuildConfigurationHasNoDepth() {
        final BuildConfiguration buildConfiguration = getBuildConfiguration();
        assertEquals(0, buildConfiguration.getDepth());
    }

    @Test
    public void buildConfigurationWithOneDependencyHasDepthOne() {
        final BuildConfiguration mainBuildConfiguration = getBuildConfiguration();
        final BuildConfiguration dependentConfiguration = getBuildConfiguration();

        dependentConfiguration.addDependency(mainBuildConfiguration);

        assertEquals(1, dependentConfiguration.getDepth());
    }

    @Test
    public void buildConfigurationWithTwoDependenciesAtSameLevelHasDepthOne() {
        final BuildConfiguration mainBuildConfiguration = getBuildConfiguration();
        final BuildConfiguration mainBuildConfiguration2 = getBuildConfiguration();
        final BuildConfiguration dependentConfiguration = getBuildConfiguration();

        dependentConfiguration.addDependency(mainBuildConfiguration);
        dependentConfiguration.addDependency(mainBuildConfiguration2);

        assertEquals(1, dependentConfiguration.getDepth());
    }

    @Test
    public void buildConfigurationWithTwoLevelOfChainDependenciesHasDepthTwo() {
        final BuildConfiguration mainBuildConfiguration = getBuildConfiguration();
        final BuildConfiguration midBuildConfiguration = getBuildConfiguration();
        final BuildConfiguration dependentConfiguration = getBuildConfiguration();

        midBuildConfiguration.addDependency(mainBuildConfiguration);
        dependentConfiguration.addDependency(midBuildConfiguration);

        assertEquals(2, dependentConfiguration.getDepth());
    }

    @Test
    public void buildConfigurationWithTwoBranchesOfDependenciesThatEquatToDepthTwo() {
        /*
         * A -> C
         * B -> D -> C
         */

        final BuildConfiguration A = getBuildConfiguration();
        final BuildConfiguration B = getBuildConfiguration();
        final BuildConfiguration C = getBuildConfiguration();
        final BuildConfiguration D = getBuildConfiguration();

        C.addDependency(A);
        D.addDependency(B);
        C.addDependency(D);

        assertEquals(2, C.getDepth());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void dependenciesCannotBeChangedOutsideOfBuildConfiguration() {
        final BuildConfiguration b = getBuildConfiguration();
        final Set<BuildConfiguration> dependencies = b.getDependencies();
        dependencies.add(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void dependentsCannotBeChangedOutsideOfBuildConfiguration() {
        final BuildConfiguration b = getBuildConfiguration();
        final Set<BuildConfiguration> dependents = b.getDependents();
        dependents.add(null);
    }

    @Test
    public void addDependency() {
        final BuildConfiguration b = getBuildConfiguration();
        final BuildConfiguration a = getBuildConfiguration();

        //This test makes a dependency of b
        //so a must be built before b
        b.addDependency(a);

        //First check that b has its dependency registered
        assertEquals(1, b.getDependencies().size());
        assertEquals(0, b.getDependents().size());

        //Now check the reverse
        //a has a dependent registered
        assertEquals(0, a.getDependencies().size());
        assertEquals(1, a.getDependents().size());
    }

    @Test
    public void buildsAreRelevantByDefault() {
        final BuildConfiguration a = getBuildConfiguration();
        assertTrue(a.isRelevant());
    }

    @Test
    public void buildConfigurationNameAvailableFromConstructor() {
        final BuildConfiguration buildConfiguration = getBuildConfiguration();
        assertEquals("my build", buildConfiguration.getName());
    }

    protected BuildConfiguration getBuildConfiguration() {
        return new BuildConfiguration("my build");
    }

    @Test
    public void relevantDepthDefaultsToZero() {
        final BuildConfiguration buildConfiguration = getBuildConfiguration();
        assertEquals(0, buildConfiguration.getRelevantDepth());
        assertEquals(0, buildConfiguration.getRelevantDependentDepth());
    }

    @Test
    public void relevantDepthMatchesNormalDepthIfDependenciesAreRelevant() {
        final BuildConfiguration a = getBuildConfiguration();
        final BuildConfiguration b = getBuildConfiguration();

        a.addDependency(b);
        a.setRelevant(false);

        assertEquals(1, a.getRelevantDepth());
        assertEquals(0, b.getRelevantDependentDepth());
    }

    @Test
    public void relevantDepthIgnoresDependencyIfIrrelevant() {
        final BuildConfiguration a = getBuildConfiguration();
        final BuildConfiguration b = getBuildConfiguration();
        b.setRelevant(false);

        a.addDependency(b);

        assertEquals(0, a.getRelevantDepth());
        assertEquals(1, b.getRelevantDependentDepth());
    }

    @Test
    public void relevantDepthIgnoresIrrelevantBuildsDownTheTree() {
        final BuildConfiguration a = getBuildConfiguration();
        final BuildConfiguration b = getBuildConfiguration();
        final BuildConfiguration c = getBuildConfiguration();

        a.addDependency(b);
        b.addDependency(c);
        c.setRelevant(false);

        assertEquals(1, a.getRelevantDepth());
        assertEquals(2, c.getRelevantDependentDepth());
    }
}