package com.codesmith.ai;

import com.badlogic.gdx.math.Vector2;

public class BasicAI implements AI {
	
	/**
	 * @uml.property  name="target"
	 * @uml.associationEnd  
	 */
	private Vector2 target;
	/**
	 * @uml.property  name="position"
	 * @uml.associationEnd  
	 */
	private Vector2 position;
	/**
	 * @uml.property  name="moveSpeed"
	 */
	private float moveSpeed;
	/**
	 * @uml.property  name="agro"
	 */
	private int agro = 0;
	
	public BasicAI(float moveSpeed) {
		this.moveSpeed = moveSpeed;
	}
	
	public void update(Vector2 nTarget, Vector2 nPosition) {
		target = nTarget;
		position = nPosition;
	}

	@Override
	public Vector2 getVelocity(float deltaTime) {
		float speed = moveSpeed;
		if(target.dst(position) < 30)
			agro++;
		if(agro == 0)
			speed = 0;

		if(Math.abs(target.x - position.x) < 0.2f)
			return new Vector2(0, 0);
		if(target.x < position.x)
			return new Vector2(-speed * deltaTime, 0);
		return new Vector2(speed * deltaTime, 0);
	}
	
	@Override
	public boolean right() {
		return target.x > position.x;
	}
	
	@Override
	public boolean agroed() {
		return agro > 0;
	}
	
	public void addAgro(int n) {
		agro += n;
	}

}
