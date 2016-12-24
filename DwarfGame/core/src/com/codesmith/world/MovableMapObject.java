package com.codesmith.world;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.codesmith.graphics.Assets;
import com.codesmith.scripting.MoveAction;
import com.codesmith.scripting.ProximityAction;
import com.codesmith.scripting.ScriptAction;
import com.codesmith.utils.Constants;

public class MovableMapObject extends Sprite {
	
	private ScriptAction action;
	private Rectangle bounds; 
	
	public MovableMapObject(MapObject obj, Player player, World world) {
		if((Float)obj.getProperties().get("width") != 16)
			throw new IllegalArgumentException("Width is not 16.");
		if((Float)obj.getProperties().get("height") % 16 != 0)
			throw new IllegalArgumentException("Height is not divisible by 16.");
		if(obj.getProperties().get("script") == null)
			throw new IllegalArgumentException("No script.");
		bounds = ((RectangleMapObject)obj).getRectangle();
		setPosition(bounds.x, bounds.y);
	}
	
	public void update(float deltaTime) {
		if(action != null)
			action = action.execute(this, deltaTime);
	}
	
	public void draw(SpriteBatch batch) {
		float height = bounds.height / 16;
		batch.draw(Assets.instance.mapAssets.thinBlueUp, bounds.x * Constants.TILE_SIZE, bounds.y * Constants.TILE_SIZE, Assets.instance.mapAssets.thinBlueUp.originalWidth * Constants.TILE_SIZE / 2, Assets.instance.mapAssets.thinBlueUp.originalHeight * Constants.TILE_SIZE / 2, Assets.instance.mapAssets.thinBlueUp.originalWidth * Constants.TILE_SIZE, Assets.instance.mapAssets.thinBlueUp.originalHeight * Constants.TILE_SIZE, 1, 1, 180);
		for(int i = 1; i < height; i++)
			batch.draw(Assets.instance.mapAssets.thinBlueVerticalMiddle, bounds.x * Constants.TILE_SIZE, bounds.y * Constants.TILE_SIZE + i * Assets.instance.mapAssets.thinBlueVerticalMiddle.originalHeight * Constants.TILE_SIZE, Assets.instance.mapAssets.thinBlueVerticalMiddle.originalWidth * Constants.TILE_SIZE / 2, Assets.instance.mapAssets.thinBlueVerticalMiddle.originalHeight * Constants.TILE_SIZE / 2, Assets.instance.mapAssets.thinBlueVerticalMiddle.originalWidth * Constants.TILE_SIZE, Assets.instance.mapAssets.thinBlueVerticalMiddle.originalHeight * Constants.TILE_SIZE, 1, 1, 180);
	}
	
	@Override
	public void setPosition(float x, float y) {
		bounds.x = x;
		bounds.y = y;
		super.setPosition(x, y);
	}
	
	public ScriptAction addScriptAction(ScriptAction action) {
		this.action = action;
		return action;
	}
	
	public void translateSpecial(float dX, float dY) {
		super.translate(dX, dY);
		bounds.x += dX;
		bounds.y += dY;
	} 
	
	@Override
	public Rectangle getBoundingRectangle() {
		return bounds;
	}
	
	public float dist(GameSprite other) {
		double sqrX = (other.getX() - this.getX() * Constants.TILE_SIZE) * (other.getX() - this.getX() * Constants.TILE_SIZE);
		double sqrY = (other.getY() - this.getY() * Constants.TILE_SIZE) * (other.getY() - this.getY() * Constants.TILE_SIZE); 
		return (float) Math.sqrt(  sqrX +  sqrY);
	}

}
