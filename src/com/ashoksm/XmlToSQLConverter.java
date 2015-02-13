package com.ashoksm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import com.thoughtworks.xstream.XStream;

public class XmlToSQLConverter {

	private static final String SQL = "INSERT INTO post_office_t VALUES('<OfficeName>', <Pincode>, <District>, <State>, <Status>, '<SubOffice>', '<HeadOffice>', <Location>, '<Telephone>');";

	private static final Map<String, String> DISTRICT_MAP = new HashMap<String, String>();

	private static final Map<String, String> LOCATION_MAP = new HashMap<String, String>();

	private static final Map<String, String> STATUS_MAP = new HashMap<String, String>();

	public static void main(String[] args) throws IOException {
		getDistrictCode();
		getLocationCode();
		getStatusCode();
		XStream xStream = new XStream();
		xStream.alias("office", Office.class);
		xStream.alias("offices", Offices.class);
		xStream.addImplicitCollection(Offices.class, "offices");
		File root = new File("xml");
		for (final File state : root.listFiles()) {
			if (!"ifsc".equals(state.getName())) {
				System.out.println(state.getName());
				BufferedWriter writer = new BufferedWriter(new FileWriter(args[0] + "\\"
						+ state.getName().substring(0, state.getName().lastIndexOf(".")) + ".sql"));
				FileInputStream file = new FileInputStream(state);
				Offices offices = (Offices) xStream.fromXML(file);
				for (Office office : offices.getOffices()) {
					String line = null;
					try {
						String districtName = office.getLocation().substring(
								office.getLocation().toLowerCase().indexOf("taluk of ") + 9,
								office.getLocation().toLowerCase().indexOf("district"));
						String stateCode = getStateCode(state.getName());
						line = SQL.replaceAll("<OfficeName>", office.getName().trim().replaceAll("'", "''"))
								.replaceAll("<Pincode>", office.getPinCode())
								.replaceAll("<Status>", STATUS_MAP.get(office.getStatus().trim()))
								.replaceAll("<HeadOffice>", office.getHeadoffice().trim().replaceAll("'", "''"))
								.replaceAll("<SubOffice>", office.getSuboffice().trim().replaceAll("'", "''"))
								.replaceAll("<Location>", LOCATION_MAP.get(office.getLocation().trim()))
								.replaceAll("<Telephone>", office.getTelephone().trim())
								.replaceAll("<State>", stateCode)
								.replaceAll("<District>", DISTRICT_MAP.get(districtName.trim()));
					} catch (Exception ex) {
						ex.printStackTrace();
						System.out.println("In exception ::::: " + office.getName() + "::::" + office.getLocation());
					}
					writer.write(line);
					writer.newLine();
				}
				writer.flush();
				writer.close();
			}
		}
	}

	private static String getStateCode(String xmlName) throws IOException {
		File stateExcel = new File("E:\\Ashok\\Dropbox\\MyDetails\\pinfinder\\states.xls");

		FileInputStream file = new FileInputStream(stateExcel);

		// Get the workbook instance for XLS file
		HSSFWorkbook workbook = new HSSFWorkbook(file);

		// Get first sheet from the workbook
		HSSFSheet sheet = workbook.getSheetAt(0);

		// Iterate through each rows from first sheet
		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			String stateName = row.getCell(1).getStringCellValue().replaceAll(" ", "").toLowerCase();
			if (xmlName.contains(stateName)) {
				return String.valueOf(new Double(row.getCell(0).getNumericCellValue()).intValue());
			}
		}
		return null;
	}

	private static void getDistrictCode() throws IOException {
		File root = new File("E:\\Ashok\\Dropbox\\MyDetails\\pinfinder\\Districts");
		for (final File district : root.listFiles()) {

			FileInputStream file = new FileInputStream(district);

			// Get the workbook instance for XLS file
			HSSFWorkbook workbook = new HSSFWorkbook(file);

			// Get first sheet from the workbook
			HSSFSheet sheet = workbook.getSheetAt(0);

			// Iterate through each rows from first sheet
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				String stateName = row.getCell(2).getStringCellValue().trim();
				DISTRICT_MAP
				.put(stateName, String.valueOf(new Double(row.getCell(1).getNumericCellValue()).intValue()));
			}
		}
	}

	private static void getLocationCode() throws IOException {
		File stateExcel = new File("E:\\Ashok\\Dropbox\\MyDetails\\pinfinder\\locations.xls");
		FileInputStream file = new FileInputStream(stateExcel);

		// Get the workbook instance for XLS file
		HSSFWorkbook workbook = new HSSFWorkbook(file);

		// Get first sheet from the workbook
		HSSFSheet sheet = workbook.getSheetAt(0);

		// Iterate through each rows from first sheet
		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			String stateName = row.getCell(1).getStringCellValue().trim();
			LOCATION_MAP.put(stateName, String.valueOf(new Double(row.getCell(0).getNumericCellValue()).intValue()));
		}
	}

	private static void getStatusCode() throws IOException {
		STATUS_MAP.put("Branch Office(Non Delivery)", "1");
		STATUS_MAP.put("Sub Office(Non Delivery)", "2");
		STATUS_MAP.put("Sub Office(Delivery)", "3");
		STATUS_MAP.put("Branch Office(Delivery) directly a/w Head Office", "4");
		STATUS_MAP.put("Head Office(Delivery)", "5");
		STATUS_MAP.put("Branch Office(Delivery)", "6");
		STATUS_MAP.put("Branch Office(Non Delivery) directly a/w Head Office", "7");
		STATUS_MAP.put("Head Office(Non Delivery)", "8");
	}
}