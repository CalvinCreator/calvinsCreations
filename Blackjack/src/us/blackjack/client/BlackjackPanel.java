package us.blackjack.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import us.blackjack.game.Player;

public class BlackjackPanel extends JPanel implements Runnable {

	private JButton hit, stand, bet;
	private DisplayPanel dPanel;
	private Graphics2D g;
	BufferedImage image;

	private BufferedReader input;
	private PrintWriter output;
	private boolean running;
	String username;
	String message;
	String winner = "";

	public BlackjackPanel(BufferedReader in, PrintWriter out, String username) {
		this.input = in;
		this.output = out;
		running = false;
		this.username = username;
		message = "";

		BorderLayout layout = new BorderLayout();
		layout.setHgap(4);
		setLayout(layout);
		JPanel bottom = new JPanel();
		hit = new JButton("HIT");
		hit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				output.println("/hit::");
				output.flush();
			}
		});
		hit.setBackground(new Color(220, 220, 220));
		stand = new JButton("STAND");
		stand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				output.println("/stand::");
				output.flush();
			}
		});
		stand.setBackground(new Color(220, 220, 220));
		bet = new JButton("BET");
		bet.addActionListener(new BetListener());
		bet.setBackground(new Color(220, 220, 220));
		bottom.setLayout(new GridLayout(1, 3));
		bottom.add(hit, 0, 0);
		bottom.add(stand, 0, 1);
		bottom.add(bet, 0, 2);
		add(bottom, BorderLayout.SOUTH);

		dPanel = new DisplayPanel(output, username);
		image = new BufferedImage(DisplayPanel.WIDTH, DisplayPanel.HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		dPanel.initMouseListener();
		add(dPanel, BorderLayout.CENTER);
	}

	public void run() {
		running = true;
		while (running) {

			try {
				if (input.ready()) {
					message += input.readLine();
				}
				if (message.indexOf("/start::") != -1) {
					hit.setEnabled(true);
					stand.setEnabled(true);
					bet.setEnabled(false);
					message = message.replace("/start::", "");
				} else if (message.indexOf("/wait::") != -1) {
					hit.setEnabled(false);
					stand.setEnabled(false);
					bet.setEnabled(false);

					message = message.replace("/wait::", "");
				} else if (message.indexOf("/startDealer::") != -1) {
					dPanel.dealersTurn = true;
					message = message.replace("/startDealer::", "");
				} else if (message.indexOf("/nextPlayer::") != -1) {
					dPanel.currentPlayer++;
					message = message.replace("/nextPlayer::", "");
				} else if (message.indexOf("/endDealer::") != -1
						&& message.substring("/endDealer::".length()).indexOf("::") != -1) {
					winner = message.substring(message.indexOf("::") + 2, message.lastIndexOf("::"));
					JFrame frame = new JFrame();
					frame.setLayout(new BorderLayout());
					frame.add(new JLabel(winner + " is the Winner! Do you wish to continue?"), BorderLayout.NORTH);
					JPanel p = new JPanel();
					JButton restart = new JButton("Continue");
					JButton leaveGame = new JButton("Leave Game");
					p.add(restart);
					p.add(leaveGame);
					frame.add(p, BorderLayout.CENTER);
					frame.pack();
					frame.setLocationRelativeTo(null);
					frame.setResizable(false);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setVisible(true);
					restart.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							output.println("/newGame::" + username + "::");
							output.flush();
							frame.dispose();
						}
					});
					leaveGame.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							output.println("/logout::");
							output.flush();
							System.exit(0);
						}
					});
					message = "";
				} else if (message.indexOf("/newGame::") != -1) {
					hit.setEnabled(false);
					stand.setEnabled(false);
					bet.setEnabled(false);
					dPanel.dealersTurn = false;
					dPanel.currentPlayer = 0;
					dPanel.mouse = new Point(-100, -100);

					message = message.replace("/newGame::", "");
				} else if (message.indexOf("/sendingGame::") != -1
						&& message.substring("/sendingGame::".length()).indexOf("::") != -1) {

					String[] players = message.substring("/sendingGame".length() + 3, message.length() - 2).split("]");

					ArrayList<Player> player = new ArrayList<Player>();
					for (int i = 0; i < players.length - 1; i++) {

						String username = players[i].substring(0, players[i].indexOf(" "));
						players[i] = players[i].substring(players[i].indexOf(" "), players[i].length() - 1);

						String[] numbers = players[i].split(",");
						for (int h = 0; h < players.length; h++) {
							players[h] = players[h].replace("[", "");
						}
						ArrayList<Integer> hand = new ArrayList<>();
						for (String n : numbers) {
							hand.add(Integer.valueOf(n.trim()));
						}

						Player p = new Player(username);
						p.setHand(hand);
						player.add(p);
					}
					ArrayList<Integer> dealerHand = new ArrayList<Integer>();
					String[] dealerNums = players[players.length - 1].substring(2).split(",");
					for (String n : dealerNums) {
						dealerHand.add(Integer.valueOf(n.trim()));
					}

					dPanel.setPlayers(player);
					dPanel.setDealer(dealerHand);
					message = "";
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(message);
			}

			if (g == null)
				g = (Graphics2D) image.getGraphics();
			dPanel.draw(g);
			Graphics g2 = dPanel.getGraphics();
			g2.drawImage(image, 0, 0, null);
			g2.dispose();

		}
	}

	private class BetListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

		}
	}

	public void setRunning(boolean b) {
		running = b;
	}

	public boolean isRunning() {
		return running;
	}

}
