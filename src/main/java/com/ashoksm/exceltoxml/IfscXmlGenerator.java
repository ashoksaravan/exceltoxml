package com.ashoksm.exceltoxml;

import com.thoughtworks.xstream.XStream;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//6
public class IfscXmlGenerator {

	private static Map<String, Integer> bankAddMap = new HashMap<>();

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		loadBankAddMap(args[0]);
		File root = new File(args[0]);
		for (final File bank : root.listFiles()) {
			if (!bank.getName().startsWith("BankNames") && !bank.getName().startsWith("BankBranchAddress")) {
				System.out.println("***** " + bank.getName() + " *****");
				List<BankBranch> bankBranchs = new ArrayList<>();
				bankBranchs.addAll(buildBanks(bank));
				System.out.println(bankBranchs.size());
				writeFile(bankBranchs, bank.getName().toLowerCase().substring(0, bank.getName().lastIndexOf(".")),
						"xml\\ifsc\\");
				System.out.println("***** END *****");
			}
		}
	}

	private static void loadBankAddMap(String loc) throws Exception {
		File bankAddFile = new File(loc + "/BankBranchAddress_1.xlsx");
		FileInputStream file = new FileInputStream(bankAddFile);
		// Get the workbook instance for XLS file
		Workbook workbook = new XSSFWorkbook(file);

		// Get first sheet from the workbook
		Sheet sheet = workbook.getSheetAt(0);
		for (Row row : sheet) {
			if (row.getCell(0).getCellType() == Cell.CELL_TYPE_NUMERIC) {
				String key = row.getCell(1).getStringCellValue().replaceAll(" ", "")
						+ row.getCell(3).getStringCellValue() + row.getCell(2).getStringCellValue();
				Double value = row.getCell(0).getNumericCellValue();
				bankAddMap.put(key.toLowerCase(), value.intValue());
			}
		}
		workbook.close();
	}

	/**
	 * @param bankFile
	 * @return
	 * @throws IOException
	 */
	private static List<BankBranch> buildBanks(File bankFile) throws IOException {
		FileInputStream file = new FileInputStream(bankFile);
		List<BankBranch> bankBranchs = new ArrayList<>();

		// Get the workbook instance
		Workbook workBook;
		if (bankFile.getName().toLowerCase().endsWith("xls")) {
			workBook = new HSSFWorkbook(file);
		} else {
			workBook = new XSSFWorkbook(file);
		}

		// Get first sheet from the workbook
		Sheet sheet = workBook.getSheetAt(0);

		BankBranch bankBranch = null;
		String key = null;

		// Iterate through each rows from first sheet
		for (Row row : sheet) {
			String cellHeader = "";
			String cellValue = "";

			if (row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING) {
				cellHeader = row.getCell(0).getStringCellValue();
			} else if (row.getCell(0).getCellType() == Cell.CELL_TYPE_NUMERIC) {
				cellHeader = String.valueOf(row.getCell(0).getNumericCellValue());
			}

			if (row.getCell(1).getCellType() == Cell.CELL_TYPE_STRING) {
				cellValue = row.getCell(1).getStringCellValue();
			} else if (row.getCell(1).getCellType() == Cell.CELL_TYPE_NUMERIC) {
				cellValue = String.valueOf(row.getCell(1).getNumericCellValue());
			}

			if ("Branch:".equals(cellHeader)) {
				bankBranch = new BankBranch();
				bankBranch.setName(cellValue);
				bankBranchs.add(bankBranch);
			}

			if ("City:".equalsIgnoreCase(cellHeader)) {
				bankBranch.setCity(cellValue);
			} else if ("Address:".equalsIgnoreCase(cellHeader)) {
				bankBranch.setAddress(cellValue);
			} else if ("Contact:".equalsIgnoreCase(cellHeader)) {
				bankBranch.setContact(cellValue);
			} else if ("MICR Code:".equalsIgnoreCase(cellHeader)) {
				bankBranch.setMicrCode(cellValue);
			} else if ("IFSC Code:".equalsIgnoreCase(cellHeader)) {
				bankBranch.setIfscCode(cellValue);
				bankBranch.setBankLoc(bankAddMap.get(key.toLowerCase()));
			} else if ("District:".equalsIgnoreCase(cellHeader)) {
				key = bankFile.getName().substring(0, bankFile.getName().lastIndexOf("."));
				if (key.endsWith("0") || key.endsWith("1") || key.endsWith("2") || key.endsWith("3")) {
					key = key.substring(0, key.length() - 1);
				}
				key = key + cellValue;
			} else if ("State:".equalsIgnoreCase(cellHeader)) {
				key = key + cellValue;
			}
		}
		workBook.close();
		return bankBranchs;
	}

	/**
	 * @param bankBranchs
	 * @param bankName
	 */
	private static void writeFile(List<BankBranch> bankBranchs, String bankName, String dest) {
		createXML(bankBranchs, bankName, dest);
	}

	/**
	 * @param bankBranchs
	 * @param stateName
	 */
	private static void createXML(List<BankBranch> bankBranchs, String stateName, String dest) {
		BankBranchs branchs = new BankBranchs();
		branchs.setBankBranchs(bankBranchs);
		XStream xStream = new XStream();
		xStream.alias("bankbranch", BankBranch.class);
		xStream.alias("bankBranchs", BankBranchs.class);
		xStream.addImplicitCollection(BankBranchs.class, "bankBranchs");
		String xml = xStream.toXML(branchs);
		File file = new File(dest + stateName + ".xml");
		try (FileOutputStream fop = new FileOutputStream(file)) {

			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// get the content in bytes
			byte[] contentInBytes = xml.getBytes();

			fop.write(contentInBytes);
			fop.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
