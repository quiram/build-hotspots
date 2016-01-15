package com.github.quiram.buildhotspots;

import com.github.quiram.buildhotspots.clients.CiClient;
import com.github.quiram.buildhotspots.drawingdata.BuildConfigurationType;
import com.github.quiram.buildhotspots.drawingdata.BuildConfigurationType.BuildStats;
import com.github.quiram.buildhotspots.drawingdata.BuildConfigurationType.Dependencies;
import com.github.quiram.buildhotspots.drawingdata.DependencyType;
import com.github.quiram.buildhotspots.drawingdata.Root;
import com.github.quiram.buildhotspots.drawingdata.Root.BuildConfigurations;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class DrawingBuilder {

    private final CiClient ciClient;
    private final DateToFrequencyTransformer transformer;

    public DrawingBuilder(CiClient ciClient, DateToFrequencyTransformer transformer) {
        this.ciClient = ciClient;
        this.transformer = transformer;
    }

    public Root buildForAll() {
        final List<String> allBuildConfigurations = ciClient.getAllBuildConfigurations();

        return buildRootDocument(allBuildConfigurations);
    }

    public Root buildFor(List<String> buildNames) {
        Set<String> workingSet = new HashSet<>(buildNames);
        Set<String> extendedList = new HashSet<>();

        while (!workingSet.isEmpty()) {
            extendedList.addAll(workingSet);

            final Set<String> dependencies = workingSet.stream().flatMap(buildName -> ciClient.getDependenciesFor(buildName).stream()).collect(Collectors.toSet());
            workingSet.addAll(dependencies);

            workingSet.removeAll(extendedList);
        }

        return buildRootDocument(new ArrayList<>(extendedList));
    }

    private Root buildRootDocument(List<String> buildNames) {
        final BuildConfigurations buildConfigurations = new BuildConfigurations();
        final Root root = new Root();
        root.setBuildConfigurations(buildConfigurations);
        final List<BuildConfigurationType> buildConfigurationsList = root.getBuildConfigurations().getBuildConfiguration();

        // First pass: collect basic data for the builds
        buildNames.forEach(buildName -> {
            final Optional<LocalDateTime> dateOfOldestBuild;

            try {
                dateOfOldestBuild = ciClient.getDateOfOldestBuildFor(buildName);
            } catch (Exception e) {
                throw new BuildNotFoundException(buildName, e);
            }

            dateOfOldestBuild.ifPresent(transformer::add);

            final BuildConfigurationType buildConfiguration = new BuildConfigurationType();
            buildConfiguration.setName(buildName);

            setDependencies(buildName, buildConfiguration);

            buildConfigurationsList.add(buildConfiguration);
        });

        // Second pass: calculate relative frequency for all available builds
        buildConfigurationsList.forEach(this::setBuildStats);

        return root;
    }

    private void setBuildStats(BuildConfigurationType buildConfiguration) {
        BuildStats buildStats = new BuildStats();
        final Optional<LocalDateTime> dateOfOldestBuild = ciClient.getDateOfOldestBuildFor(buildConfiguration.getName());
        buildStats.setPercentage(dateOfOldestBuild.map(transformer::getFrequencyFor).orElse(50L).byteValue());

        buildConfiguration.setBuildStats(buildStats);
    }

    private void setDependencies(String buildName, BuildConfigurationType buildConfiguration) {
        final Dependencies dependencies = new Dependencies();
        final List<DependencyType> dependencyList = dependencies.getDependency();

        ciClient.getDependenciesFor(buildName).forEach(dependency -> {
            final DependencyType dependencyType = new DependencyType();
            dependencyType.setBuildConfigurationName(dependency);
            dependencyList.add(dependencyType);
        });

        if (!dependencyList.isEmpty()) {
            buildConfiguration.setDependencies(dependencies);
        }
    }
}
