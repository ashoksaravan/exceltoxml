package com.ashoksm.exceltoxml;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

//1
public class CorrectDistrictNames {

    public static void main(String[] args) throws IOException {
        File file = new File("E:/Ashok/ifsc/IFCB2009_10.xlsx");
        FileInputStream inputStream = new FileInputStream(file);

        // Get the workbook instance for XLS file
        Workbook workBook = new XSSFWorkbook(inputStream);

        // Get first sheet from the workbook
        Sheet bankSheet = workBook.getSheetAt(0);
        for (Row row : bankSheet) {
            if (row.getRowNum() == 0) {
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
                if (newDistrict.startsWith(".") || newDistrict.startsWith(":") || newDistrict.startsWith("-")) {
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
