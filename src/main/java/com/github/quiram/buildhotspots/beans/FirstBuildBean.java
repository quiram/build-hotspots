package com.github.quiram.buildhotspots.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FirstBuildBean {
    @JsonProperty("number")
    private int number;

    public int getNumber()
    {
        return number;
    }
}
