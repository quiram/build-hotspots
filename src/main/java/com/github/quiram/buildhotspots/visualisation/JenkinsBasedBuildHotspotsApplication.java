package com.github.quiram.buildhotspots.visualisation;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import com.github.quiram.buildhotspots.DrawingBuilder;
import com.github.quiram.buildhotspots.clients.jenkins.beans.JenkinsClient;
import com.github.quiram.buildhotspots.drawingdata.Root;

public class JenkinsBasedBuildHotspotsApplication extends BuildHotspotsApplicationBase {

    public static void main(String[] args) {
        System.out.println("Start JenkinsBasedBuildHotspotsApplication");
        launch(args);
        System.out.println("End JenkinsBasedBuildHotspotsApplication");
    }

    private Root getRootDocument(String source) {
        JenkinsClient jenkinsClient = new JenkinsClient(source);

        DrawingBuilder drawingBuilder = new DrawingBuilder(jenkinsClient);
        return drawingBuilder.build();
    }

	@Override
	protected void showDrawingSelectionScene() {
        m_primaryStage.setTitle("Build Hotspots - Draw from Jenkins");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Scene scene = new Scene(grid, 300, 275);
        m_primaryStage.setScene(scene);

        Label label = new Label("Start by pointing at your Jenkins instance:");
        TextField textField = new TextField("url");

        Button btn = new Button();
        btn.setText("Show me hotspots!");
        btn.setOnAction(event -> AddDrawingToScene(getRootDocument(textField.getText())));

        grid.add(label, 0, 0);
        grid.add(textField, 0, 1);
        grid.add(btn, 0, 2);

        m_primaryStage.show();
	}
}
