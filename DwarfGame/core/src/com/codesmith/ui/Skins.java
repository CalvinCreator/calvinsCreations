package com.codesmith.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class Skins {
	
	public static Skin optionsSkin;
	
	public static void createSkins() {
		TextureAtlas atlas = new TextureAtlas("menuImages/uiskin.atlas");
		optionsSkin = new Skin(Gdx.files.internal("menuImages/uiskin.json"), atlas);
		optionsSkin.get("default", TextButton.TextButtonStyle.class).up = optionsSkin.newDrawable("default-round", Color.WHITE);
		optionsSkin.get("default", TextButton.TextButtonStyle.class).over = optionsSkin.newDrawable("default-round", Color.GRAY);
	}

	public static void disposeSkins() {
		optionsSkin.dispose();
	}
}
