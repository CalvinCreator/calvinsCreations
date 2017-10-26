package us.blackjack.client;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

import us.blackjack.util.Utils;

public class BlackjackClient {

	private static Socket socket = null;

	public static void main(String[] args) {

		try {
			// load properties file
			boolean b = new File("C:\\ProgramData/Blackjack").mkdir();
			File f = new File("C:\\ProgramData/Blackjack/properties.txt");
			String username, password, ip_address, port;
			if (!f.exists()) {
				username = "";
				password = "";
				ip_address = "";
				port = "";
				try {
					PrintWriter writer = new PrintWriter(f, "UTF-8");
					writer.println("username: ");
					writer.println("password: ");
					writer.println("ip_address: 100.36.16.123");
					writer.println("port: 6664");
					port = "6664";
					ip_address = "100.36.16.123";
					writer.flush();
					writer.close();
				} catch (FileNotFoundException e) {
					JOptionPane.showMessageDialog(null, e.getMessage() + "\nPlease try again.");
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					JOptionPane.showMessageDialog(null, e.getMessage() + "\nPlease try again.");
					e.printStackTrace();
				}
			} else {
				String[] properties = Utils.loadPropertiesFile(f);
				username = properties[0];
				password = properties[1];
				ip_address = properties[2];
				port = properties[3];
				if (port.equals(""))
					port = "6664";
				if (ip_address.equals(""))
					ip_address = "100.36.16.123";
			}

			try {
				URL whatismyip = new URL("http://checkip.amazonaws.com");
				BufferedReader in;
				in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
				String ip = in.readLine();
				if (ip.trim().equals(ip_address.trim())) // if trying to connect
															// to same computer
					ip_address = "localhost";
			} catch (IOException e2) {
				JOptionPane.showMessageDialog(null, e2.getMessage() + "\nPlease try again.");
				e2.printStackTrace();
				System.exit(0);
			}

			// get username and password
			String[] s = getUsernameAndPassword(username, password);
			username = s[0];
			password = s[1];

			// connect to server
			try {
				port = port.trim();
				ip_address = ip_address.trim();
				System.out.println("Creating socket at " + ip_address + " with port " + port + ".");
				socket = new Socket(ip_address, Integer.valueOf(port));
				System.out.println("Socket created.");

				// Verify that username and password are correct
				PrintWriter output = new PrintWriter(socket.getOutputStream());
				BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				output.println("/login::" + username + "," + password);
				output.flush();
				System.out.println("Waiting for response...");
				String response = input.readLine();
				System.out.println("Response recieved.");
				if (response.equals("false")) {
					JOptionPane.showMessageDialog(null, "Login failed. Please try again.");
					output.println("/exit::");
					output.flush();
					output.close();
					input.close();
					socket.close();
					System.exit(0);
				}

				// save username and password to properties file
				System.out.println("Logging in.");
				try {
					PrintWriter writer = new PrintWriter(f, "UTF-8");
					writer.println("username: " + username);
					writer.println("password: " + password);
					writer.println("ip_address: " + ip_address);
					writer.println("port: " + port);
					writer.flush();
					writer.close();
					System.out.println("Properties succesfully saved.");
				} catch (FileNotFoundException e) {
					System.out.println("Properties file not found.");
					JOptionPane.showMessageDialog(null, e.getMessage() + "\nProperties file not found.");
					System.exit(0);
				} catch (UnsupportedEncodingException e) {
					System.out.println("Unsoported encoding for properties file.");
					JOptionPane.showMessageDialog(null, e.getMessage() + "\nUnsoported encoding for properties file.");
					e.printStackTrace();
					System.exit(0);
				}

				// show game client and join a game game
				output.println("/join::");
				output.flush();
				JFrame frame = new JFrame("Blackjack");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				BlackjackPanel p = new BlackjackPanel(input, output, username);
				Thread t = new Thread(p);

				frame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						p.setRunning(false);
						output.println("/logout::");
						output.flush();
						output.close();
						System.out.println("exiting...");
						try {
							input.close();
							socket.close();
						} catch (IOException e2) {
							JOptionPane.showMessageDialog(null, e2.getMessage() + "\nPlease try again.");
							System.exit(0);
						}

						e.getWindow().dispose();
					}
				});

				frame.setContentPane(p);
				frame.setSize(566, 414);
				frame.setLocationRelativeTo(null);
				frame.setResizable(false);
				frame.setVisible(true);

				t.start();

			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, e.getMessage() + "\nPlease try again.");
				e.printStackTrace();
				if (socket != null)
					try {
						System.out.println("socket closed.");
						socket.close();
					} catch (IOException e1) {
					}
			} catch (UnknownHostException e) {
				JOptionPane.showMessageDialog(null,
						"Unknown Host, please try again. \nIP Address: " + ip_address + "Port: " + port);
				if (socket != null)
					try {
						System.out.println("socket closed.");
						socket.close();
					} catch (IOException e1) {
					}
				System.exit(-1);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, e.getMessage() + "\nIP Address: " + ip_address + "\nPort: " + port
						+ "\nPlease check your internet connection.");
				if (socket != null)
					try {
						System.out.println("socket closed.");
						socket.close();
					} catch (IOException e1) {
					}
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage() + "\nPlease try again.");
			System.exit(0);
		}

	}

	private static String[] getUsernameAndPassword(String username, String password) {
		try {
			JPanel f = new JPanel();
			JTextField field1;
			JPasswordField field2;

			field1 = new JTextField(16);
			if (username == null || username == "")
				field1.setText("Username");
			else
				field1.setText(username);
			field1.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					field1.setText("");
				}
			});
			field2 = new JPasswordField(16);
			field2.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					field2.setText("");
				}
			});
			if (password == null || password == "")
				field2.setText("Password");
			else
				field2.setText(password);

			f.add(field1);
			f.add(field2);

			JOptionPane.showConfirmDialog(null, f, null, JOptionPane.CANCEL_OPTION);

			String[] props = new String[2];
			props[0] = field1.getText().trim();
			props[1] = field2.getText().trim();
			return props;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage() + "\nPlease try again.");
			System.exit(0);
			return null;
		}
	}
}
