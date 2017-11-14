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

public class DownloadIPCDetails {

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
                FileOutputStream out = new FileOutputStream(new File("ipc.xls"))) {

            // Get first sheet from the workbook
            Sheet sheet = workbook.getSheetAt(0);
            // Iterate through each rows from first sheet
            for (Row row : sheet) {
                Cell linkCell = row.getCell(2);
                Cell ipcCell = row.getCell(0);
                if (linkCell != null && linkCell.getCellType() != Cell.CELL_TYPE_BLANK && ipcCell != null &&
                        ipcCell.getCellType() != Cell.CELL_TYPE_BLANK) {
                    driver.get(linkCell.getStringCellValue());
                    WebElement contentContainer = driver.findElement(By.id("content_container"));
                    List<WebElement> paragraphs = contentContainer.findElements(By.tagName("p"));
                    StringBuilder content = null;
                    for (int i = 1; i < paragraphs.size(); i++) {
                        if (content != null) {
                            content.append("\n\n");
                            content.append(paragraphs.get(i).getText());
                        } else {
                            content = new StringBuilder(paragraphs.get(i).getText());
                        }
                    }
                    if (content != null) {
                        row.createCell(3).setCellValue(content.toString());
                    }
                }
            }

            workbook.write(out);
        }
    }
}
