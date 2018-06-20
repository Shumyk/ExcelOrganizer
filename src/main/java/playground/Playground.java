package playground;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Playground {

	public static void main(String[] args) throws IOException {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Dicker");
		Sheet second = workbook.createSheet("Asshole");
		
		FileOutputStream fos = new FileOutputStream("example.xlsx");
		workbook.write(fos);
		
		fos.close();
	}
}
