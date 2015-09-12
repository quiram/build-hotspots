package com.github.quiram.buildhotspots.clients.jenkins.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TimestampBean {
    @JsonProperty("timestamp")
    private long timestamp;

    public long getTimestamp()
    {
        return timestamp;
    }
}
