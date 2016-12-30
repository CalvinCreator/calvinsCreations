package com.codesmith.world;

import com.badlogic.gdx.math.Rectangle;

public class WorldRectangle extends Rectangle {

	public int id;
	private Object parent;
	
	//0: map object
	//1: enemy
	//2: solid movable map object
	//3: moving platform
	//4: gate
	//5: boss
	//6: ladder movable map object
	
	public static final int MAP_OBJECT = 0, ENEMY = 1, MOVABLE_MAP_OBJECT = 2, MOVING_PLATFORM = 3, GATE = 4, BOSS = 5, LADDER_MOVABLE_MAP_OBJECT = 6;
	
	public WorldRectangle(Rectangle r, int id) {
		super(r);
		this.id = id;
		parent = null;
	}
	
	public WorldRectangle(Rectangle r, int id, Object parent) {
		super(r);
		this.id = id;
		this.parent = parent;
	}
	
	public Object getParent() {
		return parent;
	}
	
}
