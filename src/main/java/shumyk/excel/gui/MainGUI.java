package shumyk.excel.gui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainGUI extends Application {
	// files in jar's resources folders, only used to be initialize once
	private static final String NAMES_FILE = "/names/names.txt";
	private static final String NAMES_TEMP_FILE = "/names/temp.txt";
	private static final String TEMPLATE_FILE = "/template/WeekOrdersTotalTemplate.xlsx";
	// folder with external resources, which are used during runtime
	private final String FOLDER_RES = "resources";
	
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
	
	/**
	 * Creates folder "resources" if it doesn't exist yet.
	 * In that folder creates files needed for further correct work of program.
	 * @throws IOException
	 */
	private void initResources() throws IOException {
		File dirResources = new File(FOLDER_RES);
		if (dirResources.exists())
			return;
		dirResources.mkdir();

		writeFile(NAMES_FILE, "names.txt");
		writeFile(NAMES_TEMP_FILE, "temp.txt");
		writeFile(TEMPLATE_FILE, "template.xlsx");
		
	}
	
	/**
	 * Takes file from specified location and writes to resources folder with specified name.
	 * @param location where to take file
	 * @param output name of output file in resources folder
	 * @throws IOException
	 */
	private void writeFile(String location, String output) throws IOException {
			InputStream input = getClass().getResourceAsStream(location);
			File file = new File(FOLDER_RES.concat("/").concat(output));
			OutputStream out = new FileOutputStream(file);
			int read;
			byte[] bytes = new byte[1024];
			
			while ((read = input.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			input.close();
			out.close();
	}
}
