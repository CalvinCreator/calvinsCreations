package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Launcher extends JFrame implements Runnable, ChangeListener {

	private BufferStrategy bs;
	private volatile boolean running;
	private Thread gameThread;
	private int timeScale = 1; // how many ticks to wait between each update
	private long pause = 0;

	private Simulator simulation;

	public Launcher() {
		simulation = new Simulator();
	}

	protected void createAndShowGUI() {

		Canvas canvas = new Canvas();
		canvas.setSize(600, 630);
		canvas.setBackground(Color.white);
		canvas.setIgnoreRepaint(true);
		canvas.requestFocus();
		getContentPane().add(canvas);
		setTitle("Bot Simulator");
		setIgnoreRepaint(true);
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		canvas.createBufferStrategy(2);
		bs = canvas.getBufferStrategy();

		gameThread = new Thread(this);
		gameThread.start();
	}

	public void toggleDraw() {
		simulation.draw = !simulation.draw;
	}

	public void run() {
		running = true;

		while (running) {
			if (pause == -1)
				System.exit(0);
			else if (pause == 0) {
			} else
				try {
					Thread.sleep(pause);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			gameLoop();
		}
	}

	public void gameLoop() {
		do {
			do {
				Graphics g = null;
				try {
					g = bs.getDrawGraphics();
					g.clearRect(0, 0, getWidth(), getHeight());
					try {
						Thread.sleep(timeScale * 2);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
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

	private void render(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, 600, 630);
		pause = simulation.render((Graphics2D) g);
	}

	protected void onWindowClosing() {
		try {
			running = false;
			gameThread.join();
			System.exit(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	public static void main(String[] args) {
		final Launcher app = new Launcher();
		app.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				app.onWindowClosing();
			}
		});
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				app.createAndShowGUI();
			}
		});

		JFrame f = new JFrame();
		JSlider scale = new JSlider(JSlider.HORIZONTAL, 0, 50, 0);
		scale.addChangeListener(app);
		scale.setMajorTickSpacing(1);
		scale.setPaintTicks(true);
		scale.setMajorTickSpacing(10);
		JButton b = new JButton("Draw");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				app.toggleDraw();
			}
		});
		JPanel p = new JPanel();
		p.add(scale);
		p.add(b);
		f.add(p);
		f.setLocation(0, 0);
		f.pack();
		f.setVisible(true);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider) e.getSource();
		timeScale = source.getValue();
	}

}
