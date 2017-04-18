package com.ashoksm.exceltoxml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class DownloadTrains {

	private static int i = 0;
	private static HSSFWorkbook workbook = new HSSFWorkbook();
	private static HSSFSheet sheet = workbook.createSheet("Station");

	public static void main(String[] args) throws IOException {
		System.setProperty("webdriver.chrome.driver", "D:\\Download\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		driver.get("https://www.cleartrip.com/trains/list");
		processDataTable(driver);
		FileOutputStream out = new FileOutputStream(new File(
				"E:\\Ashok\\Dropbox\\MyDetails\\pinfinder\\railwaycodes\\trains.xls"));
		workbook.write(out);
		out.close();
	}

	/**
	 * @param driver
	 */
	private static void processDataTable(WebDriver driver) {
		WebElement table = driver.findElement(By.className("results"));
		WebElement tbody = table.findElement(By.tagName("tbody"));
		List<WebElement> trs = tbody.findElements(By.tagName("tr"));
		for (WebElement tr : trs) {
			List<WebElement> tds = tr.findElements(By.tagName("td"));
			HSSFRow stationRow = sheet.createRow(i);
			String trainNo = tds.get(0).getText(); 
			stationRow.createCell(0).setCellValue(trainNo);
			stationRow.createCell(1).setCellValue(tds.get(1).getText());
			stationRow.createCell(2).setCellValue(tds.get(2).getText());
			stationRow.createCell(3).setCellValue(tds.get(3).getText());
			i++;
			if (trainNo.equalsIgnoreCase("12163") || trainNo.equalsIgnoreCase("07125")
					|| trainNo.equalsIgnoreCase("13108") || trainNo.equalsIgnoreCase("18417")
					|| trainNo.equalsIgnoreCase("18048")) {
				driver.findElement(By.className("next_page")).click();
				processDataTable(driver);
			}
		}
	}

}