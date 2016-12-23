package com.codesmith.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.codesmith.graphics.Assets;
import com.codesmith.utils.GamePreferences;

public class OptionsWindow extends Window implements Menu {

	private Skin optionsSkin;

	private CheckBox chkSound, chkMusic;
	private Slider sldSound, sldMusic;

	private boolean prevSound, prevMusic;
	private float prevSoundLvl, prevMusicLvl;
	
	private Menuable parent;

	public OptionsWindow(Skin optionsSkin, Menuable parent) {
		super("Settings", optionsSkin);
		this.optionsSkin = optionsSkin;

		// Audio Settings: Sound/Music chekboxes and slider
		this.add(buildOptWinAudioSettings()).row();
		// Seperator and Buttons
		this.add(buildOptWinButtons()).pad(10, 0, 10, 10);

		this.setColor(1, 1, 1, 0.8f);
		this.setVisible(false);
		this.pack();
		this.setPosition(Gdx.graphics.getWidth() / 2 - this.getWidth() / 2,
				Gdx.graphics.getHeight() / 2 - this.getHeight() / 2);
		this.parent = parent;
	}

	private Table buildOptWinButtons() {
		Table tbl = new Table();
		// + Seperator
		Label lbl = new Label("", optionsSkin);
		lbl.setColor(0.75f, 0.75f, 0.75f, 1f);
		lbl.setStyle(new LabelStyle(lbl.getStyle()));
		lbl.getStyle().background = optionsSkin.newDrawable("white");
		tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 0, 0, 1);
		tbl.row();
		lbl = new Label("", optionsSkin);
		lbl.setColor(0.5f, 0.5f, 0.5f, 1f);
		lbl.setStyle(new LabelStyle(lbl.getStyle()));
		lbl.getStyle().background = optionsSkin.newDrawable("white");
		tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 1, 5, 0);
		tbl.row();
		// + Save button with event handler
		TextButton save = new TextButton("Save", optionsSkin);
		tbl.add(save).padRight(30);
		save.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent e, Actor actor) {
				onSaveClicked();
			}
		});
		// + Cancel Button with event handler
		TextButton cancel = new TextButton("Cancel", optionsSkin);
		tbl.add(cancel);
		cancel.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent e, Actor actor) {
				onCancelClicked();
			}
		});
		return tbl;
	}
	
	private Table buildOptWinAudioSettings() {
		Table tbl = new Table();
		// + Title
		tbl.pad(10, 10, 0, 10);
		tbl.add(new Label("Audio", optionsSkin, "default-font", Color.ORANGE)).colspan(3);
		tbl.row();
		tbl.columnDefaults(0).padRight(10);
		tbl.columnDefaults(1).padRight(10);
		// + Checkbox, Sound label, sound volume slider
		chkSound = new CheckBox("", optionsSkin);
		chkSound.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent e, Actor actor) {
				GamePreferences.instance.sound = chkSound.isChecked();
			}
		});
		chkSound.setChecked(GamePreferences.instance.sound);
		tbl.add(chkSound);
		tbl.add(new Label("Sound", optionsSkin));
		sldSound = new Slider(0.0f, 1.0f, 0.05f, false, optionsSkin);
		sldSound.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent e, Actor actor) {
				GamePreferences.instance.volSound = sldSound.getValue();
			}
		});
		sldSound.setValue(GamePreferences.instance.volSound);
		tbl.add(sldSound);
		tbl.row();
		// + Checkbox, Music label, music slider
		chkMusic = new CheckBox("", optionsSkin);
		chkMusic.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent e, Actor actor) {
				GamePreferences.instance.music = chkMusic.isChecked();
				int i = chkMusic.isChecked() ? 1 : 0;
				for(Music m : Assets.instance.songs.allSongs)
					m.setVolume(GamePreferences.instance.volMusic * i);
			}
		});
		chkMusic.setChecked(GamePreferences.instance.music);
		tbl.add(chkMusic);
		tbl.add(new Label("Music", optionsSkin));
		sldMusic = new Slider(0.0f, 1.0f, 0.05f, false, optionsSkin);
		sldMusic.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent e, Actor actor) {
				GamePreferences.instance.volMusic = sldMusic.getValue();
				int i = chkMusic.isChecked() ? 1 : 0;
				for(Music m : Assets.instance.songs.allSongs)
					m.setVolume(GamePreferences.instance.volMusic * i);
			}
		});
		sldMusic.setValue(GamePreferences.instance.volMusic);
		tbl.add(sldMusic);
		tbl.row();
		return tbl;
	}
	
	private void onSaveClicked() {
		GamePreferences.instance.save();
		prevMusic = GamePreferences.instance.music;
		prevSound = GamePreferences.instance.sound;
		prevMusicLvl = GamePreferences.instance.volMusic;
		prevSoundLvl = GamePreferences.instance.volSound;
		
		onCancelClicked();
	}
	public void onCancelClicked() {
		GamePreferences.instance.music = prevMusic;
		chkMusic.setChecked(prevMusic);
		GamePreferences.instance.sound = prevSound;
		chkSound.setChecked(prevSound);
		GamePreferences.instance.volSound = prevSoundLvl;
		sldSound.setValue(prevSoundLvl);
		GamePreferences.instance.volMusic = prevMusicLvl;
		sldMusic.setValue(prevMusicLvl);
		int i = chkMusic.isChecked() ? 1 : 0;
		for(Music m : Assets.instance.songs.allSongs)
			m.setVolume(GamePreferences.instance.volMusic * i);
		this.setVisible(false);
		parent.onClose(this);
	}
	
	@Override
	public void setParent(Menuable parent) {
		this.parent = parent;
	}

	@Override
	public void onOpening() {
		this.setVisible(true);
		prevMusic = GamePreferences.instance.music;
		prevSound = GamePreferences.instance.sound;
		prevMusicLvl = GamePreferences.instance.volMusic;
		prevSoundLvl = GamePreferences.instance.volSound;
	}

}
