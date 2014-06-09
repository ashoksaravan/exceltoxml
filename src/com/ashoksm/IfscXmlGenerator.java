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

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.thoughtworks.xstream.XStream;

public class IfscXmlGenerator {

	private static Map<String, Integer> bankAddMap = new HashMap<String, Integer>();

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		loadBankAddMap(args[0]);
		File root = new File(args[0]);
		for (final File bank : root.listFiles()) {
			if (!bank.getName().equals("BankNames.xls") && !bank.getName().equals("BankBranchAddress.xls")) {
				System.out.println("***** " + bank.getName() + " *****");
				List<BankBranch> bankBranchs = new ArrayList<BankBranch>();
				bankBranchs.addAll(buildBanks(bank));
				System.out.println(bankBranchs.size());
				writeFile(bankBranchs, bank.getName().toLowerCase().substring(0, bank.getName().lastIndexOf(".")),
						"xml\\ifsc\\");
				System.out.println("***** END *****");
			}
		}
	}

	private static void loadBankAddMap(String loc) throws Exception {
		File bankAddFile = new File(loc + "/BankBranchAddress.xls");
		FileInputStream file = new FileInputStream(bankAddFile);
		// Get the workbook instance for XLS file
		HSSFWorkbook workbook = new HSSFWorkbook(file);

		// Get first sheet from the workbook
		HSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			if (row.getCell(0).getCellType() == Cell.CELL_TYPE_NUMERIC) {
				String key = row.getCell(1).getStringCellValue().replaceAll(" ", "")
						+ row.getCell(3).getStringCellValue() + row.getCell(2).getStringCellValue();
				Double value = row.getCell(0).getNumericCellValue();
				bankAddMap.put(key, value.intValue());
			}
		}
	}

	/**
	 * @param bankFile
	 * @return
	 * @throws IOException
	 */
	private static List<BankBranch> buildBanks(File bankFile) throws IOException {
		FileInputStream file = new FileInputStream(bankFile);
		List<BankBranch> bankBranchs = new ArrayList<BankBranch>();

		// Get the workbook instance for XLS file
		HSSFWorkbook workbook = new HSSFWorkbook(file);

		// Get first sheet from the workbook
		HSSFSheet sheet = workbook.getSheetAt(0);

		BankBranch bankBranch = null;
		String key = null;

		// Iterate through each rows from first sheet
		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
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
				bankBranch.setBankLoc(bankAddMap.get(key));
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
		return bankBranchs;
	}

	/**
	 * @param bankBranchs
	 * @param bankName
	 */
	private static void writeFile(List<BankBranch> bankBranchs, String bankName, String dest) {
		if (bankBranchs.size() > 3499) {
			List<List<BankBranch>> partition = new ArrayList<List<BankBranch>>();
			List<BankBranch> subBankBranchs = new ArrayList<BankBranch>();
			boolean flag = false;
			int i = 1;
			for (BankBranch bankBranch : bankBranchs) {
				subBankBranchs.add(bankBranch);
				if (i == 3500) {
					partition.add(subBankBranchs);
					subBankBranchs = new ArrayList<BankBranch>();
					i = 1;
					flag = true;
				} else {
					i++;
					flag = false;
				}
			}
			if (!flag) {
				partition.add(subBankBranchs);
			}
			i = 1;
			for (List<BankBranch> list : partition) {
				String tempStateName = bankName + "_" + i;
				createXML(list, tempStateName, dest);
				i++;
			}
		} else {
			createXML(bankBranchs, bankName, dest);
		}
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
		FileOutputStream fop = null;
		File file;

		try {

			file = new File(dest + stateName + ".xml");
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
