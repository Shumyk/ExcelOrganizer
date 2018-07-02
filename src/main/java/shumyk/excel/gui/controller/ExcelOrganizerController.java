package shumyk.excel.gui.controller;

import java.io.File;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import shumyk.excel.generating.GeneratingExcels;

public class ExcelOrganizerController {
	private FileChooser fileChooser = new FileChooser();
	private FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Only xlsx files", "*.xlsx");

	
	private File menu;
	private File names;
	
	@FXML
	private TextField menuUrl;
	@FXML
	private TextField namesUrl;
	@FXML 
	private ListView<File> listFilesPrintView;
	
	public ExcelOrganizerController() {
		fileChooser.getExtensionFilters().add(extFilter);
	}
	
	
	/* ------------- Controller methods ---------- */
	@FXML
	private void chooseFile(ActionEvent event) {
		
		Button actionBtn = (Button) event.getSource();
		String btnId = actionBtn.getId();
		if (btnId.equals("btnMenu")) {
			fileChooser.setTitle("Choose menu file...");
		} else if (btnId.equals("btnNames")) {
			fileChooser.setTitle("Choose names file...");
		} else {
			fileChooser.setTitle("Select resources");
		}
		String textFieldId = actionBtn.getParent().getChildrenUnmodifiable().get(1).getId();
		
		File file = fileChooser.showOpenDialog(actionBtn.getContextMenu());
		
		if (file == null)
			return;
		
		if(textFieldId.equals("menuUrl")) {
			menu = file;
			menuUrl.setText(file.getAbsolutePath());
		} else {
			names = file;
			namesUrl.setText(file.getAbsolutePath());
		}
	}
	
	@FXML
	private void generateMenus() throws InterruptedException {
		if (menu == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Choose menu file.");
			alert.setHeaderText("You didn't choose menu file. \n Please choose it.");
			alert.showAndWait();
			return;
		} else if (names == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Choose name file.");
			alert.setHeaderText("You didn't choose names file. \n Please choose it.");
			alert.showAndWait();
			return;
		} 
		
		try {
			GeneratingExcels.createMenusPerPerson(menu, names);
		} catch (Exception e) {
			e.printStackTrace();
			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setResizable(true);
			alert.getDialogPane().setMinHeight(200);
			alert.getDialogPane().setPrefWidth(700);
			alert.setTitle("Ooops, something went wrong :(");
			alert.setHeaderText("Apparently, you chosed wrong files and that cause failure of execution.\nTry again :)");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
			return;
		}
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Files are created :D");
		alert.setHeaderText("Menus for every person are created. Check them out in  folder.");
		alert.showAndWait();

	}
	
	@FXML
	private void addFilesToPrint(ActionEvent event) {
		listFilesPrintView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		Button actionBtn = (Button) event.getSource();
		List<File> filesToPrint = fileChooser.showOpenMultipleDialog(actionBtn.getContextMenu());
		
		if(filesToPrint==null)
			return;
		
		listFilesPrintView.getItems().addAll(filesToPrint);
	}
	
	@FXML
	private void removeFromList() {
		ObservableList<File> itemToRemove = listFilesPrintView.getSelectionModel().getSelectedItems();
		
		listFilesPrintView.getItems().removeAll(itemToRemove);
	}
	
	@FXML
	private void generatePrintFile() {}
	
}
