package com.ashoksm.exceltoxml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import org.openqa.selenium.chrome.ChromeDriver;

public class DownloadTrainsPassingViaStation {

	public static void main(String[] args) throws Exception {
		System.setProperty("webdriver.chrome.driver", "D:\\Download\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		File bankNames = new File("E:\\Ashok\\Dropbox\\MyDetails\\pinfinder\\railwaycodes\\stationcodes.xls");
		FileInputStream file = new FileInputStream(bankNames);
		// Get the workbook instance for XLS file
		HSSFWorkbook bankWorkBook = new HSSFWorkbook(file);

		// Get first sheet from the workbook
		HSSFSheet banksheet = bankWorkBook.getSheetAt(0);
		// Iterate through each rows from first sheet
		Iterator<Row> rowIterator = banksheet.iterator();

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Trains");
		boolean flag = true;
		int i = 0;
		int j = 3;
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			if (flag) {
				flag = false;
				continue;
			}
			if(row.getRowNum() <= 2000) {
				continue;
			}
			String stationCode = row.getCell(0).getStringCellValue().trim();
			System.out.println(row.getRowNum() + "---" + stationCode);
			driver.get("https://www.cleartrip.com/trains/stations/" + stationCode);
			WebElement table = driver.findElements(By.className("results")).get(1);
			List<WebElement> trs = table.findElements(By.tagName("tr"));
			for (WebElement tr : trs) {
				List<WebElement> tds = tr.findElements(By.tagName("td"));
				HSSFRow trainRow = sheet.createRow(i);
				trainRow.createCell(0).setCellValue(stationCode);
				trainRow.createCell(1).setCellValue(tds.get(0).getText());
				trainRow.createCell(2).setCellValue(tds.get(1).getText());
				trainRow.createCell(3).setCellValue(tds.get(2).getText());
				trainRow.createCell(4).setCellValue(tds.get(3).getText());
				trainRow.createCell(5).setCellValue(tds.get(4).getText());
				trainRow.createCell(6).setCellValue(tds.get(5).getText());
				trainRow.createCell(7).setCellValue(tds.get(6).getText());
				trainRow.createCell(8).setCellValue(tds.get(7).getText());
				trainRow.createCell(9).setCellValue(tds.get(8).getText());
				trainRow.createCell(10).setCellValue(tds.get(9).getText());
				trainRow.createCell(11).setCellValue(tds.get(10).getText());
				i++;
			}
			if (row.getRowNum() != 0 && row.getRowNum() % 1000 == 0) {
				HSSFRow row1 = workbook.getSheetAt(0).getRow(0);
				for (int colNum = 0; colNum < row1.getLastCellNum(); colNum++) {
					workbook.getSheetAt(0).autoSizeColumn(colNum);
				}
				FileOutputStream out = new FileOutputStream(new File(
						"E:\\Ashok\\Dropbox\\MyDetails\\pinfinder\\railwaycodes\\trainspassingviastation_" + j
						+ ".xls"));
				workbook.write(out);
				out.close();
				System.out.println(j + " Excel written successfully..");
				i = 0;
				j++;
				workbook = new HSSFWorkbook();
				sheet = workbook.createSheet("Trains");
			}
		}
		FileOutputStream out = new FileOutputStream(new File(
				"E:\\Ashok\\Dropbox\\MyDetails\\pinfinder\\railwaycodes\\trainspassingviastation_" + j + ".xls"));
		workbook.write(out);
		out.close();
		workbook.close();
		bankWorkBook.close();
	}
}
