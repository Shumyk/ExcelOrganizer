package shumyk.excel.formula;

import static shumyk.excel.formula.ExcelFormulaUtils.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PatternFormatting;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

public class ExcelFormula {
	private Workbook workbook;
	
	private CellStyle sheetStyle;
	
	private int indexOfPrice;
	private int indexOfCalculatedSum;
	private int indexOfAmount;
	private int lastRowNum;
	
	public ExcelFormula(Workbook workbook) {
		this.workbook = workbook;
	}
	
	public Workbook createMenuWithPriceCalculating() {
		sheetStyle = workbook.createCellStyle();
		indexOfPrice = getNumOfCell(workbook.getSheetAt(0).getRow(0), "Ціна");
		if(workbook.getSheetAt(0).getRow(0).createCell(indexOfPrice + 1).getStringCellValue().isEmpty()) {
			indexOfCalculatedSum = indexOfPrice + 2;
		} else {
			indexOfCalculatedSum = indexOfPrice + 3;
		}
		indexOfAmount = indexOfCalculatedSum - 1;
		
		Sheet sheet = workbook.getSheetAt(0);
		fillCalculationPart(sheet);
		
		lastRowNum = sheet.getLastRowNum();
		Row summarizeRow = sheet.createRow(lastRowNum);
		fillSummarizeRow(summarizeRow);
		
		SheetConditionalFormatting formatting = workbook.getSheetAt(0).getSheetConditionalFormatting();
		createSheetHighlightingFormating(formatting);
		
		return workbook;
	}
	
	
	
	
	private void fillCalculationPart(Sheet sheet) {
		for (Row row : sheet) {
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
				int rowIndex = row.getRowNum();
				String formula = append(toRows(indexOfPrice), rowIndex + 1, "*", toRows(indexOfCalculatedSum - 1), rowIndex + 1);
				row.createCell(indexOfCalculatedSum).setCellFormula(formula);
			}
		}
	}
	
	private void fillSummarizeRow(Row summarizeRow) {
		Cell summarizeCell = summarizeRow.createCell(indexOfCalculatedSum - 1);
		summarizeCell.setCellValue("Сума:");
		summarizeCell.setCellStyle(sheetStyle);
		
		String sumFormula = append("SUM(", toRows(indexOfCalculatedSum), 1, ":", toRows(indexOfCalculatedSum), lastRowNum, ")");
		Cell cellSumFormula = summarizeRow.createCell(indexOfCalculatedSum);
		cellSumFormula.setCellFormula(sumFormula);
		cellSumFormula.setCellStyle(sheetStyle);
	}
	
	private void createSheetHighlightingFormating(SheetConditionalFormatting formatting) {
		ConditionalFormattingRule rule = formatting.createConditionalFormattingRule(append(
				"AND(ISNUMBER($", toRows(indexOfAmount), "1),$", toRows(indexOfAmount), "1>0)"));
		PatternFormatting pattern = rule.createPatternFormatting();
		pattern.setFillBackgroundColor(IndexedColors.LIGHT_YELLOW.index);
		CellRangeAddress[] dataRange = {CellRangeAddress.valueOf(append(toRows(0), 1, ":", toRows(indexOfCalculatedSum), lastRowNum))};
		formatting.addConditionalFormatting(dataRange, rule);
	}
}
