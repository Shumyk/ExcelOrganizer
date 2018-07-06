package shumyk.excel.gui.tab.generating;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import shumyk.excel.generating.GeneratingExcels;
import shumyk.excel.gui.controller.ExcelOrganizerController;

public class MenusGeneratingTabController extends ExcelOrganizerController {
	/* Files where are hold selected by user menu and names files */
	private File menu;
	private File names;
	
	@FXML
	private TextField menuUrl;
	@FXML
	private TextField namesUrl;
	
	// --------------- controllers ------------------ //
	
	/**
	 * Handler for "Choose menu" button.
	 * Opens file chooser and if user chose something sets variables.
	 */
	@FXML private void chooseMenuFile() {
		fileChooser.setTitle("Choose menu file...");
		menu = fileChooser.showOpenDialog(null);
		if (menu == null)
			return;
		menuUrl.setText(menu.getName());
	}
	
	/**
	 * Handler for "Choose names" button.
	 * Opens file chooser and if user chose something sets variables.
	 */
	@FXML private void chooseNamesFile() {
		fileChooser.setTitle("Choose names file...");
		names = fileChooser.showOpenDialog(null);
		if (names == null)
			return;
		namesUrl.setText(names.getName());
	}
	
	/**
	 * Handler for "Generate" button.
	 * Creates menus for every person in names file with calculation columns.
	 * 
	 * Alerts errors if some file wasn't chosen.
	 * Also, alerts error if creation of files was unsuccessful and shows issue description (usually wrong files were selected)
	 */
	@FXML private void generateMenus() {
		if (menu == null) {
			alert(AlertType.ERROR, "Choose menu file.", "You didn't choose menu file. \n Please choose it.");
			return;
		} else if (names == null) {
			alert(AlertType.ERROR, "Choose name file.", "You didn't choose names file. \n Please choose it.");
			return;
		} 
		
		try {
			GeneratingExcels.createMenusPerPerson(menu, names);
		} catch (Exception e) {
			e.printStackTrace();
			
			alert(AlertType.ERROR,
					"Ooops, something went wrong :(",
					"Apparently, you chosed wrong files and that cause failure of execution.\nTry again :)",
					e.getMessage(),
					true, 200.0, 100.0);
			return;
		}
		alert(AlertType.INFORMATION, "Files are created :D", "Menus for every person are created. Check them out in  folder.");
	}
}
