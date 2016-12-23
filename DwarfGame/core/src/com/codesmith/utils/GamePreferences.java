package com.codesmith.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;

public class GamePreferences {
	
	public static final GamePreferences instance = new GamePreferences();
	
	public boolean sound;
	public boolean music;
	public float volSound;
	public float volMusic;
	
	private Preferences prefs;
	
	private GamePreferences() {
		prefs = Gdx.app.getPreferences("dwarfGameSettings");
	}
	
	public void load() {
		sound = prefs.getBoolean("sound", true);
		music = prefs.getBoolean("music", true);
		volSound = MathUtils.clamp(prefs.getFloat("volSound", 0.5f), 0f, 1f);
		volMusic = MathUtils.clamp(prefs.getFloat("volMusic", 0.5f), 0f, 1f);
		prefs.clear();
	}
	public void save() {
		prefs.putBoolean("sound", sound);
		prefs.putBoolean("music", music);
		prefs.putFloat("volSound", volSound);
		prefs.putFloat("volMusic", volMusic);
		prefs.flush();
	}

}
