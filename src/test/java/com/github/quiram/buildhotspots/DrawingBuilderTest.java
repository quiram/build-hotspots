package com.github.quiram.buildhotspots;

import com.github.quiram.buildhotspots.clients.CiClient;
import com.github.quiram.buildhotspots.drawingdata.BuildConfigurationType;
import com.github.quiram.buildhotspots.drawingdata.BuildConfigurationType.BuildStats;
import com.github.quiram.buildhotspots.drawingdata.DependencyType;
import com.github.quiram.buildhotspots.drawingdata.Root;
import com.github.quiram.buildhotspots.drawingdata.Root.BuildConfigurations;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static java.time.LocalDateTime.now;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class DrawingBuilderTest {

    private CiClient ciClient;
    private DrawingBuilder drawingBuilder;
    private DateToFrequencyTransformer transformer;

    @Rule
    public ExpectedException onError = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        ciClient = mock(CiClient.class);
        transformer = mock(DateToFrequencyTransformer.class);
        drawingBuilder = new DrawingBuilder(ciClient, transformer);

        when(ciClient.getDateOfOldestBuildFor(anyString())).thenReturn(Optional.empty());
    }

    @Test
    public void makeEmptyDrawingDocumentByDefault() {
        Root documentRoot = drawingBuilder.buildForAll();
        assertEmptyDocument(documentRoot);
    }

    @Test
    public void oneBuildConfiguration() {
        final String buildName = "my-build";
        when(ciClient.getAllBuildConfigurations()).thenReturn(singletonList(buildName));
        Root documentRoot = drawingBuilder.buildForAll();
        assertRootHasOneBuild(documentRoot, buildName);
    }

    @Test
    public void twoBuildConfigurationsWithDependency() {
        final String mainBuildName = "my-build";
        final String dependentBuildName = "dependent-build";
        when(ciClient.getAllBuildConfigurations()).thenReturn(asList(mainBuildName, dependentBuildName));
        when(ciClient.getDependenciesFor(dependentBuildName)).thenReturn(singletonList(mainBuildName));

        Root documentRoot = drawingBuilder.buildForAll();

        assertRootHasTwoDependentBuilds(documentRoot, mainBuildName, dependentBuildName);
    }

    @Test
    public void buildStatsDefaultTo50IfNoBuildDataAvailable() {
        when(ciClient.getAllBuildConfigurations()).thenReturn(singletonList("my-build"));
        when(ciClient.getDateOfOldestBuildFor("my-build")).thenReturn(Optional.empty());

        Root documentRoot = drawingBuilder.buildForAll();
        final List<BuildConfigurationType> buildConfiguration = documentRoot.getBuildConfigurations().getBuildConfiguration();
        final BuildStats buildStats = buildConfiguration.get(0).getBuildStats();
        assertNotNull(buildStats);
        assertEquals(50, buildStats.getPercentage());
    }

    @Test
    public void buildStatsCalculatedThroughTransformer() {
        final LocalDateTime oldestAvailableBuildDate = now();
        final long frequency = new Random().nextInt(101);

        when(ciClient.getAllBuildConfigurations()).thenReturn(singletonList("my-build"));
        when(ciClient.getDateOfOldestBuildFor("my-build")).thenReturn(Optional.of(oldestAvailableBuildDate));
        when(transformer.getFrequencyFor(oldestAvailableBuildDate)).thenReturn(frequency);

        Root documentRoot = drawingBuilder.buildForAll();
        final List<BuildConfigurationType> buildConfiguration = documentRoot.getBuildConfigurations().getBuildConfiguration();
        final BuildStats buildStats = buildConfiguration.get(0).getBuildStats();
        assertNotNull(buildStats);
        assertEquals(frequency, buildStats.getPercentage());
    }

    @Test
    public void transformerMustBeFilledUpBeforeGettingData() {
        final String mainBuildName = "my-build";
        final String dependentBuildName = "dependent-build";
        when(ciClient.getAllBuildConfigurations()).thenReturn(asList(mainBuildName, dependentBuildName));
        when(ciClient.getDateOfOldestBuildFor(anyString())).thenReturn(Optional.of(now()));

        drawingBuilder.buildForAll();

        InOrder inOrder = inOrder(transformer);
        inOrder.verify(transformer, times(2)).add(any(LocalDateTime.class));
        inOrder.verify(transformer, times(2)).getFrequencyFor(any(LocalDateTime.class));
    }

    @Test
    public void onlyBuildConfigurationsWithOldestBuildAreValidForTheTransformer() {
        when(ciClient.getAllBuildConfigurations()).thenReturn(singletonList("my-build"));
        final String mainBuildName = "my-build";
        final String dependentBuildName = "dependent-build";
        when(ciClient.getAllBuildConfigurations()).thenReturn(asList(mainBuildName, dependentBuildName));
        when(ciClient.getDateOfOldestBuildFor(mainBuildName)).thenReturn(Optional.of(now()));

        drawingBuilder.buildForAll();

        verify(transformer, times(1)).add(any(LocalDateTime.class));
    }

    @Test
    public void buildForReturnsEmptyRootIfNoNames() {
        when(ciClient.getAllBuildConfigurations()).thenReturn(singletonList("my-build"));
        Root documentRoot = drawingBuilder.buildFor(emptyList());
        assertEmptyDocument(documentRoot);
    }

    @Test
    public void buildForFailsIfSpecifiedNameDoesNotExist() {
        final String buildName = "my-build";
        final Throwable innerException = new RuntimeException("Build does not exist");

        onError.expect(BuildNotFoundException.class);
        onError.expectMessage(containsString(buildName));
        onError.expectCause(equalTo(innerException));

        when(ciClient.getDateOfOldestBuildFor(buildName)).thenThrow(innerException);

        drawingBuilder.buildFor(singletonList(buildName));
    }

    @Test
    public void rootDocumentBuildsIfRequestedBuildNameExists() {
        final String buildName = "my-build";
        final List<String> buildNamesList = singletonList(buildName);

        Root documentRoot = drawingBuilder.buildFor(buildNamesList);
        assertRootHasOneBuild(documentRoot, buildName);
    }

    @Test
    public void buildForIncludesDependencies() {
        final String buildName = "my-build";
        final String dependencyBuildName = "dependency-build";
        final List<String> buildNamesList = singletonList(buildName);

        when(ciClient.getDependenciesFor(buildName)).thenReturn(singletonList(dependencyBuildName));

        Root documentRoot = drawingBuilder.buildFor(buildNamesList);
        assertRootHasTwoDependentBuilds(documentRoot, dependencyBuildName, buildName);
    }

    @Test
    public void extendedListDoesNotContainDuplicatesEvenIfDependenciesAreSpecifiedInParameter() {
        final String buildName = "my-build";
        final String dependencyBuildName = "dependency-build";
        final List<String> buildNamesList = asList(buildName, dependencyBuildName);

        when(ciClient.getDependenciesFor(buildName)).thenReturn(singletonList(dependencyBuildName));

        Root documentRoot = drawingBuilder.buildFor(buildNamesList);
        final List<BuildConfigurationType> buildConfigurationList = documentRoot.getBuildConfigurations().getBuildConfiguration();
        assertEquals(2, buildConfigurationList.size());
    }

    @Test
    public void transitiveDependenciesAreIncludedInDocumentRoot() {
        final String buildName = "my-build";
        final String dependencyBuildName = "dependency-build";

        when(ciClient.getDependenciesFor(buildName)).thenReturn(singletonList(dependencyBuildName));
        when(ciClient.getDependenciesFor(dependencyBuildName)).thenReturn(singletonList("transitive-dependency-build"));

        Root documentRoot = drawingBuilder.buildFor(singletonList(buildName));
        final List<BuildConfigurationType> buildConfigurationList = documentRoot.getBuildConfigurations().getBuildConfiguration();
        assertEquals(3, buildConfigurationList.size());
    }

    private void assertEmptyDocument(Root documentRoot) {
        assertNotNull(documentRoot);
        final BuildConfigurations buildConfigurations = documentRoot.getBuildConfigurations();
        assertNotNull(buildConfigurations);
        final List<BuildConfigurationType> buildConfiguration = buildConfigurations.getBuildConfiguration();
        assertTrue(buildConfiguration.isEmpty());
    }

    private void assertRootHasOneBuild(Root documentRoot, String buildName) {
        final List<BuildConfigurationType> buildConfigurationList = documentRoot.getBuildConfigurations().getBuildConfiguration();
        assertEquals(1, buildConfigurationList.size());
        final BuildConfigurationType buildConfigurationType = buildConfigurationList.get(0);
        assertEquals(buildName, buildConfigurationType.getName());
        assertNull(buildConfigurationType.getDependencies());
    }

    private void assertRootHasTwoDependentBuilds(Root documentRoot, String mainBuildName, String dependentBuildName) {
        final List<BuildConfigurationType> buildConfigurationList = documentRoot.getBuildConfigurations().getBuildConfiguration();

        final BuildConfigurationType buildConfiguration = buildConfigurationList.stream().filter(b -> b.getName().equals(dependentBuildName)).findFirst().get();
        final List<DependencyType> dependencyList = buildConfiguration.getDependencies().getDependency();
        assertFalse(dependencyList.isEmpty());
        assertEquals(mainBuildName, dependencyList.get(0).getBuildConfigurationName());
    }
}