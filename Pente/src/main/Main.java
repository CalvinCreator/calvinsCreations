package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.event.MouseInputListener;

import gamestates.GameStateManager;

public class Main extends JFrame implements Runnable, KeyListener, MouseInputListener, MouseListener, MouseWheelListener {
	
	private GameStateManager gsm;
	private BufferStrategy bs;
	private volatile boolean running;
	private Thread gameThread;
  
	public Main() {
		gsm = new GameStateManager();
		addKeyListener(this);
	}
	
	public void init() {
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onWindowClosing();
			}
		});
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
  
	protected void createAndShowGUI() {
		Canvas canvas = new Canvas();
		canvas.addKeyListener(this);
		canvas.setSize(570, 620);
		canvas.setBackground(Color.BLACK);
		canvas.setIgnoreRepaint(true);
		canvas.requestFocus();
		getContentPane().add(canvas);
		setTitle("Pente");
		setIgnoreRepaint(true);
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		canvas.addMouseMotionListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseWheelListener(this);
		setVisible(true);
		canvas.createBufferStrategy(2);
		bs = canvas.getBufferStrategy();

		gameThread = new Thread(this);
		gameThread.start();
	}
  
	public void run() {

		running = true;
		int FPS = 60;
		long targetTime = 1000 / FPS;

		while (running) {
			long start = System.nanoTime();
			gameLoop();
			long elapsed = System.nanoTime() - start;
			long wait = targetTime - elapsed / 1000000L;
			if (wait < 0L) wait = 1L;
			try {
				Thread.sleep(wait);
			}
			catch (Exception e) {
				System.out.println("GamePanel.THREAD Failed to Sleep");
				e.printStackTrace();
			}
		}
	}

	public void gameLoop() {
		update();
		do {
			do {
				Graphics g = null;
				try {
					g = bs.getDrawGraphics();
					g.clearRect(0, 0, getWidth(), getHeight());
					render(g);
				} finally {
					if (g != null) {
						g.dispose();
					}
				}
			} while (bs.contentsRestored());
			bs.show();
		} while (bs.contentsLost());
	}

	private void update() {
		gsm.update();
	}

	private void render(Graphics g) { gsm.draw((java.awt.Graphics2D)g); }

	protected void onWindowClosing()
	{
		try {
			running = false;
			gameThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	public void keyPressed(KeyEvent k) {
		gsm.keyPressed(k.getKeyCode());
	}

	public void keyReleased(KeyEvent k) { gsm.keyReleased(k.getKeyCode()); }


	public void keyTyped(KeyEvent k) { gsm.keyTyped(k.getKeyCode()); }

	public void mouseMoved(MouseEvent m) {
		try {
			gsm.mouseMoved(m);
		} catch (Exception localException) {}
	}

	public void mouseClicked(MouseEvent m) { gsm.mouseClicked(m); }

	public void mouseEntered(MouseEvent m) {}

	public void mouseExited(MouseEvent m) {}
	public void mousePressed(MouseEvent m) { gsm.mousePressed(m); }

	public void mouseReleased(MouseEvent m) {
		gsm.mouseReleased(m);
	}

	public void mouseDragged(MouseEvent m) { gsm.mouseDragged(m); }

	public void mouseWheelMoved(MouseWheelEvent m) {
		gsm.mouseWheelMoved(m);
	}

	public static void main(String[] args) {
		Main app = new Main();
		app.init();
	}
}
