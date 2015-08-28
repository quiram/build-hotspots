package com.github.quiram.buildhotspots;

public class JenkinsPathBuilder {
    public String build(Object... params) {
        StringBuilder sb = new StringBuilder();
        sb.append("/job");
        for (Object param : params) {
            sb.append('/').append(param);
        }

        sb.append("/api/json");

        return sb.toString();
    }
}
