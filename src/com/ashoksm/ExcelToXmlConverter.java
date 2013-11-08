package com.ashoksm;

import java.io.File;
import java.io.FileInputStream;
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

public class ExcelToXmlConverter {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		File root = new File(args[0]);
		for (final File state : root.listFiles()) {
			System.out.println("***** " + state.getName() + " *****");
			List<Office> offices = new ArrayList<Office>();
			if (state.isDirectory()) {
				for (final File district : state.listFiles()) {
					System.out.println(district.getName());
					offices.addAll(buildOffices(district));
				}
			}
			System.out.println(offices.size());
			writeFile(offices, state.getName().toLowerCase(), "xml\\");
			System.out.println("***** END *****");
		}
	}

	/**
	 * @param district
	 * @return
	 * @throws IOException
	 */
	private static List<Office> buildOffices(File district) throws IOException {
		FileInputStream file = new FileInputStream(district);
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

				if ("fficeName".equalsIgnoreCase(previousColumn) || "ficeName".equalsIgnoreCase(previousColumn)
						|| "iceName".equalsIgnoreCase(previousColumn) || "ceName".equalsIgnoreCase(previousColumn)
						|| "eName".equalsIgnoreCase(previousColumn) || "Name".equalsIgnoreCase(previousColumn)) {
					System.out.println(cellValue + " failed");
				}
				if ("OfficeName".equalsIgnoreCase(previousColumn)) {
					office.setName(cellValue.replaceAll(" S.O", "").replaceAll(" B.O", "").replaceAll(" H.O", "")
							.replaceAll(" G.P.O", ""));
				} else if ("Pincode".equalsIgnoreCase(previousColumn)) {
					try {
						office.setPinCode(cellValue.substring(0, cellValue.lastIndexOf(".")));
					} catch (Exception ex) {
						office.setPinCode(cellValue);
					}
				} else if ("status".equalsIgnoreCase(previousColumn)) {
					office.setStatus(cellValue);
				} else if ("SubOffice".equalsIgnoreCase(previousColumn)) {
					office.setSuboffice(cellValue.replaceAll(" S.O", ""));
				} else if ("HeadOffice".equalsIgnoreCase(previousColumn)) {
					office.setHeadoffice(cellValue.replaceAll(" H.O", "").replaceAll(" G.P.O", ""));
				} else if ("Location".equalsIgnoreCase(previousColumn)) {
					office.setLocation(cellValue);
					String districtNm = null;
					try {
						districtNm = cellValue.substring(cellValue.toLowerCase().indexOf(" taluk of") + 9, cellValue
								.toLowerCase().indexOf(" district"));
					} catch (Exception ex) {
						System.out.println(districtNm);
						System.out.println(office.getName() + " failed");
					}
				} else if ("Telephone".equalsIgnoreCase(previousColumn)) {
					office.setTelephone(cellValue);
				}
				previousColumn = cellValue;
			}
		}
		return offices;
	}

	/**
	 * @param offices
	 * @param stateName
	 */
	private static void writeFile(List<Office> offices, String stateName, String dest) {
		if (offices.size() > 3499) {
			List<List<Office>> partition = new ArrayList<List<Office>>();
			List<Office> subOffices = new ArrayList<Office>();
			boolean flag = false;
			int i = 1;
			for (Office office : offices) {
				subOffices.add(office);
				if (i == 3500) {
					partition.add(subOffices);
					subOffices = new ArrayList<Office>();
					i = 1;
					flag = true;
				} else {
					i++;
					flag = false;
				}
			}
			if (!flag) {
				partition.add(subOffices);
			}
			i = 1;
			for (List<Office> list : partition) {
				String tempStateName = stateName + "_" + i;
				createXML(list, tempStateName, dest);
				i++;
			}
		} else {
			createXML(offices, stateName, dest);
		}
	}

	/**
	 * @param offices
	 * @param stateName
	 */
	private static void createXML(List<Office> offices, String stateName, String dest) {
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
		FileOutputStream fop = null;
		File file;

		try {

			file = new File(dest +stateName + ".xml");
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
