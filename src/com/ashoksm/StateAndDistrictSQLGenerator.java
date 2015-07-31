package com.ashoksm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class StateAndDistrictSQLGenerator {

	private static final String STATE_SQL = "INSERT INTO state_t VALUES(<StateCode>, '<StateName>');";

	private static final String LOCATION_SQL = "INSERT INTO location_t VALUES(<LocationCode>, '<LocationName>');";

	private static final String DISTRICT_SQL = "INSERT INTO district_t VALUES(<StateCode>, <DistrictCode>, '<DistrictName>');";

	public static void main(String[] args) throws Exception {
		createStateSQL();
		createLocationsSQL();
		createDistrictSQL();
	}

	/**
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private static void createStateSQL() throws IOException {
		File stateExcel = new File("E:\\Ashok\\Dropbox\\MyDetails\\pinfinder\\states.xls");
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				"E:\\Android\\workspace\\OfflinePinFinder\\app\\src\\main\\assets\\sql\\pincode\\states.sql"));

		FileInputStream file = new FileInputStream(stateExcel);

		// Get the workbook instance for XLS file
		HSSFWorkbook workbook = new HSSFWorkbook(file);

		// Get first sheet from the workbook
		HSSFSheet sheet = workbook.getSheetAt(0);

		// Iterate through each rows from first sheet
		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			String line = null;
			Row row = rowIterator.next();
			// For each row, iterate through each columns
			Cell cell = row.getCell(0);
			line = STATE_SQL.replaceAll("<StateCode>",
					String.valueOf(new Double(cell.getNumericCellValue()).intValue()));
			Cell cell1 = row.getCell(1);
			line = line.replaceAll("<StateName>", cell1.getStringCellValue());
			writer.write(line);
			writer.newLine();
		}
		writer.flush();
		writer.close();
	}

	/**
	 * @throws Exception
	 * @throws FileNotFoundException
	 */
	private static void createLocationsSQL() throws Exception {
		File stateExcel = new File("E:\\Ashok\\Dropbox\\MyDetails\\pinfinder\\taluk.xls");
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				"E:\\Android\\workspace\\OfflinePinFinder\\app\\src\\main\\assets\\sql\\pincode\\locations.sql"));

		FileInputStream file = new FileInputStream(stateExcel);

		// Get the workbook instance for XLS file
		HSSFWorkbook workbook = new HSSFWorkbook(file);

		// Get first sheet from the workbook
		HSSFSheet sheet = workbook.getSheetAt(0);

		// Iterate through each rows from first sheet
		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			String line = null;
			Row row = rowIterator.next();
			// For each row, iterate through each columns
			Cell cell = row.getCell(0);
			line = LOCATION_SQL.replaceAll("<LocationCode>",
					String.valueOf(new Double(cell.getNumericCellValue()).intValue()));
			Cell cell1 = row.getCell(1);
			try {
				line = line.replaceAll("<LocationName>", cell1.getStringCellValue().trim().replaceAll("'", "''"));
			} catch (Exception ex) {
				System.out.println(cell1.getStringCellValue());
				writer.flush();
				writer.close();
				throw new Exception(cell1.getStringCellValue(), ex);
			}
			writer.write(line);
			writer.newLine();
		}
		writer.flush();
		writer.close();
	}

	/**
	 * @throws IOException
	 */
	private static void createDistrictSQL() throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				"E:\\Android\\workspace\\OfflinePinFinder\\app\\src\\main\\assets\\sql\\pincode\\district.sql"));
		File root = new File("E:\\Ashok\\Dropbox\\MyDetails\\pinfinder\\district.xls");

		FileInputStream file = new FileInputStream(root);

		// Get the workbook instance for XLS file
		HSSFWorkbook workbook = new HSSFWorkbook(file);

		// Get first sheet from the workbook
		HSSFSheet sheet = workbook.getSheetAt(0);

		// Iterate through each rows from first sheet
		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			String line = null;
			Row row = rowIterator.next();
			// For each row, iterate through each columns
			Cell cell = row.getCell(0);
			line = DISTRICT_SQL.replaceAll("<StateCode>",
					String.valueOf(new Double(cell.getNumericCellValue()).intValue()));
			Cell cell1 = row.getCell(1);
			line = line
					.replaceAll("<DistrictCode>", String.valueOf(new Double(cell1.getNumericCellValue()).intValue()));
			Cell cell2 = row.getCell(2);
			line = line.replaceAll("<DistrictName>", cell2.getStringCellValue());
			writer.write(line);
			writer.newLine();
		}

		writer.flush();
		writer.close();
	}
}
