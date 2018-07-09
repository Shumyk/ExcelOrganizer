package shumyk.excel.gui.tab.settings;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import shumyk.excel.gui.controller.ExcelOrganizerController;

public class SettingsTabController extends ExcelOrganizerController {
	private PrintWriter printWriter;
	/**
	 * Path to the names file.
	 */
	private String namesLoc = getClass().getResource(NAMES_FILE).getPath();
	private String namesTempLoc = getClass().getResource(NAMES_TEMP_FILE).getPath();
	
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
		selected.forEach(el -> { removeLine(el); });
		
		updateCustomersNames();
	}
	
	/**
	 * Opens template file for total weekly orders and allowing to change it.
	 * @throws IOException
	 */
	@FXML private void changeTemplate() throws IOException {
		URL template = getClass().getResource(TEMPLATE_FILE);
		Desktop.getDesktop().open(new File(template.getPath()));
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
		InputStream input = getClass().getResourceAsStream(NAMES_FILE);
		Scanner scanner = new Scanner(input);
		customersNames.getItems().clear();
		while (scanner.hasNext())
			customersNames.getItems().add(scanner.nextLine());
		scanner.close();
	}
	
	/**
	 * Removes specified line in the names file.
	 * @param lineRemove line to be removed
	 * @throws FileNotFoundException 
	 * @throws IOException
	 */
	void removeLine(String lineRemove) {
		try {
		File names = new File(namesLoc);
		File temp = new File(namesTempLoc);
		
		printWriter = new PrintWriter(new FileWriter(temp));
		printWriter.print("");
		
		Scanner sc = new Scanner(names);
		while (sc.hasNext()) {
			String line = sc.nextLine();
			if (!line.equals(lineRemove))
				printWriter.println(line);
		}
		printWriter.flush();
		printWriter.close();
		sc.close();
		
		PrintWriter toNames = new PrintWriter(new FileWriter(names));
		toNames.print("");
		sc = new Scanner(getClass().getResourceAsStream(NAMES_TEMP_FILE));
		while (sc.hasNext())
			toNames.println(sc.nextLine());
		
		toNames.flush();
		toNames.close();
		sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
