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
		this.sheetStyle = this.workbook.createCellStyle();
	}
	
	/**
	 * Creates menu with price calculation, formating functionalities.
	 * 
	 * @return result workbook
	 */
	public Workbook createMenuWithPriceCalculating() {
		// gets number of cell where is price held
		indexOfPrice = getNumOfCell(workbook.getSheetAt(0).getRow(0), "Ціна");
		// if next column is occupied choose the next one
		if(workbook.getSheetAt(0).getRow(0).createCell(indexOfPrice + 1).getStringCellValue().isEmpty()) {
			indexOfCalculatedSum = indexOfPrice + 2;
		} else {
			indexOfCalculatedSum = indexOfPrice + 3;
		} // and set amount column before calculation column
		indexOfAmount = indexOfCalculatedSum - 1;
		
		Sheet sheet = workbook.getSheetAt(0);
		// fills calculation part
		fillCalculationPart(sheet);
		
		// creates summarize row of sheet
		Row summarizeRow = sheet.createRow(lastRowNum);
		fillSummarizeRow(summarizeRow);
		
		// sets conditional formatting for sheet - highlight if amount column is not null and numeric
		SheetConditionalFormatting formatting = workbook.getSheetAt(0).getSheetConditionalFormatting();
		createSheetHighlightingFormating(formatting);
		
		return workbook;
	}
	
	
	
	/**
	 * Looks whether this row is day row or not.
	 * If yes, then fills with additional descriptions.
	 * If it is order row and fills with formulas.
	 * @param sheet to be filled
	 */
	private void fillCalculationPart(Sheet sheet) {
		for (Row row : sheet) {
			/* if price cell of row is string than it is day description row and it adds additional descriptors */
			if (isString(row.getCell(indexOfPrice))) {
				sheetStyle = row.getCell(indexOfPrice).getCellStyle();
				
				Cell cellAmount = row.createCell(indexOfCalculatedSum - 1);
				cellAmount.setCellValue("Кількість");
				cellAmount.setCellStyle(sheetStyle);
				Cell cellSumm = row.createCell(indexOfCalculatedSum);
				cellSumm.setCellValue("Сума");
				cellSumm.setCellStyle(sheetStyle);
				// this line sets the last row that was populated, cause Sheet.getLastRowNum() sometimes counts empty rows as valid
				lastRowNum = row.getRowNum() + 1;
				continue;
			}
			/* if price cell of row is numeric than it is order row and it adds formulas  */
			if (isNumeric(row.getCell(indexOfPrice))) {
				int rowIndex = row.getRowNum();
				String formula = append(toCols(indexOfPrice), rowIndex + 1, "*", toCols(indexOfCalculatedSum - 1), rowIndex + 1);
				row.createCell(indexOfCalculatedSum).setCellFormula(formula);
				// this line sets the last row that was populated, cause Sheet.getLastRowNum() sometimes counts empty rows as valid
				lastRowNum = row.getRowNum() + 1;
			}
		}
	}
	
	/**
	 * Creates description and formula cells for row.
	 * Description inserted to cell with number as indexOfAmount
	 * Formula inserted to cell with number as indexOfCalculatedSum
	 * @param summarizeRow where should be inserted cells
	 */
	private void fillSummarizeRow(Row summarizeRow) {
		Cell summarizeCell = summarizeRow.createCell(indexOfAmount);
		summarizeCell.setCellValue("Сума:");
		summarizeCell.setCellStyle(sheetStyle);
		
		String sumFormula = append("SUM(", toCols(indexOfCalculatedSum), 1, ":", toCols(indexOfCalculatedSum), lastRowNum, ")");
		Cell cellSumFormula = summarizeRow.createCell(indexOfCalculatedSum);
		cellSumFormula.setCellFormula(sumFormula);
		cellSumFormula.setCellStyle(sheetStyle);
	}
	
	/**
	 * 
	 * @param formatting
	 */
	private void createSheetHighlightingFormating(SheetConditionalFormatting formatting) {
		ConditionalFormattingRule rule = formatting.createConditionalFormattingRule(append(
				"AND(ISNUMBER($", toCols(indexOfAmount), "1),$", toCols(indexOfAmount), "1>0)"));
		PatternFormatting pattern = rule.createPatternFormatting();
		pattern.setFillBackgroundColor(IndexedColors.LIGHT_YELLOW.index);
		CellRangeAddress[] dataRange = {CellRangeAddress.valueOf(append(toCols(0), 1, ":", toCols(indexOfCalculatedSum), lastRowNum))};
		formatting.addConditionalFormatting(dataRange, rule);
	}
}
