package com.ashoksm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

public class StdExcelToSQLConverter {

	private static final String SQL = "INSERT INTO std_t VALUES(<State>, '<CityName>', '<STDCode>');";

	public static void main(String[] args) throws Exception {
		File root = new File(args[0]);
		for (final File state : root.listFiles()) {
			FileInputStream file = new FileInputStream(state);
			HSSFWorkbook workbook = new HSSFWorkbook(file);
			HSSFSheet sheet = workbook.getSheetAt(0);
			String stateCode = getStateCode(state.getName().toLowerCase());
			BufferedWriter writer = new BufferedWriter(new FileWriter(args[1] + "\\"
					+ state.getName().substring(0, state.getName().lastIndexOf(".")) + ".sql"));
			// Iterate through each rows from first sheet
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				String line = SQL.replaceAll("<State>", stateCode)
						.replaceAll("<CityName>", row.getCell(0).getStringCellValue())
						.replaceAll("<STDCode>", row.getCell(1).getStringCellValue());
				writer.write(line);
				writer.newLine();
			}
			writer.flush();
			writer.close();
			System.out.println(state.getName() + " Completed!!!");
		}
	}

	private static String getStateCode(String xmlName) throws IOException {
		File stateExcel = new File("E:\\Ashok\\Dropbox\\MyDetails\\pinfinder\\states.xls");

		FileInputStream file = new FileInputStream(stateExcel);

		// Get the workbook instance for XLS file
		HSSFWorkbook workbook = new HSSFWorkbook(file);

		// Get first sheet from the workbook
		HSSFSheet sheet = workbook.getSheetAt(0);

		// Iterate through each rows from first sheet
		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			String stateName = row.getCell(1).getStringCellValue().replaceAll(" ", "").toLowerCase();
			if (xmlName.contains(stateName)) {
				return String.valueOf(new Double(row.getCell(0).getNumericCellValue()).intValue());
			}
		}
		return null;
	}
}
