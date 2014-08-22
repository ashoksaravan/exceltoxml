package com.ashoksm;

public class Sample {

	public static void main(String[] args) {
		String foo = "east(e)";
		String boz = foo.replaceAll("\\(e\\)", "(E)"); 
		System.out.println(boz);
	}

}
