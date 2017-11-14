package com.ashoksm.exceltoxml;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

//3
public class IFSCDataCorrection {

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

            String bankName = null;
            for (Row row : sheet) {
                if (row.getRowNum() == 0 || row.getCell(1) == null
                        || (row.getCell(0) != null && "".equals(row.getCell(0).getStringCellValue().trim()))) {
                    if (row.getRowNum() == 0) {
                        Row ifscRow = sheetNew.createRow(row.getRowNum());
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
                Row ifscRow = sheetNew.createRow(row.getRowNum());
                ifscRow.createCell(0).setCellValue(getCellValue(row, 1));
                ifscRow.createCell(1).setCellValue(getCellValue(row, 2));
                ifscRow.createCell(2).setCellValue(formatString(formatString(getCellValue(row, 3), ","), " "));
                ifscRow.createCell(3).setCellValue(formatString(formatString(getCellValue(row, 4), ","), " "));
                ifscRow.createCell(4).setCellValue(formatString(formatString(getCellValue(row, 5), ","), " "));
                ifscRow.createCell(5).setCellValue(formatString(formatString(getCellValue(row, 6), ","), " "));
                ifscRow.createCell(6).setCellValue(formatString(formatString(getCellValue(row, 7), ","), " "));
                ifscRow.createCell(7).setCellValue(formatString(formatString(getCellValue(row, 8), ","), " "));
            }

            Row row = workbookNew.getSheetAt(0).getRow(0);
            for (int colNum = 0; colNum < row.getLastCellNum(); colNum++) {
                workbookNew.getSheetAt(0).autoSizeColumn(colNum);
            }
            FileOutputStream out = new FileOutputStream(new File(
                    args[1] + "\\" + bankName + (bank.getName().toLowerCase().endsWith("xls") ? ".xls" : ".xlsx")));
            workbookNew.write(out);
            out.close();
            workbookNew.close();
            workBook.close();
            System.out.println(bankName);
        }
    }

    /**
     * @param row Row
     * @return cellValue
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

    private static String formatString(String original, String delimiter) {
        if (original.length() > 1) {
            String[] temp = original.split(delimiter);
            StringBuilder sb = new StringBuilder();
            for (String cat : temp) {
                if (cat.length() > 1) {
                    String formatted;
                    if (cat.contains("@")) {
                        formatted = cat.toLowerCase();
                    } else if (!cat.equals("P.O.") && cat.length() > 3 && !cat.contains(".")) {
                        formatted = StringUtils.capitalize(cat.toLowerCase());
                    } else {
                        formatted = cat.toUpperCase();
                    }
                    sb.append(formatted.trim());
                    sb.append(delimiter);
                    if (",".equals(delimiter)) {
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
            return s.endsWith(delimiter) ? s.substring(0, s.length() - 1) : s;
        } else {
            return original.toUpperCase();
        }
    }

    private static String formatBankName(String original) {
        String[] temp = original.split(" ");
        StringBuilder sb = new StringBuilder();
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
