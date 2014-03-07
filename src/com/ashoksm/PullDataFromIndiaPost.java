package com.ashoksm;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class PullDataFromIndiaPost {

	public static void main(String[] args) throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Kanyakumari");
		WebDriver driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.get("http://utilities.cept.gov.in/pinsearch/pinsearch.aspx");
		driver.findElement(By.id("img2")).click();
		WebElement select = driver.findElement(By.id("ddl_dist"));
		List<WebElement> options = select.findElements(By.tagName("option"));
		for (WebElement option : options) {
			if (option.getText().equals("Kanyakumari")) {
				option.click();
				break;
			}
		}
		driver.findElement(By.id("btn_dist")).click();
		int index = 0;
		if (driver instanceof JavascriptExecutor) {
			for (int j = 0; j < 10; j++) {
				String js = "__doPostBack('gvw_offices','Select$" + j + "');";
				((JavascriptExecutor) driver).executeScript(js);
				WebElement table = driver.findElement(By.id("dvw_detail"));
				List<WebElement> trs = table.findElements(By.tagName("tr"));
				for (WebElement tr : trs) {
					List<WebElement> tds = tr.findElements(By.tagName("td"));
					if (!tds.get(0).getText().equalsIgnoreCase("Postal Department Information")
							&& !tds.get(0).getText().equalsIgnoreCase("SPCC")
							&& !tds.get(1).getText().equalsIgnoreCase("Not Available")) {
						HSSFRow officeNameRow = sheet.createRow(index);
						officeNameRow.createCell(0).setCellValue(tds.get(0).getText());
						officeNameRow.createCell(1).setCellValue(tds.get(1).getText());
						index++;
					}
				}
			}

			int temp = 0;
			for (int i = 2; i < 27; i++) {
				if (i > 11) {
					for (int j = 11; j <= i; j += 10) {
						((JavascriptExecutor) driver).executeScript("__doPostBack('gvw_offices','Page$" + j + "');");
						temp = j;
					}
				}

				if (i != temp) {
					String js1 = "__doPostBack('gvw_offices','Page$" + i + "');";
					((JavascriptExecutor) driver).executeScript(js1);
				}
				for (int j = 0; j < 10; j++) {
					String js = "__doPostBack('gvw_offices','Select$" + j + "');";
					((JavascriptExecutor) driver).executeScript(js);
					WebElement table = driver.findElement(By.id("dvw_detail"));
					List<WebElement> trs = table.findElements(By.tagName("tr"));
					for (WebElement tr : trs) {
						List<WebElement> tds = tr.findElements(By.tagName("td"));
						if (!tds.get(0).getText().equalsIgnoreCase("Postal Department Information")
								&& !tds.get(0).getText().equalsIgnoreCase("SPCC")
								&& !tds.get(1).getText().equalsIgnoreCase("Not Available")) {
							HSSFRow officeNameRow = sheet.createRow(index);
							officeNameRow.createCell(0).setCellValue(tds.get(0).getText());
							officeNameRow.createCell(1).setCellValue(tds.get(1).getText());
							index++;
						}
					}
				}
			}

			for (int j = 11; j <= 27; j += 10) {
				((JavascriptExecutor) driver).executeScript("__doPostBack('gvw_offices','Page$" + j + "');");
			}
			((JavascriptExecutor) driver).executeScript("__doPostBack('gvw_offices','Page$" + 27 + "');");
			for (int j = 0; j < 7; j++) {
				String js = "__doPostBack('gvw_offices','Select$" + j + "');";
				((JavascriptExecutor) driver).executeScript(js);
				WebElement table = driver.findElement(By.id("dvw_detail"));
				List<WebElement> trs = table.findElements(By.tagName("tr"));
				for (WebElement tr : trs) {
					List<WebElement> tds = tr.findElements(By.tagName("td"));
					if (!tds.get(0).getText().equalsIgnoreCase("Postal Department Information")
							&& !tds.get(0).getText().equalsIgnoreCase("SPCC")
							&& !tds.get(1).getText().equalsIgnoreCase("Not Available")) {
						HSSFRow officeNameRow = sheet.createRow(index);
						officeNameRow.createCell(0).setCellValue(tds.get(0).getText());
						officeNameRow.createCell(1).setCellValue(tds.get(1).getText());
						index++;
					}
				}
			}
		}
		FileOutputStream out = new FileOutputStream(new File("Kanyakumari.xls"));
		workbook.write(out);
		out.close();
		System.out.println("Excel written successfully..");
	}
}
