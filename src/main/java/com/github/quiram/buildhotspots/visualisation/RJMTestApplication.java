package com.github.quiram.buildhotspots.visualisation;

import javafx.scene.paint.Color;import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/*
 * Test class added by RJM to add some prototype code
 * which may be used as the structure of the visualisation section
 */
public class RJMTestApplication extends Application  {
    private Stage m_primaryStage = null;
    private Scene m_scene = null;
    final Group m_root = new Group();
	ScrollPane m_scroll = null;

    private HBox setupToolbar() {
    	HBox toolbar = new HBox();
    	
    	Button savePNG = new Button("Save PNG");
    	savePNG.setOnAction(new EventHandler<ActionEvent>() {
    	    @Override public void handle(ActionEvent e) {
				//Canvas canvas = new Canvas(root.getBoundsInLocal().getWidth(),root.getBoundsInLocal().getHeight());
				//GraphicsContext gc = canvas.getGraphicsContext2D();
				WritableImage image = new WritableImage(
						(int)m_root.getBoundsInParent().getWidth(),
						(int)m_root.getBoundsInParent().getHeight()
				);
				m_root.snapshot(null, image);
				
				BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
				try {
					ImageIO.write(bImage, "png", new File("build-hotspots.png"));
				} catch (IOException e1) {
					e1.printStackTrace();					
				}

    	    }
    	});
    	toolbar.getChildren().add(savePNG);

    	return toolbar;
    }	
    private void createScene() {
    	m_scroll = new ScrollPane();

    	BorderPane bp = new BorderPane();
    	bp.setTop(setupToolbar());
    	bp.setCenter(m_scroll);
    	
    	m_scene = new Scene(bp);
    	
    	m_scroll.setPrefSize(500, 300);
        m_scroll.prefViewportWidthProperty().bind(m_scene.widthProperty());
        m_scroll.prefViewportHeightProperty().bind(m_scene.widthProperty());        

    	m_scroll.addEventFilter(ScrollEvent.ANY, new ZoomHandler());        
    	
		m_primaryStage.setTitle("RJMTestApplication build-hotspots");
		m_primaryStage.setScene(m_scene);
        
        m_scroll.setPannable(true);
        m_scroll.setContent(new Group(m_root));
    }
    
    private void AddDrawingToScene() {
    	//TODO Process to code here
    	
    	//sourceXML - XML document containing data from builds
    	//userXML - XML document with user state (per document)
    	//combineStageXSL - XSL document to combine the source XML and user XML into masterXML
    	//masterXML - XML document with the result of the combine stage
    	
    	//STEP 1 - combine
    	
    	//generateGraphXSL - XSL document to transform masterXML to 
    	//graphFXML - FXML document to be drawn in scene
    	
    	//STEP 2 - generate Graph
    	
    	
    	
    	Circle circle = new Circle(150, Color.web("blue", 1));
    	m_root.getChildren().add(circle);
    	
    	
    	
    	
    }
    
	@Override
	public void start(final Stage primaryStage) throws Exception {
		m_primaryStage = primaryStage;
		createScene();

		AddDrawingToScene();
		
        m_root.autosize();
        
		primaryStage.show();
		
		AnimationTimer at = new AnimationTimer() {
			private int m_runs = 0;
			private long m_lastRun = -1;
			
			@Override
			public void handle(long arg0) {
				//System.out.println("now=" + arg0 + ":" + m_lastRun);
				if ((arg0-m_lastRun)<1 * 100000000) return;
				m_lastRun = arg0;
				try {
					m_runs++;
					m_root.autosize();
					m_root.layout();
					//m_mainDrawing.DrawRelationships();
					//System.out.println("RRR");
					if (m_runs>3) {
						
						this.stop();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
			}
			
		};
		at.start();
	}

    public static void main(String[] args) {
		System.out.println("Start RJMTestApplication");
        launch(args);
		System.out.println("End RJMTestApplication");
    }
	
    private static final double MAX_SCALE = 5.5d;
    private static final double MIN_SCALE = .1d;

    private class ZoomHandler implements EventHandler<ScrollEvent> {

        private ZoomHandler() {
        }

        @Override
        public void handle(ScrollEvent scrollEvent) {
            if (scrollEvent.isControlDown()) {
                final double scale = calculateScale(scrollEvent);
                m_root.setScaleX(scale);
                m_root.setScaleY(scale);
                
                m_root.autosize();
                m_root.layout();
                m_root.autosize();
                m_root.layout();
                m_root.autosize();
                m_root.layout();
                
                scrollEvent.consume();
            }
        }

        private double calculateScale(ScrollEvent scrollEvent) {
            double scale = m_root.getScaleX() + scrollEvent.getDeltaY() / 100;

            if (scale <= MIN_SCALE) {
                scale = MIN_SCALE;
            } else if (scale >= MAX_SCALE) {
                scale = MAX_SCALE;
            }
            return scale;
        }
    }      
}
