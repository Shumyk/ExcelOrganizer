package shumyk.excel.gui.tab.printing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
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
	private FileInputStream fis; 
	private Workbook schedule;
	private Sheet scheduleSheet;
	private Row scheduleMainRow;
	
	private final static String MONDAY = "Понеділок";
	private final static String TUESDAY = "Вівторок";
	private final static String WEDNESDAY = "Середа";
	private final static String THURSDAY = "Четвер";
	private final static String FRIDAY = "П'ятниця";
	private final static List<String> DAYS = Arrays.asList(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY);
	
	private final static String CURRENT_DAY = "CURRENT_DAY";
	private final static String ANOTHER_DAY = "ANOTHER_DAY";
	
	private int amountColNum;
	private int descColNum;
	
	public void initialize() {
		listFilesPrintView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		//mock , delete later
		listFilesPrintView.getItems().addAll(new File("personsMenus/04.06 Ihor.xlsx"), new File("personsMenus/04.06 Ira.xlsx"), new File("personsMenus/04.06 Olya.xlsx"), new File("personsMenus/04.06 Vika.xlsx"), new File("personsMenus/04.06 Vova.xlsx"));
	}
	
	private void initWorkbook() throws IOException {
		fis = new FileInputStream(new File("template.xlsx"));
		schedule = new XSSFWorkbook(fis);
		scheduleSheet = schedule.getSheet("Schedule");
		scheduleMainRow = scheduleSheet.getRow(0);
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
		initWorkbook();
		List<File> files = listFilesPrintView.getItems();
		
		// GOING THROUGH ALL FILES (EVERY PERSON)
		//starting from 1, because 0-row of template is populated with DAYS names
		// POINT 1
		for (int i = 0; i < files.size(); i++) {
			FileInputStream tempFis = new FileInputStream(files.get(i));
			Workbook menu = new XSSFWorkbook(tempFis);
			Sheet menuSheet = menu.getSheetAt(0);
			String personName = menuSheet.getRow(0).getCell(0).getCellComment().getString().getString();
			
			//SETTING FIRST COLUMN WITH NAMES OF PERSONS
			setNameCell(personName, i + 1);
			
			// ALL ORDERS FOR PERSON FOR WHOLE WEEK ARE IN THIS FILE ! POINT 2
			Map<String, Map<String, Integer>> personWeekOrders = getWeekOrders(menuSheet);
			
			writeWeekOrdersToSheet(personName, personWeekOrders);
			
		}
		
		for (Cell cell : scheduleMainRow) {
			scheduleSheet.autoSizeColumn(cell.getColumnIndex());
		}
		
		// ENDING / writing to the file with name "menusPerDay.xlsx"
		FileOutputStream fos = new FileOutputStream(new File("menusPerDay.xlsx"));
		schedule.write(fos);
		fos.close();
	}
	
	
	private void setNameCell(String name, int rowNum) {
		System.out.println(name);
		scheduleSheet.getRow(rowNum).getCell(0).setCellValue(name);
	}
	
	Map<String, Map<String, Integer>> getWeekOrders(Sheet menuSheet) {
		int currentDay = 0;

		// GOING THROUGH EACH ROW OF SHEET
		// PPOINT 2
		Map<String, Map<String, Integer>> ordersPerDay = new HashMap<>();
		for (Row row : menuSheet) {
			Map<String, Integer> rowItems = processRow(row, currentDay);
			
			if (rowItems.size() == 0)
				continue;
			if (rowItems.containsKey(CURRENT_DAY)) {
				processDayRow(row, currentDay);
				continue;
			}
			if (rowItems.containsKey(ANOTHER_DAY)) {
				currentDay = rowItems.get(ANOTHER_DAY);
				processDayRow(row, currentDay);
				continue;
			}
			
			String currentDAYStr = DAYS.get(currentDay);
			if (!ordersPerDay.containsKey(currentDAYStr)) {
				ordersPerDay.put(DAYS.get(currentDay), rowItems);
			} else {
				ordersPerDay.get(currentDAYStr).putAll(rowItems);
			}
			
		}
		return ordersPerDay;
	}
	
	
	Map<String, Integer> processRow(Row row, int currentDay) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		
		if(row.getCell(0) == null)
			return map;
		
		String firstCellValue = row.getCell(0).getStringCellValue();
		if (firstCellValue == null || firstCellValue.equals(""))
			return map;
		if (DAYS.contains(firstCellValue)) {
			int curDayNum = DAYS.indexOf(firstCellValue);
			if (curDayNum == currentDay)
				map.put(CURRENT_DAY, currentDay);
			else
				map.put(ANOTHER_DAY, curDayNum);
			
			return map;
		}
		
		if (null != row.getCell(amountColNum) && row.getCell(amountColNum).getNumericCellValue() != 0) {
			map.put(row.getCell(descColNum).getStringCellValue(), (int) row.getCell(amountColNum).getNumericCellValue());
		}
		return map;
	}
	
	void processDayRow(Row row, int currentDay) {
		for (Cell cell : row) {
			if (cell.getStringCellValue().equals(DAYS.get(currentDay))) {
				descColNum = cell.getColumnIndex();
				continue;
			}
			if (cell.getStringCellValue().equals("Кількість")) {
				amountColNum = cell.getColumnIndex();
				continue;
			}
		}
	}
	
	
	void writeWeekOrdersToSheet(String personName, Map<String, Map<String, Integer>> orders) {
		CellStyle style = createCellStyle();
		
		int dayCol = 0;
		for (Map.Entry<String, Map<String, Integer>> entry : orders.entrySet()) {
			for (Cell cell : scheduleMainRow) {
				if (cell.getStringCellValue().equals(entry.getKey())) {
					dayCol = cell.getColumnIndex();
				}
			}
			
			String dayOrder = entry.getKey() + "  -  " + personName;
			for (Map.Entry<String, Integer> order : entry.getValue().entrySet()) {
				dayOrder += "\n" + order.getKey() + " x" + order.getValue();
			}
			for (Row row : scheduleSheet) {
				row.setHeight((short) -1);
				if (row.getCell(0).getStringCellValue().equals(personName)) {
					Cell cell = row.getCell(dayCol);
					if(cell == null)
						cell = row.createCell(dayCol);
					
					cell.setCellStyle(style);
					cell.setCellValue(dayOrder);
				}
			}
			
		}
		
	}
	
	private CellStyle createCellStyle() {
		CellStyle style = schedule.createCellStyle();
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setWrapText(true);
		
		return style;
	}
	
	


}
