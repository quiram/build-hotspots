package com.github.quiram.buildhotspots.visualisation;

import com.github.quiram.buildhotspots.drawingdata.Root;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

    private Root getRootDocument(String source) {
        InputStream input = new BufferedInputStream(getSystemClassLoader().getResourceAsStream(source));

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Root.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            return (Root) jaxbUnmarshaller.unmarshal(input);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
    
    private void browse() {
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("Browse for File");
    	alert.setHeaderText("Browse for xml file");
    	alert.setContentText("Browse not yet implemented");

    	alert.showAndWait();
    }

	@Override
	protected void showDrawingSelectionScene() throws Exception {
        m_primaryStage.setWidth(500); //Set up initial window width
        m_primaryStage.setTitle("Build Hotspots - Draw from File");
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Scene scene = new Scene(grid, 300, 275);
        m_primaryStage.setScene(scene);

        Label label = new Label("Please specify file name:");
        label.setMinWidth(400);
        //TextField textField = new TextField("drawingDataExample001.xml");
        TextField textField = new TextField("very-large-system.xml");

        Button btn = new Button();
        btn.setText("Show me hotspots!");
        btn.setOnAction(event -> AddDrawingToScene(getRootDocument(textField.getText())));

        Button btnBrowse = new Button();
        btnBrowse.setText("...");
        btnBrowse.setOnAction(event -> browse());

        grid.add(label, 0, 0);
        grid.add(textField, 0, 1);
        grid.add(btnBrowse, 1, 1);
        grid.add(btn, 0, 2);

        m_primaryStage.show();
		
	}
}
