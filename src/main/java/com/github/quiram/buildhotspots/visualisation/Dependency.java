package com.github.quiram.buildhotspots.visualisation;

public class Dependency {
    private BuildConfiguration target = null;
    private BuildConfiguration origin = null;

    public Dependency(BuildConfiguration origin, BuildConfiguration target) {
        this.target = target;
        this.origin = origin;
    }

    public BuildConfiguration getOrigin() {
        return origin;
    }

    public BuildConfiguration getTarget() {
        return target;
    }

}
