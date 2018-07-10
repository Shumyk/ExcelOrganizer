package shumyk.excel.gui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Scanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import shumyk.excel.gui.controller.ExcelOrganizerController;

public class MainGUI extends Application {
	public static final String NAMES_FILE = "/names/names.txt";
	public static final String NAMES_TEMP_FILE = "/names/temp.txt";
	public static final String TEMPLATE_FILE = "/template/WeekOrdersTotalTemplate.xlsx";
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		initResources();
		
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
	
	private void initResources() {
		File dirResources = new File("resources");
		if (dirResources.exists())
			return;
		dirResources.mkdir();

		writeFile(NAMES_FILE, "names.txt");
		writeFile(NAMES_TEMP_FILE, "temp.txt");
		writeFile(TEMPLATE_FILE, "template.xlsx");
		
	}
	
	private void writeFile(String location, String output) {
		File file = null;
		URL template = getClass().getResource(location);
		
		if (template.toString().startsWith("jar:")) {
			try {
				InputStream input = getClass().getResourceAsStream(location);
				file = new File("resources/" + output);
				OutputStream out = new FileOutputStream(file);
				int read;
				byte[] bytes = new byte[1024];
				
				while ((read = input.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}
				input.close();
				out.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} else {
			file = new File(template.getFile());
		}
		
		if (file != null && !file.exists()) {
			throw new RuntimeException("Error: File " + file + " not found!");
		}
	}
}
