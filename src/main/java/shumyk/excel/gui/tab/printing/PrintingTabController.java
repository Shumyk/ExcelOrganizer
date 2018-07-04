package shumyk.excel.gui.tab.printing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import shumyk.excel.gui.controller.ExcelOrganizerController;

public class PrintingTabController extends ExcelOrganizerController {
	@FXML 
	private ListView<File> listFilesPrintView;
	
	public void initialize() {
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
	private void generatePrintFile() throws IOException {
		FileInputStream fis = new FileInputStream(new File("template.xlsx"));
		Workbook schedule = new XSSFWorkbook(fis);
		Sheet sheet = schedule.getSheet("Schedule");
		
		List<File> files = listFilesPrintView.getItems();
		
		for (int i = 0; i < files.size();) {
			FileInputStream tempFis = new FileInputStream(files.get(i));
			Workbook workbook = new XSSFWorkbook(tempFis);
			
			String content = files.get(i).getName();
			System.out.println(content);
			sheet.createRow(++i).createCell(0).setCellValue(content);
			
			
		}
		
		FileOutputStream fos = new FileOutputStream(new File("menusPerDay.xlsx"));
		schedule.write(fos);
		fos.close();
		
		
	}
}
