package com.github.quiram.buildhotspots.visualisation;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

public class BuildConfiguration {
    private Set<BuildConfiguration> dependencies = new HashSet<>();
    private Set<BuildConfiguration> dependents = new HashSet<>();
    private boolean relevant = true;
    private final String name;
    private int frequency;

    public BuildConfiguration(String name, int frequency) {
        this.name = name;
        this.frequency = frequency;
    }

    public void addDependency(BuildConfiguration dependency) {
        dependencies.add(dependency);
        dependency.dependents.add(this);
    }

    public int getDepth() {
        return dependencies.stream().
                mapToInt(dependency -> 1 + dependency.getDepth()).
                max().orElse(0);
    }

    public Set<BuildConfiguration> getDependencies() {
        return unmodifiableSet(dependencies);
    }

    public Set<BuildConfiguration> getDependents() {
        return unmodifiableSet(dependents);
    }

    public boolean isRelevant() {
        return relevant;
    }

    public void setRelevant(boolean relevant) {
        this.relevant = relevant;
    }

    public String getName() {
        return name;
    }

    public int getRelevantDepth() {
        return dependencies.stream().
                filter(BuildConfiguration::isRelevant).
                mapToInt(dependency -> 1 + dependency.getRelevantDepth()).
                max().orElse(0);
    }

    public int getRelevantDependentDepth() {
        return dependents.stream().
                filter(BuildConfiguration::isRelevant).
                mapToInt(dependency -> 1 + dependency.getRelevantDependentDepth()).
                max().orElse(0);
    }

    public int getFrequency() {
        return frequency;
    }

    public void setRelevantTransitively(boolean relevant) {
        setRelevantForTransitiveDependencies(relevant);
        setRelevantForTransitiveDependents(relevant);
    }

    private void setRelevantForTransitiveDependencies(boolean relevant) {
        this.relevant = relevant;
        dependencies.forEach(buildConfiguration -> buildConfiguration.setRelevantForTransitiveDependencies(relevant));
    }

    private void setRelevantForTransitiveDependents(boolean relevant) {
        this.relevant = relevant;
        dependents.forEach(buildConfiguration -> buildConfiguration.setRelevantForTransitiveDependents(relevant));
    }
}
