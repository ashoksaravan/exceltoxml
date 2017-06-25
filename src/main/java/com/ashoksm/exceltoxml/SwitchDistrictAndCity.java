package com.ashoksm.exceltoxml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
//2
public class SwitchDistrictAndCity {

	public static void main(String[] args) throws Exception {
		File root = new File("E:\\Ashok\\ifsc");
		for (File file : root.listFiles()) {
			System.out.println(file.getName());
			FileInputStream inputStream = new FileInputStream(file);
			// Get the workbook instance for XLS file
			HSSFWorkbook workBook = new HSSFWorkbook(inputStream);

			// Get first sheet from the workbook
			HSSFSheet bankSheet = workBook.getSheetAt(0);
			// Iterate through each rows from first sheet
			Iterator<Row> rowIterator = bankSheet.iterator();
			Set<String> cities = new HashSet<>(); 
			Set<String> districts = new HashSet<>(); 
			boolean flag = true;
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if(flag) {
					flag = false;
					continue;
				}
				cities.add(row.getCell(6).getStringCellValue());
				districts.add(row.getCell(7).getStringCellValue());
			}
			if(districts.size() > cities.size()) {
				Iterator<Row> rowIt = bankSheet.iterator();
				flag = true;
				while (rowIt.hasNext()) {
					Row row = rowIt.next();
					if(flag) {
						flag = false;
						continue;
					}
					System.out.println(row.getCell(0).getStringCellValue());
					String city = row.getCell(7).getStringCellValue();
					String district = row.getCell(6).getStringCellValue();
					row.getCell(6).setCellValue(city);
					row.getCell(7).setCellValue(district);
				}
				FileOutputStream out = new FileOutputStream(file);
				workBook.write(out);
				out.close();
			}
			workBook.close();
		}
	}

}
