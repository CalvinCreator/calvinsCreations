package gamestates;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;



public class GameStateManager
{
	private ArrayList<GameState> gameStates;
	private int currentState;
	public static final int PLAYSTATE = 0;

	public GameStateManager()
	{
		gameStates = new ArrayList();

		currentState = 0;
		gameStates.add(new PlayState(this));
	}


	public int getState() { return currentState; }

	public void setState(int currentState) {
		this.currentState = currentState;
		((GameState)gameStates.get(currentState)).init();
	}

	public void update() {
		((GameState)gameStates.get(currentState)).update();
	}

	public void draw(Graphics2D g) { ((GameState)gameStates.get(currentState)).draw(g); }

	public void keyPressed(int k)
	{
		((GameState)gameStates.get(currentState)).keyPressed(k);
	}

	public void keyReleased(int k) { ((GameState)gameStates.get(currentState)).keyReleased(k); }

	public void keyTyped(int k) {
		((GameState)gameStates.get(currentState)).keyTyped(k);
	}

	public void mouseMoved(MouseEvent m) { ((GameState)gameStates.get(currentState)).mouseMoved(m); }

	public void mouseClicked(MouseEvent m) {
		((GameState)gameStates.get(currentState)).mouseClicked(m);
	}

	public void mouseReleased(MouseEvent m) { ((GameState)gameStates.get(currentState)).mouseReleased(m); }

	public void mousePressed(MouseEvent m) {
		((GameState)gameStates.get(currentState)).mousePressed(m);
	}

	public void mouseDragged(MouseEvent m) { ((GameState)gameStates.get(currentState)).mouseDragged(m); }

	public void mouseWheelMoved(MouseWheelEvent m) {
		((GameState)gameStates.get(currentState)).mouseWheelMoved(m);
	}
}
