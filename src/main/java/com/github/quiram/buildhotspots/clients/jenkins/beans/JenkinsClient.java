package com.github.quiram.buildhotspots.clients.jenkins.beans;

import com.github.quiram.buildhotspots.clients.BuildConfiguration;
import com.github.quiram.buildhotspots.clients.CiClient;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static java.util.stream.Collectors.toList;

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

    public LocalDateTime getDateOfOldestAvailableFor(BuildConfiguration buildConfiguration) {
        final String buildConfigName = buildConfiguration.getName();
        TimestampBean timeStampBean = requestData(TimestampBean.class, "timestamp[*]", "job", buildConfigName, getOldestBuildNumber(buildConfigName));

        Instant instant = Instant.ofEpochMilli(timeStampBean.getTimestamp());

        return LocalDateTime.ofInstant(instant, ZoneId.of("Z"));
    }

    private <T> T requestData(Class<T> returnType, String filter, Object... pathElements) {
        String requestUrl = pathBuilder.build(pathElements);
        // TODO: Use the filter!
        return target.path(requestUrl).request().get(returnType);
    }

    @Override
    public List<String> getAllBuildConfigurations() {
        final GetBuildsResponse response = requestData(GetBuildsResponse.class, "");

        return response.getJobs().stream().map(Job::getName).collect(toList());
    }

    @Override
    public List<String> getDependenciesFor(String jobName) {
        final GetBuildResponse response = requestData(GetBuildResponse.class, "job", jobName);
        final List<UpstreamProject> upstreamProjects = response.getUpstreamProjects();
        return upstreamProjects.stream().map(UpstreamProject::getName).collect(toList());
    }
}
