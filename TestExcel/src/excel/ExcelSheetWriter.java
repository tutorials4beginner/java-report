package excel;

import java.io.File;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.IndexedColors;

/**
 * @author 
 */
public class ExcelSheetWriter {
	/**
	 * This method is used to write data to an excel file.
	 * 
	 * @param fileName
	 *            - Name of the excel file.
	 */
	private void writeExcelFile(String fileName) {
		FileOutputStream fileOutputStream = null;
		HSSFWorkbook sampleWorkbook = null;
		HSSFSheet sampleDataSheet = null;
		try {
			/**
			 * Create a new instance for HSSFWorkBook class and create a sample
			 * worksheet using HSSFSheet class to write data.
			 */
			sampleWorkbook = new HSSFWorkbook();
			sampleDataSheet = sampleWorkbook.createSheet("SampleDataSheet1");
			/**
			 * Create two rows using HSSFRow class, where headerRow denotes the
			 * header and the dataRow1 denotes the cell data.
			 */
			HSSFRow headerRow = sampleDataSheet.createRow(0);
			HSSFRow dataRow1 = sampleDataSheet.createRow(1);
			HSSFCellStyle cellStyle = setHeaderStyle(sampleWorkbook);
			/**
			 * Call the setHeaderStyle method and set the styles for the all the
			 * three header cells.
			 */
			HSSFCell firstHeaderCell = headerRow.createCell(0);
			firstHeaderCell.setCellStyle(cellStyle);
			firstHeaderCell
					.setCellValue(new HSSFRichTextString("Employer Name"));
			HSSFCell secondHeaderCell = headerRow.createCell(1);
			secondHeaderCell.setCellStyle(cellStyle);
			secondHeaderCell
					.setCellValue(new HSSFRichTextString("Designation"));
			HSSFCell thirdHeaderCell = headerRow.createCell(2);
			thirdHeaderCell.setCellStyle(cellStyle);
			thirdHeaderCell.setCellValue(new HSSFRichTextString("Country"));
			/**
			 * Set the cell value for all the data rows.
			 */
			dataRow1.createCell(0).setCellValue(
					new HSSFRichTextString("Gift Sam"));
			dataRow1.createCell(1).setCellValue("Software Engineer");
			dataRow1.createCell(2).setCellValue("Malaysia");
			fileOutputStream = new FileOutputStream(fileName);
			sampleWorkbook.write(fileOutputStream);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			/**
			 * Close the fileOutputStream.
			 */
			try {
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * This method is used to set the styles for all the headers of the excel
	 * sheet.
	 * 
	 * @param sampleWorkBook
	 *            - Name of the workbook.
	 * @return cellStyle - Styles for the Header data of Excel sheet.
	 */
	private HSSFCellStyle setHeaderStyle(HSSFWorkbook sampleWorkBook) {
		HSSFFont font = sampleWorkBook.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setColor(IndexedColors.PLUM.getIndex());
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle cellStyle = sampleWorkBook.createCellStyle();
		cellStyle.setFont(font);
		return cellStyle;
	}

	public static void main(String[] args) {
		new ExcelSheetWriter().writeExcelFile("C:" + File.separator + "Users"
				+ File.separator + "Giftsam" + File.separator + "Desktop"
				+ File.separator + "sampleexcel.xls");
	}
}
