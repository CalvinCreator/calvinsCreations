package com.codesmith.world;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.codesmith.graphics.AnimationManager;
import com.codesmith.scripting.ScriptAction;
import com.codesmith.utils.Constants;

public abstract class GameSprite extends Sprite {
	
	protected Vector2 velocity, acceleration;
	public boolean right = true;
	protected AnimationManager aManager;
	protected int currentState = IDLE;
	protected int health, maxHealth;
	public static final int IDLE = 0, RUNNING = 1, FALLING = 2, CLIMBING = 3, ATTACKING = 4;
	
	protected ScriptAction action = null;
	
	protected float widthScale, heightScale;
	
	public GameSprite() {
		velocity = new Vector2(0, 0);
		acceleration = new Vector2(0, -Constants.GRAVITY);
		aManager = new AnimationManager();
		widthScale = heightScale = 1;
	}
	
	public abstract void update(float deltaTime);
	
	public abstract boolean hit(Rectangle r, int damage, float magnitude); 
	
	public void draw(SpriteBatch batch) {
		super.draw(batch);
	}
	
	public float dist(GameSprite s) {
		Vector2 v1 = new Vector2(this.getOriginX() + this.getX(), this.getOriginY() + this.getY());
		Vector2 v2 = new Vector2(s.getOriginX() + s.getX(), s.getOriginY() + s.getY());
		return v1.dst(v2);
	}
	
	public ScriptAction addScriptAction(ScriptAction action) {
		this.action = action;
		return action;
	}

	public abstract void setState(int nState);
	
	public int getState() {
		return currentState;
	}
	
	public Vector2 getPosition() {
		return new Vector2(this.getX(), this.getY());
	}
	
	public Vector2 getVelocity() {
		return velocity;
	}
	
	public void setPosition(Vector2 pos) {
		this.setX(pos.x);
		this.setY(pos.y);
	}
	
	public int getHealth() {
		return health;
	}
	
	public int getMaxHealth() {
		return maxHealth;
	}
	
	public Rectangle getBoundingRectangle() {
		Rectangle r = super.getBoundingRectangle();
		if(right) {
			r.width *= widthScale;
			r.height *= heightScale;
		}
		return r;
	}

}
