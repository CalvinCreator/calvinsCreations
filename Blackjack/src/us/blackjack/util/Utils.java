package us.blackjack.util;

import java.io.FileInputStream;
import java.io.IOException;

public class Utils
{
	public Utils() {}

	public static String[] loadPropertiesFile(java.io.File f)
	{
		try
		{
			FileInputStream fi = new FileInputStream(f);
			String file = "";
			int c;
			while ((c = fi.read()) != -1) { 
				file = file + String.valueOf((char)c);
			}

			String[] properties = new String[4];
			properties[0] = file.substring("username: ".length(), file.indexOf("password: "));
			properties[1] = file.substring(file.indexOf("password: ") + "password: ".length(), 
					file.indexOf("ip_address: "));
			properties[2] = file.substring(file.indexOf("ip_address: ") + "ip_address: ".length(), 
					file.indexOf("port: "));
			properties[3] = file.substring(file.indexOf("port: ") + "port: ".length());
			fi.close();

			return properties;
		}
		catch (IOException e) {
			e.printStackTrace();
		} catch (StringIndexOutOfBoundsException e) {
			String[] properties = new String[4];
			properties[0] = "";
			properties[1] = "";
			properties[2] = "";
			properties[3] = "";
			return properties;
		}

		return null;
	}

	public static String getTimeStamp() {
		java.util.Date date = new java.util.Date();
		java.sql.Timestamp stamp = new java.sql.Timestamp(date.getTime());

		String s = "";
		s = stamp.getHours() + ":" + stamp.getMinutes() + ":" + stamp.getSeconds();
		return s;
	}

	public static String[][] getAccounts() {
		java.io.File f = new java.io.File("C:\\ProgramData/Blackjack/Server/users.txt");
		try
		{
			FileInputStream fi = new FileInputStream(f);

			String file = "";
			int c;
			while ((c = fi.read()) != -1) { 
				file = file + String.valueOf((char)c);
			}
			String[] users = file.split(">");
			for (int i = 0; i < users.length; i++) {
				users[i] = users[i].replaceAll("<", "");
				users[i] = users[i].replaceAll(">", "");
			}

			String[][] accounts = new String[users.length][2];
			for (int i = 0; i < users.length; i++) {
				accounts[i] = users[i].split(",");
				for (int k = 0; k < accounts[i].length; k++) {
					accounts[i][k] = accounts[i][k].trim();
				}
			}
			return accounts;
		} catch (IOException e) {
			e.printStackTrace(); }
		return null;
	}
}
