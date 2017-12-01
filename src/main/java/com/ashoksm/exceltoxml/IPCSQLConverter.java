package com.ashoksm.exceltoxml;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

public class IPCSQLConverter {

    private static final String FOOT_NOTE_SQL = "INSERT INTO foot_notes VALUES ('<ID>', '<FOOT_NOTE>')";
    private static final String IPC_SQL = "INSERT INTO ipc VALUES ('<ID>', '<PARTICULARS>', '<DESCRIPTION>')";
    private static final String IPC_DETAILS_SQL =
            "INSERT INTO ipc_details VALUES ('<ID>', '<HEADER>', '<DESCRIPTION>')";
    private static final String REGEX = "'";
    private static final String REPLACEMENT = "''";

    public static void main(String[] args) throws IOException {
        createIPC();
        createIPCDetails();
        createFootNotes();
    }

    private static void createFootNotes() throws IOException {
        try (Workbook workbook = new HSSFWorkbook(new FileInputStream("ipc_foot_note.xls"))) {
            Sheet sheet = workbook.getSheetAt(0);
            StringBuilder sb = null;
            for (Row row : sheet) {
                if (sb == null) {
                    sb = new StringBuilder(FOOT_NOTE_SQL.replaceAll("<ID>", row.getCell(0).getStringCellValue())
                            .replaceAll("<FOOT_NOTE>", row.getCell(1).getStringCellValue()));
                } else {
                    sb.append("\n");
                    sb.append(FOOT_NOTE_SQL.replaceAll("<ID>", row.getCell(0).getStringCellValue())
                            .replaceAll("<FOOT_NOTE>",
                                    row.getCell(1).getStringCellValue().replaceAll(REGEX, REPLACEMENT).trim()));
                }
            }

            if (sb != null) {
                Files.write(new File("foot_notes.sql").toPath(), sb.toString().getBytes());
            }
        }
    }

    private static void createIPC() throws IOException {
        try (Workbook workbook = new HSSFWorkbook(new FileInputStream("ipc.xls"))) {
            Sheet sheet = workbook.getSheetAt(0);
            StringBuilder sb = null;
            for (Row row : sheet) {
                Cell partCell = row.getCell(1);
                Cell descCell = row.getCell(3);
                String particulars =
                        partCell != null ? partCell.getStringCellValue().replaceAll(REGEX, REPLACEMENT).trim() : "";
                String description =
                        descCell != null ? descCell.getStringCellValue().replaceAll(REGEX, REPLACEMENT).trim() : "";
                String sql = IPC_SQL.replaceAll("<ID>", row.getCell(0).getStringCellValue())
                        .replaceAll("<PARTICULARS>", particulars)
                        .replaceAll("<DESCRIPTION>", description);
                if (sb == null) {
                    sb = new StringBuilder(sql);
                } else {
                    sb.append("\n");
                    sb.append(sql);
                }
            }

            if (sb != null) {
                Files.write(new File("ipc.sql").toPath(), sb.toString().getBytes());
            }
        }
    }

    private static void createIPCDetails() throws IOException {
        try (Workbook workbook = new HSSFWorkbook(new FileInputStream("ipc_details.xls"))) {
            Sheet sheet = workbook.getSheetAt(0);
            StringBuilder sb = null;
            for (Row row : sheet) {
                Cell partCell = row.getCell(1);
                Cell descCell = row.getCell(2);
                String particulars =
                        partCell != null ? partCell.getStringCellValue().replaceAll(REGEX, REPLACEMENT).trim() : "";
                String description =
                        descCell != null ? descCell.getStringCellValue().replaceAll(REGEX, REPLACEMENT).trim() : "";
                String sql = IPC_DETAILS_SQL.replaceAll("<ID>", row.getCell(0).getStringCellValue())
                        .replaceAll("<HEADER>", particulars)
                        .replaceAll("<DESCRIPTION>", description);
                if (sb == null) {
                    sb = new StringBuilder(sql);
                } else {
                    sb.append("\n");
                    sb.append(sql);
                }
            }

            if (sb != null) {
                Files.write(new File("ipc_details.sql").toPath(), sb.toString().getBytes());
            }
        }
    }
}
