package gamestates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class PlayState
extends GameState
{
	private Font bigFont;
	private int player2Stones;
	private int player1Stones;
	private int winner;
	private boolean win;
	private int yStone;
	private int xStone;
	private int pY = 0; private int pX = 0;

	private int[][] map;

	private int currentState;

	private GameStateManager gsm;


	public PlayState(GameStateManager gsm)
	{
		this.gsm = gsm;

		currentState = 0;

		map = new int[19][19];
		for (int i = 0; i < 19; i++) {
			for (int l = 0; l < 19; l++) {
				map[l][i] = 0;
			}
		}

		bigFont = new Font("Impact", 25, 35);

		player1Stones = 0;
		player2Stones = 0;

		win = false;
	}


	public void init() {}

	public void update()
	{
		if ((currentState == 1) || (currentState == 2)) {
			for (int i = 0; i < 19; i++) {
				for (int l = 0; l < 19; l++) {
					Rectangle temp = new Rectangle(l * 30, i * 30, 20, 20);
					temp.contains(pX, pY);
				}
			}
		}
	}

	public void draw(Graphics2D g)
	{
		g.setColor(Color.white);
		g.fillRect(0, 0, 570, 620);

		xStone = -1;
		yStone = -1;
		for (int i = 0; i < 19; i++) {
			for (int l = 0; l < 19; l++) {
				if (map[l][i] == 0) {
					g.setColor(Color.black);
					g.fillRect(l * 30 + 15 - 1, i * 30 + 15 - 1, 3, 3);
					if ((l == 9) && (i == 9))
						g.fillRect(l * 30 + 15 - 2, i * 30 + 15 - 2, 5, 5);
					if ((l == 5) && (i == 5))
						g.fillRect(l * 30 + 15 - 2, i * 30 + 15 - 2, 5, 5);
					if ((l == 5) && (i == 14))
						g.fillRect(l * 30 + 15 - 2, i * 30 + 15 - 2, 5, 5);
					if ((l == 14) && (i == 5))
						g.fillRect(l * 30 + 15 - 2, i * 30 + 15 - 2, 5, 5);
					if ((l == 14) && (i == 14))
						g.fillRect(l * 30 + 15 - 2, i * 30 + 15 - 2, 5, 5);
				}
				if (map[l][i] == 1) {
					g.setColor(Color.red);
					g.fillOval(l * 30 + 15 - 13, i * 30 + 15 - 13, 26, 26);
				}
				if (map[l][i] == 2) {
					g.setColor(Color.green);
					g.fillOval(l * 30 + 15 - 13, i * 30 + 15 - 13, 26, 26);
				}

				if ((currentState == 0) || ((currentState == 1) && (!win))) {
					Rectangle temp = new Rectangle(l * 30 + 15 - 10, i * 30 + 15 - 10, 20, 20);
					if (temp.contains(pX, pY)) {
						g.setColor(Color.gray);
						g.drawOval(l * 30 + 15 - 13, i * 30 + 15 - 13, 26, 26);
						xStone = l;
						yStone = i;
					}
					g.setColor(Color.blue);
					g.setFont(bigFont);
					g.drawString("Player " + (currentState + 1) + "'s Turn", 170, 600);
				}
			}
		}

		for (int i = 0; i < player1Stones; i++) {
			if (i != 5) {
				g.setColor(Color.green);
				g.fillOval(i * 35 + 10, 580, 15, 15);
				g.fillOval(i * 35 + 25, 580, 15, 15);
			}
			if (i == 5) {
				g.setColor(Color.green);
				g.fillOval(10, 625, 15, 15);
				g.fillOval(45, 625, 15, 15);
			}
		}

		if (win) {
			g.setColor(Color.white);
			g.fillRect(0, 0, 570, 620);
			for (int i = 0; i < 19; i++) {
				for (int l = 0; l < 19; l++) {
					if (map[l][i] == 0) {
						g.setColor(Color.black);
						g.fillRect(l * 30 + 15 - 1, i * 30 + 15 - 1, 3, 3);
						if ((l == 9) && (i == 9))
							g.fillRect(l * 30 + 15 - 2, i * 30 + 15 - 2, 5, 5);
						if ((l == 5) && (i == 5))
							g.fillRect(l * 30 + 15 - 2, i * 30 + 15 - 2, 5, 5);
						if ((l == 5) && (i == 14))
							g.fillRect(l * 30 + 15 - 2, i * 30 + 15 - 2, 5, 5);
						if ((l == 14) && (i == 5))
							g.fillRect(l * 30 + 15 - 2, i * 30 + 15 - 2, 5, 5);
						if ((l == 14) && (i == 14))
							g.fillRect(l * 30 + 15 - 2, i * 30 + 15 - 2, 5, 5);
					}
					if (map[l][i] == 1) {
						g.setColor(Color.red);
						g.fillOval(l * 30 + 15 - 13, i * 30 + 15 - 13, 26, 26);
					}
					if (map[l][i] == 2) {
						g.setColor(Color.green);
						g.fillOval(l * 30 + 15 - 13, i * 30 + 15 - 13, 26, 26);
					}
				}
			}
			g.setColor(Color.blue);
			g.setFont(bigFont);
			g.drawString("Player " + winner + " Won!", 170, 600);
		}
	}

	public void select() {
		if (currentState == 0) {
			if ((xStone == -1) || (yStone == -1) || (map[xStone][yStone] == 1) || (map[xStone][yStone] == 2))
				return;
			map[xStone][yStone] = 1;
			try {
				calculateStones();
			} finally {
				currentState = 1;
			}
		}
		else if (currentState == 1) {
			if ((xStone == -1) || (yStone == -1) || (map[xStone][yStone] == 1) || (map[xStone][yStone] == 2))
				return;
			map[xStone][yStone] = 2;
			try {
				calculateStones();
			} finally {
				currentState = 0;
			}
		}
	}

	public void calculateStones() { for (int i = 0; i < 19; i++) {
		for (int l = 0; l < 19; l++)
		{
			if (map[l][i] == currentState + 1)
			{
				if ((l > 3) && 
						(map[(l - 1)][i] == currentState + 1) && 
						(map[(l - 2)][i] == currentState + 1) && 
						(map[(l - 3)][i] == currentState + 1) && 
						(map[(l - 4)][i] == currentState + 1)) {
					win = true;
					winner = (currentState + 1);
				}


				if ((i > 3) && 
						(map[l][(i - 1)] == currentState + 1) && 
						(map[l][(i - 2)] == currentState + 1) && 
						(map[l][(i - 3)] == currentState + 1) && 
						(map[l][(i - 4)] == currentState + 1)) {
					win = true;
					winner = (currentState + 1);
				}


				if ((i > 3) && (l < 15) && 
						(map[(l + 1)][(i - 1)] == currentState + 1) && 
						(map[(l + 2)][(i - 2)] == currentState + 1) && 
						(map[(l + 3)][(i - 3)] == currentState + 1) && 
						(map[(l + 4)][(i - 4)] == currentState + 1)) {
					win = true;
					winner = (currentState + 1);
				}


				if ((l > 3) && (i > 3) && 
						(map[(l - 1)][(i - 1)] == currentState + 1) && 
						(map[(l - 2)][(i - 2)] == currentState + 1) && 
						(map[(l - 3)][(i - 3)] == currentState + 1) && 
						(map[(l - 4)][(i - 4)] == currentState + 1)) {
					win = true;
					winner = (currentState + 1);
				}
			}
		}
	}


	if (currentState == 0) {
		if ((map[(xStone - 1)][yStone] == 2) && 
				(map[(xStone - 2)][yStone] == 2) && 
				(map[(xStone - 3)][yStone] == 1)) {
			map[(xStone - 1)][yStone] = 0;
			map[(xStone - 2)][yStone] = 0;
			player1Stones += 1;
			if (player1Stones == 5) {
				win = true;
				winner = (currentState + 1);
			}
		}



		if ((map[(xStone + 1)][yStone] == 2) && 
				(map[(xStone + 2)][yStone] == 2) && 
				(map[(xStone + 3)][yStone] == 1)) {
			map[(xStone + 1)][yStone] = 0;
			map[(xStone + 2)][yStone] = 0;
			player1Stones += 1;
			if (player1Stones == 5) {
				win = true;
				winner = (currentState + 1);
			}
		}
	}


	if (currentState == 1) {
		if ((map[(xStone - 1)][yStone] == 1) && 
				(map[(xStone - 2)][yStone] == 1) && 
				(map[(xStone - 3)][yStone] == 2)) {
			map[(xStone - 1)][yStone] = 0;
			map[(xStone - 2)][yStone] = 0;
			player2Stones += 1;
			if (player2Stones == 5) {
				win = true;
				winner = (currentState + 1);
			}
		}


		if ((map[(xStone + 1)][yStone] == 1) && 
				(map[(xStone + 2)][yStone] == 1) && 
				(map[(xStone + 3)][yStone] == 2)) {
			map[(xStone + 1)][yStone] = 0;
			map[(xStone + 2)][yStone] = 0;
			player2Stones += 1;
			if (player2Stones == 5) {
				win = true;
				winner = (currentState + 1);
			}
		}
	}



	if (currentState == 0) {
		if ((map[xStone][(yStone - 1)] == 2) && 
				(map[xStone][(yStone - 2)] == 2) && 
				(map[xStone][(yStone - 3)] == 1)) {
			map[xStone][(yStone - 1)] = 0;
			map[xStone][(yStone - 2)] = 0;
			player1Stones += 1;
			if (player1Stones == 5) {
				win = true;
				winner = (currentState + 1);
			}
		}


		if ((map[xStone][(yStone + 1)] == 2) && 
				(map[xStone][(yStone + 2)] == 2) && 
				(map[xStone][(yStone + 3)] == 1)) {
			map[xStone][(yStone + 1)] = 0;
			map[xStone][(yStone + 2)] = 0;
			player1Stones += 1;
			if (player1Stones == 5) {
				win = true;
				winner = (currentState + 1);
			}
		}
	}


	if (currentState == 1) {
		if ((map[xStone][(yStone - 1)] == 1) && 
				(map[xStone][(yStone - 2)] == 1) && 
				(map[xStone][(yStone - 3)] == 2)) {
			map[xStone][(yStone - 1)] = 0;
			map[xStone][(yStone - 2)] = 0;
			player2Stones += 1;
			if (player2Stones == 5) {
				win = true;
				winner = (currentState + 1);
			}
		}


		if ((map[xStone][(yStone + 1)] == 1) && 
				(map[xStone][(yStone + 2)] == 1) && 
				(map[xStone][(yStone + 3)] == 2)) {
			map[xStone][(yStone + 1)] = 0;
			map[xStone][(yStone + 2)] = 0;
			player2Stones += 1;
			if (player2Stones == 5) {
				win = true;
				winner = (currentState + 1);
			}
		}
	}




	if ((currentState == 0) && 
			(map[(xStone + 1)][(yStone + 1)] == 2) && 
			(map[(xStone + 2)][(yStone + 2)] == 2) && 
			(map[(xStone + 3)][(yStone + 3)] == 1)) {
		map[(xStone + 1)][(yStone + 1)] = 0;
		map[(xStone + 2)][(yStone + 2)] = 0;
		player1Stones += 1;
		if (player1Stones == 5) {
			win = true;
			winner = (currentState + 1);
		}
	}

	if ((currentState == 1) && 
			(map[(xStone + 1)][(yStone + 1)] == 1) && 
			(map[(xStone + 2)][(yStone + 2)] == 1) && 
			(map[(xStone + 3)][(yStone + 3)] == 2)) {
		map[(xStone + 1)][(yStone + 1)] = 0;
		map[(xStone + 2)][(yStone + 2)] = 0;
		player2Stones += 1;
		if (player2Stones == 5) {
			win = true;
			winner = (currentState + 1);
		}
	}


	if ((currentState == 0) && 
			(map[(xStone + 1)][(yStone - 1)] == 2) && 
			(map[(xStone + 2)][(yStone - 2)] == 2) && 
			(map[(xStone + 3)][(yStone - 3)] == 1)) {
		map[(xStone + 1)][(yStone - 1)] = 0;
		map[(xStone + 2)][(yStone - 2)] = 0;
		player1Stones += 1;
		if (player1Stones == 5) {
			win = true;
			winner = (currentState + 1);
		}
	}

	if ((currentState == 1) && 
			(map[(xStone + 1)][(yStone - 1)] == 1) && 
			(map[(xStone + 2)][(yStone - 2)] == 1) && 
			(map[(xStone + 3)][(yStone - 3)] == 2)) {
		map[(xStone + 1)][(yStone - 1)] = 0;
		map[(xStone + 2)][(yStone - 2)] = 0;
		player2Stones += 1;
		if (player2Stones == 5) {
			win = true;
			winner = (currentState + 1);
		}
	}



	if ((currentState == 0) && 
			(map[(xStone - 1)][(yStone + 1)] == 2) && 
			(map[(xStone - 2)][(yStone + 2)] == 2) && 
			(map[(xStone - 3)][(yStone + 3)] == 1)) {
		map[(xStone - 1)][(yStone + 1)] = 0;
		map[(xStone - 2)][(yStone + 2)] = 0;
		player1Stones += 1;
		if (player1Stones == 5) {
			win = true;
			winner = (currentState + 1);
		}
	}

	if ((currentState == 1) && 
			(map[(xStone - 1)][(yStone + 1)] == 1) && 
			(map[(xStone - 2)][(yStone + 2)] == 1) && 
			(map[(xStone - 3)][(yStone + 3)] == 2)) {
		map[(xStone - 1)][(yStone + 1)] = 0;
		map[(xStone - 2)][(yStone + 2)] = 0;
		player2Stones += 1;
		if (player2Stones == 5) {
			win = true;
			winner = (currentState + 1);
		}
	}


	if ((currentState == 0) && 
			(map[(xStone - 1)][(yStone - 1)] == 2) && 
			(map[(xStone - 2)][(yStone - 2)] == 2) && 
			(map[(xStone - 3)][(yStone - 3)] == 1)) {
		map[(xStone - 1)][(yStone - 1)] = 0;
		map[(xStone - 2)][(yStone - 2)] = 0;
		player1Stones += 1;
		if (player1Stones == 5) {
			win = true;
			winner = (currentState + 1);
		}
	}

	if ((currentState == 1) && 
			(map[(xStone - 1)][(yStone - 1)] == 1) && 
			(map[(xStone - 2)][(yStone - 2)] == 1) && 
			(map[(xStone - 3)][(yStone - 3)] == 2)) {
		map[(xStone - 1)][(yStone - 1)] = 0;
		map[(xStone - 2)][(yStone - 2)] = 0;
		player2Stones += 1;
		if (player2Stones == 5) {
			win = true;
			winner = (currentState + 1);
		}
	}
	}

	public void keyPressed(int k) {}

	public void keyReleased(int k) {}

	public void keyTyped(int k) {}

	public void mouseMoved(MouseEvent m) {
		pX = m.getX();
		pY = m.getY(); }

	public void mouseClicked(MouseEvent m) {}

	public void mouseReleased(MouseEvent m) { select(); }

	public void mousePressed(MouseEvent m) {}

	public void mouseDragged(MouseEvent m) {}

	public void mouseWheelMoved(MouseWheelEvent m) {}
}
