package com.codesmith.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.codesmith.world.World;

public class CameraHelper {
	private static final String TAG = CameraHelper.class.getName();

	private Vector2 position;
	private float zoom;
	private Sprite target;
	private OrthographicCamera camera;
	private World world;

	public CameraHelper() {
		position = new Vector2();
		zoom = Constants.SITTING_ZOOM;
	}
	
	public void setCamera(OrthographicCamera camera, World world) {
		this.camera = camera;
		this.world = world;
	}

	public void update(float deltaTime) {
		if (!hasTarget())
			return;

		float targetX = target.getX() + target.getOriginX();
		float targetY = target.getY() + target.getOriginY();
		
		targetX = MathUtils.clamp(targetX, camera.viewportWidth / 2, world.getMap().getProperties().get("width", Integer.class) * Constants.TILE_SIZE * Constants.TILE_SIZE_PIXELS - camera.viewportWidth / 2);
		targetY = MathUtils.clamp(targetY, camera.viewportHeight / 2, world.getMap().getProperties().get("height", Integer.class) * Constants.TILE_SIZE * Constants.TILE_SIZE_PIXELS - camera.viewportHeight / 2);


		if(position.dst(new Vector2(target.getX() + target.getOriginX(), target.getY() + target.getOriginY())) > 0.1f) {
			position.x += (targetX - position.x) * Constants.CAMERA_LERP * deltaTime;
			position.y += (targetY - position.y) * Constants.CAMERA_LERP * deltaTime;
		}
		
	}

	public void setPosition(float x, float y) {
		this.position.set(x, y);
	}

	public Vector2 getPosition() {
		return position;
	}

	public void addZoom(float amount) {
		setZoom(zoom + amount);
	}

	public void setZoom(float zoom) {
		this.zoom = MathUtils.clamp(zoom, Constants.MIN_ZOOM, Constants.MAX_ZOOM);
	}

	public float getZoom() {
		return zoom;
	}

	public void setTarget(Sprite target) {
		this.target = target;
	}

	public Sprite getTarget() {
		return target;
	}

	public boolean hasTarget() {
		return target != null;
	}

	public boolean hasTarget(Sprite target) {
		return hasTarget() && this.target.equals(target);
	}

	public void apply() {

		camera.position.x = position.x;
		camera.position.y = position.y;

		float lowerX = camera.viewportWidth / 2;
		float upperX = world.getMap().getProperties().get("width", Integer.class) * Constants.TILE_SIZE * Constants.TILE_SIZE_PIXELS - camera.viewportWidth / 2;
		
		camera.zoom = zoom;
		
		camera.position.x = MathUtils.clamp(camera.position.x, lowerX, upperX);
		camera.position.y = MathUtils.clamp(camera.position.y, camera.viewportHeight / 2, world.getMap().getProperties().get("height", Integer.class) * Constants.TILE_SIZE * Constants.TILE_SIZE_PIXELS - camera.viewportHeight / 2);
		
		camera.update();
	}
	
	public void setPosition(Vector2 position) {
		this.position.x = position.x;
		this.position.y = position.y;
	}

}