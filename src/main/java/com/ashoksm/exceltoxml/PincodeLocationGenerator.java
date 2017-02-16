package com.ashoksm.exceltoxml;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

public class PincodeLocationGenerator {

	public static void main(String[] args) throws IOException {
		ICsvBeanReader beanReader = null;
		try {
			beanReader = new CsvBeanReader(new FileReader(
					"D:\\Download\\all_india_PO_list_without_APS_offices_ver2.csv"), CsvPreference.STANDARD_PREFERENCE);

			// the header elements are used to map the values to the bean (names
			// must match)
			final String[] header = beanReader.getHeader(true);
			final CellProcessor[] processors = getProcessors();
			// CsvListReader)
			Set<String> taluks = new TreeSet<>();
			Set<String> temp = new TreeSet<>();
			Map<String, Set<String>> stateMap = new TreeMap<>();

			PostOfficeModel officeModel;
			while ((officeModel = beanReader.read(PostOfficeModel.class, header, processors)) != null) {
				if (!temp.contains(officeModel.getTaluk().toLowerCase().replaceAll("\\*", "").replaceAll("\\\\+","").trim())) {
					taluks.add(officeModel.getTaluk().replaceAll("\\*", "").replaceAll("\\\\+","").trim());
				}
				temp.add(officeModel.getTaluk().toLowerCase().replaceAll("\\*", "").replaceAll("\\\\+","").trim());
				String stateName = formatString(officeModel.getStateName());
				String districtName = formatString(officeModel.getDistrictName());
				if (stateMap.containsKey(stateName)) {
					Set<String> districts = stateMap.get(stateName);
					districts.add(districtName);
				} else {
					Set<String> districts = new TreeSet<>();
					districts.add(districtName);
					stateMap.put(stateName, districts);
				}
			}

			HSSFWorkbook workbookNew = new HSSFWorkbook();
			HSSFSheet sheetNew = workbookNew.createSheet("Sheet1");
			System.out.println(taluks.size());
			int i = 0;
			for (String taluk : taluks) {
				HSSFRow talukRow = sheetNew.createRow(i);
				talukRow.createCell(0).setCellValue(i + 1);
				talukRow.createCell(1).setCellValue(taluk);
				i++;
			}
			HSSFRow row = workbookNew.getSheetAt(0).getRow(0);
			for (int colNum = 0; colNum < row.getLastCellNum(); colNum++) {
				workbookNew.getSheetAt(0).autoSizeColumn(colNum);
			}
			FileOutputStream out = new FileOutputStream("E:\\Ashok\\Dropbox\\MyDetails\\pinfinder\\taluk.xls");
			workbookNew.write(out);
			out.close();

			HSSFWorkbook workbookState = new HSSFWorkbook();
			HSSFSheet stateSheet = workbookState.createSheet("Sheet1");
			int j = 0;
			int l = 0;

			HSSFWorkbook workbookDistrict = new HSSFWorkbook();
			HSSFSheet districtSheet = workbookDistrict.createSheet("Sheet1");
			for (Map.Entry<String, Set<String>> entry : stateMap.entrySet()) {
				if (!"Null".equalsIgnoreCase(entry.getKey())) {
					HSSFRow stateRow = stateSheet.createRow(j);
					stateRow.createCell(0).setCellValue(j + 1);
					stateRow.createCell(1).setCellValue(entry.getKey());

					int k = 1;
					for (String district : entry.getValue()) {
						HSSFRow districtRow = districtSheet.createRow(l);
						districtRow.createCell(0).setCellValue(j + 1);
						districtRow.createCell(1).setCellValue(k);
						districtRow.createCell(2).setCellValue(district);
						k++;
						l++;
					}
					j++;
				}
			}

			FileOutputStream stateOutputStream = new FileOutputStream(
					"E:\\Ashok\\Dropbox\\MyDetails\\pinfinder\\states.xls");
			workbookState.write(stateOutputStream);
			stateOutputStream.close();

			FileOutputStream districtOutputStream = new FileOutputStream(
					"E:\\Ashok\\Dropbox\\MyDetails\\pinfinder\\district.xls");
			workbookDistrict.write(districtOutputStream);
			districtOutputStream.close();
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
