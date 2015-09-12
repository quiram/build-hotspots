package com.github.quiram.buildhotspots.clients;

import java.util.List;

public interface CiClient {
    BuildConfigurations getAllBuildConfigurations();

    List<String> getDependenciesFor(String jobName);
}
