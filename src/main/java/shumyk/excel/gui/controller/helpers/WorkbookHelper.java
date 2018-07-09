package shumyk.excel.gui.controller.helpers;

import java.io.IOException;
import java.io.InputStream;
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

public class WorkbookHelper {
	/* main elements of result file */
	private InputStream inputStream; 
	public Workbook schedule;
	public Sheet scheduleSheet;
	public Row scheduleMainRow;
	
	private final static String MONDAY = "Понеділок";
	private final static String TUESDAY = "Вівторок";
	private final static String WEDNESDAY = "Середа";
	private final static String THURSDAY = "Четвер";
	private final static String FRIDAY = "П'ятниця";
	private final static List<String> DAYS = Arrays.asList(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY);
	
	private final static String CURRENT_DAY = "CURRENT_DAY";
	private final static String ANOTHER_DAY = "ANOTHER_DAY";
	
	private final static String AMOUNT = "Кількість";
	
	private int amountColNum;
	private int descColNum;
	
	public WorkbookHelper() {
		try {
			initWorkbook();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Cause of the bug in Apache POI (XMLInvalidFormat or something when program writing to the same Workbook second time), 
	 * this method called every time we need to work with workbook.
	 */
	public void initWorkbook() throws IOException {
		inputStream = getClass().getResourceAsStream("/template/WeekOrdersTotalTemplate.xlsx");
		schedule = new XSSFWorkbook(inputStream);
		scheduleSheet = schedule.getSheet("Schedule");
		scheduleMainRow = scheduleSheet.getRow(0);
	}
	
	
	/**
	 * Method populates first cell of row with name of person whose file is processing currently. 
	 * 
	 * @param name of the person to be inserted
	 * @param rowNum number of row where should be name inserted
	 */
	public void setNameCell(String name, int rowNum) {
		scheduleSheet.getRow(rowNum).getCell(0).setCellValue(name);
	}
	
	/**
	 * Methods processes all rows in sheet and collecting into Map all orders per every day.
	 * @param menuSheet to be processed
	 * @return Map<DayName, Map<OrderDescription, Amount>
	 */
	public Map<String, Map<String, Integer>> getWeekOrders(Sheet menuSheet) {
		int currentDay = 0;

		/* In this Map will be selected all orders per day.
		 * So, structure is following: Day : Map<Order, Amount>
		 * e.g. Friday : Map<Salat, 2>, Map<Meat, 1>,...
		 */
		Map<String, Map<String, Integer>> ordersPerDay = new HashMap<>();
		/* Loop every row in menu, where all persons orders for this week. */
		for (Row row : menuSheet) {
			/* Retrieving row with order for current row.
			 * If it is empty, then there no order was made.
			 * If it is with day descriptor - then it is day row and should be processed by processDayRow()
			 */
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
			/* If this day has not any orders - create it and put current order here.
			 * If it was already done, then add order to existing items.
			 */
			String currentDAYStr = DAYS.get(currentDay);
			if (!ordersPerDay.containsKey(currentDAYStr)) {
				ordersPerDay.put(DAYS.get(currentDay), rowItems);
			} else {
				ordersPerDay.get(currentDAYStr).putAll(rowItems);
			}
			
		}
		return ordersPerDay;
	}
	
	/**
	 * Method investigate if row is not empty, then if it not a day descriptor row
	 * and then puts in map name of order and quantity, if row of quantity is not empty.
	 * Then returns it.
	 * 
	 * @param row which should be investigated
	 * @param currentDay of week
	 * @return Map<String, Integer> - e.g. Map<Meatball, 3>, or day descriptor and int of this day if it is day row.
	 */
	private Map<String, Integer> processRow(Row row, int currentDay) {
		/*
		 * This container contains name of order and amount.
		 * So, Map<Meatball, 3>, Map<Salat, 1>, Map<Soup, 1>
		 */
		Map<String, Integer> map = new HashMap<String, Integer>();
		
		/* If first cell of row is empty - then this row is break between days, return null */
		if(row.getCell(0) == null)
			return map;
		/* Get the value of first cell (name of order) */
		String firstCellValue = row.getCell(0).getStringCellValue();
		/* if null or empty - it is break between days */
		if (firstCellValue == null || firstCellValue.equals(""))
			return map;
		/* If first cell is some day name, then it is our case and should be processed.
		 * If current day (possible only for first day of week) then return it with the int of this day.
		 * If it is some another day name (next day, in two days) return ANOTHER_DAY variable with int of this day.
	     */
		if (DAYS.contains(firstCellValue)) {
			int curDayNum = DAYS.indexOf(firstCellValue);
			if (curDayNum == currentDay)
				map.put(CURRENT_DAY, currentDay);
			else
				map.put(ANOTHER_DAY, curDayNum);
			/* Then return it map, cause it should be not processed as row with orders */
			return map;
		}
		/* If cell with amount of order is not null and it does not empty (if empty - returns 0) - then put to the map name of order and quantity */
		if (null != row.getCell(amountColNum) && row.getCell(amountColNum).getNumericCellValue() != 0) {
			map.put(row.getCell(descColNum).getStringCellValue(), (int) row.getCell(amountColNum).getNumericCellValue());
		}
		return map;
	}
	
	/**
	 * Method checks every cell in row and searches for day and amount rows and sets their indexes to class variables,
	 * which are used by other methods.
	 * @param row to be processed (day row)
	 * @param currentDay
	 */
	private void processDayRow(Row row, int currentDay) {
		for (Cell cell : row) {
			if (cell.getStringCellValue().equals(DAYS.get(currentDay))) {
				descColNum = cell.getColumnIndex();
				continue;
			}
			if (cell.getStringCellValue().equals(AMOUNT)) {
				amountColNum = cell.getColumnIndex();
				continue;
			}
		}
	}
	
	/**
	 * Method write to schedule sheet orders of particular person for this week.
	 * @param personName required to search row where to insert data
	 * @param orders of this person for week
	 */
	public void writeWeekOrdersToSheet(String personName, Map<String, Map<String, Integer>> orders) {
		CellStyle style = createCellStyle();
		int dayCol = 0;
		
		/*
		 * Loop every entry (day of week) and search where in schedule's row is this day column index.
		 * Then collects day's orders to string builder and sets content of needed cell to this string.
		 */
		for (Map.Entry<String, Map<String, Integer>> entry : orders.entrySet()) {
			/* Search for right day column in schedule */
			for (Cell cell : scheduleMainRow) {
				if (cell.getStringCellValue().equals(entry.getKey())) {
					dayCol = cell.getColumnIndex();
				}
			}
			/* StringBuilder where will be collected all orders per particular day. */
			StringBuilder dayOrder = new StringBuilder();
			dayOrder.append(entry.getKey()).append("  -  ").append(personName);
			for (Map.Entry<String, Integer> order : entry.getValue().entrySet()) {
				dayOrder.append("\n").append(order.getKey()).append(" x").append(order.getValue());
			}
			/* Loop searches for person's row and sets in this row to relative day column style and content(orders) */
			for (Row row : scheduleSheet) {
				/* this make row be auto sized depending on cell's content */
				row.setHeight((short) -1);
				if (row.getCell(0).getStringCellValue().equals(personName)) {
					Cell cell = row.getCell(dayCol);
					if(cell == null)
						cell = row.createCell(dayCol);
					
					cell.setCellStyle(style);
					cell.setCellValue(dayOrder.toString());
				}
			}
			
		}
		
	}
	
	/**
	 * Method creates style for every cell that will be written to sheet.
	 * @return style with border and wrap text properties.
	 */
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
