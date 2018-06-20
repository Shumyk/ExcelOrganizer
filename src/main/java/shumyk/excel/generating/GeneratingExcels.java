package shumyk.excel.generating;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import shumyk.excel.formula.ExcelFormula;

public class GeneratingExcels {
	private static final Logger LOGGER = LogManager.getLogger(GeneratingExcels.class);
	
	public void createMenusPerPerson(String menuFilename, String namesFilename) throws IOException {
		FileInputStream fis = new FileInputStream(new File(namesFilename));
		Workbook workbook = new XSSFWorkbook(fis);
		String pathDir = "personsMenus/";
		new File(pathDir).mkdir();
		
		for (Row row : workbook.getSheetAt(0)) {
			ExcelFormula excelFormula = new ExcelFormula(new XSSFWorkbook(new FileInputStream(new File(menuFilename))));
			Workbook menuInitial = excelFormula.createMenuWithPriceCalculating();
			
			
			String pathNewExcel = pathDir
					.concat(menuFilename.substring(0, menuFilename.indexOf(".xlsx")))
					.concat(" ")
					.concat(row.getCell(0).getStringCellValue())
					.concat(".xlsx");
			System.out.println(pathNewExcel);
			
			FileOutputStream fos = new FileOutputStream(new File(pathNewExcel));
			menuInitial.write(fos);
			fos.close();
		}
	}


}
