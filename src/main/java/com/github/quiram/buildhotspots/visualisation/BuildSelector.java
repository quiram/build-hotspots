package com.github.quiram.buildhotspots.visualisation;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class BuildSelector extends BorderPane {

    private final ListView<BuildCheckBox> listView;

    public BuildSelector(List<String> buildNames) {
        super();
        listView = new ListView<>();

        buildNames.forEach(buildName -> {
            BuildCheckBox buildCheckBox = new BuildCheckBox(buildName, false);

            listView.getItems().add(buildCheckBox);
        });


        listView.setCellFactory(CheckBoxListCell.forListView(new Callback<BuildCheckBox, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(BuildCheckBox buildCheckBox) {
                return buildCheckBox.onProperty();
            }
        }));

        setCenter(listView);
    }

    public List<String> getSelectedBuilds() {
        return listView.getItems().stream().filter(BuildCheckBox::isOn).map(BuildCheckBox::getName).collect(toList());
    }
}
