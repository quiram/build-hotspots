package com.github.quiram.buildhotspots.clients.jenkins.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetBuildResponse {
    private List<UpstreamProject> upstreamProjects;

    public List<UpstreamProject> getUpstreamProjects() {
        return upstreamProjects;
    }
}
