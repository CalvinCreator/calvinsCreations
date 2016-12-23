package com.codesmith.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.codesmith.main.GameState;
import com.codesmith.main.MainMenu;

public class GameMenu extends Window implements Menuable, Menu {

	private Skin skin;
	private Menuable parent;

	private OptionsWindow options;

	public GameMenu(Skin skin, Menuable parent, OptionsWindow options) {
		super("Options", skin);

		this.skin = skin;
		this.parent = parent;
		this.options = options;

		this.add(buildOptButtons()).pad(10, 0, 10, 10);

		this.setColor(1, 1, 1, 0.8f);
		this.setVisible(false);
		this.pack();
		this.setPosition(Gdx.graphics.getWidth() / 2 - this.getWidth() / 2,
				Gdx.graphics.getHeight() / 2 - this.getHeight() / 2); 
	}

	private Table buildOptButtons() {
		Table tbl = new Table();

		TextButton cancel = new TextButton("Resume", skin);
		tbl.add(cancel).pad(0, 5, 5, 5);
		cancel.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent e, Actor actor) {
				onExit();
			}
		});
		
		TextButton option = new TextButton("Settings", skin);
		tbl.add(option).pad(0, 5, 5, 5).row();
		option.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent e, Actor actor) {
				setVisible(false);
				options.onOpening();
			}
		});

		Label lbl = new Label("", skin);
		lbl.setColor(0.75f, 0.75f, 0.75f, 1f);
		lbl.setStyle(new LabelStyle(lbl.getStyle()));
		lbl.getStyle().background = skin.newDrawable("white");
		tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 0, 0, 1);
		tbl.row();
		lbl = new Label("", skin);
		lbl.setColor(0.5f, 0.5f, 0.5f, 1f);
		lbl.setStyle(new LabelStyle(lbl.getStyle()));
		lbl.getStyle().background = skin.newDrawable("white");
		tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 1, 5, 0);
		tbl.row();
		
		TextButton quit = new TextButton(" Quit ", skin);
		tbl.add(quit).pad(0, 5, 0, 5).row();
		quit.setX(50);
		quit.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent e, Actor actor) {
				Gdx.app.exit();
			}
		});
		
		return tbl;
	}

	public void onExit() {
		if(options.isVisible()) {
			options.onCancelClicked();
		} else {
			this.setVisible(false);
			parent.onClose(this);
		}
	}

	@Override
	public void onOpening() {
		this.setVisible(true);
		options.setVisible(false);
	}
	
	@Override
	public void setParent(Menuable parent) {
		this.parent = parent;
	}

	// Called when a Menu this contains is closed
	@Override
	public void onClose(Window window) {
		if (window == options) {
			this.setVisible(true);
		}
	}

}
