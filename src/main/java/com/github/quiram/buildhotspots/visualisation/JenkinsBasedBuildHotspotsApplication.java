package com.github.quiram.buildhotspots.visualisation;

import com.github.quiram.buildhotspots.DateToFrequencyTransformer;
import com.github.quiram.buildhotspots.DrawingBuilder;
import com.github.quiram.buildhotspots.clients.CiClient;
import com.github.quiram.buildhotspots.clients.jenkins.beans.JenkinsClient;
import com.github.quiram.buildhotspots.drawingdata.Root;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.util.List;

public class JenkinsBasedBuildHotspotsApplication extends BuildHotspotsApplicationBase {

    private CiClient jenkinsClient;

    public static void main(String[] args) {
        System.out.println("Start JenkinsBasedBuildHotspotsApplication");
        launch(args);
        System.out.println("End JenkinsBasedBuildHotspotsApplication");
    }

    @Override
    protected String getTitle() {
        return "Build Hotspots - Draw from Jenkins";
    }

    @Override
    protected SourceSelector getSourceSelector() {
        return new UrlSourceSelector();
    }

    @Override
    protected void selectBuilds(String source) {
        jenkinsClient = new JenkinsClient(source);
        BuildSelector buildSelector = new BuildSelector(jenkinsClient.getAllBuildConfigurations());

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));


        grid.add(buildSelector, 0, 0);
        Scene scene = new Scene(grid, 250, 400);

        m_primaryStage.setScene(scene);

        Button btn = new Button();
        btn.setText("Show me hotspots!");
        btn.setOnAction(event -> AddDrawingToScene(buildSelector.getSelectedBuilds()));

        grid.add(btn, 0, 1);
    }

    private void AddDrawingToScene(List<String> selectedBuilds) {
        System.out.println("About to build root document for " + selectedBuilds);
        DrawingBuilder drawingBuilder = new DrawingBuilder(jenkinsClient, new DateToFrequencyTransformer());
        final Root root = drawingBuilder.buildFor(selectedBuilds);
        System.out.println("Basic root document obtained, beginning drawing.");
        buildConfigurationMap = createBuildConfigurationMap(root);
        AddDrawingToScene(root);
    }
}
