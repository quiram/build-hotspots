package com.github.quiram.buildhotspots.clients;

import com.github.quiram.buildhotspots.clients.jenkins.beans.Build;

import java.util.List;

public interface CiClient {
    List<Build> getAllBuilds();
}
