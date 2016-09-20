package com.ashoksm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

public class TrainDetailsSQLConverter {

	private static final String SQL = "INSERT INTO trains_t VALUES(<TrainNo>, '<TrainName>', '<Starts>', '<Ends>', '<Days>', '<Pantry>');";

	public static void main(String[] args) throws Exception {
		FileInputStream file = new FileInputStream(
				new File("/home/local/BSILIND/ashok/Downloads/railwaycodes/trains.xls"));
		HSSFWorkbook workbook = new HSSFWorkbook(file);
		HSSFSheet sheet = workbook.getSheetAt(0);
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				"/home/local/BSILIND/ashok/MySpace/OfflinePinFinder/" + "app/src/main/assets/sql/railway/trains.sql"));

		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			if (row.getRowNum() == 0) {
				continue;
			}
			String line = SQL.replaceAll("<TrainNo>", row.getCell(0).getStringCellValue())
					.replaceAll("<TrainName>", row.getCell(1).getStringCellValue())
					.replaceAll("<Starts>", row.getCell(2).getStringCellValue())
					.replaceAll("<Ends>", row.getCell(3).getStringCellValue())
					.replaceAll("<Days>", row.getCell(4).getStringCellValue())
					.replaceAll("<Pantry>", row.getCell(5).getStringCellValue());
			writer.write(line);
			writer.newLine();
		}
		writer.flush();
		writer.close();
		System.out.println("Completed!!!");
	}

}
