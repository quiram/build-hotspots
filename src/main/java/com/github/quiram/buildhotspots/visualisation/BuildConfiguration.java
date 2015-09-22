package com.github.quiram.buildhotspots.visualisation;

import com.github.quiram.buildhotspots.drawingdata.BuildConfigurationType;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
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
    private BuildHotspotsApplicationBase m_App = null;
    private Circle m_circle = null;
    private BuildConfigurationType m_xmlBC = null;
    private final ContextMenu m_contextMenu = new ContextMenu();
    private MenuItem m_hide = new MenuItem("Hide"); //exposed as we need to dynamically show/hide this menu item when the context menu is displayed
    
    public BuildConfiguration(
    		BuildConfigurationType p_xmlBC, 
    		double p_xPos, 
    		double p_yPos, 
    		BuildHotspotsApplicationBase p_App
    ) throws Exception {
        m_App = p_App;
        m_xmlBC = p_xmlBC;
        
        if (m_xmlBC.getBuildStats()==null) throw new Exception("There were no build stats attached to " + m_xmlBC.getName());

        setLayoutX(p_xPos);
        setLayoutY(p_yPos);

        m_circle = new Circle(30, Color.web("blue", 1));
        getChildren().add(m_circle);

        Text text = new Text(m_xmlBC.getName());
        getChildren().add(text);

        setOnMousePressed(new MousePressedHandler());
        setOnMouseDragged(new MouseDraggedHandler());
        setOnMouseReleased(new MouseDragReleasedHandler());
        //this.setonm
        
        MenuItem info = new MenuItem("Info");
        m_contextMenu.getItems().add(info);
        info.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	Alert alert = new Alert(AlertType.INFORMATION);
            	alert.setTitle("Information for Build Configuration");
            	alert.setHeaderText("Name: " + m_xmlBC.getName());
            	String alertText = "";
            	alertText += "Dependent Builds: " + getListStr(getDependents(),false) + "\n";
            	alertText += "Dependencies: " + getListStr(getDependencies(),true) + "\n";
            	alertText += "Percentage: " + m_xmlBC.getBuildStats().getPercentage() + "%\n";
            	alert.setContentText(alertText);
            	alert.getDialogPane().setPrefSize(680, 320);
            	alert.setResizable(true);
                alert.showAndWait();
            }
        });      
        m_hide.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	try {
	            	if (getNumDirectParents()!=0) throw new Exception("Can't hide as there are more than 0 parent configurations");
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
            }
        });      
        m_contextMenu.getItems().add(m_hide);
    }
    
    /*
     * Return a comma seperated list of all the dependency names
     * use origin or target depending on flag
     */
    private String getListStr(List<Dependency> p_dependencyList, boolean m_origin) {
    	String ret = "";
    	for (int c=0;c<p_dependencyList.size();c++) {
    		if (c>0) ret +=", ";
    		if (m_origin) {
	    		ret += p_dependencyList.get(c).getOrigin().getXMLType().getName();
    		} else {
	    		ret += p_dependencyList.get(c).getTarget().getXMLType().getName();    			
    		}
    	}
    	return ret;
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
        return m_Dependencies.stream().map(d -> {
            BuildConfiguration origin = d.getOrigin();
            int depth = 0;
            depth = 1 + origin.getDepth();
            return depth;
        }).max(Comparator.<Integer>naturalOrder()).orElse(0);
    }
    
    /*
     * Returns the number of direct parents of this build configuration
     * To prevent GAPS in diagram we only allow hiding of a BC with 0 direct parents
     * Hidden parents do not count
     */
    public int getNumDirectParents() throws Exception {
    	int ret = 0;
    	for (Dependency d : m_Dependents) {
    		if (d.isVisible()) ret++;
    	}
    	return ret;
    }
    
    public void hide() throws Exception {
    	if (getNumDirectParents()!=0) throw new Exception("Can't hide");
    	for (Dependency d : m_Dependencies) {
    		d.setVisible(false);
    	}
    	this.setVisible(false);
    }
    public void unhide() throws Exception {
    	for (Dependency d : m_Dependencies) {
    		d.setVisible(true);
    	}
    	this.setVisible(true);
    }

    private class MousePressedHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent arg0) {
        	if (arg0.getButton()==MouseButton.SECONDARY) return; //ignore right mouse button press
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
        	if (arg0.getButton()==MouseButton.SECONDARY) return; //ignore right mouse button press

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
        	if (arg0.getButton()==MouseButton.SECONDARY) { //right button
        		m_App.clearAllContextMenus(); //stop duplicate context menus appearing
        		try {
        			m_hide.setVisible(getNumDirectParents()==0);
        		} catch (Exception e) {
        			e.printStackTrace();
        			return;
        		}
        		m_contextMenu.show(m_App.getScrollPane(),arg0.getScreenX(),arg0.getScreenY());
        		return;
        	}
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
    private List<Dependency> m_Dependencies = new ArrayList<>(); //link to BC's that are dependancies of this BC
    private List<Dependency> m_Dependents = new ArrayList<>(); //link to BC's that are dependant on this one
    public List<Dependency> getDependencies() {return m_Dependencies;}
    public List<Dependency> getDependents() {return m_Dependents;}

    public Dependency addDependency(BuildConfiguration p_bc) {
        final Dependency dependency = new Dependency(p_bc, this);
        m_Dependencies.add(dependency);
        p_bc.RegisterDependantBC(dependency);
        return dependency;
    }
    
    public void RegisterDependantBC(Dependency p_dep) {
    	m_Dependents.add(p_dep);
    }
    
    public void Draw() {
    	m_Dependencies.forEach(Dependency::Draw);
    	m_Dependents.forEach(Dependency::Draw);
    }

    public double getRadius() {
        return m_circle.getRadius();
    }

	public void clearContextMenu() {
		m_contextMenu.hide();
	}
}
