package ep.core;

import ep.hadoop.StdDevReducer;
import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UiFx extends Application{
	
	public static void main(String[] args){
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Parent root = FXMLLoader.load(getClass().getResource("/ep/ui/fxml/MainWindow.fxml"));
		
		Scene scene = new Scene(root);
		
		primaryStage.setTitle("Sistemas Distribuidos - EP2");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		
		primaryStage.show();
	}

}
