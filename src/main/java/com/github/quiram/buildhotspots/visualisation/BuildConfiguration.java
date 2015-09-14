package com.github.quiram.buildhotspots.visualisation;

import java.util.ArrayList;
import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.input.MouseEvent;

/*
 * Class to represent a build configuration. It holds the JavaFX objects used to display this build configuration 
 */
public class BuildConfiguration extends Group {
	private RJMTestApplication m_App = null;
	private Circle m_circle = null;

	public BuildConfiguration(byte p_percentage, double p_xPos, double p_yPos, RJMTestApplication p_App) {
		m_App = p_App;
		
		setLayoutX(p_xPos);
		setLayoutY(p_yPos);
		
		m_circle = new Circle(scalePercentage(p_percentage), Color.web("blue", 1));
    	getChildren().add(m_circle);
		
    	setOnMousePressed(new MousePressedHandler());
    	setOnMouseDragged(new MouseDraggedHandler());
    	setOnMouseReleased(new MouseDragReleasedHandler());
    	

	}
	
	//TODO Scale the value
	private double scalePercentage(byte p_percentage) {
		//p_percentage = 0..255
		double maxRadius = 100;
		return p_percentage * (maxRadius / 255);
	}
	
	private double m_mouseDownX = -1;
	private double m_mouseDownY = -1;
	private double m_origLayoutX = -1;
	private double m_origLayoutY = -1;
	
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
    private List<Dependency> m_relatedDependencies = new ArrayList<Dependency>();
	public void registerRelatedDependency(Dependency d) {
		m_relatedDependencies.add(d);
	}    

	public void Draw() throws Exception {
		for (Dependency d : m_relatedDependencies) {
			d.Draw();
		}
	}

	public double getRadius() {
		return m_circle.getRadius();
	}
}
