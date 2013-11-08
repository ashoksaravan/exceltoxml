package com.ashoksm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import com.thoughtworks.xstream.XStream;

public class XmlToSQLConverter {

	private static String sql = "INSERT INTO post_office_t VALUES('<OfficeName>', <Pincode>, '<Status>', '<SubOffice>', '<HeadOffice>', '<Location>', '<District>', '<State>', '<Telephone>');";

	public static void main(String[] args) throws IOException {
		XStream xStream = new XStream();
		xStream.alias("office", Office.class);
		xStream.alias("offices", Offices.class);
		xStream.addImplicitCollection(Offices.class, "offices");
		File root = new File("xml");
		for (final File state : root.listFiles()) {
			System.out.println(state.getName());
			BufferedWriter writer = new BufferedWriter(new FileWriter(args[0] + "\\"
					+ state.getName().substring(0, state.getName().lastIndexOf(".")) + ".sql"));
			FileInputStream file = new FileInputStream(state);
			Offices offices = (Offices) xStream.fromXML(file);
			for (Office office : offices.getOffices()) {
				String line = null;
				try {
				line = sql
						.replaceAll("<OfficeName>", office.getName().trim().replaceAll("'", "''"))
						.replaceAll("<Pincode>", office.getPinCode())
						.replaceAll("<Status>", office.getStatus().trim().replaceAll("'", "''"))
						.replaceAll("<HeadOffice>", office.getHeadoffice().trim().replaceAll("'", "''"))
						.replaceAll("<SubOffice>", office.getSuboffice().trim().replaceAll("'", "''"))
						.replaceAll("<Location>", office.getLocation().trim().replaceAll("'", "''"))
						.replaceAll("<Telephone>", office.getTelephone().trim())
						.replaceAll("<State>", getStateName(state.getName()))
						.replaceAll(
								"<District>",
								office.getLocation().substring(
										office.getLocation().toLowerCase().indexOf("taluk of ") + 9,
										office.getLocation().toLowerCase().indexOf("district")));
				} catch (Exception ex) {
					System.out.println("In exception ::::: " + office.getName() + "::::" + office.getLocation());
				}
				writer.write(line);
				writer.newLine();
			}
			writer.flush();
			writer.close();
		}
	}

	private static String getStateName(String xmlName) {
		String stateName = null;
		if (xmlName.contains("andamanandnicobarislands")) {
			stateName = "Andaman and Nicobar Islands";
		} else if (xmlName.contains("andhrapradesh")) {
			stateName = "Andhra Pradesh";
		} else if (xmlName.contains("arunachalpradesh")) {
			stateName = "Arunachal Pradesh";
		} else if (xmlName.contains("chandigarh")) {
			stateName = "Chandigarh";
		} else if (xmlName.contains("dadraandnagarhaveli")) {
			stateName = "Dadra and Nagar Haveli";
		} else if (xmlName.contains("damananddiu")) {
			stateName = "Daman and Diu";
		} else if (xmlName.contains("delhi")) {
			stateName = "Delhi";
		} else if (xmlName.contains("goa")) {
			stateName = "Goa";
		} else if (xmlName.contains("karnataka")) {
			stateName = "Karnataka";
		} else if (xmlName.contains("kerala")) {
			stateName = "Kerala";
		} else if (xmlName.contains("lakshadweep")) {
			stateName = "Lakshadweep";
		} else if (xmlName.contains("manipur")) {
			stateName = "Manipur";
		} else if (xmlName.contains("meghalaya")) {
			stateName = "Meghalaya";
		} else if (xmlName.contains("mizoram")) {
			stateName = "Mizoram";
		} else if (xmlName.contains("nagaland")) {
			stateName = "Nagaland";
		} else if (xmlName.contains("puducherry")) {
			stateName = "Puducherry";
		} else if (xmlName.contains("sikkim")) {
			stateName = "Sikkim";
		} else if (xmlName.contains("tamilnadu")) {
			stateName = "Tamil Nadu";
		} else if (xmlName.contains("tripura")) {
			stateName = "Tripura";
		} else if (xmlName.contains("maharashtra")) {
			stateName = "Maharashtra";
		}
		return stateName;
	}
}
