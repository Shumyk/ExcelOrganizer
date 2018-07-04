package shumyk.excel.gui.tab.printing;

import java.io.File;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import shumyk.excel.gui.controller.ExcelOrganizerController;

public class PrintingTabController extends ExcelOrganizerController {
	@FXML 
	private ListView<File> listFilesPrintView;
	
	public PrintingTabController() {
		super();
		listFilesPrintView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}
	
	// --------------- controllers ------------------ //

	@FXML
	private void addFilesToPrint(ActionEvent event) {
		List<File> filesToPrint = fileChooser.showOpenMultipleDialog(null);
		
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
