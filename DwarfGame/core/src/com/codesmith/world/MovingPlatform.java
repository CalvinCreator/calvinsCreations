package com.codesmith.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.codesmith.graphics.Assets;
import com.codesmith.utils.Constants;

public class MovingPlatform {
	
	public float x, y;
	public float speed;
	public int width, height;
	public float delta;
	public int path; //0=sideways 1=vertical
	private float b1, b2;
	private boolean right = true; //or up
	
	public MovingPlatform(RectangleMapObject r) throws Exception {
		
		x = r.getRectangle().getX() * Constants.TILE_SIZE;
		y = r.getRectangle().getY() * Constants.TILE_SIZE;
		path = r.getProperties().get("path", String.class).equals("up") ? 1 : 0;
		width = Integer.valueOf((String) r.getProperties().get("width"));
		height = Integer.valueOf((String) r.getProperties().get("height"));
		if(path == 0 && width == 1)
			throw new Exception("Invalid Moving Platform: path=0 and width=1");
		else if(path == 1 && height != 2)
			throw new Exception("Invalid Moving Platform: path=1 and height is not 2");
		if(path == 0)
			b1 = x;
		else
			b1 = y;
		if(path == 0)
			b2 = r.getRectangle().getWidth() * Constants.TILE_SIZE + x - width * Constants.TILE_SIZE_PIXELS * Constants.TILE_SIZE;
		else
			b2 = r.getRectangle().getHeight() * Constants.TILE_SIZE + y - height * Constants.TILE_SIZE_PIXELS * Constants.TILE_SIZE;
		speed = Float.valueOf((String) r.getProperties().get("speed"));
		
		if(path == 1)
			y = b2;
	}
	
	public void update(float deltaTime) {
		float pX = x;
		float pY = y;
		if(path == 0) { //sideways
			if(right) {
				x += speed * deltaTime;
				delta = x - pX;
				if(x >= b2)
					right = false;
			} else {
				x -= speed * deltaTime;
				delta = x - pX;
				if(x <= b1)
					right = true;
			}
		} else {
			if(right) {
				y += speed * deltaTime;
				delta = y - pY;
				if(y >= b2)
					right = false;
			} else {
				y -= speed* deltaTime;
				delta = y - pY;
				if(y <= b1)
					right = true;
			}
		}
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public Rectangle getBoundingRectangle() {
		return new Rectangle(x / Constants.TILE_SIZE, y / Constants.TILE_SIZE, width * Constants.TILE_SIZE_PIXELS, height * Constants.TILE_SIZE_PIXELS);
	}
	
	public void draw(SpriteBatch batch)  {
		if(height == 1) {
			batch.draw(Assets.instance.mapAssets.thinBlueLeft, x, y, Assets.instance.mapAssets.thinBlueLeft.originalWidth * Constants.TILE_SIZE, Assets.instance.mapAssets.thinBlueLeft.originalHeight * Constants.TILE_SIZE);
			for(int i = 1; i < width - 1; i++) {
				batch.draw(Assets.instance.mapAssets.thinBlueMiddle, x + i * Assets.instance.mapAssets.thinBlueMiddle.originalWidth * Constants.TILE_SIZE, y, Assets.instance.mapAssets.thinBlueMiddle.originalWidth * Constants.TILE_SIZE, Assets.instance.mapAssets.thinBlueMiddle.originalHeight * Constants.TILE_SIZE);
			}
			batch.draw(Assets.instance.mapAssets.thinBlueRight, x + (width - 1) * Assets.instance.mapAssets.thinBlueRight.originalWidth * Constants.TILE_SIZE, y, Assets.instance.mapAssets.thinBlueRight.originalWidth * Constants.TILE_SIZE, Assets.instance.mapAssets.thinBlueRight.originalHeight * Constants.TILE_SIZE);
		} else if(width == 1) {
			batch.draw(Assets.instance.mapAssets.thinBlueUp, x, y, Assets.instance.mapAssets.thinBlueUp.originalWidth * Constants.TILE_SIZE / 2, Assets.instance.mapAssets.thinBlueUp.originalHeight * Constants.TILE_SIZE / 2, Assets.instance.mapAssets.thinBlueUp.originalWidth * Constants.TILE_SIZE, Assets.instance.mapAssets.thinBlueUp.originalHeight * Constants.TILE_SIZE, 1, 1, 180);
			batch.draw(Assets.instance.mapAssets.thinBlueUp, x, y + Assets.instance.mapAssets.thinBlueUp.originalHeight * Constants.TILE_SIZE, Assets.instance.mapAssets.thinBlueUp.originalWidth * Constants.TILE_SIZE / 2, Assets.instance.mapAssets.thinBlueUp.originalHeight * Constants.TILE_SIZE / 2, Assets.instance.mapAssets.thinBlueUp.originalWidth * Constants.TILE_SIZE, Assets.instance.mapAssets.thinBlueUp.originalHeight * Constants.TILE_SIZE, 1, 1, 0);
		}
	}

}
