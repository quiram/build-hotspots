package com.github.quiram.buildhotspots.clients.jenkins.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpstreamProject {
    private String name;

    public String getName() {
        return name;
    }
}
