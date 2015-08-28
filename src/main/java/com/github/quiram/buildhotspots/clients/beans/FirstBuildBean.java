package com.github.quiram.buildhotspots.clients.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FirstBuildBean {
    @JsonProperty("number")
    private int number;

    public int getNumber()
    {
        return number;
    }
}
