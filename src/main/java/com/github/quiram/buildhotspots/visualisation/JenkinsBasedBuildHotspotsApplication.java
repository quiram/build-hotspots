package com.github.quiram.buildhotspots.visualisation;

import com.github.quiram.buildhotspots.DrawingBuilder;
import com.github.quiram.buildhotspots.clients.jenkins.beans.JenkinsClient;
import com.github.quiram.buildhotspots.drawingdata.Root;

public class JenkinsBasedBuildHotspotsApplication extends BuildHotspotsApplicationBase {

    public static void main(String[] args) {
        System.out.println("Start JenkinsBasedBuildHotspotsApplication");
        launch(args);
        System.out.println("End JenkinsBasedBuildHotspotsApplication");
    }

    protected Root getRootDocument(String source) {
        JenkinsClient jenkinsClient = new JenkinsClient(source);

        DrawingBuilder drawingBuilder = new DrawingBuilder(jenkinsClient);
        return drawingBuilder.build();
    }

    @Override
    protected void browse() {
        throw new UnsupportedOperationException("Cannot browse.");
    }

    @Override
    protected String getPromptLabel() {
        return "Start by pointing at your Jenkins instance:";
    }

    @Override
    protected String getDefaultPromptValue() {
        return "run";
    }

    @Override
    protected String getTitle() {
        return "Build Hotspots - Draw from Jenkins";
    }
}
