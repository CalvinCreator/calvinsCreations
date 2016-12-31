package com.codesmith.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Rectangle;
import com.codesmith.ai.AI;
import com.codesmith.graphics.Assets;
import com.codesmith.utils.Constants;

public abstract class Enemy extends GameSprite {
	
	protected Player player;
	protected AI ai;
	protected float stunnedCounter = 0f;
	protected int damage;
	
	public Enemy(float x, float y, AtlasRegion img, Player player, float wScale, float hScale) {
		super();
		
		float width = Constants.PLAYER_HEIGHT * (float)img.getRegionWidth() / (float)Assets.instance.gameAssets.run1.getRegionWidth();
		float height = Constants.PLAYER_HEIGHT * (float)img.getRegionHeight() / (float)Assets.instance.gameAssets.run1.getRegionHeight();
		setSize(width * wScale, height * hScale);
		setScale(1, 1);
		setOrigin(getWidth() / 2, getHeight() / 2);
		setPosition(x * Constants.TILE_SIZE * Constants.TILE_SIZE_PIXELS, y * Constants.TILE_SIZE * Constants.TILE_SIZE_PIXELS);
		this.player = player;
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		setAlpha(batch.getColor().a);
		this.setRegion(aManager.getKeyFrame());
		
		if(ai.agroed())
			right = !ai.right();

		super.flip(!right, false);
		super.draw(batch);
	}
	
	public int getDamage() {
		return damage;
	}
	
	@Override
	public boolean hit(Rectangle r, int damage, float magnitude) {
		stunnedCounter = 0.5f;
		velocity.y = Constants.PLAYER_JUMP_SPEED / 2;
		if(getX() + getWidth() / 2 < r.x + r.width / 2) {
			velocity.x = -magnitude;
		}
		else
			velocity.x = magnitude;
		health-=damage;
		return true;
	}

}
