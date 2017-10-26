package us.menu;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import us.encrypt.AES;
import us.main.Constants;

public class MainMenu {

	private ToggleButton encrypt, decrypt;
	private Button start, chooseFile;

	private File file;

	public MainMenu() {
		// Encrypt button
		encrypt = new ToggleButton();
		encrypt.setBoundary(new RoundRectangle2D.Float(15, 100, 120, 30, 9, 9));
		encrypt.setNoUseColor(new Color(70, 25, 220));
		encrypt.setMouseOverColor(new Color(40, 25, 150));
		encrypt.setClickColor(new Color(20, 10, 100));
		encrypt.setText("ENCRYPTION");
		encrypt.setSelectedTextSize(24); // normal size
		encrypt.setTextLocation(3, 25);
		encrypt.useBoundary(false);

		// Decrypt button
		decrypt = new ToggleButton();
		decrypt.setBoundary(new RoundRectangle2D.Float(150, 100, 120, 30, 9, 9));
		decrypt.setNoUseColor(new Color(70, 25, 220));
		decrypt.setMouseOverColor(new Color(40, 25, 150));
		decrypt.setClickColor(new Color(20, 20, 100));
		decrypt.setText("DECRYPTION");
		decrypt.setSelectedTextSize(24);
		decrypt.setTextLocation(3, 25);
		decrypt.useBoundary(false);

		// Start button
		start = new Button();
		start.setBoundary(new RoundRectangle2D.Float(115, 135, 60, 30, 9, 9));
		start.setNoUseColor(new Color(70, 25, 220));
		start.setMouseOverColor(new Color(40, 25, 150));
		start.setClickColor(new Color(20, 10, 100));
		start.setText("BEGIN");
		start.setTextLocation(2, 25);
		start.useBoundary(false);

		// Choose File
		chooseFile = new Button();
		chooseFile.setBoundary(new RoundRectangle2D.Float(65, 74, 200, 23, 5, 5));
		chooseFile.setMouseOverColor(new Color(240, 240, 240));
		chooseFile.setClickColor(new Color(220, 220, 220));

	}

	public void draw(Graphics2D g) {
		encrypt.draw(g);
		decrypt.draw(g);
		start.draw(g);
		chooseFile.draw(g);

		drawLogo(g);

		// Draw "File"
		g.setColor(new Color(70, 25, 220));
		g.setFont(new Font("Impact", 12, 24));
		g.drawString("FILE:", 18, 95);

		if (file != null) { // if a file has been chosen...
			g.setFont(new Font("Impact", 12, 18));
			g.drawString(file.getName(), 68, 92);
		}
	}

	private void drawLogo(Graphics2D g) {
		// Outline
		g.setStroke(new BasicStroke(3));
		g.setColor(Color.black);
		g.draw(new RoundRectangle2D.Float(6, 24, Constants.WIDTH - 12, Constants.HEIGHT - 30, 29, 29));
		g.setStroke(new BasicStroke(1));

		// Title
		g.setColor(Color.white);
		g.fillRect(Constants.WIDTH / 2 - 40, 0, 80, 30);

		g.setColor(Color.black);
		g.setFont(new Font("Impact", 12, 30));
		g.drawString("CYBER", Constants.WIDTH / 2 - 38, 28);
		g.drawString("STRONG", Constants.WIDTH / 2 - 47, 55);
	}

	// Input
	public void mouseClicked(MouseEvent k) {
		encrypt.mouseClicked(k);
		decrypt.mouseClicked(k);
		start.mouseClicked(k);
		chooseFile.mouseClicked(k);
	}

	public void mouseEntered(MouseEvent k) {

	}

	public void mouseExited(MouseEvent k) {

	}

	public void mousePressed(MouseEvent k) {
		encrypt.mousePressed(k);
		decrypt.mousePressed(k);
		start.mousePressed(k);
		chooseFile.mousePressed(k);
	}

	public void mouseReleased(MouseEvent k) {
		encrypt.mouseReleased(k);
		decrypt.mouseReleased(k);
		start.mouseReleased(k);
		chooseFile.mouseReleased(k);

		if (chooseFile.isClicked()) {
			JFileChooser chooser = new JFileChooser();
			chooser.showOpenDialog(null);
			file = chooser.getSelectedFile();
		}

		// Link encrypt and decrypt buttons
		if (encrypt.isClicked()) {
			if (decrypt.useBoundary()) {
				decrypt.setUseBoundary(false);
			}
		} else if (decrypt.isClicked()) {
			if (encrypt.useBoundary())
				encrypt.setUseBoundary(false);
		}
		
		if(start.isClicked() && file == null) 
			JOptionPane.showMessageDialog(null, "Please choose a file.");
		else if(start.isClicked() && !encrypt.useBoundary() && !decrypt.useBoundary()) 
			JOptionPane.showMessageDialog(null, "Please choose to either encrypt or decrypt your file.");
		else if (start.isClicked()) {

			try {
				// Read in file
				BufferedReader br = new BufferedReader(new FileReader(file));
				String fileString = "";
				String t;
				while ((t = br.readLine()) != null) 
					fileString += t + "\r\n"; 
				
				br.close();
				
				JPasswordField passfield = new JPasswordField();
				String password = "";
				int ok = JOptionPane.showConfirmDialog(null, passfield, "Enter Password", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE);

				if (ok == JOptionPane.OK_OPTION)
					password = new String(passfield.getPassword());

				if (encrypt.useBoundary()) {

					if (password != null) {
						try {
							AES.setKey(password);
							AES.encrypt(fileString);

							PrintWriter pWriter = new PrintWriter(file);
							pWriter.print(AES.getEncryptedString());
							pWriter.flush();
							pWriter.close();

							JOptionPane.showMessageDialog(null, "File Overwritten.");
						} catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException
								| IllegalBlockSizeException | BadPaddingException e) {
							JOptionPane.showMessageDialog(null, e.getMessage() + "\nThis could be caused by an incorrect password. Please Try Again.");
						}
					}

				} else if (decrypt.useBoundary()) {

					if (password != null) {
						try {
							AES.setKey(password);
							AES.decrypt(fileString);

							PrintWriter pWriter = new PrintWriter(file);
							pWriter.print(AES.getDecryptedString());
							pWriter.flush();
							pWriter.close();

							JOptionPane.showMessageDialog(null, "File Overwritten.");
						} catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException
								| IllegalBlockSizeException | BadPaddingException e) {
							JOptionPane.showMessageDialog(null, e.getMessage() + "\nThis could be caused by an incorrect password. Please Try Again.");
						}
					}

				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public void mouseMoved(MouseEvent k) {
		encrypt.mouseMoved(k);
		decrypt.mouseMoved(k);
		start.mouseMoved(k);
		chooseFile.mouseMoved(k);
	}

	public void mouseDragged(MouseEvent k) {

	}

}
