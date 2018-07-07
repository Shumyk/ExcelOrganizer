package shumyk.excel.gui;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainGUI extends Application {
	public static final String NAMES_FILE = "/names/names.txt";

	@Override
	public void start(Stage primaryStage) throws IOException {
		URL url = getClass().getResource("/gui/ExcelOrganizer.fxml");
		FXMLLoader loader = new FXMLLoader(url);
		Parent root = loader.load();
		primaryStage.setTitle("Excel Organizer");
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/excel.png")));
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
