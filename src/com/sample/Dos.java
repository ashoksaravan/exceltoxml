package com.sample;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.StringTokenizer;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.Timer;

public class Dos {

	public static void main(String[] args) {
		Collection<String> ports = new HashSet<String>();
		while (true) {
			try {

				Process p = Runtime.getRuntime().exec("netstat -a");

				BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

				BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

				// read the output from the command
				String s = null;
				Collection<String> tempPorts = new HashSet<String>();
				while ((s = stdInput.readLine()) != null) {
					if(s.startsWith("  Proto")) {
						continue;
					}
					StringTokenizer tokenizer = new StringTokenizer(s, " ");
					int i = 0;
					boolean show = false;
					while (tokenizer.hasMoreElements()) {
						String temp = (String) tokenizer.nextElement();
						if (i == 1) {
							tempPorts.add(temp);
							if (!ports.contains(temp)) {
								show = true;
							}
						}
						if (i == 2 && show) {
							System.out.println(temp);
							PopupFactory factory = PopupFactory.getSharedInstance();

							//JButton label = new JButton(temp + " Is trying to contact you");
							
							JOptionPane optionPane = new JOptionPane(temp + " Is trying to contact you",
									JOptionPane.PLAIN_MESSAGE, JOptionPane.CLOSED_OPTION, null, null, "Net Stat");
							JDialog dialog = optionPane.createDialog(null, "Net Stat");
							
							GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
							GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
							Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
							int x = (int) rect.getMaxX() - dialog.getWidth();
							int y = (int) rect.getMaxY() - dialog.getHeight();
							//dialog.setLocation(x, y - 50);
							//dialog.setVisible(true);
							//label.setSize(dialog.getWidth(), 500);
							final Popup popup = factory.getPopup(null, optionPane, x, y);
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
						i++;
					}
				}
				ports = tempPorts;
				// read any errors from the attempted command

				while ((s = stdError.readLine()) != null) {
					System.out.println(s);
				}
			}

			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
