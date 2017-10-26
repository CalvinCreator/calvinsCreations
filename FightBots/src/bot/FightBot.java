package bot;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Stack;

import main.Simulator;


public class FightBot {
	
	private int lives;
	private int x, y;
	private float moveAngle;
	private float moveSpeed;
	private float maxAngleChange = 20;
	
	private float shootTime = 1; //1 second
	private long lastShot = 0;
	
	private ArrayList<Bullet> bullets;
	private FightBot enemy;
	private String brain;
	
	public FightBot(int x, int y) {
		lives = 3;
		this.x = x; this.y = y;
		
		moveAngle = 0;
		
		moveSpeed = 2;
		bullets = new ArrayList<Bullet>();
	}
	
	public void update() {
		for(int i = 0; i < bullets.size(); i++)
			bullets.get(i).update();
			
			float change = (float)eval(brain.split(" "));
			moveAngle = change;
			x += moveSpeed * Math.cos(getMoveAngle());
			y += moveSpeed * Math.sin(getMoveAngle());

		if((System.currentTimeMillis() - lastShot) > (shootTime * 1000)) {
			bullets.add(new Bullet(x + Simulator.BOT_SIZE / 2, y + Simulator.BOT_SIZE / 2, getMoveAngle()));
			lastShot = System.currentTimeMillis();
		}
		
		if(x < 0)
			x = 600 + x; //make it wrap in x direction
		if(x > 600)
			x %= 600;
		if(y < 0)
			y = 600 + y; //make it wrap in y direction
		if(y + Simulator.BOT_SIZE > 600)
			y %= 600;
	}
	
	public double eval(String[] s) {
		Stack<String> stack = new Stack<String>();

		for(int i = s.length - 1; i >= 0; i--) {
			if(s[i].equals("A")) {
				stack.push(Double.valueOf(stack.pop()) + Double.valueOf(stack.pop()) + "");
			} else if(s[i].equals("S")) {
				stack.push(Double.valueOf(stack.pop()) - Double.valueOf(stack.pop()) + "");
			} else if(s[i].equals("M")) {
				stack.push(Double.valueOf(stack.pop()) * Double.valueOf(stack.pop()) + "");
			}  else if(s[i].equals("D")) {
				String left = stack.pop();
				String right = stack.pop();
				if(Double.valueOf(right) == 0)
					right = "1"; //protected division
				stack.push(Double.valueOf(left) / Double.valueOf(right) + "");
			} else if(s[i].equals("Cos")) {
				stack.push(Math.cos(Double.valueOf(stack.pop())) + "");
			} else if(s[i].equals("Sin")) {
				stack.push(Math.sin(Double.valueOf(stack.pop())) + "");
			} else if(s[i].equals("ATan") ) {
				stack.push(Math.atan2(Double.valueOf(stack.pop()), Double.valueOf(stack.pop())) + "");
			}else if(s[i].equals("Rand")) {
				stack.push((Math.random() * 10 - 5) + "");
			} else if(s[i].equals("PosX")) {
				stack.push(x + "");
			} else if(s[i].equals("PosY")) {
				stack.push(y + "");
			} else if(s[i].equals("EAngle")) {
				stack.push(enemy.getMoveAngle() + "");
			} else if(s[i].equals("EPosX")) {
				stack.push(enemy.getX() + "");
			} else if(s[i].equals("EPosY")) {
				stack.push(enemy.getY() + "");
			} else {
				stack.push(s[i]); //constant
			}
		}//for loop

		return Double.valueOf(stack.pop());
	}

	public void setEnemy(FightBot e) {
		enemy = e;
	}
	
	public void resetLives() {
		lives = 3;
	}
	
	public ArrayList<Bullet> getBullets() {
		return bullets;
	}
	
	public void setBrain(String brain) {
		this.brain = brain;
	}
	public int getLives() {
		return lives;
	}
	public void looseLife() {
		lives--;
	}
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public double getMoveAngle() {
		return Math.toRadians(moveAngle);
	}
	public void setLocation(float x, float y) {
		this.x = (int)x;
		this.y = (int)y;
	}

}
