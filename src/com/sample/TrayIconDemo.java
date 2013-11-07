package com.sample;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.Timer;

public class TrayIconDemo {

	public static void main(String[] args) {
		PopupFactory factory = PopupFactory.getSharedInstance();

		JOptionPane optionPane = new JOptionPane("Hi" + " Is trying to contact you",
				JOptionPane.PLAIN_MESSAGE, 10, null, null, "Net Stat");
		JDialog dialog = optionPane.createDialog(null, "Net Stat");
		JButton label = new JButton("Hi" + " Is trying to contact you");
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
		Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
		int x = (int) rect.getMaxX()- dialog.getWidth();
		int y = (int) rect.getMaxY() - dialog.getHeight();
	
		final Popup popup = factory.getPopup(null, label, x, y);
		popup.show();
		ActionListener hider = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				popup.hide();
			}
		};
		// Hide popup in 3 seconds
		Timer timer = new Timer(3000, hider);
		timer.start();
	}
}