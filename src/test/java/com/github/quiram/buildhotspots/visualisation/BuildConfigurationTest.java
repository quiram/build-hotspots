package com.github.quiram.buildhotspots.visualisation;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BuildConfigurationTest {
    @Test
    public void newBuildConfigurationHasNoDepth() {
        final BuildConfiguration buildConfiguration = new BuildConfiguration("my build");
        assertEquals(0, buildConfiguration.getDepth());
    }

    @Test
    public void buildConfigurationWithOneDependencyHasDepthOne() {
        final BuildConfiguration mainBuildConfiguration = new BuildConfiguration("my build");
        final BuildConfiguration dependentConfiguration = new BuildConfiguration("my build");

        dependentConfiguration.addDependency(mainBuildConfiguration);

        assertEquals(1, dependentConfiguration.getDepth());
    }

    @Test
    public void buildConfigurationWithTwoDependenciesAtSameLevelHasDepthOne() {
        final BuildConfiguration mainBuildConfiguration = new BuildConfiguration("my build");
        final BuildConfiguration mainBuildConfiguration2 = new BuildConfiguration("my build");
        final BuildConfiguration dependentConfiguration = new BuildConfiguration("my build");

        dependentConfiguration.addDependency(mainBuildConfiguration);
        dependentConfiguration.addDependency(mainBuildConfiguration2);

        assertEquals(1, dependentConfiguration.getDepth());
    }

    @Test
    public void buildConfigurationWithTwoLevelOfChainDependenciesHasDepthTwo() {
        final BuildConfiguration mainBuildConfiguration = new BuildConfiguration("my build");
        final BuildConfiguration midBuildConfiguration = new BuildConfiguration("my build");
        final BuildConfiguration dependentConfiguration = new BuildConfiguration("my build");

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

        final BuildConfiguration A = new BuildConfiguration("my build");
        final BuildConfiguration B = new BuildConfiguration("my build");
        final BuildConfiguration C = new BuildConfiguration("my build");
        final BuildConfiguration D = new BuildConfiguration("my build");

        C.addDependency(A);
        D.addDependency(B);
        C.addDependency(D);

        assertEquals(2, C.getDepth());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void dependenciesCannotBeChangedOutsideOfBuildConfiguration() {
        final BuildConfiguration b = new BuildConfiguration("my build");
        final Set<BuildConfiguration> dependencies = b.getDependencies();
        dependencies.add(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void dependentsCannotBeChangedOutsideOfBuildConfiguration() {
        final BuildConfiguration b = new BuildConfiguration("my build");
        final Set<BuildConfiguration> dependents = b.getDependents();
        dependents.add(null);
    }

    @Test
    public void addDependency() {
        final BuildConfiguration b = new BuildConfiguration("my build");
        final BuildConfiguration a = new BuildConfiguration("my build");

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
        final BuildConfiguration a = new BuildConfiguration("my build");
        assertTrue(a.isRelevant());
    }

    @Test
    public void buildConfigurationNameAvailableFromConstructor() {
        final BuildConfiguration buildConfiguration = new BuildConfiguration("my build");
        assertEquals("my build", buildConfiguration.getName());
    }
}