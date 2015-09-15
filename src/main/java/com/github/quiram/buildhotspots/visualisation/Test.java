/**
 * 12 Sep 2015, 15:17:53
 ***************************************
 * @author Marco					   *
 * @email marco.montalto.93@gmail.com  *
 ***************************************
 */
package com.github.quiram.buildhotspots.visualisation;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * 
 */

@SuppressWarnings("restriction")
public class Test extends Application {
		@Override
		public void start(Stage primaryStage) {
			try {

				String testFXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +

						"<?import javafx.scene.shape.*?>" +
						"<?import java.lang.*?>" +
						"<?import javafx.scene.layout.*?>" +
						"<BorderPane maxHeight=\"-Infinity\" maxWidth=\"-Infinity\" minHeight=\"-Infinity\" minWidth=\"-Infinity\" prefHeight=\"400.0\" prefWidth=\"600.0\" xmlns=\"http://javafx.com/javafx/8\" xmlns:fx=\"http://javafx.com/fxml/1\">"+
						"<center>"+
						"<Circle fill=\"DODGERBLUE\" radius=\"100.0\" stroke=\"BLACK\" strokeType=\"INSIDE\" BorderPane.alignment=\"CENTER\" />"+
						"</center>"+
						"</BorderPane>";
				
				System.out.println(testFXML);
				
				FXMLLoader loader = new FXMLLoader();
				InputStream is = new ByteArrayInputStream(testFXML.getBytes()); 
				BorderPane root = (BorderPane) loader.load(
						is
						);
				
				

//				ControllerName controller = loader.getController(); // 2. Controller NAME!

				Scene scene = new Scene(root);
//				Model model = new Model(); // 3. Model NAME!

				// 4. Create a setModel in the controller class
//				controller.setModel(model);

//				scene.getStylesheets().add(
//						getClass().getResource("application.css")
//								.toExternalForm());
				primaryStage.setScene(scene);

				//OPTIONAL
				//primaryStage.setResizable(false);

				primaryStage.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public static void main(String[] args) {
			launch(args);
		}
}
