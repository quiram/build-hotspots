package com.github.quiram.buildhotspots.visualisation;

import com.github.quiram.buildhotspots.DrawingBuilder;
import com.github.quiram.buildhotspots.clients.jenkins.beans.JenkinsClient;
import com.github.quiram.buildhotspots.drawingdata.Root;

public class JenkinsBasedBuildHotspotsApplication extends BuildHotspotsApplicationBase {

    public static void main(String[] args) {
        System.out.println("Start BuildHotspotsApplication");
        launch(args);
        System.out.println("End BuildHotspotsApplication");
    }

    @Override
    protected Root getRootDocument(String source) {
        JenkinsClient jenkinsClient = new JenkinsClient(source);

        DrawingBuilder drawingBuilder = new DrawingBuilder(jenkinsClient);
        return drawingBuilder.build();
    }
}
