package com.codesmith.ai;

import com.badlogic.gdx.math.Vector2;

public interface AI {
	
	Vector2 getVelocity(float deltaTime);
	void update(Vector2 nTarget, Vector2 nPosition);
	boolean right();
	boolean agroed();
	void addAgro(int n);
	
	//TODO some way to get actions
	
	//TODO update?

}
