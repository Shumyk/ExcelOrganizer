package shumyk.excel.formula;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ComparisonOperator;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.FontFormatting;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PatternFormatting;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFConditionalFormattingRule;
import org.apache.poi.xssf.usermodel.XSSFSheetConditionalFormatting;

public class ExcelFormula {
	private Workbook workbook;
	
	public ExcelFormula(Workbook workbook) {
		this.workbook = workbook;
	}
	
	public Workbook createMenuWithPriceCalculating() {

		
		CellStyle sheetStyle = workbook.createCellStyle();
		int indexOfPrice = getNumOfCell(workbook.getSheetAt(0).getRow(0), "Ціна");
		int  indexOfCalculatedSum;
		if(workbook.getSheetAt(0).getRow(0).createCell(indexOfPrice + 1).getStringCellValue().isEmpty()) {
			indexOfCalculatedSum = indexOfPrice + 2;
		} else {
			indexOfCalculatedSum = indexOfPrice + 3;
		}
		int indexOfAmount = indexOfCalculatedSum - 1;
		
		
		Sheet sheet = workbook.getSheetAt(0);
		for (Row row : sheet) {
			int rowIndex = row.getRowNum();
			
			
			if (isString(row.getCell(indexOfPrice))) {
				sheetStyle = row.getCell(indexOfPrice).getCellStyle();
				
				Cell cellAmount = row.createCell(indexOfCalculatedSum - 1);
				cellAmount.setCellValue("Кількість");
				cellAmount.setCellStyle(sheetStyle);
				Cell cellSumm = row.createCell(indexOfCalculatedSum);
				cellSumm.setCellValue("Сума");
				cellSumm.setCellStyle(sheetStyle);
				continue;
			}
			
			if (isNumeric(row.getCell(indexOfPrice))) {
				String formula = mapToAlphabetic(indexOfPrice)
						.concat(String.valueOf(rowIndex + 1))
						.concat("*")
						.concat(mapToAlphabetic(indexOfCalculatedSum - 1))
						.concat(String.valueOf(rowIndex + 1));
						
				row.createCell(indexOfCalculatedSum).setCellFormula(formula);
			}
			
		}
		
		int lastRowNum = sheet.getLastRowNum();
		Row summarizeRow = sheet.createRow(lastRowNum + 1);
		
		Cell summarizeCell = summarizeRow.createCell(indexOfCalculatedSum - 1);
		summarizeCell.setCellValue("Сума:");
		summarizeCell.setCellStyle(sheetStyle);
		
		String sumFormula = "SUM("
				.concat(mapToAlphabetic(indexOfCalculatedSum))
				.concat(String.valueOf(1))
				.concat(":")
				.concat(mapToAlphabetic(indexOfCalculatedSum))
				.concat(String.valueOf(lastRowNum))
				.concat(")");
		Cell cellSumFormula = summarizeRow.createCell(indexOfCalculatedSum);
		cellSumFormula.setCellFormula(sumFormula);
		cellSumFormula.setCellStyle(sheetStyle);
		
		
		SheetConditionalFormatting formatting = workbook.getSheetAt(0).getSheetConditionalFormatting();
		ConditionalFormattingRule rule = formatting.createConditionalFormattingRule("AND(ISNUMBER($"
				.concat(mapToAlphabetic(indexOfAmount))
				.concat("1),$")
				.concat(mapToAlphabetic(indexOfAmount))
				.concat("1>0)"));
		PatternFormatting pattern = rule.createPatternFormatting();
		pattern.setFillBackgroundColor(IndexedColors.LIGHT_YELLOW.index);
		CellRangeAddress[] dataRange = {CellRangeAddress.valueOf(mapToAlphabetic(0)
				.concat(String.valueOf(1))
				.concat(":")
				.concat(mapToAlphabetic(indexOfCalculatedSum))
				.concat(String.valueOf(lastRowNum)) )};
		formatting.addConditionalFormatting(dataRange, rule);
		
		return workbook;
	}
	
	
	
	private int getNumOfCell(Row row, String pattern) {
		for (Cell cell : row) {
			if(cell.getStringCellValue().equalsIgnoreCase(pattern))
				return cell.getColumnIndex();
		}
		return -1;
	}
	
	private String mapToAlphabetic(int index) {
		switch (index) {
		case 0:
			return "A";
		case 1:
			return "B";
		case 2:
			return "C";
		case 3:
			return "D";
		case 4:
			return "E";

		default:
			return "";
		}
	}
	
	private boolean isNumeric(Cell cell) {
		if(null != cell && 0 == cell.getCellType()) {
			return true;
		} else { 
			return false;
		}
	}
	private boolean isString(Cell cell) {
		if(null != cell && 1 == cell.getCellType())
			return true;
		else
			return false;
	}
	
}
