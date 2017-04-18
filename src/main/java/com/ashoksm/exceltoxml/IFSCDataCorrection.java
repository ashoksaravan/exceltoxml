package com.ashoksm.exceltoxml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class IFSCDataCorrection {

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

			String bankName = null;
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if (row.getRowNum() == 0 || row.getCell(1) == null
						|| (row.getCell(0) != null && "".equals(row.getCell(0).getStringCellValue().trim()))) {
					if (row.getRowNum() == 0) {
						HSSFRow ifscRow = sheetNew.createRow(row.getRowNum());
						ifscRow.createCell(0).setCellValue("IFSC CODE");
						ifscRow.createCell(1).setCellValue("MICR CODE");
						ifscRow.createCell(2).setCellValue("BRANCH NAME");
						ifscRow.createCell(3).setCellValue("ADDRESS");
						ifscRow.createCell(4).setCellValue("CONTACT");
						ifscRow.createCell(5).setCellValue("CITY");
						ifscRow.createCell(6).setCellValue("DISTRICT");
						ifscRow.createCell(7).setCellValue("STATE");
					}
					continue;
				}
				if (bankName == null) {
					bankName = formatBankName(getCellValue(row, 0)).replaceAll(" ", "").trim();
				}
				HSSFRow ifscRow = sheetNew.createRow(row.getRowNum());
				ifscRow.createCell(0).setCellValue(getCellValue(row, 1));
				ifscRow.createCell(1).setCellValue(getCellValue(row, 2));
				ifscRow.createCell(2).setCellValue(formatString(formatString(getCellValue(row, 3), ","), " "));
				ifscRow.createCell(3).setCellValue(formatString(formatString(getCellValue(row, 4), ","), " "));
				ifscRow.createCell(4).setCellValue(formatString(formatString(getCellValue(row, 5), ","), " "));
				ifscRow.createCell(5).setCellValue(formatString(formatString(getCellValue(row, 6), ","), " "));
				ifscRow.createCell(6).setCellValue(formatString(formatString(getCellValue(row, 7), ","), " "));
				ifscRow.createCell(7).setCellValue(formatString(formatString(getCellValue(row, 8), ","), " "));
			}

			HSSFRow row = workbookNew.getSheetAt(0).getRow(0);
			for (int colNum = 0; colNum < row.getLastCellNum(); colNum++) {
				workbookNew.getSheetAt(0).autoSizeColumn(colNum);
			}
			FileOutputStream out = new FileOutputStream(new File(args[1] + "\\" + bankName + ".xls"));
			workbookNew.write(out);
			out.close();
			workbookNew.close();
			workbook.close();
			System.out.println(bankName);
		}
	}

	/**
	 * @param row
	 * @return
	 */
	private static String getCellValue(Row row, int cellNum) {
		String cellValue = "";
		if (row.getCell(cellNum) != null && row.getCell(cellNum).getCellType() == Cell.CELL_TYPE_STRING) {
			cellValue = row.getCell(cellNum).getStringCellValue();
		} else if (row.getCell(cellNum) != null && row.getCell(cellNum).getCellType() == Cell.CELL_TYPE_NUMERIC) {
			cellValue = String.valueOf(new Double(row.getCell(cellNum).getNumericCellValue()).intValue());
		}
		return cellValue;
	}

	private static String formatString(String orginal, String delimiter) {
		if (orginal.length() > 1) {
			String[] temp = orginal.split(delimiter);
			StringBuffer sb = new StringBuffer();
			for (String cat : temp) {
				if (cat.length() > 1) {
					String formated = null;
					if (cat.contains("@")) {
						formated = cat.toLowerCase();
					} else if (!cat.equals("P.O.") && cat.length() > 3 && !cat.contains(".")) {
						formated = StringUtils.capitalize(cat.toLowerCase());
					} else {
						formated = cat.toUpperCase();
					}
					sb.append(formated.trim());
					sb.append(delimiter);
					if(",".equals(delimiter)) {
						sb.append(" ");
					}
				} else {
					sb.append(cat.toUpperCase());
					sb.append(delimiter);
				}
			}
			String s = sb.toString().replaceAll("\\(e\\)", "(E)").replaceAll("\\(w\\)", "(W)")
					.replaceAll("\\(s\\)", "\\(S\\)").replaceAll("\\(n\\)", "\\(N\\)")
					.replaceAll("\\(west\\)", "(WEST)").replaceAll("\\(north\\)", "(NORTH)")
					.replaceAll("\\(south\\)", "(SOUTH)").replaceAll("\\(east\\)", "(EAST)").trim();
			return s.endsWith(delimiter) ? s.substring(0, s.length()-1) : s;
		} else {
			return orginal.toUpperCase();
		}
	}

	private static String formatBankName(String orginal) {
		String[] temp = orginal.split(" ");
		StringBuffer sb = new StringBuffer();
		for (String cat : temp) {
			if ("Cooperative".equalsIgnoreCase(cat)) {
				sb.append("Co-Op");
			} else if ("Limited".equalsIgnoreCase(cat)) {
				sb.append("Ltd");
			} else {
				sb.append(StringUtils.capitalize(cat.toLowerCase()));
			}
		}
		return sb.toString();
	}
}