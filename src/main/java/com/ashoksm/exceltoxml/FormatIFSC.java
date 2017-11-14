package com.ashoksm.exceltoxml;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

//4
public class FormatIFSC {

    public static void main(String[] args) throws Exception {
        File root = new File(args[0]);
        for (final File bank : root.listFiles()) {
            FileInputStream file = new FileInputStream(bank);
            // Get the workbook instance
            Workbook workBook;
            Workbook workbookNew;
            if (bank.getName().toLowerCase().endsWith("xls")) {
                workBook = new HSSFWorkbook(file);
                workbookNew = new HSSFWorkbook();
            } else {
                workBook = new XSSFWorkbook(file);
                workbookNew = new XSSFWorkbook();
            }

            // Get first sheet from the workbook
            Sheet sheet = workBook.getSheetAt(0);
            Sheet sheetNew = workbookNew.createSheet("Sheet1");

            int i = 0;
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }
                Row branchRow = sheetNew.createRow(i);
                branchRow.createCell(0).setCellValue("Branch:");
                if (row.getCell(2) != null) {
                    branchRow.createCell(1).setCellValue(row.getCell(2).getStringCellValue());
                } else {
                    branchRow.createCell(1).setCellValue("");
                }
                i++;
                Row cityRow = sheetNew.createRow(i);
                cityRow.createCell(0).setCellValue("City:");
                if (row.getCell(5) != null) {
                    cityRow.createCell(1).setCellValue(row.getCell(5).getStringCellValue());
                } else {
                    cityRow.createCell(1).setCellValue("");
                }
                i++;
                Row districtRow = sheetNew.createRow(i);
                districtRow.createCell(0).setCellValue("District:");
                if (row.getCell(6) != null) {
                    districtRow.createCell(1).setCellValue(row.getCell(6).getStringCellValue());
                } else {
                    districtRow.createCell(1).setCellValue("");
                }
                i++;
                Row stateRow = sheetNew.createRow(i);
                stateRow.createCell(0).setCellValue("State:");
                stateRow.createCell(1).setCellValue(row.getCell(7).getStringCellValue());
                i++;
                Row addRow = sheetNew.createRow(i);
                addRow.createCell(0).setCellValue("Address:");
                if (row.getCell(3) != null) {
                    addRow.createCell(1).setCellValue(row.getCell(3).getStringCellValue());
                } else {
                    addRow.createCell(1).setCellValue("");
                }
                i++;
                Row contactRow = sheetNew.createRow(i);
                contactRow.createCell(0).setCellValue("Contact:");
                if (row.getCell(4) != null) {
                    contactRow.createCell(1).setCellValue(row.getCell(4).getStringCellValue());
                } else {
                    contactRow.createCell(1).setCellValue("");
                }
                i++;
                Row micrRow = sheetNew.createRow(i);
                micrRow.createCell(0).setCellValue("MICR Code:");
                if (row.getCell(1) != null) {
                    micrRow.createCell(1).setCellValue(row.getCell(1).getStringCellValue());
                } else {
                    micrRow.createCell(1).setCellValue("");
                }
                i++;
                Row ifscRow = sheetNew.createRow(i);
                ifscRow.createCell(0).setCellValue("IFSC Code:");
                if (row.getCell(0) != null) {
                    ifscRow.createCell(1).setCellValue(row.getCell(0).getStringCellValue());
                } else {
                    ifscRow.createCell(1).setCellValue("");
                }
                i++;
            }
            Row row = workbookNew.getSheetAt(0).getRow(0);
            for (int colNum = 0; colNum < row.getLastCellNum(); colNum++) {
                workbookNew.getSheetAt(0).autoSizeColumn(colNum);
            }
            FileOutputStream out = new FileOutputStream(new File(args[1] + "\\" + bank.getName()));
            workbookNew.write(out);
            out.close();
            workbookNew.close();
            workBook.close();
        }

    }

}
