package shumyk.excel.gui.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

/**
 * Class provides common objects and methods that are used in several controllers.
 * 
 * @author shumyk
 */
public class ExcelOrganizerController {
	/* FileChooser with restriction to choose only file with .xlsx extension. */
	protected FileChooser fileChooser = new FileChooser();
	protected FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Only xlsx files", "*.xlsx");

	public static final String NAMES_FILE = "/names/names.txt";
	public static final String NAMES_TEMP_FILE = "/names/temp.txt";
	public static final String TEMPLATE_FILE = "/template/WeekOrdersTotalTemplate.xlsx";

	public ExcelOrganizerController() {
		fileChooser.getExtensionFilters().add(extFilter);
	}
	
	/**
	 * Provides alert functionality in more handy way.
	 * @param type of alert
	 * @param title of alert
	 * @param header of alert
	 * @param content of alert
	 * @param resizable true / false
	 * @param minHeight of alert
	 * @param minWidth of alert
	 */
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
	/**
	 * Same as alert above, but sets not specified arguments to nulls.
	 */
	protected void alert(AlertType type, String title, String header, String content) {
		alert(type, title, header, content, false, null, null);
	}
	protected void alert(AlertType type, String title, String header) {
		alert(type, title, header, null);
	}
}