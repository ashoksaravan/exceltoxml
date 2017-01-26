package com.ashoksm.exceltoxml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

public class PincodeSQLGenerator {

	private static final String SQL = "INSERT INTO post_office_t VALUES('<OfficeName>', <Pincode>, <District>, <State>, <Status>, '<SubOffice>', '<HeadOffice>', <Location>, '<Telephone>');";

	private static final Map<String, String> DISTRICT_MAP = new HashMap<>();

	private static final Map<String, String> LOCATION_MAP = new HashMap<>();

	private static final Map<String, String> STATUS_MAP = new HashMap<>();

	private static final Map<String, String> STATE_MAP = new HashMap<>();

	public static void main(String[] args) throws Exception {
		getStateCode();
		getDistrictCode();
		getLocationCode();
		getStatusCode();
		ICsvBeanReader beanReader = null;
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(
					"E:\\Android\\workspace\\OfflinePinFinder\\app\\src\\main\\assets\\sql\\pincode\\pincode_1.sql"));

			beanReader = new CsvBeanReader(new FileReader(
					"D:\\Download\\all_india_PO_list_without_APS_offices_ver2.csv"), CsvPreference.STANDARD_PREFERENCE);

			// the header elements are used to map the values to the bean (names
			// must match)
			final String[] header = beanReader.getHeader(true);
			final CellProcessor[] processors = getProcessors();
			// CsvListReader)
			PostOfficeModel officeModel;
			int i = 1;
			while ((officeModel = beanReader.read(PostOfficeModel.class, header, processors)) != null) {
				if (i % 9000 == 0) {
					writer.flush();
					writer.close();
					writer = new BufferedWriter(new FileWriter(
							"E:\\Android\\workspace\\OfflinePinFinder\\app\\src\\main\\assets\\sql\\pincode\\pincode_"
									+ (i / 9000 + 1) + ".sql"));
				}
				if (!"Null".equalsIgnoreCase(officeModel.getStateName())) {
					String line = null;
					try {
						line = SQL
								.replaceAll(
										"<OfficeName>",
										officeModel.getOfficeName().replaceAll(" S.O", "").replaceAll(" B.O", "")
										.replaceAll(" H.O", "").replaceAll(" G.P.O", "").trim()
										.replaceAll("'", "''"))
										.replaceAll("<Pincode>", officeModel.getPincode())
										.replaceAll(
												"<Status>",
												STATUS_MAP.get(officeModel.getOfficeType().trim()
														+ officeModel.getDeliveryStatus().trim()))
														.replaceAll(
																"<HeadOffice>",
																officeModel.getRelatedHeadoffice().equalsIgnoreCase("NA") ? "" : officeModel
																		.getRelatedHeadoffice().replaceAll(" H.O", "").replaceAll(" G.P.O", "")
																		.trim().replaceAll("'", "''"))
																		.replaceAll(
																				"<SubOffice>",
																				officeModel.getRelatedSuboffice().equalsIgnoreCase("NA") ? "" : officeModel
																						.getRelatedSuboffice().replaceAll(" S.O", "").trim()
																						.replaceAll("'", "''"))
																						.replaceAll(
																								"<Location>",
																								LOCATION_MAP.get(officeModel.getTaluk().replaceAll("\\*", "").replaceAll("\\\\+","").trim()
																										.toLowerCase()))
																										.replaceAll(
																												"<Telephone>",
																												officeModel.getTelephone().equalsIgnoreCase("NA") ? "" : officeModel
																														.getTelephone().trim())
																														.replaceAll("<State>", STATE_MAP.get(formatString(officeModel.getStateName())))
																														.replaceAll("<District>", DISTRICT_MAP.get(formatString(officeModel.getDistrictName())));
					} catch (Exception ex) {
						ex.printStackTrace();
						System.out.println(officeModel.getOfficeName());
					}
					writer.write(line);
					writer.newLine();
				}
				i++;
			}

			writer.flush();
			writer.close();
		} finally {
			if (beanReader != null) {
				beanReader.close();
			}
		}
	}

	private static CellProcessor[] getProcessors() {
		final CellProcessor[] processors = new CellProcessor[] { new NotNull(), new NotNull(), new NotNull(),
				new NotNull(), new NotNull(), new NotNull(), new NotNull(), new NotNull(), new NotNull(),
				new NotNull(), new NotNull(), new NotNull(), new NotNull() };

		return processors;
	}

	private static void getStatusCode() {
		STATUS_MAP.put("B.ONon-Delivery", "1");
		STATUS_MAP.put("S.ONon-Delivery", "2");
		STATUS_MAP.put("S.ODelivery", "3");
		STATUS_MAP.put("B.O directly a/w Head OfficeDelivery", "4");
		STATUS_MAP.put("H.ODelivery", "5");
		STATUS_MAP.put("B.ODelivery", "6");
		STATUS_MAP.put("B.O directly a/w Head OfficeNon-Delivery", "7");
		STATUS_MAP.put("H.ONon-Delivery", "8");
	}

	private static void getDistrictCode() throws IOException {
		File root = new File("E:\\Ashok\\Dropbox\\MyDetails\\pinfinder\\district.xls");
		FileInputStream file = new FileInputStream(root);

		// Get the workbook instance for XLS file
		HSSFWorkbook workbook = new HSSFWorkbook(file);

		// Get first sheet from the workbook
		HSSFSheet sheet = workbook.getSheetAt(0);

		// Iterate through each rows from first sheet
		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			String districtName = row.getCell(2).getStringCellValue().trim();
			DISTRICT_MAP.put(districtName, String.valueOf(new Double(row.getCell(1).getNumericCellValue()).intValue()));
		}
	}

	private static void getLocationCode() throws IOException {
		File stateExcel = new File("E:\\Ashok\\Dropbox\\MyDetails\\pinfinder\\taluk.xls");
		FileInputStream file = new FileInputStream(stateExcel);

		// Get the workbook instance for XLS file
		HSSFWorkbook workbook = new HSSFWorkbook(file);

		// Get first sheet from the workbook
		HSSFSheet sheet = workbook.getSheetAt(0);

		// Iterate through each rows from first sheet
		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			String taluk = row.getCell(1).getStringCellValue().trim().toLowerCase();
			LOCATION_MAP.put(taluk, String.valueOf(new Double(row.getCell(0).getNumericCellValue()).intValue()));
		}
	}

	private static void getStateCode() throws IOException {
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
			String stateName = row.getCell(1).getStringCellValue().trim();
			STATE_MAP.put(stateName, String.valueOf(new Double(row.getCell(0).getNumericCellValue()).intValue()));
		}
	}

	private static String formatString(String name) {
		String[] temp = name.toLowerCase().split(" ");
		StringBuilder sb = new StringBuilder();
		for (String string : temp) {
			if (string.contains("(")) {
				string = string.substring(0, string.indexOf("("))
						+ string.substring(string.indexOf("("), string.indexOf(")")).toUpperCase()
						+ string.substring(string.indexOf(")"));
			}
			sb.append(StringUtils.capitalize(string));
			sb.append(" ");
		}
		return sb.toString().trim();
	}
}
