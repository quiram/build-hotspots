package com.github.quiram.buildhotspots;

public class BuildNotFoundException extends RuntimeException {
    public BuildNotFoundException(String buildName, Exception e) {
        super("Missing build name: " + buildName, e);
    }
}
