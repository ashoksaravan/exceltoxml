package com.ashoksm.exceltoxml;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.Set;

//2
public class SwitchDistrictAndCity {

    public static void main(String[] args) throws Exception {
        File root = new File("E:\\Ashok\\ifsc");
        for (File file : root.listFiles()) {
            System.out.println(file.getName());
            FileInputStream inputStream = new FileInputStream(file);
            // Get the workbook instance
            Workbook workBook;
            if (file.getName().toLowerCase().endsWith("xls")) {
                workBook = new HSSFWorkbook(inputStream);
            } else {
                workBook = new XSSFWorkbook(inputStream);
            }

            // Get first sheet from the workbook
            Sheet bankSheet = workBook.getSheetAt(0);
            Set<String> cities = new HashSet<>();
            Set<String> districts = new HashSet<>();
            for (Row row : bankSheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }
                cities.add(row.getCell(6).getStringCellValue());
                districts.add(row.getCell(7).getStringCellValue());
            }
            if (districts.size() > cities.size()) {
                for (Row row : bankSheet) {
                    if (row.getRowNum() == 0) {
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
