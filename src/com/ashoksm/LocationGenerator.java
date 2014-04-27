package com.ashoksm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.thoughtworks.xstream.XStream;

public class LocationGenerator {

	public static void main(String[] args) throws IOException {
		Set<String> locations = new TreeSet<String>();
		XStream xStream = new XStream();
		xStream.alias("office", Office.class);
		xStream.alias("offices", Offices.class);
		xStream.addImplicitCollection(Offices.class, "offices");
		File root = new File("xml");
		for (final File state : root.listFiles()) {
			System.out.println(state.getName());
			FileInputStream file = new FileInputStream(state);
			Offices offices = (Offices) xStream.fromXML(file);
			for (Office office : offices.getOffices()) {
				locations.add(office.getLocation());
			}
		}
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Locations");
		int index = 0;
		for (String loc : locations) {
			HSSFRow officeNameRow = sheet.createRow(index);
			officeNameRow.createCell(0).setCellValue(index + 1);
			officeNameRow.createCell(1).setCellValue(loc);
			index++;
		}
		
		FileOutputStream out = new FileOutputStream(new File("E:\\Ashok\\Dropbox\\MyDetails\\pinfinder\\locations.xls"));
		workbook.write(out);
		out.close();
		System.out.println("Excel written successfully...");
	}
}
