package com.github.quiram.buildhotspots.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Build {
    private final String buildName;

    public Build(@JsonProperty("name") String buildName) {
        this.buildName = buildName;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Build)) {
            return false;
        }

        Build otherBuild = Build.class.cast(o);

        return buildName.equals(otherBuild.buildName);
    }

    @Override
    public int hashCode() {
        return buildName.hashCode();
    }
}
