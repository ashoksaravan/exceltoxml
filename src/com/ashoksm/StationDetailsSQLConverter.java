package com.ashoksm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

public class StationDetailsSQLConverter {

	private static final String SQL = "INSERT INTO train_detail_t VALUES(<StationNo>, <TrainNo>, '<StationCode>', '<Arrives>',"
			+ " '<Departs>', '<StopTime>', '<Distance_Travelled>', <Day>, <Route>);";

	public static void main(String[] args) throws Exception {
		File root = new File("/home/local/BSILIND/ashok/Downloads/railwaycodes");
		for (File stationDetails : root.listFiles()) {
			if (stationDetails.getName().startsWith("traindetails")) {
				FileInputStream file = new FileInputStream(stationDetails);
				HSSFWorkbook workbook = new HSSFWorkbook(file);
				HSSFSheet sheet = workbook.getSheetAt(0);
				BufferedWriter writer = new BufferedWriter(new FileWriter(
						"/home/local/BSILIND/ashok/MySpace/OfflinePinFinder/app/src/main/assets/sql/railway/"
								+ stationDetails.getName().substring(0, stationDetails.getName().lastIndexOf("."))
								+ ".sql"));

				Set<String> trains = new HashSet<>();
				Iterator<Row> rowIterator = sheet.iterator();
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					String stationCode = row.getCell(0).getStringCellValue();
					String trainNo = row.getCell(1).getStringCellValue();
					if (trains.add(stationCode + trainNo)) {
						String line = SQL.replaceAll("<TrainNo>", row.getCell(0).getStringCellValue())
								.replaceAll("<StationNo>", row.getCell(1).getStringCellValue())
								.replaceAll("<StationCode>", row.getCell(2).getStringCellValue())
								.replaceAll("<Arrives>", row.getCell(3).getStringCellValue())
								.replaceAll("<Departs>", row.getCell(4).getStringCellValue())
								.replaceAll("<StopTime>", row.getCell(5).getStringCellValue())
								.replaceAll("<Distance_Travelled>", row.getCell(6).getStringCellValue())
								.replaceAll("<Day>", row.getCell(7).getStringCellValue())
								.replaceAll("<Route>", row.getCell(8).getStringCellValue());
						writer.write(line);
						writer.newLine();
					} else {
						System.out.println("Duplicate Code:::" + stationCode + ":::" + trainNo);
					}
				}
				writer.flush();
				writer.close();
			}
		}
	}

}
