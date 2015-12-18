package com.github.quiram.buildhotspots.clients.jenkins.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

public class FirstBuildResponseBean {
    @JsonProperty("firstBuild")
    private FirstBuildBean firstBuild;

    public Optional<Integer> getNumber() {
        if (firstBuild == null) {
            return Optional.empty();
        }

        return Optional.of(firstBuild.getNumber());
    }
}
