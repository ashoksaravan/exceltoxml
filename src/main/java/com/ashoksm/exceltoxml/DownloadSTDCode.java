package com.ashoksm.exceltoxml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DownloadSTDCode {

	public static void main(String[] args) throws IOException, InterruptedException {
		WebDriver driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		File std = new File("std.xls");
		FileInputStream file = new FileInputStream(std);
		// Get the workbook instance for XLS file
		HSSFWorkbook bankWorkBook = new HSSFWorkbook(file);

		// Get first sheet from the workbook
		HSSFSheet banksheet = bankWorkBook.getSheetAt(0);
		// Iterate through each rows from first sheet
		Iterator<Row> rowIterator = banksheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			String stateName = row.getCell(0).getStringCellValue().trim();
			Double countd = row.getCell(1).getNumericCellValue();
			int count = countd.intValue();
			System.out.println(stateName + ": " + count);
			driver.get("http://218.248.243.16/bsnl-web/viewCityCode.seam?cid=167555");
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet(stateName);

			WebElement select = driver.findElement(By.id("viewState:firstField"));
			List<WebElement> options = select.findElements(By.tagName("option"));
			for (WebElement option : options) {
				if (option.getText().equalsIgnoreCase(stateName.trim())) {
					option.click();
					break;
				}
			}
			driver.findElement(By.id("viewState:goButton")).click();
			int index = 0;
			for (int i = 0; i < count; i++) {
				WebElement table = driver.findElement(By.id("second:myTab"));
				WebElement tbody = table.findElement(By.tagName("tbody"));
				List<WebElement> trs = tbody.findElements(By.tagName("tr"));
				for (WebElement tr : trs) {
					List<WebElement> tds = tr.findElements(By.tagName("td"));
					HSSFRow bankRow = sheet.createRow(index);
					bankRow.createCell(0).setCellValue(tds.get(0).getText());
					bankRow.createCell(1).setCellValue(tds.get(1).getText());
					index++;
				}
				driver.findElement(By.id("j_id10:PGDOWNLink")).click();
				Thread.sleep(5000);
			}

			HSSFRow row1 = workbook.getSheetAt(0).getRow(0);
			for (int colNum = 0; colNum < row1.getLastCellNum(); colNum++) {
				workbook.getSheetAt(0).autoSizeColumn(colNum);
			}
			FileOutputStream out = new FileOutputStream(new File("E:\\Ashok\\Dropbox\\MyDetails\\pinfinder\\stdcodes\\"
					+ stateName.replaceAll(" ", "") + ".xls"));
			workbook.write(out);
			out.close();
			System.out.println("Excel written successfully..");
		}
	}
}
