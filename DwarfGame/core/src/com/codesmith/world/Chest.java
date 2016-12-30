package com.codesmith.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import com.codesmith.scripting.ProximityAction;
import com.codesmith.scripting.ScriptAction;
import com.codesmith.utils.Constants;

public class Chest extends Sprite {
	
	private ScriptAction script;
	
	private int[] inventory;
	
	private Animation ani;
	
	//TODO: FINISH CHEST
	
	public Chest(RectangleMapObject obj, World world) {
	
		setPosition(Integer.valueOf((String)obj.getProperties().get("x")) * Constants.TILE_SIZE, Integer.valueOf((String)obj.getProperties().get("y")) * Constants.TILE_SIZE);
		try {
			script = ScriptAction.loadScript(Gdx.files.internal("scripts/chestScript.txt").readString(), world);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String[] i = ((String)(obj.getProperties().get("inventory"))).split(",");
		inventory = new int[i.length];
		for(int k = 0; k < i.length; k++)
			inventory[k] = Integer.valueOf(i[k]);
		
	}
	
	public void update(float deltaTime) {
		if(script instanceof ProximityAction)
			((ProximityAction)(script)).execute(this, deltaTime);
		else {
			
		}
	}
	
	public void render(SpriteBatch b) {
		//if(script instanceof ProximityAction)
			//b.draw(region, x, y, width, height);
	}
	
	public Vector2 getPosition() {
		return new Vector2(getX(), getY());
	}
	
}
