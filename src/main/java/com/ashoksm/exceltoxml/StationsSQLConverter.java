package com.ashoksm.exceltoxml;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

public class StationsSQLConverter {

    private static final String SQL = "INSERT INTO stations_t VALUES('<StationCode>', '<StationName>', '<Location>',"
            + " <TrainsPassingThrough>, '<State>', '<City>');";

    public static void main(String[] args) throws Exception {
        FileInputStream file = new FileInputStream(
                new File("/home/local/BSILIND/ashok/Downloads/railwaycodes/stationcodes.xls"));
        HSSFWorkbook workbook = new HSSFWorkbook(file);
        HSSFSheet sheet = workbook.getSheetAt(0);
        BufferedWriter writer = new BufferedWriter(new FileWriter("/home/local/BSILIND/ashok/MySpace/OfflinePinFinder/"
                + "app/src/main/assets/sql/railway/stations.sql"));

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }
            String line = SQL.replaceAll("<StationCode>", row.getCell(0).getStringCellValue())
                    .replaceAll("<StationName>", row.getCell(1).getStringCellValue())
                    .replaceAll("<Location>", row.getCell(2).getStringCellValue())
                    .replaceAll("<TrainsPassingThrough>",
                            row.getCell(3).getStringCellValue().trim().length() > 0
                                    ? row.getCell(3).getStringCellValue() : "0")
                    .replaceAll("<City>", row.getCell(4).getStringCellValue())
                    .replaceAll("<State>", row.getCell(5).getStringCellValue());
            writer.write(line);
            writer.newLine();
        }
        writer.flush();
        writer.close();
        workbook.close();
        System.out.println("Completed!!!");
    }

}
