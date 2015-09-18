package com.github.quiram.buildhotspots.visualisation;

import com.github.quiram.buildhotspots.drawingdata.BuildConfigurationType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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

        connectBuildConfigurations(mainBuildConfiguration, dependentConfiguration);

        assertEquals(1, dependentConfiguration.getDepth());
    }

    @Test
    public void buildConfigurationWithTwoDependenciesAtSameLevelHasDepthOne() {
        final BuildConfiguration mainBuildConfiguration = getBuildConfiguration();
        final BuildConfiguration mainBuildConfiguration2 = getBuildConfiguration();
        final BuildConfiguration dependentConfiguration = getBuildConfiguration();

        connectBuildConfigurations(mainBuildConfiguration, dependentConfiguration);
        connectBuildConfigurations(mainBuildConfiguration2, dependentConfiguration);

        assertEquals(1, dependentConfiguration.getDepth());
    }

    @Test
    public void buildConfigurationWithTwoLevelOfChainDependenciesHasDepthTwo() {
        final BuildConfiguration mainBuildConfiguration = getBuildConfiguration();
        final BuildConfiguration midBuildConfiguration = getBuildConfiguration();
        final BuildConfiguration dependentConfiguration = getBuildConfiguration();

        connectBuildConfigurations(mainBuildConfiguration, midBuildConfiguration);
        connectBuildConfigurations(midBuildConfiguration, dependentConfiguration);

        assertEquals(2, dependentConfiguration.getDepth());
    }

    private BuildConfiguration getBuildConfiguration() {
        return new BuildConfiguration(new BuildConfigurationType(), 1, 1, null);
    }

    @Test
    public void buildConfigurationWithTwoBranchesOfDepenciesThatEquatToDepthTwo() {
        /*
         * A -> C
         * B -> D -> C
         */

        final BuildConfiguration A = getBuildConfiguration();
        final BuildConfiguration B = getBuildConfiguration();
        final BuildConfiguration C = getBuildConfiguration();
        final BuildConfiguration D = getBuildConfiguration();

        connectBuildConfigurations(A, C);
        connectBuildConfigurations(B, D);
        connectBuildConfigurations(D, C);

        assertEquals(2, C.getDepth());
    }

    private void connectBuildConfigurations(BuildConfiguration mainBuildConfiguration, BuildConfiguration dependentConfiguration) {
        final Dependency dependency = new Dependency(mainBuildConfiguration, dependentConfiguration);
        mainBuildConfiguration.registerRelatedDependency(dependency);
        dependentConfiguration.registerRelatedDependency(dependency);
    }
}