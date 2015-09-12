package com.github.quiram.buildhotspots.clients.jenkins.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Job {
    private final String name;

    public Job(@JsonProperty("name") String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Job)) {
            return false;
        }

        Job otherBuild = Job.class.cast(o);

        return name.equals(otherBuild.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }
}
