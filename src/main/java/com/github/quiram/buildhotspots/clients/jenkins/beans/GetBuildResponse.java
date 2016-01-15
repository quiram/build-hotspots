package com.github.quiram.buildhotspots.clients.jenkins.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetBuildResponse {
    private List<Project> upstreamProjects;
    private List<Project> downstreamProjects;

    public List<Project> getUpstreamProjects() {
        return upstreamProjects;
    }

    public List<Project> getDownstreamProjects() {
        return downstreamProjects;
    }
}
