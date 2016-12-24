package com.codesmith.scripting;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.codesmith.world.GameSprite;
import com.codesmith.world.MovableMapObject;

public class ProximityAction extends ScriptAction {
	
	//will call next action when focus gets close enough
	private GameSprite focus = null;
	private float distance;
	private Vector2 location = null;
	
	public ProximityAction(GameSprite target, float distance) {
		focus = target;
		this.distance = distance;
	}
	
	public ProximityAction(Vector2 location, float distance) {
		this.location = location;
		this.distance = distance;
	}

	@Override
	public ScriptAction execute(GameSprite target, float deltaTime) {
		if(location == null) {
			if(Math.abs(target.dist(focus)) < distance) 
				return next;
		} else {
			double dist = location.dst(target.getPosition());
			if(Math.abs(dist) < distance) 
				return next;
		}
		return this;
		
	}
	
	public String toString() {
		String s = location == null ? "GameSprite," + distance : location.toString() + "," + distance;
		return "Proximity Action: " + s + " " + next.toString(); 
	}

	@Override
	public ScriptAction execute(MovableMapObject obj, float deltaTime) {
		if(Math.abs(obj.dist(focus)) < distance)
			return next;
		return this;
	}

}
