package shumyk.excel.gui.tab.settings;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.List;
import java.util.Scanner;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import shumyk.excel.gui.MainGUI;
import shumyk.excel.gui.controller.ExcelOrganizerController;

public class SettingsTabController extends ExcelOrganizerController {
	private PrintWriter printWriter;
	/**
	 * Path to the names file.
	 */
	private String namesLoc = getClass().getResource(MainGUI.NAMES_FILE).getPath();
	
	@FXML
	private ListView<String> customersNames;
	@FXML
	private TextField textField;
	
	
	public void initialize() {
		customersNames.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		updateCustomersNames();
	}
	
	// ---------------- controllers -------------------
	
	/**
	 * Handler for "Add" button.
	 * Takes string form text field and add to the names file, if not empty or null.
	 * After adding updates ListView and clears TextField.
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	@FXML private void addCustomerName() throws IOException, URISyntaxException {
		String customerName = textField.getText();
		if (null == customerName || customerName.equals(""))
			return;
		
		printWriter = new PrintWriter(new BufferedWriter(new FileWriter(namesLoc, true)));
		printWriter.println(customerName);
		printWriter.close();
		
		updateCustomersNames();
		textField.clear();
	}
	
	/**
	 * Handler for "Remove" button.
	 * Gets selected items from ListView and one by one removes them from file, then updates ListView.
	 * @throws IOException
	 */
	@FXML private void removeSelectedNames() throws IOException {
		List<String> selected = customersNames.getSelectionModel().getSelectedItems();
		selected.forEach(el -> {
			try {
				removeLine(el);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		
		updateCustomersNames();
	}
	
	/**
	 * Allows user to add customer name on text field by pressing ENTER key.
	 * Just checks if key pressed is ENTER and then calls add method.
	 * @param event what happened
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	@FXML private void addCustomerNameByKeyPressed(KeyEvent event) throws IOException, URISyntaxException {
		if (event.getCode().equals(KeyCode.ENTER))
			addCustomerName();
	}
	
	/**
	 * Allows user to remove selected names from ListView by pressing DELETE & BACKSPACE keys.
	 * Just checks if right key is pressed and calls remove method.
	 * @param event what happened
	 * @throws IOException
	 */
	@FXML private void removeSelectedNamesByKeyPressed(KeyEvent event) throws IOException {
		if (event.getCode().equals(KeyCode.DELETE) || event.getCode().equals(KeyCode.BACK_SPACE)) {
			removeSelectedNames();
		}
	}
	
	/**
	 * Updates ListView with the current names file.
	 */
	private void updateCustomersNames() {
		Scanner scanner = new Scanner(getClass().getResourceAsStream(MainGUI.NAMES_FILE));
		customersNames.getItems().clear();
		while (scanner.hasNext())
			customersNames.getItems().add(scanner.nextLine());
		scanner.close();
	}
	
	/**
	 * Removes specified line in the names file.
	 * @param lineRemove line to be removed
	 * @throws IOException
	 */
	void removeLine(String lineRemove) throws IOException {
		File names = new File(namesLoc);
		File temp = new File("temp");
		
		printWriter = new PrintWriter(new FileWriter(temp));
		
		Files.lines(names.toPath())
			.filter(line -> !line.equals(lineRemove))
			.forEach(printWriter::println);
		
		printWriter.close();
		temp.renameTo(names);
	}
}
