package com.ashoksm.exceltoxml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

//1
public class CorrectDistrictNames {

	public static void main(String[] args) throws IOException {
		File file = new File("E:/Ashok/ifsc/IFCB2009_10.xls");
		FileInputStream inputStream = new FileInputStream(file);

		// Get the workbook instance for XLS file
		HSSFWorkbook workBook = new HSSFWorkbook(inputStream);

		// Get first sheet from the workbook
		HSSFSheet bankSheet = workBook.getSheetAt(0);
		// Iterate through each rows from first sheet
		Iterator<Row> rowIt = bankSheet.iterator();
		boolean flag = true;
		while (rowIt.hasNext()) {
			Row row = rowIt.next();
			if (flag) {
				flag = false;
				continue;
			}
			String district = row.getCell(7).getStringCellValue();
			String newDistrict = null;
			if (district.contains("DISTRICT")) {
				newDistrict = district.substring(district.indexOf("DISTRICT") + 8);
			} else if (district.contains("DIST.")) {
				newDistrict = district.substring(district.indexOf("DIST.") + 5);
			} else if (district.contains("DISTT.")) {
				newDistrict = district.substring(district.indexOf("DISTT.") + 6);
			} else if (district.contains("DISTT")) {
				newDistrict = district.substring(district.indexOf("DISTT") + 5);
			} else if (district.contains("DIST")) {
				newDistrict = district.substring(district.indexOf("DIST") + 4);
			} 
			if (newDistrict != null) {
				newDistrict = newDistrict.trim();
				if (newDistrict.contains(")") && !newDistrict.contains("(")) {
					newDistrict = newDistrict.replace(')', ' ').trim();
				}
				if(newDistrict.startsWith(".") || newDistrict.startsWith(":") || newDistrict.startsWith("-")) {
					newDistrict = newDistrict.substring(1).trim();
				}
				row.getCell(7).setCellValue(newDistrict);
			}
		}
		FileOutputStream out = new FileOutputStream(file);
		workBook.write(out);
		out.close();
		workBook.close();
	}

}
