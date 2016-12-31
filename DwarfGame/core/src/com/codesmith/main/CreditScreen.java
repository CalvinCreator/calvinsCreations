	package com.codesmith.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.codesmith.graphics.Assets;
import com.codesmith.ui.GameMenu;
import com.codesmith.ui.Menuable;
import com.codesmith.ui.MyActor;
import com.codesmith.ui.OptionsWindow;
import com.codesmith.ui.Skins;

public class CreditScreen extends CScreen implements Menuable {
	
	private Stage stage;
	private MyActor credits;

	@Override
	public void show() {
		stage = new Stage();
		stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		
		credits = new MyActor(Assets.instance.credits, new Vector2(0, 1), 1);
		credits.translate(0, -Gdx.graphics.getHeight() * 0.9f);
		stage.addActor(credits);
		
		if(Assets.instance.songs.currentSong != Assets.instance.songs.trackFour) {
			Assets.instance.songs.trackFour.setLooping(true);
			Assets.instance.songs.trackFour.play();
			Assets.instance.songs.currentSong = Assets.instance.songs.trackFour;
		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.365f, 0.592f, 0.553f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.getBatch().setColor(1, 1, 1, alpha);
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void hide() {
		stage.dispose();
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
	
	@Override
	public boolean keyUp(int keycode) {
		stage.keyUp(keycode);
		if(keycode == Keys.ESCAPE)
			Gdx.app.exit();
		return false;
	}

	@Override
	public Batch getBatch() {
		return stage.getBatch();
	}

	@Override
	public void onClose(Window window) {
		
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

}
