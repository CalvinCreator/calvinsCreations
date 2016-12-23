package com.codesmith.utils;

public class Constants {

	public static final float PLAYER_HEIGHT = 6.5f;
	public static final float PLAYER_MOVE_SPEED = 40f;
	public static final float GRAVITY = 3f;
	public static final float PLAYER_JUMP_SPEED = 1.65f;
	public static final float PLAYER_MAX_SPEED = 3f;
	
	public static final float VIEWPORT_WIDTH = 100f;
	public static final float VIEWPORT_HEIGHT = 100f;
	public static final float TILE_SIZE_PIXELS = 16;
	public static final float TILE_SIZE = (PLAYER_HEIGHT * 1.2f) / TILE_SIZE_PIXELS;
	public static final float SCALE = Constants.PLAYER_HEIGHT  / 23f;
	
	public static final float MIN_ZOOM = 0.25f;
	public static final float MAX_ZOOM = 3f;
	public static final float SITTING_ZOOM = 0.95f;
	public static final float CAMERA_LERP = 5f;
	
	public static final float VIEWPORT_GUI_WIDTH = 800;
	public static final float VIEWPORT_GUI_HEIGHT = 480;

}
