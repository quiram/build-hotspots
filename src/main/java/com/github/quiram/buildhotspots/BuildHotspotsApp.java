package com.github.quiram.buildhotspots;

import com.github.quiram.buildhotspots.clients.JenkinsClient;
import com.github.quiram.buildhotspots.clients.beans.Build;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.IntStream;


public class BuildHotspotsApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Build Hotspots");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Scene scene = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);


        Label label = new Label("Start by pointing at your Jenkins instance:");
        TextField textField = new TextField("url");
        Text textArea = new Text();

        Button btn = new Button();
        btn.setText("Show me hotspots!");
        btn.setOnAction(event -> {
            JenkinsClient jenkinsClient = new JenkinsClient(textField.getText());
            final List<Build> allBuilds = jenkinsClient.getAllBuilds();
            StringBuilder sb = new StringBuilder();
            IntStream.range(0, 3).forEach(i -> sb.append(allBuilds.get(i)).append("\n"));
            textArea.setText(sb.toString());
        });

        grid.add(label, 0, 0);
        grid.add(textField, 0, 1);
        grid.add(btn, 0, 2);
        grid.add(textArea, 0, 3);

        primaryStage.show();
    }
}
