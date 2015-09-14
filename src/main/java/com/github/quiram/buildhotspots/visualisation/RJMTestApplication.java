package com.github.quiram.buildhotspots.visualisation;

import javafx.scene.paint.Color;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.bind.Unmarshaller;

import com.github.quiram.buildhotspots.drawingdata.BuildConfigurationType;
import com.github.quiram.buildhotspots.drawingdata.Root;

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
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/*
 * Test class added by RJM to add some prototype code
 * which may be used as the structure of the visualisation section
 */
@SuppressWarnings("restriction")
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
        
    	//Add initial object that is not visible to give canvas minimum size (Stops all objects appearing at top left)
    	Rectangle r = new Rectangle();
    	r.setX(0);
    	r.setY(0);
    	r.setWidth(100);
    	r.setHeight(100);
    	//r.setVisible(false); //dosen't work, with this set it was the same as if the rectangle wasn't drawn at all
    	r.setFill(Color.TRANSPARENT); //used transparent instead
    	m_root.getChildren().add(r);
    	
        
    }
    
    private List<BuildConfiguration> m_buildConfigurations = new ArrayList<BuildConfiguration>();
    
    private void AddDrawingToScene() throws Exception {

    	
    	InputStream input = new BufferedInputStream(ClassLoader.getSystemClassLoader().getResourceAsStream("drawingDataExample001.xml"));
    	//byte[] buffer = new byte[8192];
    	//try {
    	//    for (int length = 0; (length = input.read(buffer)) != -1;) {
    	//        System.out.write(buffer, 0, length);
    	//    }
    	//} finally {
    	//    input.close();
    	//}    	
    	JAXBContext jaxbContext = JAXBContext.newInstance(Root.class);
    	Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    	Root docRoot = (Root) jaxbUnmarshaller.unmarshal(input);
    	
    	double initialposX = 60; 
    	double initialposY = 60; 
    	double initialpos_setupWidth = 100; 
    	double initialpos_setupHeight = 100; 

    	int c=0;
    	for (BuildConfigurationType curBC : docRoot.getBuildConfigurations().getBuildConfiguration()) {
    		BuildConfiguration bc = new BuildConfiguration (
    				(byte) ((c * 10) + 10), //percentage
    				initialposX + (c*initialpos_setupWidth),
    				initialposY + (c*initialpos_setupHeight),
    				this
    		);
    		m_root.getChildren().add(bc);
    		
    		if (c>0) {
    			Dependency d = new Dependency(m_buildConfigurations.get(c-1),bc);
    			m_buildConfigurations.get(c-1).registerRelatedDependency(d);
    			bc.registerRelatedDependency(d);
    			d.Draw();
    			m_root.getChildren().add(d);
    		}
    		
    		m_buildConfigurations.add(bc);
    		
    		c++;
    	}
    	
    	
    	
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

	public void LockScroll() {
        m_scroll.setPannable(false);
	}
	public void ReleaseScrollLock() {
        m_scroll.setPannable(true);		
	}
	public boolean isScrollLocked() {
		return !m_scroll.isPannable();
	}
    
    
}
