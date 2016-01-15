package com.github.quiram.buildhotspots.visualisation;

import com.github.quiram.buildhotspots.drawingdata.Root;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedInputStream;
import java.io.InputStream;

import static java.lang.ClassLoader.getSystemClassLoader;

public class FileBasedBuildHotspotsApplication extends BuildHotspotsApplicationBase {
    public static void main(String[] args) {
        System.out.println("Start FileBasedBuildHotspotsApplication");
        launch(args);
        System.out.println("End FileBasedBuildHotspotsApplication");
    }

    protected Root getRootDocument(String source) {
        InputStream input = new BufferedInputStream(getSystemClassLoader().getResourceAsStream(source));

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Root.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            return (Root) jaxbUnmarshaller.unmarshal(input);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    protected String getTitle() {
        return "Build Hotspots - Draw from File";
    }

    @Override
    protected SourceSelector getSourceSelector() {
        return new FileSourceSelector();
    }

    @Override
    protected void selectBuilds(String source) {
        selectBuilds(getRootDocument(source));
    }

    private void selectBuilds(Root p_docRoot) {
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
}
