package com.github.quiram.buildhotspots.visualisation;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.join;
import static java.util.stream.Collectors.toList;

/*
 * Class to represent a build configuration. It holds the JavaFX objects used to display this build configuration 
 */
@SuppressWarnings("restriction")
public class BuildConfigurationGroup extends Group {
    public static final String NEW_LINE = System.lineSeparator();
    private final BuildConfiguration buildConfiguration;
    private BuildHotspotsApplicationBase m_App = null;
    private Circle m_circle = null;
    private final ContextMenu m_contextMenu = new ContextMenu();
    private MenuItem m_hide = new MenuItem("Hide"); //exposed as we need to dynamically show/hide this menu item when the context menu is displayed
    private Set<DependencyLink> dependencyLinks = new HashSet<>();

    public BuildConfigurationGroup(
            BuildConfiguration buildConfiguration,
            double p_xPos,
            double p_yPos,
            BuildHotspotsApplicationBase p_App
    ) throws Exception {
        this.buildConfiguration = buildConfiguration;
        m_App = p_App;

        setLayoutX(p_xPos);
        setLayoutY(p_yPos);

        int circleRadius = 50;

        //TODO Code to change colour of circle dependent on percentage
        m_circle = new Circle(circleRadius, Color.web("blue", 1));
        getChildren().add(m_circle);

        HBox hb = new HBox();
        Label t = new Label(buildConfiguration.getName());
        t.setTextFill(Color.WHITE);
        t.setFont(Font.font(null, FontWeight.BOLD, 14));
        
        /*
         * this section commented out
         * we may want to experiment with this if we want to decrease the size of the text to fit inside circle
        Bounds circleBounds = m_circle.getBoundsInLocal();
        Bounds textBounds = t.getBoundsInLocal();
        //double wid_dif = textBounds.getWidth() - circleBounds.getWidth();
        //if (circleBounds.getWidth()<textBounds.getWidth()) {
        if (wid_dif>0) {
            double scalex = circleBounds.getWidth()/textBounds.getWidth();
            //System.out.println(scalex);
            // scale the text down to what the circle
            //t.setScaleX( scalex );
            
            //t.setLayoutX(- (wid_dif / 2));
            //System.out.println(wid_dif);
        }
        */

        hb.getChildren().add(t);
        hb.setPrefHeight(circleRadius * 2);
        hb.setPrefWidth(circleRadius * 2);

        hb.setLayoutX(-circleRadius);
        hb.setLayoutY(-circleRadius);
        hb.setAlignment(Pos.CENTER);

        getChildren().add(hb);

        //
        //Showing borders for debugging
        /*
        hb.setBorder(
        		new Border(
        				new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, null, null)
        		)
        );
        t.setBorder(
        		new Border(
        				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null)
        		)
        );        
        */

        setOnMousePressed(new MousePressedHandler());
        setOnMouseDragged(new MouseDraggedHandler());
        setOnMouseReleased(new MouseDragReleasedHandler());
        //this.setonm

        MenuItem info = new MenuItem("Info");
        m_contextMenu.getItems().add(info);
        info.setOnAction(event -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information for Build Configuration");
            alert.setHeaderText("Name: " + buildConfiguration.getName());
            String alertText = "";
            alertText += "Dependent Builds: " + join(",", getNamesOfDependents()) + NEW_LINE;
            alertText += "Dependent Depth: " + getDependentDepth() + NEW_LINE;
            alertText += "Dependencies: " + join(",", getNamesOfDependencies()) + NEW_LINE;
            alertText += "Dependency Depth: " + getDependencyDepth() + NEW_LINE;
            alertText += "Percentage: " + buildConfiguration.getFrequency() + "%" + NEW_LINE;
            alert.setContentText(alertText);
            alert.getDialogPane().setPrefSize(680, 320);
            alert.setResizable(true);
            alert.showAndWait();
        });

        m_hide.setOnAction(event -> {
            try {
                if (getNumDirectParents() != 0)
                    throw new Exception("Can't hide as there are more than 0 parent configurations");
                hide();
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Exception hiding");
                alert.setHeaderText(e.getMessage());
                String s = "";
                alert.setContentText(s);

                alert.setResizable(true);
                alert.getDialogPane().setPrefSize(680, 320);

                alert.showAndWait();
            }
        });

        m_contextMenu.getItems().add(m_hide);
    }

    public void registerDependencyLink(DependencyLink dependencyLink) {
        dependencyLinks.add(dependencyLink);
    }

    public List<String> getNamesOfDependencies() {
        return buildConfiguration.getDependencies().stream().map(BuildConfiguration::getName).collect(toList());
    }

    public List<String> getNamesOfDependents() {
        return buildConfiguration.getDependents().stream().map(BuildConfiguration::getName).collect(toList());
    }

    public void setPosition(double x, double y) {
        setLayoutX(x);
        setLayoutY(y);
        Draw();
    }

    public String getName() {
        return buildConfiguration.getName();
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

    /*
     * Returns the number of direct parents of this build configuration
     * To prevent GAPS in diagram we only allow hiding of a BC with 0 direct parents
     * Hidden parents do not count
     */
    public int getNumDirectParents() throws Exception {
        return (int) buildConfiguration.getDependents().stream().filter(BuildConfiguration::isRelevant).count();
    }

    /*
     * Returns the dependency depth of the build configuration excluding hidden build configurations
     * the dependency depth is 0 if there are no dependencies or only hidden dependencies
     * otherwise the dependency depth is one greater than the maximum depth of the dependencies
     */
    public int getDependencyDepth() {
        return buildConfiguration.getRelevantDepth();
    }

    /*
     * Returns the dependent depth of the build configuration excluding hidden build configurations
     * the dependent depth is 0 if there are no dependencies or only hidden dependencies
     * otherwise the dependency depth is one greater than the maximum depth of the dependencies
     */
    public int getDependentDepth() {
        return buildConfiguration.getRelevantDependentDepth();
    }

    public void hide() throws Exception {
        if (getNumDirectParents() != 0) throw new Exception("Can't hide");

        buildConfiguration.setRelevant(false);

        dependencyLinks.forEach(dependencyLink -> dependencyLink.setVisible(false));

        this.setVisible(false);
    }

    public void unhide() throws Exception {
        buildConfiguration.setRelevant(true);

        dependencyLinks.forEach(dependencyLink -> dependencyLink.setVisible(true));

        this.setVisible(true);
    }

    private class MousePressedHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent arg0) {
            if (arg0.getButton() == MouseButton.SECONDARY) return; //ignore right mouse button press
            if (m_App.isScrollLocked()) {
                return;
            }
            m_App.clearAllContextMenus(); //stop duplicate context menus appearing

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
            if (arg0.getButton() == MouseButton.SECONDARY) return; //ignore right mouse button press

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
            if (arg0.getButton() == MouseButton.SECONDARY) { //right button
                m_App.clearAllContextMenus(); //stop duplicate context menus appearing
                try {
                    m_hide.setVisible(getNumDirectParents() == 0);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                m_contextMenu.show(m_App.getScrollPane(), arg0.getScreenX(), arg0.getScreenY());
                return;
            }
            m_mouseDownX = -1;
            m_mouseDownY = -1;
            m_origLayoutX = -1;
            m_origLayoutY = -1;
            m_App.ReleaseScrollLock();
        }
    }

    public void Draw() {
        dependencyLinks.forEach(DependencyLink::Draw);
    }

    public double getRadius() {
        return m_circle.getRadius();
    }

    public void clearContextMenu() {
        m_contextMenu.hide();
    }
}
