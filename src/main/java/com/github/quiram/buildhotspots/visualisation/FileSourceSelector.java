package com.github.quiram.buildhotspots.visualisation;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class FileSourceSelector extends SourceSelector {

    public FileSourceSelector() {
        super();

        Button btnBrowse = new Button();
        btnBrowse.setText("...");
        btnBrowse.setOnAction(event -> browse());

        gridPane.add(btnBrowse, 1, 1);
    }

    @Override
    protected String getDefaultSourceText() {
        return "very-large-system.xml";
    }

    @Override
    protected String getPromptLabel() {
        return "Please specify file name:";
    }

    private void browse() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Browse for File");
        alert.setHeaderText("Browse for xml file");
        alert.setContentText("Browse not yet implemented");

        alert.showAndWait();
    }
}
