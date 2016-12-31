package com.codesmith.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MyActor extends Actor {
	
	/**
	 * @uml.property  name="tex"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private TextureRegion tex;
	/**
	 * @uml.property  name="x"
	 */
	private float x;
	/**
	 * @uml.property  name="y"
	 */
	private float y;
	/**
	 * @uml.property  name="velX"
	 */
	private float velX;
	//private float scale;
	/**
	 * @uml.property  name="velY"
	 */
	private float velY;
	
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
