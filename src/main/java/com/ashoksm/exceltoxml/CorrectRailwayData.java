package com.ashoksm.exceltoxml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

public class CorrectRailwayData {

	public static void main(String[] args) throws Exception {
		File root = new File("E:\\Ashok\\Dropbox\\MyDetails\\pinfinder\\railwaycodes");
		for (File file : root.listFiles()) {
			if (file.getName().startsWith("trainspassingviastation_")) {
				System.out.println(file.getName());
				FileInputStream inputStream = new FileInputStream(file);
				// Get the workbook instance for XLS file
				HSSFWorkbook workBook = new HSSFWorkbook(inputStream);

				// Get first sheet from the workbook
				HSSFSheet stationSheet = workBook.getSheetAt(0);
				// Iterate through each rows from first sheet
				Iterator<Row> rowIterator = stationSheet.iterator();

				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					String stationCode = row.getCell(1).getStringCellValue();
					row.getCell(1).setCellValue(
							stationCode.substring(stationCode.lastIndexOf("(") + 1, stationCode.lastIndexOf(")")));
				}
				FileOutputStream out = new FileOutputStream(file);
				workBook.write(out);
				out.close();
			}
		}
	}
}
