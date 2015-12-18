package com.github.quiram.buildhotspots;

import com.github.quiram.buildhotspots.clients.CiClient;
import com.github.quiram.buildhotspots.drawingdata.BuildConfigurationType;
import com.github.quiram.buildhotspots.drawingdata.BuildConfigurationType.BuildStats;
import com.github.quiram.buildhotspots.drawingdata.BuildConfigurationType.Dependencies;
import com.github.quiram.buildhotspots.drawingdata.DependencyType;
import com.github.quiram.buildhotspots.drawingdata.Root;
import com.github.quiram.buildhotspots.drawingdata.Root.BuildConfigurations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class DrawingBuilder {

    private final CiClient ciClient;
    private final DateToFrequencyTransformer transformer;

    public DrawingBuilder(CiClient ciClient, DateToFrequencyTransformer transformer) {
        this.ciClient = ciClient;
        this.transformer = transformer;
    }

    public Root build() {
        final BuildConfigurations buildConfigurations = new BuildConfigurations();
        final List<BuildConfigurationType> buildConfigurationsList = buildConfigurations.getBuildConfiguration();

        // First pass: collect basic data for all builds
        ciClient.getAllBuildConfigurations().forEach(buildName -> {
            final Optional<LocalDateTime> dateOfOldestBuild = ciClient.getDateOfOldestBuildFor(buildName);
            dateOfOldestBuild.ifPresent(transformer::add);

            final BuildConfigurationType buildConfiguration = new BuildConfigurationType();
            buildConfiguration.setName(buildName);

            setDependencies(buildName, buildConfiguration);

            buildConfigurationsList.add(buildConfiguration);
        });

        // Second pass: calculate relative frequency for all available builds
        buildConfigurationsList.forEach(this::setBuildStats);

        final Root root = new Root();
        root.setBuildConfigurations(buildConfigurations);
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
