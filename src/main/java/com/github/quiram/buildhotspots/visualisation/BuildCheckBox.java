package com.github.quiram.buildhotspots.visualisation;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;

public class BuildCheckBox {
    private final StringProperty name;
    private final BooleanProperty on = new SimpleBooleanProperty();

    public BuildCheckBox(String name, boolean on) {
        this.name = new ReadOnlyStringWrapper(name);
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
