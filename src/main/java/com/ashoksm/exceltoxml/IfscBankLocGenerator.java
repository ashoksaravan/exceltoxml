package com.ashoksm.exceltoxml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class IfscBankLocGenerator {

	/** SQL. */
	private static final String SQL = "INSERT INTO bank_loc_t VALUES(<LocNum>, '<BankName>', '<State>', '<District>');";

	public static void main(String[] args) throws Exception {
		File bankAddFile = new File(args[0] + "/BankBranchAddress_1.xls");
		FileInputStream file = new FileInputStream(bankAddFile);
		BufferedWriter writer = new BufferedWriter(new FileWriter(args[1] + "\\" + "banklocation.sql"));
		// Get the workbook instance for XLS file
		HSSFWorkbook workbook = new HSSFWorkbook(file);

		// Get first sheet from the workbook
		HSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			String line = null;
			Row row = rowIterator.next();
			if (row.getCell(0).getCellType() == Cell.CELL_TYPE_NUMERIC) {
				Double value = row.getCell(0).getNumericCellValue();
				line = SQL.replaceAll("<LocNum>", String.valueOf(value.intValue()))
						.replaceAll("<BankName>", row.getCell(1).getStringCellValue().replaceAll("'", "''"))
						.replaceAll("<State>", row.getCell(2).getStringCellValue().replaceAll("'", "''"))
						.replaceAll("<District>", row.getCell(3).getStringCellValue().replaceAll("'", "''"));
				writer.write(line);
				writer.newLine();
			}

		}
		writer.flush();
		writer.close();
		workbook.close();
	}
}
