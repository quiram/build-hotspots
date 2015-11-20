package com.github.quiram.buildhotspots.visualisation;

import com.github.quiram.buildhotspots.drawingdata.BuildConfigurationType;
import com.github.quiram.buildhotspots.drawingdata.Root;
import com.github.quiram.buildhotspots.visualisation.layouts.DependencyWalkLayout;
import com.github.quiram.buildhotspots.visualisation.layouts.LayoutBase;
import com.github.quiram.buildhotspots.visualisation.layouts.OriginalLayout;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.github.quiram.buildhotspots.visualisation.layouts.OriginalLayout.ORIGINAL_LAYOUT;

/*
 * Test class added by RJM to add some prototype code
 * which may be used as the structure of the visualisation section
 */
@SuppressWarnings("restriction")
public abstract class BuildHotspotsApplicationBase extends Application {
    protected Stage m_primaryStage = null;
    private final Group m_root = new Group();
    private ScrollPane m_scroll = null;
    private Map<String, LayoutBase> m_layouts = null; //List of supported layouts
    private Map<String, BuildConfiguration> buildConfigurationMap;

    public BuildHotspotsApplicationBase() {
        m_layouts = new HashMap<>();
        LayoutBase lb = new OriginalLayout();
        m_layouts.put(lb.getName(), lb);
        lb = new DependencyWalkLayout();
        m_layouts.put(lb.getName(), lb);
    }

    private HBox setupToolbar() {
        HBox toolbar = new HBox();

        Button savePNG = new Button("Save PNG");
        savePNG.setOnAction(e -> {
            //Canvas canvas = new Canvas(root.getBoundsInLocal().getWidth(),root.getBoundsInLocal().getHeight());
            //GraphicsContext gc = canvas.getGraphicsContext2D();
            WritableImage image = new WritableImage(
                    (int) m_root.getBoundsInParent().getWidth(),
                    (int) m_root.getBoundsInParent().getHeight()
            );
            m_root.snapshot(null, image);

            BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
            try {
                ImageIO.write(bImage, "png", new File("build-hotspots.png"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        });
        toolbar.getChildren().add(savePNG);

        toolbar.getChildren().add(new Separator());

        Button showAll = new Button("Show All");
        showAll.setOnAction(ee -> {
            try {
                for (BuildConfigurationGroup bc : m_buildConfigurations.values()) {
                    if (!bc.isVisible()) {
                        bc.unhide();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Exception showing");
                alert.setHeaderText(e.getMessage());
                String s = "";
                alert.setContentText(s);

                alert.setResizable(true);
                alert.getDialogPane().setPrefSize(680, 320);

                alert.showAndWait();
            }
        });
        toolbar.getChildren().add(showAll);

        toolbar.getChildren().add(new Separator());

        for (LayoutBase lb : m_layouts.values()) {
            Button curBut = new Button(lb.getName());
            curBut.setOnAction(new layoutEventHandler(lb));
            toolbar.getChildren().add(curBut);
        }

        return toolbar;
    }

    private class layoutEventHandler implements EventHandler<ActionEvent> {
        LayoutBase m_lb = null;

        public layoutEventHandler(LayoutBase p_lb) {
            m_lb = p_lb;
        }

        @Override
        public void handle(ActionEvent arg0) {
            try {
                m_lb.executeLayout(m_buildConfigurations);
            } catch (Exception e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Executing Layout");
                alert.setHeaderText("Layout " + m_lb.getName() + " failed");
                String alertText = e.getMessage();
                alert.setContentText(alertText);
                alert.showAndWait();
                e.printStackTrace();
            }
        }

    }

    private void createScene() {
        m_scroll = new ScrollPane();

        BorderPane bp = new BorderPane();
        bp.setTop(setupToolbar());
        bp.setCenter(m_scroll);

        Scene m_scene = new Scene(bp);

        m_scroll.setPrefSize(500, 300);
        m_scroll.prefViewportWidthProperty().bind(m_scene.widthProperty());
        m_scroll.prefViewportHeightProperty().bind(m_scene.widthProperty());

        m_scroll.addEventFilter(ScrollEvent.ANY, new ZoomHandler());

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

        m_scroll.setOnMousePressed(new MousePressedHandler());
    }

    private class MousePressedHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent arg0) {
            clearAllContextMenus();
        }
    }

    private Map<String, BuildConfigurationGroup> m_buildConfigurations = new HashMap<>();

    protected void selectBuilds(Root p_docRoot) {
        buildConfigurationMap = createBuildConfigurationMap(p_docRoot);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));


        BorderPane listOfBuilds = getListOfBuildsPane();
        grid.add(listOfBuilds, 0, 0);
        Scene scene = new Scene(grid, 250, 400);

        m_primaryStage.setScene(scene);

        Button btn = new Button();
        btn.setText("Show me hotspots!");
        btn.setOnAction(event -> AddDrawingToScene(p_docRoot));

        grid.add(btn, 0, 1);
    }

    private BorderPane getListOfBuildsPane() {
        ListView<BuildConfigurationCheckBox> listView = new ListView<>();

        buildConfigurationMap.keySet().stream().sorted().forEach(buildConfigurationName ->
        {
            BuildConfigurationCheckBox buildConfigurationCheckBox = new BuildConfigurationCheckBox(buildConfigurationMap.get(buildConfigurationName), false);

            // observe checkbox's on property and display message if it changes:
            buildConfigurationCheckBox.onProperty().addListener((obs, wasOn, isNowOn) -> {
                System.out.println(buildConfigurationCheckBox.getName() + " changed on state from " + wasOn + " to " + isNowOn);
            });

            listView.getItems().add(buildConfigurationCheckBox);
        });

        listView.setCellFactory(CheckBoxListCell.forListView(new Callback<BuildConfigurationCheckBox, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(BuildConfigurationCheckBox buildConfigurationCheckBox) {
                return buildConfigurationCheckBox.onProperty();
            }
        }));

        return new BorderPane(listView);
    }

    protected void AddDrawingToScene(Root p_docRoot) {
        try {
            createScene();

            double initialposX = 60;
            double initialposY = 60;
            double initialpos_setupWidth = 100;
            double initialpos_setupHeight = 100;

            // Create BuildConfigurationGroups
            int c = 0;
            for (BuildConfigurationType curBCXML : p_docRoot.getBuildConfigurations().getBuildConfiguration()) {
                final BuildConfiguration buildConfiguration = buildConfigurationMap.get(curBCXML.getName());
                BuildConfigurationGroup bc = new BuildConfigurationGroup(
                        buildConfiguration,
                        initialposX + (c * initialpos_setupWidth),
                        initialposY + (c * initialpos_setupHeight),
                        this
                );

                if (!buildConfiguration.isRelevant()) {
                    bc.hide();
                }

                m_root.getChildren().add(bc);

                BuildConfigurationGroup foundBC = m_buildConfigurations.get(curBCXML.getName());
                if (foundBC != null)
                    throw new RuntimeException("Two BuildConfigurations exist in this file with the name " + curBCXML.getName());
                m_buildConfigurations.put(curBCXML.getName(), bc);

                c++;
            }

            // Register Dependency Links for visualisation
            //noinspection CodeBlock2Expr
            buildConfigurationMap.values().forEach(buildConfiguration -> {
                buildConfiguration.getDependencies().forEach(dependency -> {
                    BuildConfigurationGroup currentBuildConfigurationGroup = m_buildConfigurations.get(buildConfiguration.getName());
                    BuildConfigurationGroup foreignBuildConfigurationGroup = m_buildConfigurations.get(dependency.getName());

                    final DependencyLink dependencyLink = new DependencyLink(currentBuildConfigurationGroup, foreignBuildConfigurationGroup);
                    dependencyLink.Draw();
                    m_root.getChildren().add(dependencyLink);
                });
            });

            //TODO Future version will replace this with code to load state rather then preform layout
            LayoutBase lo = this.m_layouts.get(ORIGINAL_LAYOUT);
            lo.executeLayout(m_buildConfigurations);
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Exception drawing scene");
            alert.setHeaderText(e.getMessage());
            String s = "";
            for (int c = 0; c < e.getStackTrace().length; c++) {
                s += e.getStackTrace()[c].toString() + System.getProperty("line.separator");
            }
            alert.setContentText(s);

            alert.setResizable(true);
            alert.getDialogPane().setPrefSize(680, 320);

            alert.showAndWait();
            m_primaryStage.close();
        }
    }

    private Map<String, BuildConfiguration> createBuildConfigurationMap(Root p_docRoot) {
        Map<String, BuildConfiguration> buildConfigurationMap = new HashMap<>();
        // First pass: create an object for each buildConfiguration
        p_docRoot.getBuildConfigurations().getBuildConfiguration().stream()
                .forEach(buildConfigurationType -> buildConfigurationMap.put(buildConfigurationType.getName(),
                        new BuildConfiguration(buildConfigurationType.getName(), buildConfigurationType.getBuildStats().getPercentage())));

        // Second pass: link the different buildConfigurations according to the dependency data
        p_docRoot.getBuildConfigurations().getBuildConfiguration().stream()
                .forEach(buildConfigurationType -> {
                    final BuildConfigurationType.Dependencies dependencies = buildConfigurationType.getDependencies();
                    if (dependencies != null) {
                        dependencies.getDependency().forEach(dependencyType -> {
                            BuildConfiguration buildConfiguration = buildConfigurationMap.get(buildConfigurationType.getName());
                            BuildConfiguration dependency = buildConfigurationMap.get(dependencyType.getBuildConfigurationName());
                            buildConfiguration.addDependency(dependency);
                        });
                    }
                });
        return buildConfigurationMap;
    }

    /*
     * Allow use to have a generic selection scene - different for file and jenkins
     */
    protected void showDrawingSelectionScene() throws Exception {
        m_primaryStage.setWidth(500); //Set up initial window width
        m_primaryStage.setTitle(getTitle());

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Scene scene = new Scene(grid, 300, 275);
        m_primaryStage.setScene(scene);

        SourceSelector sourceSelector = getSourceSelector();

        Button btn = new Button();
        btn.setText("Select builds");
        btn.setOnAction(event -> selectBuilds(getRootDocument(sourceSelector.getSource())));

        grid.add(sourceSelector, 0, 0);
        grid.add(btn, 0, 1);

        m_primaryStage.show();
    }

    protected abstract String getTitle();

    protected abstract SourceSelector getSourceSelector();

    protected abstract Root getRootDocument(String source);

    @Override
    public void start(final Stage primaryStage) throws Exception {
        m_primaryStage = primaryStage;
        createScene();

        showDrawingSelectionScene();

        m_root.autosize();

        primaryStage.show();

        AnimationTimer at = new AnimationTimer() {
            private int m_runs = 0;
            private long m_lastRun = -1;

            @Override
            public void handle(long arg0) {
                //System.out.println("now=" + arg0 + ":" + m_lastRun);
                if ((arg0 - m_lastRun) < 100000000) return;
                m_lastRun = arg0;
                try {
                    m_runs++;
                    m_root.autosize();
                    m_root.layout();
                    //m_mainDrawing.DrawRelationships();
                    //System.out.println("RRR");
                    if (m_runs > 3) {

                        this.stop();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        at.start();
    }

    private static final double MAX_SCALE = 5.5d;
    private static final double MIN_SCALE = .01d;

    /*
     * Scroll the window so that the specified point in the canvas lines up with the specified point in the window
     * 
     * lots of comments here as coding this made my head hurt
     */
    private void scrollTo(Point2D p_windowPos, Point2D p_canvasPos) {
        double scale = m_root.getScaleX();
        //this code currently dosen't work.
        //it seems to behave correctly when the xscrollbar is at the left most but it is wanders off target
        //as we zoom in and out. It needs looking at

        //System.out.println(" window X:" + p_windowPos.getX());
        //System.out.println(" canvas X:" + p_canvasPos.getX());
        //System.out.println(" scale:" + scale);

        //System.out.println("Canvas:" + m_root.getBoundsInLocal().getWidth());

        //The area the window takes up in canvas co-ords
        //System.out.println("Scroll Width on canvas:" + (m_scroll.getWidth() / scale));

        //xTotalScrollRange in canvas co-ordinates
        double xTotalScrollRange = m_root.getBoundsInLocal().getWidth() - (m_scroll.getWidth() / scale);

        double xOffscreen = p_canvasPos.getX() - (p_windowPos.getX() / scale);
        if (xOffscreen < 0) xOffscreen = 0;

        m_scroll.setHvalue(xOffscreen / xTotalScrollRange);


        //Y code below - should mirror first bit
        double yTotalScrollRange = m_root.getBoundsInLocal().getHeight() - (m_scroll.getHeight() / scale);
        double yOffscreen = p_canvasPos.getY() - (p_windowPos.getY() / scale);
        if (yOffscreen < 0) yOffscreen = 0;
        m_scroll.setVvalue(yOffscreen / yTotalScrollRange);

    }

    private class ZoomHandler implements EventHandler<ScrollEvent> {

        private ZoomHandler() {
        }

        @Override
        public void handle(ScrollEvent scrollEvent) {
            if (scrollEvent.isControlDown()) {

                Point2D mousePos = new Point2D(scrollEvent.getSceneX(), scrollEvent.getSceneY());
                Point2D scenePositionToCentreZoomAround = m_root.sceneToLocal(new Point2D(scrollEvent.getSceneX(), scrollEvent.getSceneY()));

                final double scale = calculateScale(scrollEvent);
                m_root.setScaleX(scale);
                m_root.setScaleY(scale);

                scrollTo(mousePos, scenePositionToCentreZoomAround);

                scrollEvent.consume();
            }
        }

        private double calculateScale(ScrollEvent scrollEvent) {
            double scale = m_root.getScaleX() + scrollEvent.getDeltaY() / 500;

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

    public Node getScrollPane() {
        return m_scroll;
    }

    public void clearAllContextMenus() {
        for (BuildConfigurationGroup bc : m_buildConfigurations.values()) {
            bc.clearContextMenu();
        }

    }

}
