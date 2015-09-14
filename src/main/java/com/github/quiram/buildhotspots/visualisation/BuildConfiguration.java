package com.github.quiram.buildhotspots.visualisation;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.input.MouseEvent;

/*
 * Class to represent a build configuration. It holds the JavaFX objects used to display this build confiugration 
 */
public class BuildConfiguration {
	Group m_root = null;
	RJMTestApplication m_App = null;

	public BuildConfiguration(Group p_container, double p_xPos, double p_yPos, RJMTestApplication p_App) {
		m_App = p_App;
		
		m_root = new Group();
		m_root.setLayoutX(p_xPos);
		m_root.setLayoutY(p_yPos);
		
    	Circle circle = new Circle(50, Color.web("blue", 1));
    	m_root.getChildren().add(circle);
		
    	m_root.setOnMousePressed(new MousePressedHandler());
    	m_root.setOnMouseDragged(new MouseDraggedHandler());
    	m_root.setOnMouseReleased(new MouseDragReleasedHandler());
    	
		p_container.getChildren().add(m_root);
		
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
			m_origLayoutX = m_root.getLayoutX();
			m_origLayoutY = m_root.getLayoutY();
			m_App.LockScroll();
		}
    }
    private class MouseDraggedHandler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent arg0) {
			double offsetX = arg0.getSceneX() - m_mouseDownX;
			double offsetY = arg0.getSceneY() - m_mouseDownY;
			offsetX /= m_root.getParent().getScaleX();
			offsetY /= m_root.getParent().getScaleY();
			m_root.setLayoutX(m_origLayoutX + offsetX);
			m_root.setLayoutY(m_origLayoutY + offsetY);
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

	
}
