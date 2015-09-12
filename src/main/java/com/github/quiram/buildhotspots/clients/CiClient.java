package com.github.quiram.buildhotspots.clients;

import java.util.List;

public interface CiClient {
    List<String> getAllBuildConfigurations();

    List<String> getDependenciesFor(String jobName);
}
