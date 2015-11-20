package com.github.quiram.buildhotspots.visualisation;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

abstract public class SourceSelector extends Group {
    protected final GridPane gridPane;
    private final TextField textField;

    public SourceSelector() {
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label label = new Label(getPromptLabel());
        label.setMinWidth(400);
        gridPane.add(label, 0, 0);

        textField = new TextField(getDefaultSourceText());
        gridPane.add(textField, 0, 1);
        getChildren().add(gridPane);
    }

    abstract protected String getDefaultSourceText();

    abstract protected String getPromptLabel();

    public String getSource() {
        return textField.getText();
    }
}
