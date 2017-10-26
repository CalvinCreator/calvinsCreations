package us.blackjack.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.MouseInputListener;

import us.blackjack.game.Dealer;
import us.blackjack.game.DrawCard;
import us.blackjack.game.Player;

public class DisplayPanel extends JPanel implements MouseInputListener {

	public static final int WIDTH = 560;
	public static final int HEIGHT = 360;
	public static final int CARD_WIDTH = 55;
	public static final int CARD_HEIGHT = 90;

	public Point mouse;
	private boolean canPlay;
	private boolean editDeck;
	private String editThisDeck;
	private ArrayList<Rectangle> aces;
	private PrintWriter output;

	private Dealer dealer;
	private ArrayList<Player> players;
	public int currentPlayer;
	private String username;
	public boolean dealersTurn;

	public DisplayPanel(PrintWriter output, String username) {
		setSize(WIDTH, HEIGHT);
		players = new ArrayList<Player>();
		dealer = new Dealer();
		mouse = new Point();
		canPlay = false;
		editDeck = false;
		editThisDeck = "";
		this.username = username;
		aces = new ArrayList<Rectangle>();
		this.output = output;
		dealersTurn = false;
		currentPlayer = 0;
	}

	public void initMouseListener() {
		setFocusable(true);
		requestFocus(true);
		addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		drawDealer(g);
		int spacing = WIDTH / (players.size() + 1);
		for (int i = 0; i < players.size(); i++) {
			if (i == currentPlayer) {
				g.setColor(Color.yellow);
				drawDeckOutline((i + 1) * spacing, g);
			}
			drawDeck((i + 1) * spacing, players.get(i), g);
		}

		if (dealersTurn) {
			int rows = (int) (dealer.getHand().size() / 10 + 1);
			int y = HEIGHT - 99 - rows * 100;
			int currIndex = 0;
			for (int k = 0; k < rows; k++) {
				ArrayList<String> row = new ArrayList<String>();
				int currentRow = 0;
				for (; currIndex < dealer.getHand().size() && currentRow < 9; currentRow++) {
					row.add(String.valueOf(dealer.getHand().get(currIndex++)));
				}
				drawRowOfCards(y, row, g);
				y += 96;
			}
		}

		if (editDeck && canPlay && !dealersTurn) {
			Player player = null;
			for (Player p : players)
				if (p.getUsername().equals(editThisDeck))
					player = p;
			int rows = (int) (player.getHand().size() / 10 + 1);
			int y = HEIGHT - 99 - rows * 100;
			int currIndex = 0;
			for (int k = 0; k < rows; k++) {
				ArrayList<String> row = new ArrayList<String>();
				int currentRow = 0;
				for (; currIndex < player.getHand().size() && currentRow < 9; currentRow++) {
					row.add(String.valueOf(player.getHand().get(currIndex++)));
				}
				drawRowOfCards(y, row, g);
				y += 96;
			}
		}
	}

	private void drawDeckOutline(int x, Graphics2D g) {
		int outerCardX = x - CARD_WIDTH / 2;
		int outerCardY = HEIGHT - 95;
		Rectangle r = new Rectangle(outerCardX, outerCardY, CARD_WIDTH, CARD_HEIGHT);

		r.x -= 2;
		r.width += 4;
		r.y -= 2;
		r.height += 4;
		g.fill(r);
	}

	private int getSum(ArrayList<Integer> hand) {
		int sum = 0;
		for (Integer i : hand)
			if (i == 0)
				sum += 1;
			else if (i == 1)
				sum += 11;
			else if (i == 11 || i == 12 || i == 13)
				sum += 10;
			else
				sum += i;
		return sum;
	}

	private void drawDeck(int x, Player player, Graphics2D g) {
		if (!canPlay)
			return;
		int outerCardX = x - CARD_WIDTH / 2;
		int outerCardY = HEIGHT - 95;
		Rectangle r = new Rectangle(outerCardX, outerCardY, CARD_WIDTH, CARD_HEIGHT);

		drawCard(x, outerCardY, getSum(player.getHand()) + "", g);
		drawUsername(x, outerCardY, player.getUsername(), g);
		if (!editDeck && r.contains(mouse)) {
			int rows = (int) (player.getHand().size() / 10 + 1);
			int y = HEIGHT - 99 - rows * 100;
			int currIndex = 0;
			for (int k = 0; k < rows; k++) {
				ArrayList<String> row = new ArrayList<String>();
				int currentRow = 0;
				for (; currIndex < player.getHand().size() && currentRow < 9; currentRow++) {
					row.add(String.valueOf(player.getHand().get(currIndex++)));
				}
				drawRowOfCards(y, row, g);
				y += 96;
			}
		}
	}

	private void drawRowOfCards(int y, ArrayList<String> cards, Graphics2D g) {
		int x = (CARD_WIDTH / 2 + WIDTH / 2) - CARD_WIDTH / 2 - (CARD_WIDTH / 2 + 3) * (cards.size() - 1)
				- (int) (0.8 * cards.size() - 1);
		for (String text : cards) {
			drawCard(x, y, DrawCard.getCardValue(Integer.valueOf(text)), g);
			x += CARD_WIDTH + 6;
		}
	}

	private void drawCard(int x, int y, String text, Graphics2D g) {
		int outerCardX = x - CARD_WIDTH / 2;
		int outerCardY = y;
		Rectangle r = new Rectangle(outerCardX, outerCardY, CARD_WIDTH, CARD_HEIGHT);
		g.setColor(new Color(220, 220, 220));
		g.fill(r);
		g.setColor(Color.black);
		g.draw(r);
		float scaleChange = 0.2f;
		int innerCardX = outerCardX + (int) (CARD_WIDTH * scaleChange * 0.5);
		int innerCardY = outerCardY + (int) (CARD_HEIGHT * scaleChange / 2 * 0.5);
		g.setColor(Color.white);
		g.fillRoundRect(innerCardX, innerCardY, (int) (CARD_WIDTH * (1 - scaleChange)),
				(int) (CARD_HEIGHT * (1 - scaleChange / 2)), 11, 11);
		g.setColor(Color.black);
		g.drawRoundRect(innerCardX, innerCardY, (int) (CARD_WIDTH * (1 - scaleChange)),
				(int) (CARD_HEIGHT * (1 - scaleChange / 2)), 11, 11);
		g.setColor(Color.black);
		g.setFont(new Font("Ariel", 12, 36));
		int fontWidth = g.getFontMetrics().stringWidth(text);
		FontRenderContext frc = g.getFontRenderContext();
		GlyphVector gv = g.getFont().createGlyphVector(frc, text);
		int fontHeight = (int) gv.getPixelBounds(null, 0, 0).getHeight();
		g.drawString(text, outerCardX + (int) (CARD_WIDTH / 2 - fontWidth / 2),
				outerCardY + (int) (CARD_HEIGHT / 2) + fontHeight / 2);
	}

	private void drawUsername(int x, int outerCardY, String text, Graphics2D g) {
		g.setFont(new Font("Ariel", 12, 12));
		int fontWidth = g.getFontMetrics().stringWidth(text);
		g.drawString(text, x - fontWidth / 2, outerCardY - 2);
	}

	private void drawDealer(Graphics2D g) {
		if (!dealersTurn)
			drawCard(WIDTH / 2, 6, DrawCard.getCardValue(dealer.getHand().get(dealer.getHand().size() - 1)), g);
		else
			drawCard(WIDTH / 2, 6, getSum(dealer.getHand()) + "", g);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		int spacing = WIDTH / (players.size() + 1);
		editDeck = false;
		for (int i = 0; i < players.size(); i++) {
			int x = (i + 1) * spacing;

			int outerCardX = x - CARD_WIDTH / 2;
			int outerCardY = HEIGHT - 95;
			Rectangle r = new Rectangle(outerCardX, outerCardY, CARD_WIDTH, CARD_HEIGHT);
			if (r.contains(arg0.getPoint())) {
				editDeck = true;
				editThisDeck = players.get(i).getUsername();
			}
		}
		mouse = arg0.getPoint();
	}

	private ArrayList<Rectangle> getAceRects(Player player) {

		ArrayList<Rectangle> rects = new ArrayList<Rectangle>();

		int rows = (int) (player.getHand().size() / 10 + 1);
		int y = HEIGHT - 99 - rows * 100;
		int currIndex = 0;
		for (int k = 0; k < rows; k++) {
			ArrayList<String> row = new ArrayList<String>();
			int currentRow = 0;
			for (; currIndex < player.getHand().size() && currentRow < 9; currentRow++) {
				row.add(String.valueOf(player.getHand().get(currIndex++)));
			}
			rects.addAll(getRectsByRow(y, row));
			y += 96;
		}

		return rects;
	}

	private ArrayList<Rectangle> getRectsByRow(int y, ArrayList<String> cards) {
		ArrayList<Rectangle> rects = new ArrayList<Rectangle>();

		int x = (CARD_WIDTH / 2 + WIDTH / 2) - CARD_WIDTH / 2 - (CARD_WIDTH / 2 + 3) * (cards.size() - 1)
				- (int) (0.8 * cards.size() - 1);
		for (String text : cards) {

			int outerCardX = x - CARD_WIDTH / 2;
			int outerCardY = y;
			if (text.equals("1") || text.equals("0")) {
				Rectangle r = new Rectangle(outerCardX, outerCardY, CARD_WIDTH, CARD_HEIGHT);
				rects.add(r);
			}

			x += CARD_WIDTH + 6;
		}
		return rects;
	}

	private void updateServer() {

		Player p = null;
		for (Player pl : players)
			if (pl.getUsername().equals(username))
				p = pl;
		String msg = "/updateHand::";
		msg += "[" + username + " ";
		for (Integer i : p.getHand())
			msg += String.valueOf(i) + ",";
		msg.substring(0, msg.length() - 1);
		msg += "]::";

		output.println(msg);
		output.flush();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	public void mousePressed(MouseEvent arg0) {

	}

	public void mouseReleased(MouseEvent arg0) {
		if (editDeck) {

			Player player = null;
			for (Player p : players)
				if (p.getUsername().equals(editThisDeck) && p.getUsername().equals(username))
					player = p;

			for (Rectangle r : aces) {
				if (r.contains(arg0.getPoint())) {

					int indexInRects = 0;
					int indexInDeck = 0;
					boolean cont = true;
					for (int i = 0; i < player.getHand().size() && cont; i++)
						if (player.getHand().get(i) == 0 || player.getHand().get(i) == 1) {
							if (aces.get(indexInRects).equals(r)) {
								cont = false;
								indexInDeck = i;
							} else
								indexInRects++;
						}

					JPanel p = new JPanel();
					JRadioButton one = new JRadioButton("1");
					JRadioButton eleven = new JRadioButton("11");
					one.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							if (eleven.isSelected() && one.isSelected()) {
								eleven.setSelected(false);
							}
						}
					});
					eleven.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							if (one.isSelected() && eleven.isSelected()) {
								one.setSelected(false);
							}
						}
					});
					p.add(one);
					p.add(eleven);
					JOptionPane.showConfirmDialog(null, p, null, JOptionPane.PLAIN_MESSAGE);

					int answer = 0;
					if (eleven.isSelected())
						answer++;

					player.getHand().remove(indexInDeck);
					player.getHand().add(indexInDeck, new Integer(answer));

					updateServer();
				}
			}
		}
	}

	public void mouseDragged(MouseEvent arg0) {

	}

	public void mouseMoved(MouseEvent arg0) {
		if (!editDeck)
			mouse = arg0.getPoint();
		else {
			Player player = null;
			for (Player p : players)
				if (p.getUsername().equals(editThisDeck))
					player = p;

			aces = getAceRects(player);
		}
	}

	public void setDealer(ArrayList<Integer> dealer) {
		this.dealer.setHand(dealer);
	}

	public void setPlayers(ArrayList<Player> players) {
		canPlay = players.size() > 0;
		this.players = players;
	}
}
