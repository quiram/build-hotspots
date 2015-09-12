package com.github.quiram.buildhotspots.clients.jenkins.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetBuildsResponse {
    @JsonProperty("jobs")
    List<Build> jobs;

    public List<Build> getJobs() {
        return jobs;
    }
}
