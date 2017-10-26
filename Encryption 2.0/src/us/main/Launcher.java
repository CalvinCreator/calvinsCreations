package us.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import us.menu.MainMenu;



public class Launcher extends JFrame implements Runnable, MouseListener, MouseMotionListener {

	   private BufferStrategy bs;
	   private volatile boolean running;
	   private Thread gameThread;
	   
	   MainMenu menu;

	   public Launcher() {
		   menu = new MainMenu();
	   }
	   
	   protected void createAndShowGUI() {
	      Canvas canvas = new Canvas();
	      canvas.setSize(Constants.WIDTH, Constants.HEIGHT);
	      canvas.setBackground( Color.white );
	      canvas.setIgnoreRepaint( true );
	      canvas.requestFocus();
	      getContentPane().add( canvas );
	      setTitle("CyberStrong");
	      setIgnoreRepaint( true );
	      setResizable(false);
	      pack();
	      setLocationRelativeTo(null);
	      setVisible( true );
	      canvas.createBufferStrategy(2);
	      canvas.addMouseListener(this);
	      canvas.addMouseMotionListener(this);
	      bs = canvas.getBufferStrategy();
	      
	      gameThread = new Thread( this );
	      gameThread.start();
	   }
	   
	   public void run() {
		   running = true;
		   
		   while(running)
			   gameLoop();
	   }
	   
	   public void gameLoop() {
	      do {
	         do {
	            Graphics g = null;
	            try {
	               g = bs.getDrawGraphics();
	               g.clearRect( 0, 0, getWidth(), getHeight() );
	               render( g );
	            } finally {
	               if( g != null ) {
	                  g.dispose();
	               }
	            }
	         } while( bs.contentsRestored() );
	         bs.show();
	      } while( bs.contentsLost() );
	   }
	   private void render(Graphics g) {
		   g.setColor(Color.white);
		   g.fillRect(0, 0, 820, 600);
		   menu.draw((Graphics2D)g);
	   }
	   
	   protected void onWindowClosing() {
	      try {
	         running = false;
	         gameThread.join();
	         System.exit(0);
	      } catch( InterruptedException e ) {
	         e.printStackTrace();
	      }
	      System.exit(0);
	   }
	   
	 
	   static final Launcher app = new Launcher();
	   public static void main( String[] args ) {
	       app.addWindowListener( new WindowAdapter() {
	    	   public void windowClosing( WindowEvent e ) {
	    		   app.onWindowClosing();
	           }
	       });
	       SwingUtilities.invokeLater( new Runnable() {
	           public void run() {
	              app.createAndShowGUI();
	           }
	       });
	   }
	   
	   public static Launcher getApp() {
		   return app;
	   }
	   
	//   public static File chooseFile() { //use JFileChooser to get a file
		   
	 //  }

	//Input
	public void mouseClicked(MouseEvent k) {
		menu.mouseClicked(k);
	}
	public void mouseEntered(MouseEvent k) {
		menu.mouseEntered(k);
	}
	public void mouseExited(MouseEvent k) {
		menu.mouseExited(k);
	}
	public void mousePressed(MouseEvent k) {
		menu.mousePressed(k);
	}
	public void mouseReleased(MouseEvent k) {
		menu.mouseReleased(k);
	}
	public void mouseDragged(MouseEvent k) {
		menu.mouseDragged(k);
	}
	public void mouseMoved(MouseEvent k) {
		menu.mouseMoved(k);
	}

}
