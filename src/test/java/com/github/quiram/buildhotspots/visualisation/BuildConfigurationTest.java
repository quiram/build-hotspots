package com.github.quiram.buildhotspots.visualisation;

import com.github.quiram.buildhotspots.drawingdata.BuildConfigurationType;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class BuildConfigurationTest {
    @Test
    public void newBuildConfigurationHasNoDepth() throws Exception {
        final BuildConfiguration buildConfiguration = getBuildConfiguration();
        assertEquals(0, buildConfiguration.getDepth());
    }

    @Test
    public void buildConfigurationWithOneDependencyHasDepthOne() throws Exception {
        final BuildConfiguration mainBuildConfiguration = getBuildConfiguration();
        final BuildConfiguration dependentConfiguration = getBuildConfiguration();

        dependentConfiguration.addDependency(mainBuildConfiguration);

        assertEquals(1, dependentConfiguration.getDepth());
    }

    @Test
    public void buildConfigurationWithTwoDependenciesAtSameLevelHasDepthOne() throws Exception {
        final BuildConfiguration mainBuildConfiguration = getBuildConfiguration();
        final BuildConfiguration mainBuildConfiguration2 = getBuildConfiguration();
        final BuildConfiguration dependentConfiguration = getBuildConfiguration();

        dependentConfiguration.addDependency(mainBuildConfiguration);
        dependentConfiguration.addDependency(mainBuildConfiguration2);

        assertEquals(1, dependentConfiguration.getDepth());
    }

    @Test
    public void buildConfigurationWithTwoLevelOfChainDependenciesHasDepthTwo() throws Exception {
        final BuildConfiguration mainBuildConfiguration = getBuildConfiguration();
        final BuildConfiguration midBuildConfiguration = getBuildConfiguration();
        final BuildConfiguration dependentConfiguration = getBuildConfiguration();

        midBuildConfiguration.addDependency(mainBuildConfiguration);
        dependentConfiguration.addDependency(midBuildConfiguration);

        assertEquals(2, dependentConfiguration.getDepth());
    }

    private BuildConfiguration getBuildConfiguration() throws Exception {
        return new BuildConfiguration(new BuildConfigurationType(), 1, 1, null);
    }

    @Test
    public void buildConfigurationWithTwoBranchesOfDepenciesThatEquatToDepthTwo() throws Exception {
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
    public void dependenciesCannotBeChangedOutsideOfBuildConfiguration() throws Exception {
        final BuildConfiguration b = getBuildConfiguration();
        final List<Dependency> dependencies = b.getDependencies();
        dependencies.add(null);
    }

    @Test
    public void addDependency() throws Exception {
        final BuildConfiguration b = getBuildConfiguration();
        final BuildConfiguration a = getBuildConfiguration();
        
        //This test makes a dependancy of b
        //so a must be built before b
        Dependency dependency = b.addDependency(a);

        //First check that b has it's dependency registered
        List<Dependency> dependencies = b.getDependencies();
        List<Dependency> dependants = b.getDependents();
        assertEquals(1, dependencies.size());
        assertEquals(0, dependants.size());
        assertEquals(a, dependency.getOrigin());
        assertEquals(b, dependency.getTarget());
        
        //Now check the reverse
        //a has a dependent registered
        dependencies = a.getDependencies();
        dependants = a.getDependents();
        assertEquals(0, dependencies.size());
        assertEquals(1, dependants.size());
    }

}