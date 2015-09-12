package com.github.quiram.buildhotspots.clients;

import java.util.ArrayList;
import java.util.List;

public class BuildConfigurations {
    private List<BuildConfiguration> buildConfigurations = new ArrayList<>();

    public void add(BuildConfiguration buildConfiguration) {
        buildConfigurations.add(buildConfiguration);
    }

    public BuildConfiguration get(int i) {
        return buildConfigurations.get(i);
    }

    public int size() {
        return buildConfigurations.size();
    }

    public boolean contains(BuildConfiguration buildConfiguration) {
        return buildConfigurations.contains(buildConfiguration);
    }
}
