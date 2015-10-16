package com.github.quiram.buildhotspots.visualisation;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

public class BuildConfiguration {
    private Set<BuildConfiguration> dependencies = new HashSet<>();
    private Set<BuildConfiguration> dependents = new HashSet<>();
    private boolean relevant = true;

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
}
