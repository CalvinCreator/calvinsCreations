package com.codesmith.scripting;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.codesmith.graphics.Assets;
import com.codesmith.world.GameSprite;
import com.codesmith.world.MovableMapObject;

public class MusicChangeAction extends ScriptAction implements Spritable, Updatable {
	
	public Music m;
	
	public MusicChangeAction(Music m) {
		this.m = m;
	}

	@Override
	public ScriptAction execute(GameSprite target, float deltaTime) {
		Assets.instance.songs.currentSong.stop();
		m.play();
		Assets.instance.songs.currentSong = m;
		return next;
	}

	@Override
	public ScriptAction execute(MovableMapObject obj, float deltaTime) {
		Assets.instance.songs.currentSong.stop();
		m.play();
		Assets.instance.songs.currentSong = m;
		return next;
	}

	@Override
	public ScriptAction update() {
		Assets.instance.songs.currentSong.stop();
		m.play();
		Assets.instance.songs.currentSong = m;
		return next;
	}

	@Override
	public ScriptAction execute(Sprite s, float deltaTime) {
		Assets.instance.songs.currentSong.stop();
		m.play();
		Assets.instance.songs.currentSong = m;
		return next;
	}

}
