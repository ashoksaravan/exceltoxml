package com.ashoksm.exceltoxml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class UpdateTrains {

	public static void main(String[] args) throws Exception {
		System.setProperty("webdriver.chrome.driver", "D:\\Download\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		File bankNames = new File("E:\\Ashok\\Dropbox\\MyDetails\\pinfinder\\railwaycodes\\trains.xls");
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
			driver.get("https://www.cleartrip.com/trains/" + stationCode);
			String days = "";
			String pantry = "N";
			try {
				WebElement ul = driver.findElement(By.className("summaryInfo"));
				List<WebElement> lis = ul.findElements(By.tagName("li"));
				for (WebElement webElement : lis) {
					if (webElement.getText().startsWith("Days\n")) {
						pantry = webElement.getText().replaceAll("Days\n", "");
					}
					if (webElement.getText().startsWith("Pantry\n")) {
						days = webElement.getText().replaceAll("Pantry\n", "");
					}
				}
			} catch (Exception ex) {
				System.out.println(stationCode +" Failed");
			}
			row.createCell(4).setCellValue(pantry);
			row.createCell(5).setCellValue(days);
		}
		FileOutputStream out = new FileOutputStream(
				new File("E:\\Ashok\\Dropbox\\MyDetails\\pinfinder\\railwaycodes\\trains.xls"));
		workBook.write(out);
		out.close();
		workBook.close();
	}
}
