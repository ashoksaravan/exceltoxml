package com.ashoksm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class IFSCDataCorrection {

	private static final List<String> STATES = new ArrayList<String>();

	private static final Map<String, String> STATES_MAP = new HashMap<String, String>();

	static {
		STATES.add("Andaman and Nicobar Islands");
		STATES.add("Andhra Pradesh");
		STATES.add("Arunachal Pradesh");
		STATES.add("Assam");
		STATES.add("Bihar");
		STATES.add("Chandigarh");
		STATES.add("Chhattisgarh");
		STATES.add("Dadra and Nagar Haveli");
		STATES.add("Daman and Diu");
		STATES.add("Delhi");
		STATES.add("Goa");
		STATES.add("Gujarat");
		STATES.add("Haryana");
		STATES.add("Himachal Pradesh");
		STATES.add("Jammu and Kashmir");
		STATES.add("Jharkhand");
		STATES.add("Karnataka");
		STATES.add("Kerala");
		STATES.add("Madhya Pradesh");
		STATES.add("Lakshadweep");
		STATES.add("Maharashtra");
		STATES.add("Manipur");
		STATES.add("Meghalaya");
		STATES.add("Mizoram");
		STATES.add("Nagaland");
		STATES.add("Odisha");
		STATES.add("Puducherry");
		STATES.add("Punjab");
		STATES.add("Rajasthan");
		STATES.add("Sikkim");
		STATES.add("Tamil Nadu");
		STATES.add("Telangana");
		STATES.add("Tripura");
		STATES.add("Uttar Pradesh");
		STATES.add("Uttarakhand");
		STATES.add("West Bengal");
	}

	public static void main(String[] args) throws Exception {
		loadStateMap(args[2]);
		File root = new File(args[0]);
		for (final File bank : root.listFiles()) {
			FileInputStream file = new FileInputStream(bank);
			// Get the workbook instance for XLS file
			HSSFWorkbook workbook = new HSSFWorkbook(file);

			// Get first sheet from the workbook
			HSSFSheet sheet = workbook.getSheetAt(0);

			HSSFWorkbook workbookNew = new HSSFWorkbook();
			HSSFSheet sheetNew = workbookNew.createSheet("Sheet1");

			String bankName = null;
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if (row.getRowNum() == 0 || row.getCell(1) == null
						|| (row.getCell(0) != null && "".equals(row.getCell(0).getStringCellValue().trim()))) {
					if (row.getRowNum() == 0) {
						HSSFRow ifscRow = sheetNew.createRow(row.getRowNum());
						ifscRow.createCell(0).setCellValue("IFSC CODE");
						ifscRow.createCell(1).setCellValue("MICR CODE");
						ifscRow.createCell(2).setCellValue("BRANCH NAME");
						ifscRow.createCell(3).setCellValue("ADDRESS");
						ifscRow.createCell(4).setCellValue("CONTACT");
						ifscRow.createCell(5).setCellValue("CITY");
						ifscRow.createCell(6).setCellValue("DISTRICT");
						ifscRow.createCell(7).setCellValue("STATE");
					}
					continue;
				}
				if (bankName == null) {
					bankName = row.getCell(0).getStringCellValue().replaceAll(" ", "").toLowerCase().trim();
				}
				HSSFRow ifscRow = sheetNew.createRow(row.getRowNum());
				ifscRow.createCell(0).setCellValue(getCellValue(row, 1));
				ifscRow.createCell(1).setCellValue(getCellValue(row, 2));
				ifscRow.createCell(2).setCellValue(formatString(getCellValue(row, 3).toLowerCase(), true));
				ifscRow.createCell(3).setCellValue(formatString(getCellValue(row, 4).toLowerCase(), true));
				String contact = getCellValue(row, 5);
				String city = formatString(getCellValue(row, 6).toLowerCase(), true);
				String state = formatString(getCellValue(row, 7).toLowerCase(), true);
				if (city.trim().length() == 0 && state.trim().length() == 0) {
					ifscRow.createCell(4).setCellValue("");
					ifscRow.createCell(5).setCellValue(formatString(contact.toLowerCase(), true));
					ifscRow.createCell(6).setCellValue(formatString(contact.toLowerCase(), true));
					String temp = STATES_MAP.get(contact.toLowerCase().replaceAll(" ", ""));
					if (temp == null) {
						temp = formatString(getCellValue(row, 8).toLowerCase(), true);
					}
					if (temp.length() > 0) {
						String statex = getStateFromList(temp.replaceAll(" & ", " and ").replaceAll(" ", ""));
						if (statex != null) {
							ifscRow.createCell(7).setCellValue(statex);
						} else {
							String statey = STATES_MAP.get(temp.toLowerCase().replaceAll(" ", ""));
							if (statey != null) {
								ifscRow.createCell(7).setCellValue(
										getStateFromList(statey.replaceAll(" & ", " and ").replaceAll(" ", "")));
							} else {
								ifscRow.createCell(7).setCellValue(
										formatString(getCellValue(row, 8).toLowerCase(), true));
							}
						}
					} else {
						ifscRow.createCell(5).setCellValue("");
						ifscRow.createCell(6).setCellValue("");
						String statex = getStateFromList(contact.replaceAll(" & ", " and ").replaceAll(" ", ""));
						if (statex != null) {
							ifscRow.createCell(7).setCellValue(statex);
						} else {
							String statey = STATES_MAP.get(contact.toLowerCase().replaceAll(" ", ""));
							if (statey != null) {
								ifscRow.createCell(7).setCellValue(
										getStateFromList(statey.replaceAll(" & ", " and ").replaceAll(" ", "")));
							} else {
								ifscRow.createCell(7).setCellValue(
										formatString(getCellValue(row, 8).toLowerCase(), true));
							}
						}
					}
				} else {
					ifscRow.createCell(4).setCellValue(formatString(contact.toLowerCase(), false));
					String stateN = getStateFromList(getCellValue(row, 8).toLowerCase().replaceAll(" & ", " and ")
							.replaceAll(" ", ""));
					if (stateN != null && stateN.length() > 0) {
						ifscRow.createCell(5).setCellValue(formatString(getCellValue(row, 6).toLowerCase(), true));
						ifscRow.createCell(6).setCellValue(formatString(getCellValue(row, 7).toLowerCase(), true));
						ifscRow.createCell(7).setCellValue(stateN);
					} else {
						String stateM = STATES_MAP.get(getCellValue(row, 8).toLowerCase().toLowerCase()
								.replaceAll(" ", ""));
						if (stateM != null) {
							ifscRow.createCell(5).setCellValue(formatString(getCellValue(row, 8).toLowerCase(), true));
							ifscRow.createCell(6).setCellValue(formatString(getCellValue(row, 8).toLowerCase(), true));
							ifscRow.createCell(7).setCellValue(
									getStateFromList(stateM.replaceAll(" & ", " and ").replaceAll(" ", "")));
						} else {
							ifscRow.createCell(5).setCellValue(formatString(getCellValue(row, 6).toLowerCase(), true));
							ifscRow.createCell(6).setCellValue(formatString(getCellValue(row, 7).toLowerCase(), true));
							String statex = STATES_MAP.get(getCellValue(row, 7).toLowerCase().toLowerCase()
									.replaceAll(" ", ""));
							if (statex != null && statex.length() > 0) {
								ifscRow.createCell(7).setCellValue(
										getStateFromList(statex.replaceAll(" & ", " and ").replaceAll(" ", "")));
							} else {
								String statey = getStateFromList(formatString(getCellValue(row, 7).toLowerCase(), true)
										.replaceAll(" & ", " and ").replaceAll(" ", ""));
								if (statey != null && statey.length() > 0) {
									ifscRow.createCell(7).setCellValue(statey);
								} else {
									ifscRow.createCell(7).setCellValue(
											formatString(getCellValue(row, 8).toLowerCase(), true));
								}
							}

						}
					}
				}
			}

			HSSFRow row = workbookNew.getSheetAt(0).getRow(0);
			for (int colNum = 0; colNum < row.getLastCellNum(); colNum++) {
				workbookNew.getSheetAt(0).autoSizeColumn(colNum);
			}
			FileOutputStream out = new FileOutputStream(new File(args[1] + "\\" + bankName + ".xls"));
			workbookNew.write(out);
			out.close();
			System.out.println(bankName);
		}
	}

	private static String getStateFromList(String state) {
		if ("Orissa".equalsIgnoreCase(state)) {
			state = "Odisha";
		} else if ("Pondicherry".equalsIgnoreCase(state)) {
			state = "Puducherry";
		} else if ("Uttaranchal".equalsIgnoreCase(state)) {
			state = "Uttarakhand";
		}
		for (String st : STATES) {
			String temp = st.toLowerCase().replaceAll(" ", "");
			if (temp.equalsIgnoreCase(state) || temp.contains(state)) {
				return st;
			}
		}
		return null;
	}

	private static void loadStateMap(String loc) throws IOException {
		File bankAddFile = new File(loc + "/BankBranchAddress.xls");
		FileInputStream file = new FileInputStream(bankAddFile);
		// Get the workbook instance for XLS file
		HSSFWorkbook workbook = new HSSFWorkbook(file);

		// Get first sheet from the workbook
		HSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			String key = row.getCell(3).getStringCellValue().toLowerCase().replaceAll(" ", "");
			String value = row.getCell(2).getStringCellValue();
			if (key != null && key.trim().length() > 0) {
				STATES_MAP.put(key, value);
			}
		}
	}

	/**
	 * @param row
	 * @return
	 */
	private static String getCellValue(Row row, int cellNum) {
		String cellValue = "";
		if (row.getCell(cellNum) != null && row.getCell(cellNum).getCellType() == Cell.CELL_TYPE_STRING) {
			cellValue = row.getCell(cellNum).getStringCellValue();
		} else if (row.getCell(cellNum) != null && row.getCell(cellNum).getCellType() == Cell.CELL_TYPE_NUMERIC) {
			cellValue = String.valueOf(new Double(row.getCell(cellNum).getNumericCellValue()).intValue());
		}
		return cellValue;
	}

	private static String formatString(String orginal, boolean format) {
		if (orginal.length() > 1) {
			String[] temp = orginal.split(" ");
			boolean flag = false;
			StringBuffer sb = new StringBuffer();
			for (String cat : temp) {
				if (cat.length() > 1) {
					String formated = null;
					if (cat.contains("@")) {
						formated = cat.toLowerCase();
					} else {
						formated = cat.substring(0, 1).toUpperCase() + cat.substring(1, cat.length());
					}
					sb.append(formated);
					sb.append(" ");
				} else {
					sb.append(cat.toUpperCase());
					sb.append(" ");
				}
			}
			if (format) {
				temp = sb.toString().trim().split(",");
				sb = new StringBuffer();
				for (String cat : temp) {
					String formated = null;
					if (cat.length() > 1) {
						formated = cat.substring(0, 1).toUpperCase() + cat.substring(1, cat.length());
					} else {
						formated = cat.toUpperCase();
					}
					if (flag) {
						sb.append(",");
					} else {
						flag = true;
					}
					sb.append(formated);
				}
				flag = false;
				temp = sb.toString().trim().split("\\.");
				sb = new StringBuffer();
				for (String cat : temp) {
					String formated = null;
					if (cat.length() > 1) {
						formated = cat.substring(0, 1).toUpperCase() + cat.substring(1, cat.length());
					} else {
						formated = cat.toUpperCase();
					}
					if (flag) {
						sb.append(".");
					} else {
						flag = true;
					}
					sb.append(formated);
				}
			}
			return sb.toString().replaceAll("\\(e\\)", "(E)").replaceAll("\\(w\\)", "(W)")
					.replaceAll("\\(s\\)", "\\(S\\)").replaceAll("\\(n\\)", "\\(N\\)")
					.replaceAll("\\(west\\)", "(WEST)").replaceAll("\\(north\\)", "(NORTH)")
					.replaceAll("\\(south\\)", "(SOUTH)").replaceAll("\\(east\\)", "(EAST)");
		} else {
			return orginal.toUpperCase();
		}
	}
}
