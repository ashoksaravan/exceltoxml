package com.sample;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Test extends JFrame implements ActionListener{

	public Test (){
		JButton hi = new JButton("Hi");
		hi.setSize(500, 500);
		hi.addActionListener(this);
		this.getContentPane().add(hi);
	}
	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		new Test().setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		String event = (String) e.getActionCommand();
		if (event.equals("Hi")) {
			System.out.println("Hi");
		} 
	}

}
