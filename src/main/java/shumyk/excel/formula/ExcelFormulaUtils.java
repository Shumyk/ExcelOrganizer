package shumyk.excel.formula;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class ExcelFormulaUtils {
	static int getNumOfCell(Row row, String pattern) {
		for (Cell cell : row) {
			if(cell.getStringCellValue().equalsIgnoreCase(pattern))
				return cell.getColumnIndex();
		}
		return -1;
	}
	
	static String toRows(int index) {
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
	
	static String append(Object... items) {
		StringBuilder builder = new StringBuilder();
		
		for (Object object : items) {
			builder.append(object);
		}
		System.out.println(builder.toString());
		return builder.toString();
	}
	
	static boolean isNumeric(Cell cell) {
		if(null != cell && 0 == cell.getCellType()) {
			return true;
		} else { 
			return false;
		}
	}
	static boolean isString(Cell cell) {
		if(null != cell && 1 == cell.getCellType())
			return true;
		else
			return false;
	}
}
