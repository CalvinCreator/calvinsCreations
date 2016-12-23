package com.codesmith.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.codesmith.graphics.Assets;
import com.codesmith.utils.MyActor;
import com.badlogic.gdx.Game;

public class MainMenu extends CScreen {

	private Stage stage;
	private Skin skin;

	private Actor closeMountains, closeTrees, farMountains, farTrees, bg;

	public MainMenu() {
	}

	@Override
	public void show() {
		Assets.instance.songs.trackOne.setLooping(true);
		Assets.instance.songs.trackOne.play();
		Assets.instance.songs.trackOne.setVolume(0.2f);

		stage = new Stage();

		// Initialise skin
		createBasicSkin();

		TextButton newGameButton = new TextButton("", skin.get("newGameStyle", TextButton.TextButtonStyle.class)); // Use
																													// the
																													// initialised
																													// skin
		newGameButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				Assets.instance.songs.trackOne.stop();
				((Game) Gdx.app.getApplicationListener()).setScreen(new GameState());
			}
		});
		newGameButton.setPosition(Gdx.graphics.getWidth() / 2 - newGameButton.getWidth() / 2,
				Gdx.graphics.getHeight() * 0.6f - newGameButton.getHeight() / 2);
		stage.addActor(newGameButton);

		TextButton quit = new TextButton("", skin.get("quitStyle", TextButton.TextButtonStyle.class));
		quit.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.exit();
			}
		});
		quit.setPosition(Gdx.graphics.getWidth() / 2 - quit.getWidth() / 2,
				Gdx.graphics.getHeight() * 0.4f - quit.getHeight() / 2);
		stage.addActor(quit);

		TextButton options = new TextButton("", skin.get("options", TextButton.TextButtonStyle.class));
		options.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {

			}
		});
		options.setSize(100, 100);
		options.setPosition(Gdx.graphics.getWidth() - options.getWidth() * 1.2f, options.getHeight() * 0.2f);
		stage.addActor(options);

		closeMountains = new MyActor(Assets.instance.backgrounds.findRegion("closeMountains"), 12, 2f);
		farMountains = new MyActor(Assets.instance.backgrounds.findRegion("farMountain"), 4, 1);
		farTrees = new MyActor(Assets.instance.backgrounds.findRegion("farTrees"), 32, 1.3f);
		closeTrees = new MyActor(Assets.instance.backgrounds.findRegion("closeTrees"), 48, 1.3f);
		bg = new MyActor(Assets.instance.backgrounds.findRegion("bg"), 0, 1);

	}

	private void createBasicSkin() {
		// Create a font
		BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/largeFont.fnt"));

		skin = new Skin();
		skin.add("default", font);
		skin.addRegions(Assets.instance.menuAssets);

		// Create a button style
		TextButton.TextButtonStyle newGameStyle = new TextButton.TextButtonStyle();
		newGameStyle.up = skin.getDrawable("newGame");
		newGameStyle.down = skin.getDrawable("newGame");
		newGameStyle.over = skin.getDrawable("newGameLight");
		newGameStyle.font = skin.getFont("default");
		skin.add("newGameStyle", newGameStyle);

		TextButton.TextButtonStyle quit = new TextButton.TextButtonStyle();
		quit.up = skin.getDrawable("quit");
		quit.down = skin.getDrawable("quit");
		quit.over = skin.getDrawable("quitLight");
		quit.font = skin.getFont("default");
		skin.add("quitStyle", quit);

		TextButton.TextButtonStyle optionStyle = new TextButton.TextButtonStyle();
		optionStyle.up = skin.getDrawable("options");
		optionStyle.down = skin.getDrawable("options");
		optionStyle.over = skin.getDrawable("optionsGray");
		optionStyle.font = font;
		skin.add("options", optionStyle);
	}

	@Override
	public void render(float delta) {
		stage.getBatch().begin();
		
		bg.act(delta);
		bg.draw(stage.getBatch(), 1);
		
		farMountains.act(delta);
		farMountains.draw(stage.getBatch(), 1);
		
		closeMountains.act(delta);
		closeMountains.draw(stage.getBatch(), 1);
		
		farTrees.act(delta);
		farTrees.draw(stage.getBatch(), 1);
		
		closeTrees.act(delta);
		closeTrees.draw(stage.getBatch(), 1);
		
		stage.getBatch().end();
		
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().setScreenSize(width, height);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
	}

	@Override
	public boolean keyUp(int keycode) {
		stage.keyUp(keycode);
		if (keycode == Keys.ESCAPE)
			Gdx.app.exit();
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
