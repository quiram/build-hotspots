package com.github.quiram.buildhotspots.clients.jenkins.beans;

public class JenkinsPathBuilder {
    public String build(Object... params) {
        StringBuilder sb = new StringBuilder();
        for (Object param : params) {
            sb.append('/').append(param);
        }

        sb.append("/api/json");

        return sb.toString();
    }
}
