package com.codesmith.world;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.codesmith.ai.AI;
import com.codesmith.ai.BasicAI;
import com.codesmith.ai.DevilAI;
import com.codesmith.graphics.Assets;
import com.codesmith.scripting.ProximityAction;
import com.codesmith.scripting.ScriptAction;
import com.codesmith.scripting.Updatable;
import com.codesmith.utils.Constants;

public class Devil extends Enemy {
	

	public Devil(float x, float y, Player p) {
		super(x, y, Assets.instance.gameAssets.devilWalk1, p, 1, 1);

		// Add animations
		AtlasRegion[] walk = { Assets.instance.gameAssets.devilWalk1, Assets.instance.gameAssets.devilWalk2,
				Assets.instance.gameAssets.devilWalk3, Assets.instance.gameAssets.devilWalk4 };
		aManager.addAnimation("walk", new Animation(0.13f, walk));

		AtlasRegion[] attack = { Assets.instance.gameAssets.devilAttack1, Assets.instance.gameAssets.devilAttack1, Assets.instance.gameAssets.devilAttack1,
				Assets.instance.gameAssets.devilAttack2, Assets.instance.gameAssets.devilAttack3, Assets.instance.gameAssets.devilAttack4 };
		aManager.addAnimation("attack", new Animation(0.2f, attack));

		AtlasRegion[] idle = { Assets.instance.gameAssets.devilWalk1 };
		aManager.addAnimation("idle", new Animation(1f, idle));

		ai = new DevilAI(Constants.PLAYER_MOVE_SPEED * 0.3f, this, aManager, player);
		damage = 1;

		aManager.setAnimation("idle", 0, true);
		currentState = GameSprite.IDLE;

		setScale(1, 1);
		health = maxHealth = 3;
		right = true;
		float height = 40 * Constants.TILE_SIZE;
		float width = aManager.getKeyFrame().getRegionWidth() * Constants.TILE_SIZE;
		setSize(width, height);
		setOrigin(width / 2, height / 2);
	}

	@Override
	public void update(float deltaTime) {
		if (stunnedCounter > 0)
			stunnedCounter -= deltaTime;

		//velocity.x += acceleration.x * deltaTime;
		velocity.y += acceleration.y * deltaTime;
		velocity.y = MathUtils.clamp(velocity.y, -Constants.PLAYER_MAX_SPEED, Constants.PLAYER_MAX_SPEED);

		if (action == null || action instanceof ProximityAction) {
			aManager.update(deltaTime);
			ai.update(player.getPosition(), getPosition());
			if (stunnedCounter > 0)
				velocity.y = ai.getVelocity(deltaTime).add(velocity).y;
			else {
				velocity.y = ai.getVelocity(deltaTime).add(velocity).y;
				velocity.x = ai.getVelocity(deltaTime).x;
			}

			velocity.x = MathUtils.clamp(velocity.x, -Constants.PLAYER_MOVE_SPEED / 2, Constants.PLAYER_MOVE_SPEED / 2);
			this.translate(velocity.x, velocity.y);

			if (action instanceof ProximityAction)
				action = action.execute(this, deltaTime);
		} else
			action = action.execute(this, deltaTime);

		//anchor needs to be in the same place after resizing
		float anchor = right ? getX() : getX() + getWidth();
		float height = 40 * Constants.TILE_SIZE;
		float width = aManager.getKeyFrame().getRegionWidth() * Constants.TILE_SIZE;
		setSize(width, height);
		setOrigin(width / 2, height / 2);
		if(!right) {
			float diff = anchor - getX() - getWidth();
			translate(diff, 0);
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		batch.draw(aManager.getKeyFrame().getTexture(), getX(), getY(), getWidth(), getHeight(), aManager.getKeyFrame().getRegionX(), aManager.getKeyFrame().getRegionY(), aManager.getKeyFrame().getRegionWidth(), aManager.getKeyFrame().getRegionHeight(), !right, false);
	}

	@Override
	public Rectangle getBoundingRectangle() {
		int w = 26;
	
		if (currentState == GameSprite.ATTACKING && aManager.getFrameIndex() == 4 || currentState == GameSprite.RUNNING && aManager.getFrameIndex() == 3)
			w = 30;
		else if (currentState == GameSprite.ATTACKING && aManager.getFrameIndex() == 3)
			w = 34;

		if (right)
			return new Rectangle(getX(), getY(), w * Constants.TILE_SIZE, 40 * Constants.TILE_SIZE);
		else {
			float maxWidth = aManager.getKeyFrame().getRegionWidth() * Constants.TILE_SIZE;
			float width = w * Constants.TILE_SIZE;
			return new Rectangle(getX(), getY(), maxWidth, 40 * Constants.TILE_SIZE);
		}
	}
	
	public Rectangle getPlayerCollisionRectangle() {
		int w = 26;
		if(currentState == GameSprite.ATTACKING && (aManager.getFrameIndex() == 0 || aManager.getFrameIndex() == 1 || aManager.getFrameIndex() == 2) || currentState == GameSprite.RUNNING && aManager.getFrameIndex() == 1)
				return new Rectangle(getX() + 5 * Constants.TILE_SIZE, getY(), 22 * Constants.TILE_SIZE, 40 * Constants.TILE_SIZE);
				
		if (currentState == GameSprite.ATTACKING && aManager.getFrameIndex() == 4 || currentState == GameSprite.RUNNING && aManager.getFrameIndex() == 3)
			w = 30;
		else if (currentState == GameSprite.ATTACKING && aManager.getFrameIndex() == 3)
			w = 34;

		if (right)
			return new Rectangle(getX(), getY(), w * Constants.TILE_SIZE, 40 * Constants.TILE_SIZE);
		else {
			float maxWidth = aManager.getKeyFrame().getRegionWidth() * Constants.TILE_SIZE;
			float width = w * Constants.TILE_SIZE;
			return new Rectangle(getX(), getY(), maxWidth, 40 * Constants.TILE_SIZE);
		}
	}

	public Rectangle getWeaponHitbox() {
		if (currentState == GameSprite.IDLE || currentState == GameSprite.FALLING)
			if (right)
				return new Rectangle(getX() + 26 * Constants.TILE_SIZE, getY() + 3 * Constants.TILE_SIZE,
						10 * Constants.TILE_SIZE, 11 * Constants.TILE_SIZE);
			else
				return new Rectangle(getX(), getY() + 3 * Constants.TILE_SIZE, 10 * Constants.TILE_SIZE,
						11 * Constants.TILE_SIZE);
		if (currentState == GameSprite.RUNNING)
			return getRunningWeaponRectangle(aManager.getFrameIndex());
		if (currentState == GameSprite.ATTACKING)
			return getAttackingWeaponRectangle(aManager.getFrameIndex());
		return new Rectangle(0, 0, 1, 1);
	}

	private Rectangle getRunningWeaponRectangle(int frame) {
		switch (frame) {
		case 0:
			if (right)
				return new Rectangle(getX() + 25 * Constants.TILE_SIZE, getY() + 4 * Constants.TILE_SIZE,
						11 * Constants.TILE_SIZE, 11 * Constants.TILE_SIZE);
			else
				return new Rectangle(getX(), getY() + 4 * Constants.TILE_SIZE, 11 * Constants.TILE_SIZE,
						11 * Constants.TILE_SIZE);
		case 1:
			if (right)
				return new Rectangle(getX() + 27 * Constants.TILE_SIZE, getY() + 4 * Constants.TILE_SIZE,
						4 * Constants.TILE_SIZE, 9 * Constants.TILE_SIZE);
			else
				return new Rectangle(getX(), getY() + 4 * Constants.TILE_SIZE, 4 * Constants.TILE_SIZE,
						9 * Constants.TILE_SIZE);
		case 2:
			if (right)
				return new Rectangle(getX() + 26 * Constants.TILE_SIZE, getY() + 4 * Constants.TILE_SIZE,
						10 * Constants.TILE_SIZE, 11 * Constants.TILE_SIZE);
			else
				return new Rectangle(getX(), getY() + 4 * Constants.TILE_SIZE, 10 * Constants.TILE_SIZE,
						11 * Constants.TILE_SIZE);
		case 3:
			if (right)
				return new Rectangle(getX() + 32 * Constants.TILE_SIZE, getY() + 4 * Constants.TILE_SIZE,
						13 * Constants.TILE_SIZE, 11 * Constants.TILE_SIZE);
			else
				return new Rectangle(getX(), getY() + 4 * Constants.TILE_SIZE, 13 * Constants.TILE_SIZE,
						11 * Constants.TILE_SIZE);
		}

		return null;
	}

	private Rectangle getAttackingWeaponRectangle(int frame) {
		if(frame == 1 || frame == 2) frame = 0;
		switch (frame) {
		case 0:
			if (right)
				return new Rectangle(getX() + 27 * Constants.TILE_SIZE, getY() + 4 * Constants.TILE_SIZE,
						5 * Constants.TILE_SIZE, 10 * Constants.TILE_SIZE);
			else
				return new Rectangle(getX(), getY() + 4 * Constants.TILE_SIZE, 5 * Constants.TILE_SIZE,
						10 * Constants.TILE_SIZE);
		case 3:
			if (right)
				return new Rectangle(getX() + 37 * Constants.TILE_SIZE, getY() + 3 * Constants.TILE_SIZE,
						14 * Constants.TILE_SIZE, 11 * Constants.TILE_SIZE);
			else
				return new Rectangle(getX(), getY() + 3 * Constants.TILE_SIZE, 14 * Constants.TILE_SIZE,
						11 * Constants.TILE_SIZE);
		case 4:
			if (right)
				return new Rectangle(getX() + 33 * Constants.TILE_SIZE, getY() + 3 * Constants.TILE_SIZE,
						14 * Constants.TILE_SIZE, 11 * Constants.TILE_SIZE);
			else
				return new Rectangle(getX(), getY() + 3 * Constants.TILE_SIZE, 14 * Constants.TILE_SIZE,
						11 * Constants.TILE_SIZE);
		case 5:
			if (right)
				return new Rectangle(getX() + 26 * Constants.TILE_SIZE, getY() + 4 * Constants.TILE_SIZE,
						11 * Constants.TILE_SIZE, 11 * Constants.TILE_SIZE);
			else
				return new Rectangle(getX(), getY() + 4 * Constants.TILE_SIZE, 11 * Constants.TILE_SIZE,
						11 * Constants.TILE_SIZE);
		}

		return new Rectangle(0, 0, 1, 1);
	}

	@Override
	public boolean hit(Rectangle r, int damage, float magnitude) {
		stunnedCounter = 0.3f;
		velocity.y = Constants.PLAYER_JUMP_SPEED / 2;
		if(getX() + getPlayerCollisionRectangle().width / 2 / 2 < r.x + r.width / 2) {
			velocity.x = -magnitude;
		}
		else
			velocity.x = magnitude;
		health-=damage;
		setState(GameSprite.RUNNING);
		return true;
	}

	@Override
	public void setState(int nState) {
		if (currentState == nState)
			return;

		//System.out.println(currentState + " to " + nState);
		
		switch (nState) {
		case Devil.ATTACKING:
			aManager.setAnimation("attack", 0, false);
			currentState = nState;
			break;
		case GameSprite.RUNNING:
			aManager.setAnimation("walk", 0, true);
			currentState = nState;
			break;
		}
	}

}
