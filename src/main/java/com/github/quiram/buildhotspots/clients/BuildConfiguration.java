package com.github.quiram.buildhotspots.clients;

public class BuildConfiguration {
    private final String name;

    public BuildConfiguration(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BuildConfiguration)) {
            return false;
        }

        BuildConfiguration otherBuild = BuildConfiguration.class.cast(o);

        return name.equals(otherBuild.name);
    }
}
