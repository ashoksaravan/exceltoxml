package com.ashoksm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

import com.thoughtworks.xstream.XStream;

public class IfscXmlTOSQLConverter {

	/** SQL. */
	private static final String SQL = "INSERT INTO bank_branch_t VALUES('<BranchName>', '<City>', '<Address>', '<Contact>', '<MicrCode>', '<IfscCode>', <BankLoc>);";

	public static void main(String[] args) throws Exception {
		XStream xStream = new XStream();
		xStream.alias("bankbranch", BankBranch.class);
		xStream.alias("bankBranchs", BankBranchs.class);
		xStream.addImplicitCollection(BankBranchs.class, "bankBranchs");
		File root = new File("xml\\ifsc\\");
		for (final File bank : root.listFiles()) {
			System.out.println(bank.getName());
			BufferedWriter writer = new BufferedWriter(new FileWriter(args[0] + "\\"
					+ bank.getName().substring(0, bank.getName().lastIndexOf(".")) + ".sql"));
			FileInputStream file = new FileInputStream(bank);
			BankBranchs bankBranchs = (BankBranchs) xStream.fromXML(file);
			for (BankBranch bankBranch : bankBranchs.getBankBranchs()) {
				String line = null;
				try {
					line = SQL.replaceAll("<BranchName>", bankBranch.getName().trim().replaceAll("'", "''"))
							.replaceAll("<City>", bankBranch.getCity().trim().replaceAll("'", "''"))
							.replaceAll("<Address>", bankBranch.getAddress().trim().replaceAll("'", "''"))
							.replaceAll("<Contact>", bankBranch.getContact().trim().replaceAll("'", "''"))
							.replaceAll("<MicrCode>", bankBranch.getMicrCode())
							.replaceAll("<IfscCode>", bankBranch.getIfscCode())
							.replaceAll("<BankLoc>", bankBranch.getBankLoc().toString());
				} catch (Exception ex) {
					ex.printStackTrace();
					System.out.println("In exception ::::: " + bankBranch.getName());
				}
				writer.write(line);
				writer.newLine();
			}
			writer.flush();
			writer.close();
		}
	}
}
