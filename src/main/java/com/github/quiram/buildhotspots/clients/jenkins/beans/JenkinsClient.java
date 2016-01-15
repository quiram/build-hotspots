package com.github.quiram.buildhotspots.clients.jenkins.beans;

import com.github.quiram.buildhotspots.clients.CiClient;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public class JenkinsClient implements CiClient {

    private WebTarget target;
    private JenkinsPathBuilder pathBuilder = new JenkinsPathBuilder();

    public JenkinsClient(String jenkinsBaseUrl) {
        target = ClientBuilder.newClient().target(jenkinsBaseUrl);
    }

    /**
     * Provide the serial number for the oldest available build, if any.
     *
     * @param buildName Name of the build
     * @return Number of the oldest available build, if there is any.
     */
    public Optional<Integer> getOldestBuildNumber(String buildName) {
        FirstBuildResponseBean buildNumberBean = requestData(FirstBuildResponseBean.class, "firstBuild[number]", "job", buildName);

        try {
            return buildNumberBean.getNumber();
        } catch (Exception e) {
            System.err.println(format("Failed to obtain oldest build number for '%s'.", buildName));
            throw e;
        }
    }

    @Override
    public Optional<LocalDateTime> getDateOfOldestBuildFor(String jobName) {
        final Optional<Integer> oldestBuildNumber = getOldestBuildNumber(jobName);

        if (!oldestBuildNumber.isPresent()) {
            return Optional.empty();
        }

        TimestampBean timeStampBean = requestData(TimestampBean.class, "timestamp[*]", "job", jobName, oldestBuildNumber.get());

        Instant instant = Instant.ofEpochMilli(timeStampBean.getTimestamp());

        return Optional.of(LocalDateTime.ofInstant(instant, ZoneId.of("Z")));
    }

    private <T> T requestData(Class<T> returnType, String filter, Object... pathElements) {
        String requestUrl = pathBuilder.build(pathElements);

        try {
            WebTarget path = target.path(requestUrl);

            if (!filter.equals("")) {
                path = path.queryParam("tree", filter);
            }

            return path.request().get(returnType);
        } catch (Exception e) {
            System.err.println(format("Failed to request '%s' with tree filter '%s'", requestUrl, filter));
            throw e;
        }
    }

    @Override
    public List<String> getAllBuildConfigurations() {
        final GetBuildsResponse response = requestData(GetBuildsResponse.class, "");

        return response.getJobs().stream().map(Job::getName).collect(toList());
    }

    @Override
    public List<String> getDependenciesFor(String jobName) {
        return getDataForJob(jobName, GetBuildResponse::getUpstreamProjects);
    }

    @Override
    public List<String> getDependentsFor(String jobName) {
        return getDataForJob(jobName, GetBuildResponse::getDownstreamProjects);
    }

    private List<String> getDataForJob(String jobName, Function<GetBuildResponse, List<Project>> operation) {
        final GetBuildResponse response = requestData(GetBuildResponse.class, "", "job", jobName);
        final List<Project> referredProjects = operation.apply(response);
        return referredProjects.stream().map(Project::getName).collect(toList());
    }
}
