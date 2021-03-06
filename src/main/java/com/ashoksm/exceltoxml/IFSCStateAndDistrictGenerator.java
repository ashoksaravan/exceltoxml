package com.ashoksm.exceltoxml;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;

//5
public class IFSCStateAndDistrictGenerator {

    /** BANK_NAMES. */
    private final static List<String> BANK_NAMES = new ArrayList<>();

    static {
        BANK_NAMES.add("Abhyudaya Co-Op Bank Ltd");
        BANK_NAMES.add("Abu Dhabi Commercial Bank");
        BANK_NAMES.add("Ahmedabad Mercantile Co-Op Bank");
        BANK_NAMES.add("Allahabad Bank");
        BANK_NAMES.add("Almora Urban Co-Op Bank Ltd");
        BANK_NAMES.add("Andhra Bank");
        BANK_NAMES.add("Andhra Pragathi Grameena Bank");
        BANK_NAMES.add("Apna Sahakari Bank Ltd");
        BANK_NAMES.add("Australia And New Zealand Banking Group Ltd");
        BANK_NAMES.add("Axis Bank");
        BANK_NAMES.add("Bandhan Bank Ltd");
        BANK_NAMES.add("Bank Internasional Indonesia");
        BANK_NAMES.add("Bank Of America");
        BANK_NAMES.add("Bank Of Bahrain And Kuwait BSC");
        BANK_NAMES.add("Bank Of Baroda");
        BANK_NAMES.add("Bank Of Ceylon");
        BANK_NAMES.add("Bank Of India");
        BANK_NAMES.add("Bank Of Maharashtra");
        BANK_NAMES.add("The Bank Of Nova Scotia");
        BANK_NAMES.add("Bank Of Tokyo Mitsubishi Ltd");
        BANK_NAMES.add("Barclays Bank");
        BANK_NAMES.add("Bassein Catholic Co-Op Bank Ltd");
        BANK_NAMES.add("Bharat Co-Op Bank Mumbai Ltd");
        BANK_NAMES.add("Bnp Paribas");
        BANK_NAMES.add("Canara Bank");
        BANK_NAMES.add("Capital Small Finance Bank Ltd");
        BANK_NAMES.add("Catholic Syrian Bank Ltd");
        BANK_NAMES.add("Central Bank Of India");
        BANK_NAMES.add("Chinatrust Commercial Bank Ltd");
        BANK_NAMES.add("Citibank");
        BANK_NAMES.add("Citizencredit Co-Op Bank Ltd");
        BANK_NAMES.add("City Union Bank Ltd");
        BANK_NAMES.add("Commonwealth Bank of Australia");
        BANK_NAMES.add("Corporation Bank");
        BANK_NAMES.add("The Cosmos Co-Op Bank Ltd");
        BANK_NAMES.add("Credit Agricole Corporate And Investment Bank Calyon Bank");
        BANK_NAMES.add("Credit Suisee Ag");
        BANK_NAMES.add("Development Bank Of Singapore");
        BANK_NAMES.add("Dena Bank");
        BANK_NAMES.add("Deutsche Bank");
        BANK_NAMES.add("Dcb Bank Ltd");
        BANK_NAMES.add("Dhanlaxmi Bank");
        BANK_NAMES.add("Deposit Insurance And Credit Guarantee Corporation");
        BANK_NAMES.add("Dombivli Nagari Sahakari Bank Ltd");
        BANK_NAMES.add("Doha Bank");
        BANK_NAMES.add("Export Import Bank Of India");
        BANK_NAMES.add("Federal Bank");
        BANK_NAMES.add("Firstrand Bank Ltd");
        BANK_NAMES.add("GP Parsik Bank");
        BANK_NAMES.add("The Greater Bombay Co-Op Bank Ltd");
        BANK_NAMES.add("Gurgaon Gramin Bank");
        BANK_NAMES.add("Hdfc Bank");
        BANK_NAMES.add("Hsbc Bank");
        BANK_NAMES.add("Hsbc Bank Oman Saog");
        BANK_NAMES.add("Icici Bank Ltd");
        BANK_NAMES.add("Idbi Bank");
        BANK_NAMES.add("IDFC Bank Ltd");
        BANK_NAMES.add("Indian Bank");
        BANK_NAMES.add("Indian Overseas Bank");
        BANK_NAMES.add("Indusind Bank");
        BANK_NAMES.add("Industrial and Commercial Bank of China Ltd");
        BANK_NAMES.add("Industrial Bank of Korea");
        BANK_NAMES.add("Ing Vysya Bank");
        BANK_NAMES.add("Jalgaon Janata Sahakari Bank Ltd");
        BANK_NAMES.add("Jammu And Kashmir Bank Ltd");
        BANK_NAMES.add("Janakalyan Sahakari Bank Ltd");
        BANK_NAMES.add("Janaseva Sahakari Bank Borivli Ltd");
        BANK_NAMES.add("Janaseva Sahakari Bank Ltd");
        BANK_NAMES.add("Janata Sahakari Bank Ltd");
        BANK_NAMES.add("JPMorgan Chase Bank");
        BANK_NAMES.add("Kallappanna Awade Ichalkaranji Janata Sahakari Bank Ltd");
        BANK_NAMES.add("Kalupur Commercial Co-Op Bank");
        BANK_NAMES.add("Kalyan Janata Sahakari Bank");
        BANK_NAMES.add("Kapol Co-Op Bank Ltd");
        BANK_NAMES.add("Karnataka Bank Ltd");
        BANK_NAMES.add("The Karnataka State Co-Op Apex Bank Ltd");
        BANK_NAMES.add("Karnataka Vikas Grameena Bank");
        BANK_NAMES.add("Karur Vysya Bank");
        BANK_NAMES.add("Kotak Mahindra Bank Ltd");
        BANK_NAMES.add("The Kurmanchal Nagar Sahakari Bank Ltd");
        BANK_NAMES.add("Lakshmi Vilas Bank");
        BANK_NAMES.add("Mahanagar Co-Op Bank");
        BANK_NAMES.add("Maharashtra State Co-Op Bank");
        BANK_NAMES.add("Mashreq Bank Psc");
        BANK_NAMES.add("The Mehsana Urban Co-Op Bank");
        BANK_NAMES.add("Mizuho Corporate Bank Ltd");
        BANK_NAMES.add("The Mumbai District Central Co-Op Bank Ltd");
        BANK_NAMES.add("Nagar Urban Co-Op Bank");
        BANK_NAMES.add("National Bank of Abu Dhabi PJSC");
        BANK_NAMES.add("Nagpur Nagarik Sahakari Bank Ltd");
        BANK_NAMES.add("The Nainital Bank Ltd");
        BANK_NAMES.add("National Australia Bank Ltd");
        BANK_NAMES.add("New India Co-Op Bank Ltd");
        BANK_NAMES.add("Nkgsb Co-Op Bank Ltd");
        BANK_NAMES.add("North Malabar Gramin Bank");
        BANK_NAMES.add("Nutan Nagarik Sahakari Bank Ltd");
        BANK_NAMES.add("Oman International Bank Saog");
        BANK_NAMES.add("Oriental Bank Of Commerce");
        BANK_NAMES.add("Parsik Janata Sahakari Bank Ltd");
        BANK_NAMES.add("Prathama Bank");
        BANK_NAMES.add("Prime Co-Op Bank Ltd");
        BANK_NAMES.add("Punjab And Maharashtra Co-Op Bank");
        BANK_NAMES.add("Punjab And Sind Bank");
        BANK_NAMES.add("Punjab National Bank");
        BANK_NAMES.add("Rabobank International");
        BANK_NAMES.add("Rajgurunagar Sahakari Bank Ltd");
        BANK_NAMES.add("Rajkot Nagarik Sahakari Bank Ltd");
        BANK_NAMES.add("RBL Bank Ltd");
        BANK_NAMES.add("Reserve Bank Of India");
        BANK_NAMES.add("The Royal Bank Of Scotland");
        BANK_NAMES.add("Saraswat Co-Op Bank Ltd");
        BANK_NAMES.add("SBER Bank");
        BANK_NAMES.add("Shikshak Sahakari Bank Ltd");
        BANK_NAMES.add("The Shamrao Vithal Co-Op Bank");
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
        BANK_NAMES.add("The Surat Peoples Co-Op Bank Ltd");
        BANK_NAMES.add("Syndicate Bank");
        BANK_NAMES.add("Tamilnad Mercantile Bank Ltd");
        BANK_NAMES.add("The Tamilnadu State Apex Co-Op Bank");
        BANK_NAMES.add("The Thane Bharat Sahakari Bank Ltd");
        BANK_NAMES.add("The Thane District Central Co-Op Bank Ltd");
        BANK_NAMES.add("TJSB Sahakari Bank Ltd");
        BANK_NAMES.add("The A.P. Mahesh Co-Op Urban Bank Ltd");
        BANK_NAMES.add("The Akola District Central Co-Op Bank");
        BANK_NAMES.add("Akola Janata Commercial Co-Op Bank");
        BANK_NAMES.add("The Andhra Pradesh State Co-Op Bank Ltd");
        BANK_NAMES.add("The Delhi State Co-Op Bank Ltd");
        BANK_NAMES.add("The Gadchiroli District Central Co-Op Bank Ltd");
        BANK_NAMES.add("The Gujarat State Co-Op Bank Ltd");
        BANK_NAMES.add("The Jalgaon Peoples Co-op Bank Ltd");
        BANK_NAMES.add("The Kangra Central Co-Op Bank Ltd");
        BANK_NAMES.add("The Kangra Co-Op Bank Ltd");
        BANK_NAMES.add("The Karad Urban Co-Op Bank Ltd");
        BANK_NAMES.add("The Municipal Co-Op Bank Ltd");
        BANK_NAMES.add("The Nasik Merchants Co-Op Bank Ltd");
        BANK_NAMES.add("The Rajasthan State Co-Op Bank Ltd");
        BANK_NAMES.add("Sahebrao Deshmukh Co-Op Bank Ltd");
        BANK_NAMES.add("The Seva Vikas Co-Op Bank Ltd");
        BANK_NAMES.add("Solapur Janata Sahakari Bank Ltd");
        BANK_NAMES.add("The Surat District Co-Op Bank Ltd");
        BANK_NAMES.add("Sutex Co-Op Bank Ltd");
        BANK_NAMES.add("The Varachha Co-Op Bank Ltd");
        BANK_NAMES.add("The Vishweshwar Sahakari Bank Ltd");
        BANK_NAMES.add("The Zoroastrian Co-Op Bank Ltd");
        BANK_NAMES.add("Tumkur Grain Merchants Co-Op Bank Ltd");
        BANK_NAMES.add("UBS AG");
        BANK_NAMES.add("Uco Bank");
        BANK_NAMES.add("Union Bank Of India");
        BANK_NAMES.add("United Bank Of India");
        BANK_NAMES.add("United Overseas Bank Ltd");
        BANK_NAMES.add("Vasai Vikas Sahakari Bank Ltd");
        BANK_NAMES.add("Vijaya Bank");
        BANK_NAMES.add("The West Bengal State Co-Op Bank");
        BANK_NAMES.add("Westpac Banking Corporation");
        BANK_NAMES.add("Woori Bank");
        BANK_NAMES.add("Yes Bank");
        BANK_NAMES.add("Zila Sahkari Bank Ltd Ghaziabad");
        BANK_NAMES.add("Bharatiya Mahila Bank Ltd");
        BANK_NAMES.add("Kerala Gramin Bank");
        BANK_NAMES.add("Pragathi Krishna Gramin Bank");
        BANK_NAMES.add("Surat National Co-Op Bank Ltd");
        BANK_NAMES.add("The HASTI Co-Op Bank Ltd");
        BANK_NAMES.add("KEB Hana Bank");
        BANK_NAMES.add("Samarth Sahakari Bank Ltd");
        BANK_NAMES.add("SBM Bank Mauritius Ltd");
        BANK_NAMES.add("Shivalik Mercantile Co Operative Bank Ltd");
        BANK_NAMES.add("The Pandharpur Urban Co Op. Bank Ltd. Pandharpur");
        BANK_NAMES.add("Himachal Pradesh State Co-Op Bank Ltd");
        BANK_NAMES.add("Deogiri Nagari Sahakari Bank Ltd. Aurangabad");
        BANK_NAMES.add("Maharashtra Gramin Bank");
        BANK_NAMES.add("PT Bank Maybank Indonesia TBK");
        BANK_NAMES.add("Airtel Payments Bank Ltd");
        BANK_NAMES.add("Equitas Small Finance Bank Ltd");
        BANK_NAMES.add("Idukki District Co Operative Bank Ltd");
        BANK_NAMES.add("Telangana State Coop Apex Bank");
        BANK_NAMES.add("The Navnirman Co-Operative Bank Ltd");
        BANK_NAMES.add("Ujjivan Small Finance Bank Ltd");
        BANK_NAMES.add("The Baramati Sahakari Bank Ltd");
        BANK_NAMES.add("Esaf Small Finance Bank Ltd");
        BANK_NAMES.add("Suryoday Small Finance Bank Ltd");
        BANK_NAMES.add("Utkarsh Small Finance Bank");
        BANK_NAMES.add("Textile Traders Co Operative Bank Ltd");
        BANK_NAMES.add("AU Small Finance Bank Ltd");
        BANK_NAMES.add("Paytm Payments Bank Ltd");
        BANK_NAMES.add("The Sindhudurg District Central Coop Bank Ltd");
        BANK_NAMES.add("Fino Payments Bank");
        BANK_NAMES.add("Emirates NBD Bank P J S C");
        BANK_NAMES.add("Fincare Small Finance Bank Ltd");
        BANK_NAMES.add("First Abu Dhabi Bank PJSC");
        BANK_NAMES.add("Kozhikode District Cooperative Bank Ltd");
    }

    public static void main(String[] args) throws Exception {
        List<District> districts = new ArrayList<>();
        File root = new File(args[0]);
        for (final File bank : root.listFiles()) {
            System.out.println(bank.getName());
            if (!bank.getName().startsWith("BankNames") && !bank.getName().startsWith("BankBranchAddress")) {
                String bankName = bank.getName().substring(0, bank.getName().lastIndexOf("."));
                if (bankName.endsWith("0") || bankName.endsWith("1") || bankName.endsWith("2")
                        || bankName.endsWith("3")) {
                    bankName = bankName.substring(0, bankName.length() - 1);
                }
                FileInputStream file = new FileInputStream(bank);
                // Get the workbook instance
                Workbook workBook;
                if (bank.getName().toLowerCase().endsWith("xls")) {
                    workBook = new HSSFWorkbook(file);
                } else {
                    workBook = new XSSFWorkbook(file);
                }

                // Get first sheet from the workbook
                Sheet sheet = workBook.getSheetAt(0);
                District district = null;
                String previousRow = null;
                for (Row row : sheet) {
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
                workBook.close();
            }
        }

        // remove duplicates
        Map<String, Map<String, Set<String>>> banksMap = new TreeMap<>();
        for (District district : districts) {
            Map<String, Set<String>> districtsMap;
            if (banksMap.containsKey(district.getBankName())) {
                districtsMap = banksMap.get(district.getBankName());
            } else {
                districtsMap = new TreeMap<>();
            }
            Set<String> districtsSet;
            if (districtsMap.containsKey(district.getStateName())) {
                districtsSet = districtsMap.get(district.getStateName());
            } else {
                districtsSet = new TreeSet<>();
            }
            districtsSet.add(district.getDistrictName());
            districtsMap.put(district.getStateName(), districtsSet);
            banksMap.put(district.getBankName(), districtsMap);
        }

        // write in excel
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Bank Name");
        int index = 0;
        int row = 0;
        for (Map.Entry<String, Map<String, Set<String>>> state : banksMap.entrySet()) {
            String bankName = state.getKey();
            Map<String, Set<String>> districtsMap = state.getValue();
            for (Map.Entry<String, Set<String>> district : districtsMap.entrySet()) {
                String stateName = district.getKey();
                for (String districtName : district.getValue()) {
                    if (index != 0 && index % 65000 == 0) {
                        FileOutputStream out = new FileOutputStream(
                                new File(args[0] + "/BankBranchAddress_" + (index / 65000) + ".xlsx"));
                        workbook.write(out);
                        workbook = new HSSFWorkbook();
                        sheet = workbook.createSheet("Bank Name");
                        row = 0;
                    }
                    Row bankAddRow = sheet.createRow(row);
                    bankAddRow.createCell(0).setCellValue(index + 1);
                    bankAddRow.createCell(1).setCellValue(bankName);
                    bankAddRow.createCell(2).setCellValue(stateName);
                    bankAddRow.createCell(3).setCellValue(districtName);
                    index++;
                    row++;
                }
            }
        }
        FileOutputStream out = new FileOutputStream(
                new File(args[0] + "/BankBranchAddress_" + ((index / 65000) + 1) + ".xlsx"));
        workbook.write(out);
        out.close();
        workbook.close();
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
