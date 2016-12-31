package com.codesmith.world;

import com.badlogic.gdx.math.Rectangle;

public class WorldRectangle extends Rectangle {

	/**
	 * @uml.property  name="id"
	 */
	public int id;
	/**
	 * @uml.property  name="parent"
	 */
	private Object parent;
	
	//0: map object
	//1: enemy
	//2: solid movable map object
	//3: moving platform
	//4: gate
	//5: boss
	//6: ladder movable map object
	
	public static final int MAP_OBJECT = 0;
	public static final int ENEMY = 1;
	public static final int MOVABLE_MAP_OBJECT = 2;
	public static final int MOVING_PLATFORM = 3;
	public static final int GATE = 4;
	public static final int BOSS = 5;
	public static final int LADDER_MOVABLE_MAP_OBJECT = 6;
	
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
	
	/**
	 * @return
	 * @uml.property  name="parent"
	 */
	public Object getParent() {
		return parent;
	}
	
}
