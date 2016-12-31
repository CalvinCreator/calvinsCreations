package com.codesmith.main;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.codesmith.graphics.Assets;
import com.codesmith.ui.Skins;
import com.codesmith.utils.GamePreferences;

public class MainLoop extends Game implements InputProcessor {
	public static final String TAG = MainLoop.class.getName();
	
	//for fade in/out transitions
	private float d1;
	private float d2;
	private float elapsed = 0;
	private Texture screen = null;
	private String transition = "";
	private CScreen next;
	
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
		if(transition.equals("")) {
			Gdx.gl.glClearColor(0.365f, 0.592f, 0.553f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			getScreen().render(Gdx.graphics.getDeltaTime());
		} else if(transition.indexOf("fadeOut") != -1) {
			fadeOut(Gdx.graphics.getDeltaTime());
		} else if(transition.indexOf("fadeIn") != -1) {
			fadeIn(Gdx.graphics.getDeltaTime());
		}
	}
	
	private void fadeOut(float deltaTime) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		elapsed += deltaTime;
		if(elapsed >= d1) 
			elapsed = d1;
		
		float alpha = 1 - elapsed / d1;
		
		((CScreen)(getScreen())).setAlpha(alpha);
		getScreen().render(deltaTime);
		
		if(elapsed >= d1) {
			if(next != null) {
				setScreen(next);
				next = null;
			}
			transition = transition.replace("Out", "");
			elapsed = 0;
			d1 = d2;
			if(transition.equals("fade")) transition = "";
		}
	}
	
	private void fadeIn(float deltaTime) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		elapsed += deltaTime;
		if(elapsed >= d1)
			elapsed = d1;
		
		float alpha = elapsed / d1;
		((CScreen)(getScreen())).setAlpha(alpha);
		getScreen().render(deltaTime);
		
		if(elapsed >= d1) {
			if(next != null) {
				setScreen(next);
				next = null;
			}
			transition = transition.replace("In", "");
			elapsed = 0;
			d1 = d2;
			if(transition.equals("fade")) transition = "";
					
		}

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
		if(screen != null)
			screen.dispose();
	}
	
	public void fadeTransition(CScreen nextScreen, float d1, float d2) {
		transition = "fadeOutIn";
		this.d1 = d1;
		this.d2 = d2;
		next = nextScreen;
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
