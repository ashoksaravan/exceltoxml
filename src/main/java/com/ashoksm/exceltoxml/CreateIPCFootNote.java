package com.ashoksm.exceltoxml;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class CreateIPCFootNote {

    @SuppressWarnings("ConstantConditions")
    public static void main(String[] args) throws IOException {

        try (// Get the workbook instance for XLS file
             Workbook workbook = new HSSFWorkbook();
             FileOutputStream out = new FileOutputStream(new File("ipc_foot_note.xls"))) {

            Sheet sheet = workbook.createSheet("Foot Note");
            List<String> lines = Files.readAllLines(new File("foot_notes").toPath());
            int rowNum = 0;
            String ipc = null;
            StringBuilder footNote = null;
            for (String line : lines) {
                if (line.trim().length() > 0 && Character.isDigit(line.charAt(0))) {
                    if (ipc != null && footNote != null) {
                        Row row = sheet.createRow(rowNum);
                        row.createCell(0).setCellValue(ipc);
                        row.createCell(1).setCellValue(footNote.toString());
                        rowNum++;
                    }
                    ipc = line.substring(0, line.indexOf(".")).trim();
                    footNote = new StringBuilder(line.substring(line.indexOf(".") + 1, line.length()).trim());
                } else {
                    footNote.append(" ").append(line.trim());
                }
            }
            workbook.write(out);
        }
    }
}
