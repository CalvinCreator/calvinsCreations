package com.codesmith.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.codesmith.graphics.Assets;
import com.codesmith.utils.CameraHelper;
import com.codesmith.world.World;

public class GameState extends CScreen {
	public static final String TAG = GameState.class.getName();

	private World world;
	private Renderer renderer;

	private CameraHelper camHelper;

	public GameState() {
		init();
	}

	private void init() {
		Assets.instance.songs.trackTwo.setLooping(true);
		Assets.instance.songs.trackTwo.setVolume(0.05f);
		Assets.instance.songs.trackTwo.play();
		world = new World();
		world.setMap("maps/map2.tmx");
		camHelper = new CameraHelper();
		camHelper.setTarget(world.getPlayer());
		renderer = new Renderer(world, camHelper);
		world.setRenderer(renderer);
		try {
			renderer.updateMapData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update(float deltaTime) {
		resolveInput(deltaTime);
		world.update(deltaTime);

		camHelper.update(deltaTime);
	}

	// Input
	public void resolveInput(float deltaTime) {
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE))
			Gdx.app.exit(); // temporary until we build menus
	}

	@Override
	public void resize(int width, int height) {
		renderer.resize(width, height);
	}

	public void dispose() {
		renderer.dispose();
		world.dispose();
	}

	@Override
	public boolean keyUp(int keycode) {
		world.getPlayer().keyUp(keycode);
		
		return false;
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		world.getPlayer().touchDown(screenX, screenY, pointer, button);
		return false;
	}

	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		update(delta);
		
		renderer.render();
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		
	}


}
