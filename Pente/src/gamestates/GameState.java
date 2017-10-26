package gamestates;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public abstract class GameState
{
	protected GameStateManager gsm;

	public GameState() {}

	public abstract void init();

	public abstract void update();

	public abstract void draw(Graphics2D paramGraphics2D);

	public abstract void keyPressed(int paramInt);

	public abstract void keyReleased(int paramInt);

	public abstract void keyTyped(int paramInt);

	public abstract void mouseMoved(MouseEvent paramMouseEvent);

	public abstract void mouseClicked(MouseEvent paramMouseEvent);

	public abstract void mouseReleased(MouseEvent paramMouseEvent);

	public abstract void mousePressed(MouseEvent paramMouseEvent);

	public abstract void mouseDragged(MouseEvent paramMouseEvent);

	public abstract void mouseWheelMoved(MouseWheelEvent paramMouseWheelEvent);
}
