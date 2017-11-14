package com.ashoksm.exceltoxml;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class DownloadIPC {

    public static void main(String[] args) throws IOException {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("IPC");
        //For firefox webdriver.gecko.driver
        //For chrome webdriver.chrome.driver
        System.setProperty("webdriver.chrome.driver",
                "/home/local/BSILIND/ashok/Documents/SeleniumDrivers/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(
                "http://www.advocatekhoj.com/library/bareacts/indianpenalcode/index.php?Title=Indian%20Penal%20Code,%201860");
        WebElement table = driver.findElement(By.className("MsoTableGrid"));
        List<WebElement> trs = table.findElements(By.tagName("tr"));
        int i = 0;
        for (WebElement tr : trs) {
            List<WebElement> tds = tr.findElements(By.tagName("td"));
            Row row = sheet.createRow(i);
            if (tds.size() == 1) {
                row.createCell(0).setCellValue(tds.get(0).getText());
            } else {
                row.createCell(0).setCellValue(tds.get(0).getText());
                row.createCell(1).setCellValue(tds.get(1).getText());
                try {
                    WebElement link = tds.get(1).findElement(By.tagName("a"));
                    row.createCell(2).setCellValue(link.getAttribute("href"));
                } catch (NoSuchElementException e) {
                    //Ignore this exception
                }
            }
            i++;
        }
        Row header = workbook.getSheetAt(0).getRow(0);
        for (int colNum = 0; colNum < header.getLastCellNum(); colNum++) {
            workbook.getSheetAt(0).autoSizeColumn(colNum);
        }
        FileOutputStream out = new FileOutputStream(new File("ipc.xls"));
        workbook.write(out);
        out.close();
        workbook.close();
        System.out.println("Excel written successfully..");
    }
}
