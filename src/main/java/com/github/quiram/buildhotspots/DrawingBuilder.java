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
import java.util.function.BiFunction;

import static java.lang.String.format;
import static java.util.stream.Collectors.toSet;

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

        // Work out dependencies (travel only up)
        while (!workingSet.isEmpty()) {
            System.out.println("Working set is " + workingSet);
            extendedList.addAll(workingSet);

            final Set<String> dependencies = getBuildsApplying(CiClient::getDependenciesFor, workingSet);
            System.out.println("Dependencies found: " + dependencies);
            workingSet.addAll(dependencies);

            workingSet.removeAll(extendedList);
        }

        // Work out dependents (travel only down)
        workingSet.addAll(buildNames);
        while (!workingSet.isEmpty()) {
            System.out.println("Working set is " + workingSet);
            extendedList.addAll(workingSet);

            final Set<String> dependents = getBuildsApplying(CiClient::getDependentsFor, workingSet);
            System.out.println("Dependents found: " + dependents);

            workingSet.addAll(dependents);

            workingSet.removeAll(extendedList);
        }

        System.out.println(format("Building drawing for '%s', which will include: %s", buildNames, extendedList));

        return buildRootDocument(new ArrayList<>(extendedList));
    }

    private Set<String> getBuildsApplying(BiFunction<CiClient, String, List<String>> relatedProjectsOperation, Set<String> workingSet) {
        return workingSet.stream().flatMap(buildName -> relatedProjectsOperation.apply(ciClient, buildName).stream()).collect(toSet());
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
        final String buildName = buildConfiguration.getName();
        final Optional<LocalDateTime> dateOfOldestBuild = ciClient.getDateOfOldestBuildFor(buildName);
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
