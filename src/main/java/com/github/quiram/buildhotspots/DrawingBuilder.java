package com.github.quiram.buildhotspots;

import com.github.quiram.buildhotspots.clients.CiClient;
import com.github.quiram.buildhotspots.drawingdata.BuildConfigurationType;
import com.github.quiram.buildhotspots.drawingdata.BuildConfigurationType.Dependencies;
import com.github.quiram.buildhotspots.drawingdata.DependencyType;
import com.github.quiram.buildhotspots.drawingdata.Root;
import com.github.quiram.buildhotspots.drawingdata.Root.BuildConfigurations;

import java.util.List;

public class DrawingBuilder {

    private final CiClient ciClient;

    public DrawingBuilder(CiClient ciClient) {
        this.ciClient = ciClient;
    }

    public Root build() {
        final BuildConfigurations buildConfigurations = new BuildConfigurations();
        final List<BuildConfigurationType> buildConfigurationsList = buildConfigurations.getBuildConfiguration();

        ciClient.getAllBuildConfigurations().forEach(buildName -> {
            final BuildConfigurationType buildConfiguration = new BuildConfigurationType();
            buildConfiguration.setName(buildName);

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

            buildConfigurationsList.add(buildConfiguration);
        });

        final Root root = new Root();
        root.setBuildConfigurations(buildConfigurations);
        return root;
    }
}
