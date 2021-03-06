package shumyk.excel.gui.tab.settings;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
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
	private File namesFile = new File(NAMES_RES);
	private File namesTmpFile = new File(NAMES_TMP_RES);
	
	@FXML
	private ListView<String> customersNames;
	@FXML
	private TextField textField;
	
	
	public void initialize() throws FileNotFoundException {
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
		
		printWriter = new PrintWriter(new BufferedWriter(new FileWriter(namesFile, true)));
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
		File file = new File(TEMPLATE_RES);
		Desktop.getDesktop().open(file);
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
	 * @throws FileNotFoundException 
	 */
	private void updateCustomersNames() throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(NAMES_RES));
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
			scanFromTo(namesFile, namesTmpFile, true, lineRemove);
			scanFromTo(namesTmpFile, namesFile, false, lineRemove);
	}
	
	/**
	 * Scans content from first file to second.
	 * If third argument is true, then method scans to temporary file and it should filter lines
	 * and do not scan line what equals lineRemove.
	 * @param from file
	 * @param to file
	 * @param toTemp does it scans to temporary file?
	 * @param lineRemove if yes, then this line should be skipped.
	 */
	private void scanFromTo(File from, File to, boolean toTemp, String lineRemove) {
		try {
		printWriter = new PrintWriter(new FileWriter(to));
		printWriter.print("");
		
		Scanner sc = new Scanner(from);
		while (sc.hasNext()) {
			String line = sc.nextLine();
			if (toTemp) {
				if (!line.equals(lineRemove))	printWriter.println(line);
			}
			else
				printWriter.println(line);
		}
		
		printWriter.flush();
		printWriter.close();
		sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
