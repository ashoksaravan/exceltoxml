package com.ashoksm.exceltoxml;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

//8
public class IfscBankLocGenerator {

    /**
     * SQL.
     */
    private static final String SQL = "INSERT INTO bank_loc_t VALUES(<LocNum>, '<BankName>', '<State>', '<District>');";

    public static void main(String[] args) throws Exception {
        File bankAddFile = new File(args[0] + "/BankBranchAddress.xlsx");
        FileInputStream file = new FileInputStream(bankAddFile);
        BufferedWriter writer = new BufferedWriter(new FileWriter(args[1] + "\\" + "banklocation.sql"));
        // Get the workbook instance for XLS file
        Workbook workbook = new XSSFWorkbook(file);

        // Get first sheet from the workbook
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            String line;
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
