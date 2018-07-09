package shumyk.excel.gui.tab.printing;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import shumyk.excel.generating.GeneratingExcels;
import shumyk.excel.gui.controller.ExcelOrganizerController;
import shumyk.excel.gui.controller.helpers.WorkbookHelper;

public class PrintingTabController extends ExcelOrganizerController {
	@FXML 
	private ListView<File> listFilesPrintView;
	
	private WorkbookHelper helper = new WorkbookHelper();
	
	/**
	 * When all FXML element will be loaded here this method called and it sets property of ListView.
	 */
	public void initialize() {
		listFilesPrintView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		populateListView();
	}
	

	// --------------- controllers ------------------ //

	/** 
	 * Handler for button "Add files". 
	 * Opens file chooser where user select menus of persons (could be multiply).
	 * Then if user chose something adds it to ListView. 
	 */
	@FXML private void addFilesToPrint() {
		List<File> filesToPrint = fileChooser.showOpenMultipleDialog(null);
		
		if(filesToPrint==null)
			return;
		
		listFilesPrintView.getItems().addAll(filesToPrint);
	}
	
	/**
	 * Handler for button "Remove".
	 * Removes all selected elements for ListView.
	 */
	@FXML private void removeFromList() {
		ObservableList<File> itemToRemove = listFilesPrintView.getSelectionModel().getSelectedItems();
		listFilesPrintView.getItems().removeAll(itemToRemove);
	}
	
	/**
	 * Handler for button "Generate".
	 * The most complicated method here.
	 * Process data from selected files and writes output to separate xlsx file.
	 * 
	 * @throws IOException
	 */
	@FXML private void generatePrintFile() {
		try {
		/* Initialize workbook, sheet etc. every time to avoid POI bug */
		helper.initWorkbook();
		List<File> files = listFilesPrintView.getItems();
		
		/* Loop every file we have */ 
		for (int i = 0; i < files.size(); i++) {
			/* Initializing of person's order workbook */
			FileInputStream tempFis = new FileInputStream(files.get(i));
			Workbook menu = new XSSFWorkbook(tempFis);
			Sheet menuSheet = menu.getSheetAt(0);
			/* Retrieving person's name (stored as comment at first cell) */
			String personName = menuSheet.getRow(0).getCell(0).getCellComment().getString().getString();
			
			/* Sets first cell of row with name of person */
			helper.setNameCell(personName, i + 1);
			
			/* Retrieving all orders per every day of week for this person's menu */
			Map<String, Map<String, Integer>> personWeekOrders = helper.getWeekOrders(menuSheet);
			/* Writes to main sheet all data that was collected before */
			helper.writeWeekOrdersToSheet(personName, personWeekOrders);
			tempFis.close();
		}
		/* Setting column size depending of content of cells */
		for (Cell cell : helper.scheduleMainRow) {
			helper.scheduleSheet.autoSizeColumn(cell.getColumnIndex());
		}

		/* Writing to file our sheet */
		String fileName = "Week Orders Total.xlsx";
		FileOutputStream fos = new FileOutputStream(new File(fileName));
		helper.schedule.write(fos);
		fos.close();
		
		Desktop.getDesktop().open(new File(fileName));
		} catch (Exception e) {
			alert(AlertType.ERROR, "Error occured :(", "Something went wrong. \nProbably there somewhere problem in your xlsx files.", e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds all items from generated folder where all menus per person is collected.
	 */
	private void populateListView() {
		File menuDir = new File(GeneratingExcels.MENUS_DIR);
		if (!menuDir.isDirectory())
			return;
		
		for (final File file : menuDir.listFiles()) {
			listFilesPrintView.getItems().add(file);			
		}
	}
}
