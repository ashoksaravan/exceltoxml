package com.ashoksm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.thoughtworks.xstream.XStream;

public class NewXMLGenerator {

	public static void main(String[] args) {
		try {
			FileInputStream file = new FileInputStream(new File("idukki.xls"));
			List<Office> offices = new ArrayList<Office>();

			// Get the workbook instance for XLS file
			HSSFWorkbook workbook = new HSSFWorkbook(file);

			// Get first sheet from the workbook
			HSSFSheet sheet = workbook.getSheetAt(0);

			String previousColumn = null;
			Office office = null;

			// Iterate through each rows from first sheet
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				// For each row, iterate through each columns
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {

					Cell cell = cellIterator.next();
					String cellValue = "";
					if (cell.getCellType() == 1) {
						cellValue = cell.getStringCellValue();
					} else if (cell.getCellType() == 0) {
						cellValue = String.valueOf(cell.getNumericCellValue());
					}

					if ("OfficeName".equals(cellValue)) {
						office = new Office();
						offices.add(office);
					}

					if ("OfficeName".equalsIgnoreCase(previousColumn)) {
						office.setName(cellValue.replaceAll(" S.O", "").replaceAll(" B.O", "").replaceAll(" H.O", "")
								.replaceAll(" G.P.O", ""));
					} else if ("Pincode".equalsIgnoreCase(previousColumn)) {
						office.setPinCode(cellValue.substring(0, cellValue.lastIndexOf(".")));
					} else if ("status".equalsIgnoreCase(previousColumn)) {
						office.setStatus(cellValue);
					} else if ("SubOffice".equalsIgnoreCase(previousColumn)) {
						office.setSuboffice(cellValue.replaceAll(" S.O", ""));
					} else if ("HeadOffice".equalsIgnoreCase(previousColumn)) {
						office.setHeadoffice(cellValue.replaceAll(" H.O", "").replaceAll(" G.P.O", ""));
					} else if ("Location".equalsIgnoreCase(previousColumn)) {
						office.setLocation(cellValue);
					} else if ("Telephone".equalsIgnoreCase(previousColumn)) {
						office.setTelephone(cellValue);
					}
					previousColumn = cellValue;
				}
			}
			Offices off = new Offices();
			off.setOffices(offices);
			XStream xStream = new XStream();
			xStream.alias("office", Office.class);
			xStream.alias("offices", Offices.class);
			xStream.addImplicitCollection(Offices.class, "offices");
			String xml = xStream.toXML(off);
			xml = xml.replaceAll("<telephone></telephone>", "<telephone />")
					.replaceAll("<suboffice></suboffice>", "<suboffice />")
					.replaceAll("<headoffice></headoffice>", "<headoffice />");
			// System.out.println(xml);
			writeFile(xml);
			file.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeFile(String xml) {
		FileOutputStream fop = null;
		File file;

		try {

			file = new File("kerala_idukki.xml");
			fop = new FileOutputStream(file);

			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// get the content in bytes
			byte[] contentInBytes = xml.getBytes();

			fop.write(contentInBytes);
			fop.flush();
			fop.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
