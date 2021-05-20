package kr.co.danal.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import kr.co.danal.util.AlertUtil;

public class TestLauncher extends Application{

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Home.class.getClass().getResource("/home.fxml"));
			BorderPane home = (BorderPane) loader.load();
			
			primaryStage.setTitle(Home.TITLE);
			primaryStage.setScene(new Scene(home));
			
			primaryStage.show();
			 
		} catch (NullPointerException e) {
			e.printStackTrace();
			AlertUtil.error(e);
		} catch(Exception e) {
			e.printStackTrace();
			AlertUtil.error(e);
		}
	}


}
