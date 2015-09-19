package com.github.quiram.buildhotspots.visualisation;

import com.github.quiram.buildhotspots.drawingdata.BuildConfigurationType;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.Collections.unmodifiableList;

/*
 * Class to represent a build configuration. It holds the JavaFX objects used to display this build configuration 
 */
@SuppressWarnings("restriction")
public class BuildConfiguration extends Group {
    private BuildHotspotsApplication m_App = null;
    private Circle m_circle = null;
    private BuildConfigurationType m_xmlBC = null;

    public BuildConfiguration(BuildConfigurationType p_xmlBC, double p_xPos, double p_yPos, BuildHotspotsApplication p_App) {
        m_App = p_App;
        m_xmlBC = p_xmlBC;

        setLayoutX(p_xPos);
        setLayoutY(p_yPos);

        m_circle = new Circle(30, Color.web("blue", 1));
        getChildren().add(m_circle);

        Text text = new Text(m_xmlBC.getName());
        getChildren().add(text);

        setOnMousePressed(new MousePressedHandler());
        setOnMouseDragged(new MouseDraggedHandler());
        setOnMouseReleased(new MouseDragReleasedHandler());
    }

    public void setPosition(double x, double y) {
        setLayoutX(x);
        setLayoutY(y);
    }

    public BuildConfigurationType getXMLType() {
        return m_xmlBC;
    }

    //Scale the value
    private double scalePercentage(byte p_percentage) {
        //p_percentage = 0..255
        double maxRadius = 100;
        return p_percentage * (maxRadius / 255);
    }

    private double m_mouseDownX = -1;
    private double m_mouseDownY = -1;
    private double m_origLayoutX = -1;
    private double m_origLayoutY = -1;

    public int getDepth() {
        return m_relatedDependencies.stream().map(d -> {
            BuildConfiguration origin = d.getOrigin();
            int depth = 0;
            if (origin != this) {
                depth = 1 + origin.getDepth();
            }
            return depth;
        }).max(Comparator.<Integer>naturalOrder()).orElse(0);
    }

    public List<Dependency> getDependencies() {
        return unmodifiableList(m_relatedDependencies);
    }

    public Dependency addDependency(BuildConfiguration dependency) {
        final Dependency dependencyLink = new Dependency(dependency, this);
        m_relatedDependencies.add(dependencyLink);
        dependency.registerRelatedDependency(dependencyLink);
        return dependencyLink;
    }

    private class MousePressedHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent arg0) {
            if (m_App.isScrollLocked()) {
                return;
            }
            m_mouseDownX = arg0.getSceneX();
            m_mouseDownY = arg0.getSceneY();
            m_origLayoutX = getLayoutX();
            m_origLayoutY = getLayoutY();
            m_App.LockScroll();
        }
    }

    private class MouseDraggedHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent arg0) {
            double offsetX = arg0.getSceneX() - m_mouseDownX;
            double offsetY = arg0.getSceneY() - m_mouseDownY;
            offsetX /= getParent().getScaleX();
            offsetY /= getParent().getScaleY();
            setLayoutX(m_origLayoutX + offsetX);
            setLayoutY(m_origLayoutY + offsetY);

            try {
                Draw();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private class MouseDragReleasedHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent arg0) {
            m_mouseDownX = -1;
            m_mouseDownY = -1;
            m_origLayoutX = -1;
            m_origLayoutY = -1;
            m_App.ReleaseScrollLock();
        }
    }

    /*
     * Keep a list of all related dependencies so that this object can redraw them when it is moved
     */
    private List<Dependency> m_relatedDependencies = new ArrayList<>();

    public void registerRelatedDependency(Dependency d) {
        m_relatedDependencies.add(d);
    }

    public void Draw() {
        m_relatedDependencies.forEach(Dependency::Draw);
    }

    public double getRadius() {
        return m_circle.getRadius();
    }
}
