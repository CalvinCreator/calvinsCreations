package com.codesmith.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MyActor extends Actor {
	
	private TextureRegion tex;
	private float x, y;
	private float vel;
	//private float scale;
	
	public MyActor(TextureRegion r, float vel, float scale) {
		tex = r;
		float width = Gdx.graphics.getWidth() * scale;
		float height = width * tex.getRegionHeight() / tex.getRegionWidth();
		setBounds(x, y, width, height);
		this.vel = vel;
	}
	
	@Override
	public void draw(Batch batch, float alpha) {
		batch.draw(tex, x, y, this.getWidth(), this.getHeight());
		batch.draw(tex, x - this.getWidth(), y, this.getWidth(), this.getHeight());
	}
	
	@Override
	public void act(float delta) {
		x += vel * delta;
		x %= this.getWidth();
	}

}
