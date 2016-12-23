package com.codesmith.main;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.codesmith.graphics.Assets;
import com.codesmith.ui.Menuable;
import com.codesmith.ui.OptionsWindow;
import com.codesmith.ui.Skins;
import com.codesmith.utils.GamePreferences;

public class MainLoop extends Game implements InputProcessor {
	public static final String TAG = MainLoop.class.getName();
	
	@Override
	public void create () {
		Assets.instance.init(new AssetManager());
		GamePreferences.instance.load();
		Skins.createSkins();
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		setScreen(new MainMenu());
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.365f, 0.592f, 0.553f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		getScreen().render(Gdx.graphics.getDeltaTime() * 1f);
	}
	
	@Override
	public void resize(int width, int height) {
		getScreen().resize(width, height);
	}
	
	@Override 
	public void dispose() {
		getScreen().dispose();
		Assets.instance.dispose();
		Skins.disposeSkins();
	}
	
	@Override
	public void setScreen(Screen s) {
		super.setScreen(s);
	}

	
	@Override
	public boolean keyDown(int keycode) {
		((CScreen)getScreen()).keyDown(keycode);
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		((CScreen)getScreen()).keyUp(keycode);
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		((CScreen)getScreen()).keyTyped(character);
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		((CScreen)getScreen()).touchDown(screenX, screenY, pointer, button);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		((CScreen)getScreen()).touchUp(screenX, screenY, pointer, button);
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		((CScreen)getScreen()).touchDragged(screenX, screenY, pointer);
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		((CScreen)getScreen()).mouseMoved(screenX, screenY);
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		((CScreen)getScreen()).scrolled(amount);
		return false;
	}
	
}
