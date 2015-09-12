package com.github.quiram.buildhotspots;

import com.github.quiram.buildhotspots.clients.CiClient;
import com.github.quiram.buildhotspots.drawingdata.BuildConfigurationType;
import com.github.quiram.buildhotspots.drawingdata.DependencyType;
import com.github.quiram.buildhotspots.drawingdata.Root;
import com.github.quiram.buildhotspots.drawingdata.Root.BuildConfigurations;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DrawingBuilderTest {

    private CiClient ciClient;
    private DrawingBuilder drawingBuilder;

    @Before
    public void setUp() throws Exception {
        ciClient = mock(CiClient.class);
        drawingBuilder = new DrawingBuilder(ciClient);
    }

    @Test
    public void makeEmptyDrawingDocumentByDefault() {
        Root documentRoot = drawingBuilder.build();
        assertNotNull(documentRoot);
        final BuildConfigurations buildConfigurations = documentRoot.getBuildConfigurations();
        assertNotNull(buildConfigurations);
        final List<BuildConfigurationType> buildConfiguration = buildConfigurations.getBuildConfiguration();
        assertTrue(buildConfiguration.isEmpty());
    }

    @Test
    public void oneBuildConfiguration() {
        when(ciClient.getAllBuildConfigurations()).thenReturn(singletonList("my-build"));
        Root documentRoot = drawingBuilder.build();
        final List<BuildConfigurationType> buildConfigurationList = documentRoot.getBuildConfigurations().getBuildConfiguration();
        assertEquals(1, buildConfigurationList.size());
        final BuildConfigurationType buildConfigurationType = buildConfigurationList.get(0);
        assertEquals("my-build", buildConfigurationType.getName());
        assertNull(buildConfigurationType.getDependencies());
    }

    @Test
    public void twoBuildConfigurationsWithDependency() {
        final String mainBuildName = "my-build";
        final String dependentBuildName = "dependent-build";
        when(ciClient.getAllBuildConfigurations()).thenReturn(asList(mainBuildName, dependentBuildName));
        when(ciClient.getDependenciesFor(dependentBuildName)).thenReturn(singletonList(mainBuildName));

        Root documentRoot = drawingBuilder.build();

        final List<BuildConfigurationType> buildConfigurationList = documentRoot.getBuildConfigurations().getBuildConfiguration();

        final BuildConfigurationType buildConfiguration = buildConfigurationList.stream().filter(b -> b.getName().equals(dependentBuildName)).findFirst().get();
        final List<DependencyType> dependencyList = buildConfiguration.getDependencies().getDependency();
        assertFalse(dependencyList.isEmpty());
        assertEquals(mainBuildName, dependencyList.get(0).getBuildConfigurationName());
    }
}