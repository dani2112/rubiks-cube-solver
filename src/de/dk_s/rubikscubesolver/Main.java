package de.dk_s.rubikscubesolver;
	
import org.opencv.core.Core;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("views/MainLayout.fxml"));
            
            
            BorderPane mainLayout = (BorderPane) loader.load();
            Scene scene = new Scene(mainLayout, 800, 600);
            
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            
			primaryStage.setScene(scene);
			primaryStage.show();
			MainController controller = loader.getController();
			
			primaryStage.setOnCloseRequest((new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we)
				{
					
					controller.setClosed();
				}
			}));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		launch(args);
	}
}
