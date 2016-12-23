package com.codesmith.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.codesmith.graphics.Assets;
import com.codesmith.ui.Menuable;
import com.codesmith.ui.MyActor;
import com.codesmith.ui.OptionsWindow;
import com.codesmith.ui.Skins;
import com.codesmith.utils.GamePreferences;

public class MainMenu extends CScreen implements Menuable {

	private Stage stage;
	private Skin skin;

	private OptionsWindow optionsWindow;
	
	private Actor closeMountains, closeTrees, farMountains, farTrees, bg;
	
	private BitmapFont defaultFont;
	
	private TextButton options, newGameButton, quit;

	public MainMenu() {
		
	}

	@Override
	public void show() {
		Assets.instance.songs.trackOne.setLooping(true);
		Assets.instance.songs.trackOne.play();
		int i = GamePreferences.instance.music ? 1 : 0;
		Assets.instance.songs.trackOne.setVolume(i * GamePreferences.instance.volMusic);

		stage = new Stage();
		stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

		// Initialise skin
		createBasicSkin();
		
		
		optionsWindow = new OptionsWindow(Skins.optionsSkin, this);

		newGameButton = new TextButton("", skin.get("newGameStyle", TextButton.TextButtonStyle.class)); 
		
		newGameButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				Assets.instance.songs.trackOne.stop();
				((Game) Gdx.app.getApplicationListener()).setScreen(new GameState());
			}
		});
		newGameButton.setPosition(Gdx.graphics.getWidth() / 2 - newGameButton.getWidth() / 2,
				Gdx.graphics.getHeight() * 0.6f - newGameButton.getHeight() / 2);
		stage.addActor(newGameButton);

		quit = new TextButton("", skin.get("quitStyle", TextButton.TextButtonStyle.class));
		quit.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.exit();
			}
		});
		quit.setPosition(Gdx.graphics.getWidth() / 2 - quit.getWidth() / 2,
				Gdx.graphics.getHeight() * 0.4f - quit.getHeight() / 2);
		stage.addActor(quit);

		options = new TextButton("", skin.get("options", TextButton.TextButtonStyle.class));
		options.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				options.setVisible(false);
				newGameButton.setVisible(false);
				quit.setVisible(false);
				optionsWindow.onOpening();
			}
		});
		options.setSize(100, 100);
		options.setPosition(Gdx.graphics.getWidth() - options.getWidth() * 1.2f, options.getHeight() * 0.2f);
		stage.addActor(options);

		closeMountains = new MyActor(Assets.instance.backgrounds.findRegion("closeMountains"), 12, 2f);
		farMountains = new MyActor(Assets.instance.backgrounds.findRegion("farMountain"), 4, 1);
		farTrees = new MyActor(Assets.instance.backgrounds.findRegion("farTrees"), 39, 1.3f);
		closeTrees = new MyActor(Assets.instance.backgrounds.findRegion("closeTrees"), 58, 1.3f);
		bg = new MyActor(Assets.instance.backgrounds.findRegion("bg"), 0, 1);

		stage.addActor(optionsWindow);
	}

	private void createBasicSkin() {
		// Create a font
		defaultFont = new BitmapFont(Gdx.files.internal("fonts/largeFont.fnt"));

		skin = new Skin();
		skin.add("default", defaultFont);
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
		optionStyle.font = defaultFont;
		skin.add("options", optionStyle);
	}
	
	
	
	@Override
	public void render(float delta) {
		stage.getBatch().setColor(1, 1, 1, 1);
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
	
	public void onClose(Window window) {
		newGameButton.setVisible(true);
		options.setVisible(true);
		quit.setVisible(true);
	}
	
	

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
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
		if (keycode == Keys.ESCAPE) {
			if(optionsWindow.isVisible())
				optionsWindow.onCancelClicked();
			else
				Gdx.app.exit();
		}
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
