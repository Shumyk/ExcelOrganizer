package shumyk.excel.gui;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import shumyk.excel.generating.GeneratingExcels;


public class ExcelOrganizerGUI extends Application {
	Button button;
	FileChooser fileChooser = new FileChooser();
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		fileChooser.setTitle("Select resource ;)");
		
		primaryStage.setTitle("Excel Organizer");
		button = new Button("Generate menus");
		button.setOnAction(event -> {
			GeneratingExcels generator = new GeneratingExcels();
			File namesFile = fileChooser.showOpenDialog(primaryStage);
			String namesFilename = "names.xlsx";
			String menuFilename = "04.06.xlsx";
			try {
				generator.createMenusPerPerson(menuFilename, namesFilename);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		
		StackPane layout = new StackPane();
		layout.getChildren().add(button);
		
		Scene scene = new Scene(layout, 300, 300);
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

}
