package shumyk.excel.formula;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;

/**
 * Class provides utilities for excel related jobs. 
 * @author shumyk
 */
public class ExcelFormulaUtils {
	/**
	 * Method finds cell in particular row for specified pattern.
	 * 
	 * @param row where to search
	 * @param pattern of content
	 * @return number of cell, -1 if not found
	 */
	static int getNumOfCell(Row row, String pattern) {
		for (Cell cell : row) {
			if(cell.getStringCellValue().equalsIgnoreCase(pattern))
				return cell.getColumnIndex();
		}
		return -1;
	}
	
	/**
	 * Translates integer indexes to alphabetic representation.
	 * @param index of row to be translated
	 * @return alphabetic representation of column
	 */
	static String toCols(int index) {
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
	
	/**
	 * Appends all items into one liner string, without any spaces.
	 * @param items to be appended
	 * @return collected string
	 */
	public static String append(Object... items) {
		StringBuilder builder = new StringBuilder();
		
		for (Object object : items) {
			builder.append(object);
		}
		return builder.toString();
	}
	
	/**
	 * Checks if cell contains numeric type of data
	 * @param cell to check
	 * @return true / false
	 */
	static boolean isNumeric(Cell cell) {
		if(null != cell && Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
			return true;
		} else { 
			return false;
		}
	}
	/**
	 * Checks if cell contains string type of data.
	 * @param cell to check
	 * @return true / false
	 */
	static boolean isString(Cell cell) {
		if(null != cell && Cell.CELL_TYPE_STRING == cell.getCellType())
			return true;
		else
			return false;
	}
	
	/**
	 * Sets comment with passed message to passed cell.
	 * @param cell where comment should be set comment
	 * @param message which should be in comment
	 */
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
		comment.setAuthor("shumyk");
		
		cell.setCellComment(comment);
	}
}
