package com.codesmith.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MyActor extends Actor {
	
	private TextureRegion tex;
	private float x, y;
	private float velX, velY;
	//private float scale;
	
	public MyActor(TextureRegion r, Vector2 vel, float scale) {
		tex = r;
		float width = Gdx.graphics.getWidth() * scale;
		float height = width * tex.getRegionHeight() / tex.getRegionWidth();
		setBounds(x, y, width, height);
		this.velX = vel.x;
		this.velY = vel.y;
	}
	
	public void translate(float x, float y) {
		this.x += x;
		this.y += y;
	}
	
	@Override
	public void draw(Batch batch, float alpha) {
		batch.draw(tex, x, y, this.getWidth(), this.getHeight());
		batch.draw(tex, x - this.getWidth(), y, this.getWidth(), this.getHeight());
	}
	
	@Override
	public void act(float delta) {
		x += velX * delta;
		x %= this.getWidth();
		y += velY;
	}

}
