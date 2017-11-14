package com.ashoksm.exceltoxml;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UpdateStations {

    public static void main(String[] args) throws Exception {
        System.setProperty("webdriver.chrome.driver", "D:\\Download\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.manage().window().maximize();

        File bankNames = new File("E:\\Ashok\\Dropbox\\MyDetails\\pinfinder\\railwaycodes\\stationcodes.xls");
        FileInputStream file = new FileInputStream(bankNames);
        // Get the workbook instance for XLS file
        HSSFWorkbook workBook = new HSSFWorkbook(file);

        // Get first sheet from the workbook
        HSSFSheet stationSheet = workBook.getSheetAt(0);
        // Iterate through each rows from first sheet
        Iterator<Row> rowIterator = stationSheet.iterator();
        boolean flag = true;
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (flag) {
                flag = false;
                continue;
            }
            String stationCode = row.getCell(0).getStringCellValue().trim();
            System.out.println(row.getRowNum() + "---" + stationCode);
            driver.get("https://www.cleartrip.com/trains/stations/" + stationCode);
            String state = "";
            String city = "";
            WebElement ul = driver.findElement(By.className("summaryInfo"));
            List<WebElement> lis = ul.findElements(By.tagName("li"));
            for (WebElement webElement : lis) {
                if (webElement.getText().startsWith("City\n")) {
                    city = webElement.getText().replaceAll("City\n", "");
                }
                if (webElement.getText().startsWith("State\n")) {
                    state = webElement.getText().replaceAll("State\n", "");
                }
            }
            row.createCell(4).setCellValue(city);
            row.createCell(5).setCellValue(state);
        }
        FileOutputStream out = new FileOutputStream(new File(
                "E:\\Ashok\\Dropbox\\MyDetails\\pinfinder\\railwaycodes\\stationcodes.xls"));
        workBook.write(out);
        out.close();
        workBook.close();
    }
}
