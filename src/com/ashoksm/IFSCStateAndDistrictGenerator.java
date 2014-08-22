package com.ashoksm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

public class IFSCStateAndDistrictGenerator {

	private static List<String> BANK_NAMES = new ArrayList<String>();

	static {
		BANK_NAMES.add("Abhyudaya Co-Op Bank Ltd");
		BANK_NAMES.add("Abu Dhabi Commercial Bank");
		BANK_NAMES.add("Ahmedabad Mercantile Co-Op Bank Ltd");
		BANK_NAMES.add("Allahabad Bank");
		BANK_NAMES.add("Almora Urban Co-Operative Bank Ltd");
		BANK_NAMES.add("Andhra Bank");
		BANK_NAMES.add("Andhra Pragathi Grameena Bank");
		BANK_NAMES.add("Apna Sahakari Bank Ltd");
		BANK_NAMES.add("Austarlia and New Zealand Banking Gorup Ltd");
		BANK_NAMES.add("Axis Bank");
		BANK_NAMES.add("Bank Internasional Indonesia");
		BANK_NAMES.add("Bank Of America");
		BANK_NAMES.add("Bank Of Bahrain And Kuwait");
		BANK_NAMES.add("Bank Of Baroda");
		BANK_NAMES.add("Bank Of Ceylon");
		BANK_NAMES.add("Bank Of India");
		BANK_NAMES.add("Bank Of Maharashtra");
		BANK_NAMES.add("Bank Of Nova Scotia");
		BANK_NAMES.add("Bank Of Tokyo-Mitsubishi Ufj Ltd");
		BANK_NAMES.add("Barclays Bank Plc");
		BANK_NAMES.add("Bassein Catholic Co-Op Bank Ltd");
		BANK_NAMES.add("Bharat Co-Op Bank (Mumbai) Ltd");
		BANK_NAMES.add("Bnp Paribas");
		BANK_NAMES.add("Canara Bank");
		BANK_NAMES.add("Capital Local Area Bank Ltd");
		BANK_NAMES.add("Catholic Syrian Bank Ltd");
		BANK_NAMES.add("Central Bank Of India");
		BANK_NAMES.add("Chinatrust Commercial Bank");
		BANK_NAMES.add("Citibank");
		BANK_NAMES.add("Citizencredit Co-Op Bank Ltd");
		BANK_NAMES.add("City Union Bank Ltd");
		BANK_NAMES.add("Commonwealth Bank of Australia");
		BANK_NAMES.add("Corporation Bank");
		BANK_NAMES.add("Cosmos Co-Op Bank Ltd");
		BANK_NAMES.add("Credit Agricole Corp and Investment Bank");
		BANK_NAMES.add("Credit Suisse Ag");
		BANK_NAMES.add("Dbs Bank Ltd");
		BANK_NAMES.add("Dena Bank");
		BANK_NAMES.add("Deutsche Bank Ag");
		BANK_NAMES.add("Development Credit Bank Ltd");
		BANK_NAMES.add("Dhanlaxmi Bank Ltd");
		BANK_NAMES.add("Dicgc");
		BANK_NAMES.add("Dombivli Nagari Sahakari Bank Ltd");
		BANK_NAMES.add("Federal Bank Ltd");
		BANK_NAMES.add("Firstrand Bank Ltd");
		BANK_NAMES.add("Greater Bombay Co-Op Bank Ltd");
		BANK_NAMES.add("Gurgaon Gramin Bank");
		BANK_NAMES.add("Hdfc Bank Ltd");
		BANK_NAMES.add("Hsbc");
		BANK_NAMES.add("Icici Bank Ltd");
		BANK_NAMES.add("Idbi Bank Ltd");
		BANK_NAMES.add("Indian Bank");
		BANK_NAMES.add("Indian Overseas Bank");
		BANK_NAMES.add("Indusind Bank Ltd");
		BANK_NAMES.add("Industrial and Commercial Bank of China Ltd.");
		BANK_NAMES.add("Ing Vysya Bank Ltd");
		BANK_NAMES.add("Jalgaon Janata Sahkari Bank Ltd");
		BANK_NAMES.add("Jammu And Kashmir Bank Ltd");
		BANK_NAMES.add("Janakalyan Sahakari Bank Ltd");
		BANK_NAMES.add("Janaseva Sahakari Bank Borivli Ltd.");
		BANK_NAMES.add("Janaseva Sahakari Bank Ltd Pune");
		BANK_NAMES.add("Janata Sahkari Bank Ltd Pune");
		BANK_NAMES.add("Jpmorgan Chase Bank");
		BANK_NAMES.add("Kallappanna Awade Ich Janata S Bank");
		BANK_NAMES.add("Kalupur Commercial Co Op Bank Ltd");
		BANK_NAMES.add("Kalyan Janata Sahakari Bank Ltd");
		BANK_NAMES.add("Kapole Co-Op Bank");
		BANK_NAMES.add("Karnataka Bank Ltd");
		BANK_NAMES.add("Karnataka State Co-Op Apex Bank Ltd");
		BANK_NAMES.add("Karnataka Vikas Grameena Bank");
		BANK_NAMES.add("Karur Vysya Bank");
		BANK_NAMES.add("Kotak Mahindra Bank");
		BANK_NAMES.add("Kurmanchal Nagar Sahakari Bank Ltd");
		BANK_NAMES.add("Lakshmi Vilas Bank Ltd");
		BANK_NAMES.add("Mahanagar Co-Op Bank Ltd");
		BANK_NAMES.add("Maharashtra State Co-Op Bank");
		BANK_NAMES.add("Mashreq Bank Psc");
		BANK_NAMES.add("Mehsana Urban Co-Op Bank Ltd");
		BANK_NAMES.add("Mizuho Corporate Bank Ltd");
		BANK_NAMES.add("Mumbai District Central Co-Op Bank Ltd");
		BANK_NAMES.add("Nagar Urban Co-Operative Bank");
		BANK_NAMES.add("Nagpur Nagarik Sahakari Bank Ltd"); 
		BANK_NAMES.add("Nainital Bank Ltd");
		BANK_NAMES.add("National Australia Bank");
		BANK_NAMES.add("New India Co-Op Bank Ltd");
		BANK_NAMES.add("Nkgsb Co-Op Bank Ltd");
		BANK_NAMES.add("North Malabar Gramin Bank");
		BANK_NAMES.add("Nutan Nagarik Sahakari Bank Ltd");
		BANK_NAMES.add("Oman International Bank Saog");
		BANK_NAMES.add("Oriental Bank Of Commerce");
		BANK_NAMES.add("Parsik Janata Sahakari Bank Ltd");
		BANK_NAMES.add("Prathama Bank");
		BANK_NAMES.add("Prime Co-Operative Bank Ltd");
		BANK_NAMES.add("Punjab And Maharashtra Co-Op Bank Ltd");
		BANK_NAMES.add("Punjab And Sind Bank");
		BANK_NAMES.add("Punjab National Bank");
		BANK_NAMES.add("Rabobank International (CCRB)");
		BANK_NAMES.add("Rajgurunagar Sahkari Bank Ltd");
		BANK_NAMES.add("Rajkot Nagarik Sahakari Bank Ltd");
		BANK_NAMES.add("Ratnakar Bank Ltd");
		BANK_NAMES.add("Reserve Bank Of India");
		BANK_NAMES.add("Royal Bank Of Scotland");
		BANK_NAMES.add("Saraswat Co-Op Bank Ltd");
		BANK_NAMES.add("SBER Bank");
		BANK_NAMES.add("Shamrao Vithal Co-Op Bank Ltd");
		BANK_NAMES.add("Shinhan Bank");
		BANK_NAMES.add("Shri Chhatrapati Rajarshi Shahu Urban Co-Op Bank Ltd");
		BANK_NAMES.add("Societe Generale");
		BANK_NAMES.add("South Indian Bank");
		BANK_NAMES.add("Standard Chartered Bank");
		BANK_NAMES.add("State Bank Of Bikaner And Jaipur");
		BANK_NAMES.add("State Bank Of Hyderabad");
		BANK_NAMES.add("State Bank Of India");
		BANK_NAMES.add("State Bank Of Mauritius Ltd");
		BANK_NAMES.add("State Bank Of Mysore");
		BANK_NAMES.add("State Bank Of Patiala");
		BANK_NAMES.add("State Bank Of Travancore");
		BANK_NAMES.add("Sumitomo Mitsui Banking Corporation");
		BANK_NAMES.add("Surat Peoples Co-Op Bank Ltd");
		BANK_NAMES.add("Syndicate Bank");
		BANK_NAMES.add("Tamilnad Mercantile Bank Ltd");
		BANK_NAMES.add("Tamilnadu State Apex Co-Op Bank Ltd");
		BANK_NAMES.add("Thane Bharat Sahakari Bank Ltd");
		BANK_NAMES.add("Thane District Central Co-operative Bank Ltd");
		BANK_NAMES.add("Thane Janata Sahakari Bank Ltd");
		BANK_NAMES.add("The A.P. Mahesh Co-Op Urban Bank Ltd");
		BANK_NAMES.add("The Akola District Central Co-operative Bank");
		BANK_NAMES.add("The Akola Janata Commercial Co-operative Bank");
		BANK_NAMES.add("The Andhra Pradesh State Co-op Bank Ltd.");
		BANK_NAMES.add("The Delhi State Co-operative Bank Ltd.");
		BANK_NAMES.add("The Gadchiroli District Central Co-operative Bank Ltd");
		BANK_NAMES.add("The Gujarat State Co-Operative Bank Ltd");
		BANK_NAMES.add("The Jalgaon Peoples Co-op Bank");
		BANK_NAMES.add("The Kangra Co-Operative Bank Ltd.");
		BANK_NAMES.add("The Karad Urban Co-Op Bank Ltd");
		BANK_NAMES.add("The Municipal Co-Operative Bank Ltd. Mumbai");
		BANK_NAMES.add("The Nasik Merchants Co-Op Bank Ltd");
		BANK_NAMES.add("The Rajasthan State Co-Operative Bank Ltd");
		BANK_NAMES.add("The Sahebrao Deshmukh Co-op Bank Ltd.");
		BANK_NAMES.add("The Seva Vikas Co-operative Bank Ltd");
		BANK_NAMES.add("The Solapur Janata Sahkari Bank Ltd.");
		BANK_NAMES.add("The Surat District Co-Operative Bank Ltd");
		BANK_NAMES.add("The Sutex Co Op Bank Ltd");
		BANK_NAMES.add("The Varachha Co-Op. Bank Ltd");
		BANK_NAMES.add("The Vishweshwar Sahakari Bank Ltd Pune");
		BANK_NAMES.add("Tumkur Grain Merchants Cooperative Bank Ltd");
		BANK_NAMES.add("UBS AG");
		BANK_NAMES.add("Uco Bank");
		BANK_NAMES.add("Union Bank Of India");
		BANK_NAMES.add("United Bank Of India");
		BANK_NAMES.add("United Overseas Bank");
		BANK_NAMES.add("Vasai Vikas Sahakari Bank Ltd");
		BANK_NAMES.add("Vijaya Bank");
		BANK_NAMES.add("West Bengal State Co-Op Bank Ltd");
		BANK_NAMES.add("Westpac Banking Corporation");
		BANK_NAMES.add("Woori Bank");
		BANK_NAMES.add("Yes Bank Ltd");
		BANK_NAMES.add("Zila Sahkari Bank Ltd. Ghaziabad");
	}

	public static void main(String[] args) throws Exception {
		List<District> districts = new ArrayList<District>();
		File root = new File(args[0]);
		for (final File bank : root.listFiles()) {
			System.out.println(bank.getName());
			if (!bank.getName().equals("BankNames.xls") && !"BankBranchAddress.xls".equals(bank.getName())) {
				String bankName = bank.getName().substring(0, bank.getName().lastIndexOf("."));
				if (bankName.endsWith("0") || bankName.endsWith("1") || bankName.endsWith("2")
						|| bankName.endsWith("3")) {
					bankName = bankName.substring(0, bankName.length() - 1);
				}
				FileInputStream file = new FileInputStream(bank);
				// Get the workbook instance for XLS file
				HSSFWorkbook workbook = new HSSFWorkbook(file);

				// Get first sheet from the workbook
				HSSFSheet sheet = workbook.getSheetAt(0);
				Iterator<Row> rowIterator = sheet.iterator();
				District district = null;
				String previousRow = null;
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					String currentRow = row.getCell(0).getStringCellValue();
					if ("District:".equalsIgnoreCase(currentRow)) {
						district = new District();
						district.setDistrictName(row.getCell(1).getStringCellValue());
						district.setBankName(getBankName(bankName));
						districts.add(district);
					}
					if (previousRow != null && previousRow.equalsIgnoreCase("District:")) {
						district.setStateName(row.getCell(1).getStringCellValue());
					}
					previousRow = currentRow;
				}
			}
		}

		// remove duplicates
		Map<String, Map<String, Set<String>>> banksMap = new TreeMap<String, Map<String, Set<String>>>();
		for (District district : districts) {
			Map<String, Set<String>> districtsMap = new TreeMap<String, Set<String>>();
			if (banksMap.containsKey(district.getBankName())) {
				districtsMap = banksMap.get(district.getBankName());
			} else {
				districtsMap = new TreeMap<String, Set<String>>();
			}
			Set<String> districtsSet;
			if (districtsMap.containsKey(district.getStateName())) {
				districtsSet = districtsMap.get(district.getStateName());
			} else {
				districtsSet = new TreeSet<String>();
			}
			districtsSet.add(district.getDistrictName());
			districtsMap.put(district.getStateName(), districtsSet);
			banksMap.put(district.getBankName(), districtsMap);
		}

		// write in excel
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Bank Name");
		int index = 0;
		for (Map.Entry<String, Map<String, Set<String>>> state : banksMap.entrySet()) {
			String bankName = state.getKey();
			Map<String, Set<String>> districtsMap = state.getValue();
			for (Map.Entry<String, Set<String>> district : districtsMap.entrySet()) {
				String stateName = district.getKey();
				for (String districtName : district.getValue()) {
					// System.out.println(bankName + " : " + stateName + " : " +
					// districtName);
					HSSFRow bankAddRow = sheet.createRow(index);
					bankAddRow.createCell(0).setCellValue(index + 1);
					bankAddRow.createCell(1).setCellValue(bankName);
					bankAddRow.createCell(2).setCellValue(stateName);
					bankAddRow.createCell(3).setCellValue(districtName);
					index++;
				}
			}
		}
		FileOutputStream out = new FileOutputStream(new File(args[0] + "/BankBranchAddress.xls"));
		workbook.write(out);
		out.close();
		System.out.println("Written successfully!!!");
	}

	private static String getBankName(String bankNameIn) throws Exception {
		for (String bankName : BANK_NAMES) {
			String temp = bankName.replaceAll(" ", "").trim();
			if (temp.equalsIgnoreCase(bankNameIn)) {
				return bankName;
			}
		}
		throw new Exception(bankNameIn);
	}
}
