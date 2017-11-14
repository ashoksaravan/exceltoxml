package com.ashoksm.exceltoxml;

import com.thoughtworks.xstream.XStream;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Set;

//7
public class IfscXmlTOSQLConverter {

    /**
     * SQL.
     */
    private static final String SQL =
            "INSERT INTO bank_branch_t VALUES('<BranchName>', '<City>', '<Address>', '<Contact>', '<MicrCode>', '<IfscCode>', <BankLoc>);";

    public static void main(String[] args) throws Exception {
        Set<String> locSet = new HashSet<>();
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
                    String loc = bankBranch.getName().trim() + "_" + bankBranch.getIfscCode() + "_"
                            + bankBranch.getBankLoc().toString();
                    if (locSet.add(loc)) {
                        line = SQL.replaceAll("<BranchName>", bankBranch.getName().trim().replaceAll("'", "''"))
                                .replaceAll("<City>", bankBranch.getCity().trim().replaceAll("'", "''"))
                                .replaceAll("<Address>", bankBranch.getAddress().trim().replaceAll("'", "''"))
                                .replaceAll("<Contact>", bankBranch.getContact().trim().replaceAll("'", "''"))
                                .replaceAll("<MicrCode>", bankBranch.getMicrCode())
                                .replaceAll("<IfscCode>", bankBranch.getIfscCode())
                                .replaceAll("<BankLoc>", bankBranch.getBankLoc().toString());
                    } else {
                        System.out.println(loc);
                    }
                } catch (Exception ex) {
                    throw new Exception("In exception ::::: " + bankBranch.getName(), ex);
                } finally {
                    if (line != null) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }
            writer.flush();
            writer.close();
        }
        System.out.println(locSet.size());
    }
}
