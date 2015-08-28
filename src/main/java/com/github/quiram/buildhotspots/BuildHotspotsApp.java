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
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;


public class BuildHotspotsApp extends Application {
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        showWelcomeScreen();
    }

    private void showWelcomeScreen() {
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

        Button btn = new Button();
        btn.setText("Show me hotspots!");
        btn.setOnAction(event -> showHotspots(textField.getText()));

        grid.add(label, 0, 0);
        grid.add(textField, 0, 1);
        grid.add(btn, 0, 2);

        primaryStage.show();
    }

    private void showHotspots(String url) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Scene scene = new Scene(grid, 1200, 600);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();


        JenkinsClient jenkinsClient = new JenkinsClient(url);
        final List<Build> allBuilds = jenkinsClient.getAllBuilds();

        final int rows = 3;
        final int cols = 4;
        final List<StackPane> objectsToDraw = IntStream.range(0, rows * cols).mapToObj(i -> paintBuild(allBuilds.get(i))).collect(toList());

        IntStream.range(0, cols).forEach(i -> {
                    IntStream.range(0, rows).forEach(j -> {
                        grid.add(objectsToDraw.get(i * rows + j), i, j);
                    });
                }
        );

    }

    private StackPane paintBuild(Build build) {
        StackPane stack = new StackPane();

        final Circle circle = new Circle(90, Color.web("blue"));
        stack.getChildren().add(circle);
        final Text text = new Text(build.toString());
        text.setBoundsType(TextBoundsType.VISUAL);
        stack.getChildren().add(text);

        return stack;
    }
}
