package com.github.quiram.buildhotspots.clients.jenkins.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FirstBuildResponseBean {
    @JsonProperty("firstBuild")
    private FirstBuildBean firstBuild;

    public int getNumber() {
        return firstBuild.getNumber();
    }
}
