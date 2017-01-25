package com.ashoksm.exceltoxml;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

public class PincodeItemArrayGenerator {

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
			Map<String, Set<String>> stateMap = new TreeMap<>();

			PostOfficeModel officeModel;
			while ((officeModel = beanReader.read(PostOfficeModel.class, header, processors)) != null) {
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

			StringBuilder stateArray = new StringBuilder("<string-array name=\"states_array\">");
			StringBuilder allDistrictArray = new StringBuilder("<string-array name=\"district_all\">");
			for (Map.Entry<String, Set<String>> entry : stateMap.entrySet()) {
				if (!"Null".equalsIgnoreCase(entry.getKey())) {
					stateArray.append("<item>");
					stateArray.append(entry.getKey().replaceAll("&", "&amp;"));
					stateArray.append("</item>");
					StringBuilder districtArray = new StringBuilder("<string-array name=\"district_"
							+ entry.getKey().trim().replaceAll("&", "").replaceAll(" ", "").toLowerCase().trim()
							+ "\">");
					for (String district : entry.getValue()) {
						allDistrictArray.append("<item>");
						allDistrictArray.append(district.replaceAll("&", "&amp;"));
						allDistrictArray.append("</item>");
						districtArray.append("<item>");
						districtArray.append(district.replaceAll("&", "&amp;"));
						districtArray.append("</item>");
					}
					districtArray.append("</string-array>");
					System.out.println(districtArray.toString());
				}
			}
			stateArray.append("</string-array>");
			System.out.println(stateArray.toString());
			allDistrictArray.append("</string-array>");
			System.out.println(allDistrictArray.toString());
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
