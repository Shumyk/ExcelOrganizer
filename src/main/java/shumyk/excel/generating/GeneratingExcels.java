package shumyk.excel.generating;

import static shumyk.excel.formula.ExcelFormulaUtils.append;
import static shumyk.excel.formula.ExcelFormulaUtils.setCellComment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import shumyk.excel.formula.ExcelFormula;

public class GeneratingExcels {
	private static final Logger LOGGER = LogManager.getLogger(GeneratingExcels.class);
	
	public static void createMenusPerPerson(File menu, File names) throws IOException {
		FileInputStream fis = new FileInputStream(names);
		Workbook workbook = new XSSFWorkbook(fis);
		Sheet sheet = workbook.getSheetAt(0);
		String pathDir = "personsMenus/";
		new File(pathDir).mkdir();
		
		for (Row row : sheet) {
			ExcelFormula excelFormula = new ExcelFormula(new XSSFWorkbook(new FileInputStream(menu)));
			Workbook menuResult = excelFormula.createMenuWithPriceCalculating();
			
			String menuFileName = menu.getName();
			String nameOfPerson = row.getCell(0).getStringCellValue();
			
			Cell cell = menuResult.getSheetAt(0).getRow(0).getCell(0);
			setCellComment(cell, nameOfPerson);
			
			String pathNewExcel = append(pathDir, menuFileName.substring(0, menuFileName.indexOf(".xlsx")), " ", nameOfPerson, ".xlsx");
			System.out.println(pathNewExcel);
			
			
			FileOutputStream fos = new FileOutputStream(new File(pathNewExcel));
			menuResult.write(fos);
			fos.close();
		}
	}
	

}
