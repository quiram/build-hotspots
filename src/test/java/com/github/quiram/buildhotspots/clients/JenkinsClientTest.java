package com.github.quiram.buildhotspots.clients;

import com.github.quiram.buildhotspots.clients.jenkins.beans.JenkinsClient;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.time.LocalDateTime.parse;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JenkinsClientTest {

    public static final String DATASETS_SERVICE = "datasets-service";
    private JenkinsClient jenkinsClient;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8080);
    public static final int DATASETS_SERVICE_OLDEST_BUILD_NUMBER = 51;

    @Before
    public void setUp() throws Exception {
        jenkinsClient = new JenkinsClient("http://localhost:8080/");

        mockOldestBuildNumber(DATASETS_SERVICE, DATASETS_SERVICE_OLDEST_BUILD_NUMBER);
    }

    /* Test to highlight the fact that I'm not filtering requests from JIRA,
     * this is wrong, but I don't know how to make this work with WireMock
     */
    @Ignore
    @Test
    public void testConnectToWireMock() throws InterruptedException {
        stubFor(get(urlEqualTo("/")).withQueryParam("tree", equalTo("firstBuild[number]")).
                willReturn(aResponse().withStatus(200).withBody("it works!")));

        WebTarget target = ClientBuilder.newClient().target("http://localhost:8080/");
        String s = target.path("").queryParam("tree", "firstBuild[number]").request().get(String.class);
        assertEquals("it works!", s);
    }

    @Test
    public void testGetOldestBuildNumberForDatasetsService() {
        int buildNumber = jenkinsClient.getOldestBuildNumber(DATASETS_SERVICE);
        assertThat(buildNumber).isEqualTo(DATASETS_SERVICE_OLDEST_BUILD_NUMBER);
    }

    @Test
    public void testGetOldestBuildNumberForDatasetsApi() {
        final String DATASETS_API = "datasets-api";
        int oldestBuildNumber = 3;
        mockOldestBuildNumber(DATASETS_API, oldestBuildNumber);
        int buildNumber = jenkinsClient.getOldestBuildNumber(DATASETS_API);
        assertThat(buildNumber).isEqualTo(oldestBuildNumber);
    }

    @Test
    public void testGetDateOfOldestAvailableBuildFromDatasetsService() {
        String jobName = DATASETS_SERVICE;
        String oldestBuildDatetime = mockDateOfBuild(jobName, DATASETS_SERVICE_OLDEST_BUILD_NUMBER);

        LocalDateTime date = jenkinsClient.getDateOfOldestAvailableBuild(DATASETS_SERVICE);
        LocalDateTime expectedDate = parse(oldestBuildDatetime);

        assertThat(date).isEqualTo(expectedDate);
    }

    @Test
    public void getListOfOneBuild() {
        stubReturnsListOfBuildsFrom("list-of-one-build.json");

        BuildConfigurations allBuilds = jenkinsClient.getAllBuildConfigurations();
        assertEquals(1, allBuilds.size());
        assertTrue(allBuilds.contains(new BuildConfiguration("build-one")));
    }

    @Test
    public void getListOfAllBuilds() {
        stubReturnsListOfBuildsFrom("list-of-multiple-builds.json");

        BuildConfigurations buildConfigurations = jenkinsClient.getAllBuildConfigurations();
        assertEquals(3, buildConfigurations.size());
    }

    private void stubReturnsListOfBuildsFrom(String fileName) {
        stubFor(get(urlEqualTo("/api/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile(fileName)));
    }

    private void mockOldestBuildNumber(String jobName, int oldestBuildNumber) {
        String url = "/job/" + jobName + "/api/json";
        stubFor(get(urlEqualTo(url))
                .willReturn(
                        aResponse().withStatus(200)
                                .withBody("{\"firstBuild\":{\"number\":" + oldestBuildNumber + "}}")
                                .withHeader("Content-Type", "application/json")));
    }

    private String mockDateOfBuild(String jobName, int buildNumber) {
        String oldestBuildDatetime = "2015-06-10T13:49:53";
        long buildDatetimeMilli = parse(oldestBuildDatetime).toInstant(ZoneOffset.of("Z")).toEpochMilli();

        String url = "/job/" + jobName + "/" + buildNumber + "/api/json";
        stubFor(get(urlEqualTo(url))
                .willReturn(
                        aResponse().withStatus(200)
                                .withBody("{\"timestamp\":" + buildDatetimeMilli + "}")
                                .withHeader("Content-Type", "application/json")));
        return oldestBuildDatetime;
    }
}

