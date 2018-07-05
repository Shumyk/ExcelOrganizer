package shumyk.excel.formula;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.RichTextString;
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
	
	public static String append(Object... items) {
		StringBuilder builder = new StringBuilder();
		
		for (Object object : items) {
			builder.append(object);
		}
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
	
	public static void setCellComment(Cell cell, String message) {
		Drawing drawing = cell.getSheet().createDrawingPatriarch();
		CreationHelper helper = cell.getSheet().getWorkbook().getCreationHelper();
		
		ClientAnchor anchor = helper.createClientAnchor();
		anchor.setCol1(cell.getColumnIndex());
		anchor.setCol2(cell.getColumnIndex() + 1);
		anchor.setRow1(cell.getRowIndex());
		anchor.setRow2(cell.getRowIndex() + 1);
		anchor.setDx1(100);
		anchor.setDx2(100);
		anchor.setDy1(100);
		anchor.setDy2(100);
		
		Comment comment = drawing.createCellComment(anchor);
		RichTextString str = helper.createRichTextString(message);
		comment.setString(str);
		comment.equals("shumyk");
		
		cell.setCellComment(comment);
	}
}
