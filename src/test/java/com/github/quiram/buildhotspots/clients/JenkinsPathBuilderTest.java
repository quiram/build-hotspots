package com.github.quiram.buildhotspots.clients;

import com.github.quiram.buildhotspots.clients.jenkins.beans.JenkinsPathBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JenkinsPathBuilderTest {
    @Test
    public void buildPathForDataset() {
        JenkinsPathBuilder pathBuilder = new JenkinsPathBuilder();
        String path = pathBuilder.build("job", "data-sets");
        assertEquals("/job/data-sets/api/json", path);
    }
}
