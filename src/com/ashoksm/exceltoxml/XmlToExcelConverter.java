package com.ashoksm.exceltoxml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.thoughtworks.xstream.XStream;

public class XmlToExcelConverter {

	public static void main(String[] args) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Ariyalur");
		XStream xStream = new XStream();
		xStream.alias("office", Office.class);
		xStream.alias("offices", Offices.class);
		xStream.addImplicitCollection(Offices.class, "offices");
		FileInputStream file = new FileInputStream(new File("tamilnadu_ariyalur.xml"));
		Offices offices = (Offices) xStream.fromXML(file);
		int index = 0;
		for (Office office : offices.getOffices()) {
			HSSFRow officeNameRow = sheet.createRow(index);
			officeNameRow.createCell(0).setCellValue("OfficeName");
			officeNameRow.createCell(1).setCellValue(office.getName());
			index++;
			HSSFRow pinCodeRow = sheet.createRow(index);
			pinCodeRow.createCell(0).setCellValue("Pincode");
			pinCodeRow.createCell(1).setCellValue(office.getPinCode());
			index++;
			HSSFRow statusRow = sheet.createRow(index);
			statusRow.createCell(0).setCellValue("status");
			statusRow.createCell(1).setCellValue(office.getStatus());
			index++;
			if (office.getSuboffice() != null && office.getSuboffice().trim().length() > 0) {
				HSSFRow subOfficeRow = sheet.createRow(index);
				subOfficeRow.createCell(0).setCellValue("SubOffice");
				subOfficeRow.createCell(1).setCellValue(office.getSuboffice());
				index++;
			}
			if (office.getHeadoffice() != null && office.getHeadoffice().trim().length() > 0) {
				HSSFRow headOfficeRow = sheet.createRow(index);
				headOfficeRow.createCell(0).setCellValue("HeadOffice");
				headOfficeRow.createCell(1).setCellValue(office.getHeadoffice());
				index++;
			}
			if (office.getLocation() != null && office.getLocation().trim().length() > 0) {
				HSSFRow headOfficeRow = sheet.createRow(index);
				headOfficeRow.createCell(0).setCellValue("Location");
				headOfficeRow.createCell(1).setCellValue(office.getLocation());
				index++;
			}
			if (office.getTelephone() != null && office.getTelephone().trim().length() > 0) {
				HSSFRow headOfficeRow = sheet.createRow(index);
				headOfficeRow.createCell(0).setCellValue("Telephone");
				headOfficeRow.createCell(1).setCellValue(office.getTelephone());
				index++;
			}
		}
		FileOutputStream out = new FileOutputStream(new File("Ariyalur.xls"));
		workbook.write(out);
		out.close();
		System.out.println("Excel written successfully..");
	}
}
