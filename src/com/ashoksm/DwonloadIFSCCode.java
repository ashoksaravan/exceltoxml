package com.ashoksm;

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

public class DwonloadIFSCCode {

	public static void main(String[] args) throws Exception {
		System.setProperty("webdriver.chrome.driver", "D:\\Download\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		File bankNames = new File("banks.xls");
		FileInputStream file = new FileInputStream(bankNames);
		// Get the workbook instance for XLS file
		HSSFWorkbook bankWorkBook = new HSSFWorkbook(file);

		// Get first sheet from the workbook
		HSSFSheet banksheet = bankWorkBook.getSheetAt(0);
		// Iterate through each rows from first sheet
		Iterator<Row> rowIterator = banksheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			String bankName = row.getCell(0).getStringCellValue().trim();
			Double countd = row.getCell(1).getNumericCellValue();
			int count = countd.intValue();
			System.out.println(bankName + ": " + count);
			driver.get("http://www.bankbranchin.com/");
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet(bankName);

			WebElement select = driver.findElement(By.id("BC_ddlBank"));
			List<WebElement> options = select.findElements(By.tagName("option"));
			for (WebElement option : options) {
				if (option.getText().equalsIgnoreCase(bankName.trim())) {
					option.click();
					break;
				}
			}

			int index = 0;
			for (int i = 0; i < count; i++) {
				int j = 0;
				List<WebElement> tr_collection = driver.findElements(By.xpath("id('BC_GridView1')/tbody/tr"));
				for (WebElement tr : tr_collection) {
					if (j == 5) {
						break;
					}
					WebElement leftDiv = tr.findElement(By.className("floatLEFT"));

					WebElement table1 = leftDiv.findElement(By.tagName("table"));
					List<WebElement> innerTrs = table1.findElements(By.tagName("tr"));
					writeData(sheet, index, innerTrs.get(1));
					index++;
					writeData(sheet, index, innerTrs.get(4));
					index++;
					writeData(sheet, index, innerTrs.get(5));
					index++;
					writeData(sheet, index, innerTrs.get(6));
					index++;

					WebElement rightDiv = tr.findElement(By.className("floatRIGHT"));
					WebElement table2 = rightDiv.findElement(By.tagName("table"));
					innerTrs = table2.findElements(By.tagName("tr"));
					writeData(sheet, index, innerTrs.get(1));
					index++;
					writeData(sheet, index, innerTrs.get(2));
					index++;
					writeData(sheet, index, innerTrs.get(3));
					index++;
					writeData(sheet, index, innerTrs.get(6));
					index++;
					j++;
				}
				if (i != 0 && i % 1500 == 0) {
					HSSFRow row1 = workbook.getSheetAt(0).getRow(0);
					for (int colNum = 0; colNum < row1.getLastCellNum(); colNum++) {
						workbook.getSheetAt(0).autoSizeColumn(colNum);
					}
					FileOutputStream out = new FileOutputStream(
							new File("E:\\Ashok\\Dropbox\\MyDetails\\pinfinder\\ifsccodes\\"
									+ bankName.replaceAll(" ", "") + (i / 1500) + ".xls"));
					workbook.write(out);
					out.close();
					System.out.println((i / 1500) + " Excel written successfully..");
					index = 0;
					workbook = new HSSFWorkbook();
					sheet = workbook.createSheet(bankName);
				}
				if (count > 1) {
					driver.findElement(By.id("BC_GridView1_GridViewPager1_ImageButtonNext")).click();
				}
			}

			HSSFRow row1 = workbook.getSheetAt(0).getRow(0);
			for (int colNum = 0; colNum < row1.getLastCellNum(); colNum++) {
				workbook.getSheetAt(0).autoSizeColumn(colNum);
			}
			FileOutputStream out = new FileOutputStream(new File(
					"E:\\Ashok\\Dropbox\\MyDetails\\pinfinder\\ifsccodes\\" + bankName.replaceAll(" ", "") + ".xls"));
			workbook.write(out);
			out.close();
			System.out.println("Excel written successfully..");
		}
	}

	private static void writeData(HSSFSheet sheet, int index, WebElement innerTr) {
		List<WebElement> tds = innerTr.findElements(By.tagName("td"));
		HSSFRow bankRow = sheet.createRow(index);
		bankRow.createCell(0).setCellValue(tds.get(0).getText());
		bankRow.createCell(1).setCellValue(tds.get(1).getText());
	}
}
