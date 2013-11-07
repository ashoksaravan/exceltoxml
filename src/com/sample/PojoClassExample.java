package com.sample;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ListIterator;
import java.io.*;

class Pojo {
	String strName = "";

	public String getStrName() {
		return strName;
	}

	public void setStrName(String strName) {
		this.strName = strName;
	}

	public String getStrAdress() {
		return strAdress;
	}

	public void setStrAdress(String strAdress) {
		this.strAdress = strAdress;
	}

	String strAdress = "";

	public String toString() {
		return "Name :" + this.strName + ", " + "Address:" + this.strAdress;

	}
}

public class PojoClassExample {
	public static void main(String args[]) throws IOException {
		Pojo objPojo = new Pojo();

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Please select curd operation of which one do u want ");
		System.out.println("1-Insert");
		System.out.println("2-Update");
		System.out.println("3-Select");
		System.out.println("4-Delete");

		String strcurdOperaion = br.readLine();
		int curdOperaion = Integer.parseInt(strcurdOperaion);

		switch (curdOperaion) {

		case 1:

			Pojo insertPojo = new Pojo();

			System.out.println("Enter values");

			System.out.println("Please enter employe name");

			insertPojo.setStrName(br.readLine());

			System.out.println("Please enter employe detalis");

			insertPojo.setStrAdress(br.readLine());

			System.out.println("insertPojo:" + insertPojo.toString());

			Pojo insertPojo1 = new Pojo();

			System.out.println("Enter values");

			System.out.println("Please enter employe name");

			insertPojo1.setStrName(br.readLine());

			System.out.println("Please enter employe detalis");

			insertPojo1.setStrAdress(br.readLine());

			System.out.println("insertPojo1:" + insertPojo1.toString());

			ArrayList<Pojo> al = new ArrayList<Pojo>();
			al.add(insertPojo);
			al.add(insertPojo1);

			ListIterator<Pojo> litr = al.listIterator();

			while (litr.hasNext()) {
				System.out.println(litr.next());
			}
			break;

		case 2:

			System.out.println("update operation");

			Pojo updatePojo = new Pojo();

			System.out.println("Enter values");

			System.out.println("Please enter employe name");

			updatePojo.setStrName(br.readLine());

			System.out.println("Please enter employe detalis");

			updatePojo.setStrAdress(br.readLine());

			System.out.println("updatePojo:" + updatePojo.toString());

			Pojo updatePojo1 = new Pojo();

			System.out.println("Enter values");

			System.out.println("Please enter employe name");

			updatePojo1.setStrName(br.readLine());

			System.out.println("Please enter employe detalis");

			updatePojo1.setStrAdress(br.readLine());

			System.out.println("updatePojo1:" + updatePojo1.toString());

			ArrayList<Pojo> al1 = new ArrayList<Pojo>();
			al1.add(updatePojo);
			ArrayList<Pojo> al2 = new ArrayList<Pojo>();
			al2.add(updatePojo1);

			ListIterator<Pojo> litr1 = al1.listIterator();

			while (litr1.hasNext()) {
				System.out.println(litr1.next());
			}
			break;

		case 3:

			System.out.println("delete operation");

			Pojo deletePojo = new Pojo();

			System.out.println("Enter values");

			System.out.println("Please enter employe name");

			deletePojo.setStrName(br.readLine());

			System.out.println("Please enter employe detalis");

			ArrayList<Pojo> al3 = new ArrayList<Pojo>();
			al3.add(deletePojo);

			System.out.println("delete operation");

			Pojo deletePojo1 = new Pojo();

			System.out.println("Enter values");

			System.out.println("Please enter employe name");

			deletePojo.setStrName(br.readLine());

			System.out.println("Please enter employe detalis");

			ArrayList<Pojo> al4 = new ArrayList<Pojo>();
			al4.add(deletePojo1);
			al3.removeAll(al4);
			ListIterator<Pojo> litr3 = al3.listIterator();

			while (litr3.hasNext()) {
				System.out.println(litr3.next());

			}
			break;

		case 4:

			System.out.println("sort Operation");
			break;

		default:
			System.out.println("Please select either 1,2,3,4");
			break;
		}

	}

}
