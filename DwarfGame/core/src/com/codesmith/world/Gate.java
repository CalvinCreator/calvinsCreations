package com.codesmith.world;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.codesmith.graphics.Assets;
import com.codesmith.utils.Constants;

public class Gate {


	private Animation animation;
	private String map;
	private Vector2 position;
	private String destination;
	private float stateTime;
	public boolean collide = false;

	public Gate(int id, String map, Vector2 position, String destination) {
		this.map = map;
		this.position = position;
		this.destination = destination;

		switch (id) {
		case 0: // stone gate
			AtlasRegion[] a = { Assets.instance.icons.gate1, Assets.instance.icons.gate2, Assets.instance.icons.gate3,
					Assets.instance.icons.gate4 };
			animation = new Animation(0.09f, a);
			break;
		}
	}
	
	/**
	 * @return
	 * @uml.property  name="destination"
	 */
	public String getDestination() {
		return destination;
	}
	
	public void update(float deltaTime) {
		if(collide)
			stateTime += deltaTime;
		else
			stateTime -= deltaTime;
		
		if(stateTime > animation.getAnimationDuration())
			stateTime = animation.getAnimationDuration();
		else if(stateTime < 0)
			stateTime = 0;
	}
	
	public boolean open() {
		return stateTime >= animation.getAnimationDuration();
	}
	
	/**
	 * @return
	 * @uml.property  name="map"
	 */
	public String getMap() {
		return map;
	}

	public void draw(SpriteBatch batch) {
		batch.draw(animation.getKeyFrame(stateTime), position.x * Constants.TILE_SIZE, position.y * Constants.TILE_SIZE, animation.getKeyFrame(stateTime).getRegionWidth() * Constants.SCALE, animation.getKeyFrame(stateTime).getRegionHeight() * Constants.SCALE);
	}
	
	public Rectangle getBoundingRectangle() {
		float width = animation.getKeyFrame(stateTime).getRegionWidth() * Constants.SCALE / Constants.TILE_SIZE;
		float height = animation.getKeyFrame(stateTime).getRegionHeight() * Constants.SCALE / Constants.TILE_SIZE;
		return new Rectangle(position.x + width * 0.2f, position.y, width * 0.6f, height * 0.95f);
	}

}
