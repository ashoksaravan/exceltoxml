package com.ashoksm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CorrectLocNum {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(
				"E:\\Android\\workspace\\OfflinePinFinder\\assets\\sql\\ifsc\\bankofindia_2.sql"));
		try {
			StringBuilder sb = new StringBuilder();
			String line;

			while ((line = br.readLine()) != null) {
				String first = line.substring(0, line.lastIndexOf(", ") + 1);
				String second = line.substring(line.lastIndexOf(", ") + 1);
				System.out.println(first + " 9" + second.trim());
				// sb.append(line);
				// sb.append(System.lineSeparator());
				// line = br.readLine();
			}
			// String everything = sb.toString();
		} finally {
			br.close();
		}
	}

}
