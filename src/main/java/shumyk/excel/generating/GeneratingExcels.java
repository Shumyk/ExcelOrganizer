package shumyk.excel.generating;

import static shumyk.excel.formula.ExcelFormulaUtils.append;
import static shumyk.excel.formula.ExcelFormulaUtils.setCellComment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import shumyk.excel.formula.ExcelFormula;

public class GeneratingExcels {
	
	/**
	 * Creates separate menu for every person with calculation and formating possibilities.
	 * @param menu of this week
	 * @param names of people for whom created separate menu
	 * @throws IOException
	 */
	public static void createMenusPerPerson(File menu, File names) throws IOException {
		/* Creating of names sheet */
		FileInputStream fis = new FileInputStream(names);
		Sheet sheet = new XSSFWorkbook(fis).getSheetAt(0);
		
		/* Creating of folder where all menus gonna be hold */
		String pathDir = "menu per person/";
		new File(pathDir).mkdir();
		
		/* Loops over every row in names sheet and creates for this person separate menu */
		for (Row row : sheet) {
			ExcelFormula excelFormula = new ExcelFormula(new XSSFWorkbook(new FileInputStream(menu)));
			// retrieving workbook with formulas and formating
			Workbook menuResult = excelFormula.createMenuWithPriceCalculating();
			
			String menuFileName = menu.getName();
			String nameOfPerson = row.getCell(0).getStringCellValue();
			Cell cell = menuResult.getSheetAt(0).getRow(0).getCell(0);
			// sets comment with name of person to initial cell (needed in further operations)
			setCellComment(cell, nameOfPerson);
			// creating name of file with person's name
			String pathNewExcel = append(pathDir, menuFileName.substring(0, menuFileName.indexOf(".xlsx")), " ", nameOfPerson, ".xlsx");
			// writing excel file
			FileOutputStream fos = new FileOutputStream(new File(pathNewExcel));
			menuResult.write(fos);
			fos.close();
		}
	}
	

}
