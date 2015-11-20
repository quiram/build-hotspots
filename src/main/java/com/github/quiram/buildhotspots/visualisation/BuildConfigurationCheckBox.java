package com.github.quiram.buildhotspots.visualisation;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;

public class BuildConfigurationCheckBox {
    private final StringProperty name;
    private final BooleanProperty on = new SimpleBooleanProperty();

    public BuildConfigurationCheckBox(BuildConfiguration buildConfiguration, boolean on) {
        name = new ReadOnlyStringWrapper(buildConfiguration.getName());
        this.on.addListener(observable -> buildConfiguration.setRelevantTransitively(isOn()));
        setOn(on);
    }

    public final StringProperty nameProperty() {
        return this.name;
    }

    public final String getName() {
        return this.nameProperty().get();
    }

    public final BooleanProperty onProperty() {
        return this.on;
    }

    public final boolean isOn() {
        return this.onProperty().get();
    }

    public final void setOn(final boolean on) {
        this.onProperty().set(on);
    }

    @Override
    public String toString() {
        return getName();
    }
}
