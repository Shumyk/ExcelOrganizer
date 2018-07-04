package shumyk.excel.gui.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

public class ExcelOrganizerController {
	protected FileChooser fileChooser = new FileChooser();
	protected FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Only xlsx files", "*.xlsx");

	
	public ExcelOrganizerController() {
		fileChooser.getExtensionFilters().add(extFilter);
	}
	
	
	protected void alert(AlertType type, String title, String header, String content, Boolean resizable, Double minHeight, Double minWidth) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		
		if (resizable) {
			alert.setResizable(resizable);
			alert.getDialogPane().setMinHeight(minHeight);
			alert.getDialogPane().setMinHeight(minWidth);
		}
		alert.showAndWait();
	}
	
	protected void alert(AlertType type, String title, String header, String content) {
		alert(type, title, header, content, false, null, null);
	}
	
	protected void alert(AlertType type, String title, String header) {
		alert(type, title, header, null);
	}
}