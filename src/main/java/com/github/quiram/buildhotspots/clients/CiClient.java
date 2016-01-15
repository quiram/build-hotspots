package com.github.quiram.buildhotspots.clients;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CiClient {
    Optional<LocalDateTime> getDateOfOldestBuildFor(String jobName);

    List<String> getAllBuildConfigurations();

    List<String> getDependenciesFor(String jobName);

    List<String> getDependentsFor(String jobName);
}
