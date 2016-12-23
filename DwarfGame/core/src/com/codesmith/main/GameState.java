package com.codesmith.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.codesmith.graphics.Assets;
import com.codesmith.ui.GameMenu;
import com.codesmith.ui.Menuable;
import com.codesmith.ui.OptionsWindow;
import com.codesmith.ui.Skins;
import com.codesmith.utils.CameraHelper;
import com.codesmith.world.World;

public class GameState extends CScreen implements Menuable {
	public static final String TAG = GameState.class.getName();

	private World world;
	private Renderer renderer;

	private CameraHelper camHelper;

	private Stage stage;
	private GameMenu menu;
	private boolean showMenu = false;

	public GameState() {
		init();
	}

	private void init() {
		Assets.instance.songs.trackTwo.setLooping(true);
		Assets.instance.songs.trackTwo.play();
		world = new World();
		world.setMap("maps/map1Map.tmx");
		camHelper = new CameraHelper();
		camHelper.setTarget(world.getPlayer());
		renderer = new Renderer(world, camHelper);
		world.setRenderer(renderer);
		try {
			renderer.updateMapData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		stage = new Stage();
		stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		OptionsWindow o = new OptionsWindow(Skins.optionsSkin, this);
		menu = new GameMenu(Skins.optionsSkin, this, o);
		o.setParent(menu);
		stage.addActor(o);
		stage.addActor(menu);
	}

	public void update(float deltaTime) {
		resolveInput(deltaTime);

		world.update(deltaTime);
		camHelper.update(deltaTime);
	}

	// Input
	public void resolveInput(float deltaTime) {
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			if (!showMenu) {
				menu.onOpening();
				showMenu = true;
			} else {
				menu.onExit();
			}
		}
	}

	// Called when menu is closed
	@Override
	public void onClose(Window window) {
		showMenu = false;
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		renderer.resize(width, height);
	}

	public void dispose() {
		renderer.dispose();
		world.dispose();
	}

	@Override
	public boolean keyUp(int keycode) {
		if (!showMenu)
			world.getPlayer().keyUp(keycode);
		else
			stage.keyUp(keycode);
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		stage.keyDown(keycode);
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		stage.keyTyped(character);
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (!showMenu)
			world.getPlayer().touchDown(screenX, screenY, pointer, button);
		else
			stage.touchDown(screenX, screenY, pointer, button);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		stage.touchUp(screenX, screenY, pointer, button);
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		stage.touchDragged(screenX, screenY, pointer);
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		stage.mouseMoved(screenX, screenY);
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		stage.scrolled(amount);
		return false;
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		update(delta);
		if(showMenu)
			stage.act(delta);
		renderer.render();
		if (showMenu)
			stage.draw();
	}

	@Override
	public void hide() {
		Assets.instance.songs.trackTwo.stop();
	}

}
