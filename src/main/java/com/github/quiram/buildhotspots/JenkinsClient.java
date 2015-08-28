package com.github.quiram.buildhotspots;

import com.github.quiram.buildhotspots.beans.FirstBuildResponseBean;
import com.github.quiram.buildhotspots.beans.TimestampBean;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class JenkinsClient {

    private WebTarget target;
    private JenkinsPathBuilder pathBuilder = new JenkinsPathBuilder();

    public JenkinsClient(String jenkinsBaseUrl) {
        target = ClientBuilder.newClient().target(jenkinsBaseUrl);
    }

    public int getOldestBuildNumber(String buildName) {
        FirstBuildResponseBean buildNumberBean = requestData(FirstBuildResponseBean.class, "firstBuild[number]", buildName);

        return buildNumberBean.getNumber();
    }

    public LocalDateTime getDateOfOldestAvailableBuild(String buildName) {
        TimestampBean timeStampBean = requestData(TimestampBean.class, "timestamp[*]", buildName, getOldestBuildNumber(buildName));

        Instant instant = Instant.ofEpochMilli(timeStampBean.getTimestamp());

        return LocalDateTime.ofInstant(instant, ZoneId.of("Z"));
    }

    private <T> T requestData(Class<T> returnType, String filter, Object... pathElements) {
        String requestUrl = pathBuilder.build(pathElements);
        // TODO: Use the filter!
        return target.path(requestUrl).request().get(returnType);
    }
}
