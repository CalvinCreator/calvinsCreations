package com.codesmith.world;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.codesmith.graphics.Assets;
import com.codesmith.scripting.ScriptAction;
import com.codesmith.scripting.Spritable;
import com.codesmith.scripting.Updatable;
import com.codesmith.utils.Constants;

public class ItemSprite extends Sprite {
	
	private int id;
	
	private float dT;
	
	private ScriptAction action = null;
	
	public ItemSprite(float x, float y, int id) {
		setPosition(x * Constants.TILE_SIZE, y * Constants.TILE_SIZE);
		this.id = id;
		setSize(16 * Constants.SCALE, 16 * Constants.SCALE);
		setOrigin(getWidth() / 2, getHeight() / 2);
		
		switch(id) {
		case 2:
			setRegion(Assets.instance.icons.axe1);
			break;
		}
	}
	
	public void update(float deltaTime) {
		translate(0, (float)Math.cos(dT) * 0.03f);
		dT += deltaTime;
		if(action instanceof Spritable)
			action = ((Spritable) action).execute(this, deltaTime);
		else if(action instanceof Updatable)
			action = ((Updatable) action).update();
	}
	
	public void draw(SpriteBatch b) {
		setAlpha(b.getColor().a);
		super.draw(b);
	}
	
	public void setScriptAction(ScriptAction a) {
		action = a;
	}
	
	/**
	 * @return
	 * @uml.property  name="id"
	 */
	public int getId() {
		return id;
	}

}
