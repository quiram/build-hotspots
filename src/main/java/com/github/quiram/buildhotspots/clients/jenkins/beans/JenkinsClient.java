package com.github.quiram.buildhotspots.clients.jenkins.beans;

import com.github.quiram.buildhotspots.clients.BuildConfiguration;
import com.github.quiram.buildhotspots.clients.BuildConfigurations;
import com.github.quiram.buildhotspots.clients.CiClient;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class JenkinsClient implements CiClient {

    private WebTarget target;
    private JenkinsPathBuilder pathBuilder = new JenkinsPathBuilder();

    public JenkinsClient(String jenkinsBaseUrl) {
        target = ClientBuilder.newClient().target(jenkinsBaseUrl);
    }

    public int getOldestBuildNumber(String buildName) {
        FirstBuildResponseBean buildNumberBean = requestData(FirstBuildResponseBean.class, "firstBuild[number]", "job", buildName);

        return buildNumberBean.getNumber();
    }

    public LocalDateTime getDateOfOldestAvailableBuild(String buildName) {
        TimestampBean timeStampBean = requestData(TimestampBean.class, "timestamp[*]", "job", buildName, getOldestBuildNumber(buildName));

        Instant instant = Instant.ofEpochMilli(timeStampBean.getTimestamp());

        return LocalDateTime.ofInstant(instant, ZoneId.of("Z"));
    }

    private <T> T requestData(Class<T> returnType, String filter, Object... pathElements) {
        String requestUrl = pathBuilder.build(pathElements);
        // TODO: Use the filter!
        return target.path(requestUrl).request().get(returnType);
    }

    @Override
    public BuildConfigurations getAllBuildConfigurations() {
        final GetBuildsResponse response = requestData(GetBuildsResponse.class, "");

        BuildConfigurations buildConfigurations = new BuildConfigurations();

        response.getJobs().forEach(job -> buildConfigurations.add(new BuildConfiguration(job.getName())));

        return buildConfigurations;
    }
}
