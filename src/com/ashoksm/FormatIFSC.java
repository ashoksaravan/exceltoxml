package com.ashoksm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

public class FormatIFSC {

	public static void main(String[] args) throws Exception {
		File root = new File(args[0]);
		for (final File bank : root.listFiles()) {
			FileInputStream file = new FileInputStream(bank);
			// Get the workbook instance for XLS file
			HSSFWorkbook workbook = new HSSFWorkbook(file);

			// Get first sheet from the workbook
			HSSFSheet sheet = workbook.getSheetAt(0);

			HSSFWorkbook workbookNew = new HSSFWorkbook();
			HSSFSheet sheetNew = workbookNew.createSheet("Sheet1");

			Iterator<Row> rowIterator = sheet.iterator();
			int i = 0;
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if (row.getRowNum() == 0) {
					continue;
				}
				HSSFRow branchRow = sheetNew.createRow(i);
				branchRow.createCell(0).setCellValue("Branch:");
				if (row.getCell(2) != null) {
					branchRow.createCell(1).setCellValue(row.getCell(2).getStringCellValue());
				} else {
					branchRow.createCell(1).setCellValue("");
				}
				i++;
				HSSFRow cityRow = sheetNew.createRow(i);
				cityRow.createCell(0).setCellValue("City:");
				if (row.getCell(5) != null) {
					cityRow.createCell(1).setCellValue(row.getCell(5).getStringCellValue());
				} else {
					cityRow.createCell(1).setCellValue("");
				}
				i++;
				HSSFRow districtRow = sheetNew.createRow(i);
				districtRow.createCell(0).setCellValue("District:");
				if (row.getCell(6) != null) {
					districtRow.createCell(1).setCellValue(row.getCell(6).getStringCellValue());
				} else {
					districtRow.createCell(1).setCellValue("");
				}
				i++;
				HSSFRow stateRow = sheetNew.createRow(i);
				stateRow.createCell(0).setCellValue("State:");
				stateRow.createCell(1).setCellValue(row.getCell(7).getStringCellValue());
				i++;
				HSSFRow addRow = sheetNew.createRow(i);
				addRow.createCell(0).setCellValue("Address:");
				if (row.getCell(3) != null) {
					addRow.createCell(1).setCellValue(row.getCell(3).getStringCellValue());
				} else {
					addRow.createCell(1).setCellValue("");
				}
				i++;
				HSSFRow contactRow = sheetNew.createRow(i);
				contactRow.createCell(0).setCellValue("Contact:");
				if (row.getCell(4) != null) {
					contactRow.createCell(1).setCellValue(row.getCell(4).getStringCellValue());
				} else {
					contactRow.createCell(1).setCellValue("");
				}
				i++;
				HSSFRow micrRow = sheetNew.createRow(i);
				micrRow.createCell(0).setCellValue("MICR Code:");
				if (row.getCell(1) != null) {
					micrRow.createCell(1).setCellValue(row.getCell(1).getStringCellValue());
				} else {
					micrRow.createCell(1).setCellValue("");
				}
				i++;
				HSSFRow ifscRow = sheetNew.createRow(i);
				ifscRow.createCell(0).setCellValue("IFSC Code:");
				if (row.getCell(0) != null) {
					ifscRow.createCell(1).setCellValue(row.getCell(0).getStringCellValue());
				} else {
					ifscRow.createCell(1).setCellValue("");
				}
				i++;
				if (row.getRowNum() != 0 && row.getRowNum() % 7500 == 0) {
					HSSFRow row1 = workbookNew.getSheetAt(0).getRow(0);
					for (int colNum = 0; colNum < row1.getLastCellNum(); colNum++) {
						workbookNew.getSheetAt(0).autoSizeColumn(colNum);
					}
					FileOutputStream out = new FileOutputStream(new File(args[1] + "\\"
							+ bank.getName().substring(0, bank.getName().lastIndexOf('.')) + (row.getRowNum() / 7500)
							+ ".xls"));
					workbookNew.write(out);
					out.close();
					System.out.println((row.getRowNum() / 7500) + " Excel written successfully..");
					i = 0;
					workbookNew = new HSSFWorkbook();
					sheetNew = workbookNew.createSheet("Sheet1");
				}
			}
			HSSFRow row = workbookNew.getSheetAt(0).getRow(0);
			for (int colNum = 0; colNum < row.getLastCellNum(); colNum++) {
				workbookNew.getSheetAt(0).autoSizeColumn(colNum);
			}
			FileOutputStream out = new FileOutputStream(new File(args[1] + "\\" + bank.getName()));
			workbookNew.write(out);
			out.close();
		}

	}

}
