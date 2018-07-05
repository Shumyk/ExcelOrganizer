package shumyk.excel.gui.tab.generating;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import shumyk.excel.generating.GeneratingExcels;
import shumyk.excel.gui.controller.ExcelOrganizerController;

public class MenusGeneratingTabController extends ExcelOrganizerController {
	private File menu;
	private File names;
	
	@FXML
	private TextField menuUrl;
	@FXML
	private TextField namesUrl;
	
	// mock / delete later / was created for ease development
	@FXML 
	public void initialize() {
		menuUrl.setText("D:\\workspaceOwn\\ExcelOrganizer\\04.06.xlsx");
		menu = new File("D:\\\\workspaceOwn\\\\ExcelOrganizer\\\\04.06.xlsx");
		
		namesUrl.setText("D:\\workspaceOwn\\ExcelOrganizer\\names.xlsx");
		names = new File("D:\\\\workspaceOwn\\\\ExcelOrganizer\\\\names.xlsx");
	}
	
	
	// --------------- controllers ------------------ //
	
	@FXML
	private void chooseMenuFile() {
		fileChooser.setTitle("Choose menu file...");
		menu = fileChooser.showOpenDialog(null);
		
		if (menu == null)
			return;
		
		menuUrl.setText(menu.getName());
	}
	
	@FXML
	private void chooseNamesFile() {
		fileChooser.setTitle("Choose names file...");
		names = fileChooser.showOpenDialog(null);
		
		if (names == null)
			return;
		
		namesUrl.setText(names.getName());
	}
	
	@FXML
	private void generateMenus() throws InterruptedException {
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
