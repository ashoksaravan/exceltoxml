package com.ashoksm.exceltoxml;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownloadIPCDetails {

    private static final String EXPLANATION = "Explanation";
    private static final String EXCEPTION = "Exception-";
    private static final String STATE_OF = "State of";

    public static void main(String[] args) throws IOException {
        //For firefox webdriver.gecko.driver
        //For chrome webdriver.chrome.driver
        System.setProperty("webdriver.chrome.driver",
                "/home/local/BSILIND/ashok/Documents/SeleniumDrivers/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        try (
                //load ipc
                FileInputStream file = new FileInputStream(new File("ipc.xls"));

                // Get the workbook instance for XLS file
                Workbook workbook = new HSSFWorkbook(file);
                FileOutputStream out = new FileOutputStream(new File("ipc.xls"));

                //Create a new excel for details
                // Get the workbook instance for XLS file
                Workbook details = new HSSFWorkbook();
                FileOutputStream detailsOut = new FileOutputStream(new File("ipc_details.xls"))) {

            // Get first sheet from the workbook
            Sheet sheet = workbook.getSheetAt(0);
            Sheet detailsSheet = details.createSheet();

            // Iterate through each rows from first sheet
            int rowNum = 0;
            for (Row row : sheet) {
                Cell linkCell = row.getCell(2);
                Cell ipcCell = row.getCell(0);
                if (linkCell != null && linkCell.getCellType() != Cell.CELL_TYPE_BLANK && ipcCell != null &&
                        ipcCell.getCellType() != Cell.CELL_TYPE_BLANK &&
                        ipcCell.getStringCellValue().trim().length() > 0) {
                    driver.get(linkCell.getStringCellValue());
                    WebElement contentContainer = driver.findElement(By.id("content_container"));
                    List<WebElement> paragraphs = contentContainer.findElements(By.tagName("p"));
                    StringBuilder content = getContent(paragraphs);
                    String header;
                    try {
                        header = getHeader(contentContainer.findElement(By.tagName("h5")));
                    } catch (Exception e) {
                        header = content.toString();
                    }
                    row.createCell(3).setCellValue("Y");
                    Row detail = detailsSheet.createRow(rowNum);
                    detail.createCell(0).setCellValue(ipcCell.getStringCellValue());
                    detail.createCell(1).setCellValue(header.trim());
                    detail.createCell(2).setCellValue(content.toString());
                    rowNum++;
                } else {
                    row.createCell(3).setCellValue("N");
                }
            }
            workbook.write(out);
            details.write(detailsOut);
        }


    }

    private static StringBuilder getContent(List<WebElement> paragraphs) {
        StringBuilder content = null;
        for (int i = 1; i < paragraphs.size(); i++) {
            String text = paragraphs.get(i).getText();

            if ((text.startsWith(EXPLANATION) && text.contains("-")) || text.contains(EXCEPTION)) {
                text = "<b>" + text.substring(0, text.indexOf("-")) + "</b>" + text.substring(text.indexOf("-"));
            } else if (text.startsWith(STATE_OF)) {
                text = "<b>" + text + "</b>";
            }

            String regEx = "[0-9]+[\\[]";
            Pattern pattern = Pattern.compile(regEx);
            Matcher matcher = pattern.matcher(text);

            while (matcher.find()) {
                String replacement = "<sub><u><small><font color='#1565C0'>" +
                        matcher.group().substring(0, matcher.group().length() - 1) + "</font></small></u></sub>[";
                text = matcher.replaceFirst(replacement);
                matcher = pattern.matcher(text);
            }
            if (content != null) {
                content.append("<br/><br/>");
                content.append(text);
            } else {
                content = new StringBuilder(text);
            }
        }
        return content;
    }

    private static String getHeader(WebElement h5) {
        String header = h5.getText();
        String regEx = "[0-9]+[\\[]";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(header);
        while (matcher.find()) {
            String replacement =
                    "<sub><u><small>" + matcher.group().substring(0, matcher.group().length() - 1) +
                            "</small></u></sub>[";
            header = matcher.replaceFirst(replacement);
            matcher = pattern.matcher(header);
        }
        return header;
    }
}
