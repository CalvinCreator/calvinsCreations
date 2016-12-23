package com.codesmith.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.codesmith.graphics.AnimationManager;
import com.codesmith.graphics.Assets;
import com.codesmith.graphics.VectorAnimation;
import com.codesmith.utils.Constants;
import com.codesmith.utils.GamePreferences;

public class Player extends GameSprite {
	public static final String TAG = Player.class.getName();

	private float stunnedCounter = 0;

	private AnimationManager aManager;

	private Inventory inventory;

	private World world;

	private Vector2 spawnLocation;
	private String spawnMap;
	private long lastAttack = 0;
	private float attackCooldown = 0.3f;

	public Player(World world) {
		super();
		setSize(Constants.PLAYER_HEIGHT * (float) Assets.instance.gameAssets.run1.getRegionWidth()
				/ (float) Assets.instance.gameAssets.run1.getRegionHeight(), Constants.PLAYER_HEIGHT);
		setOrigin(getWidth() / 2, getHeight() / 2);
		setPosition(4, 4);

		this.world = world;
		maxHealth = health = 4;
		spawnLocation = new Vector2(4, 3);
		spawnMap = "maps/map1Map.tmx";
		inventory = new Inventory();
		inventory.setWeapon(new Weapon(0));

		// Create animations
		aManager = new AnimationManager();
		AtlasRegion[] idle = { Assets.instance.gameAssets.run1 };
		aManager.addAnimation("idle", new Animation(0.12f, idle));
		AtlasRegion[] run = { Assets.instance.gameAssets.run1, Assets.instance.gameAssets.run2,
				Assets.instance.gameAssets.run3, Assets.instance.gameAssets.run4 };
		aManager.addAnimation("run", new Animation(0.09f, run));
		AtlasRegion[] fall = { Assets.instance.gameAssets.jump1, Assets.instance.gameAssets.jump4 };
		aManager.addAnimation("falling", new VectorAnimation(0.12f, fall, velocity));
		AtlasRegion[] hit = { Assets.instance.gameAssets.hit1, Assets.instance.gameAssets.hit2,
				Assets.instance.gameAssets.hit1 };
		aManager.addAnimation("hit", new Animation(1.52f, hit));
		AtlasRegion[] slash = { Assets.instance.gameAssets.slash2, Assets.instance.gameAssets.slash1,
				Assets.instance.gameAssets.slash2, Assets.instance.gameAssets.slash3 };
		aManager.addAnimation("slash", new Animation(0.08f, slash));
		AtlasRegion[] climb = { Assets.instance.gameAssets.climb1, Assets.instance.gameAssets.climb2,
				Assets.instance.gameAssets.climb3, Assets.instance.gameAssets.climb4 };
		aManager.addAnimation("climbing", new Animation(0.1f, climb));
		aManager.setAnimation("idle", 0, true);
	}

	public void update(float deltaTime) {
		if (stunnedCounter > 0)
			stunnedCounter -= deltaTime;
		if(getY() < 0)
			hit(new Rectangle(0, 0, 1, 1), 1);
		resolveInput(deltaTime);
		aManager.updateWithClimbing(deltaTime, velocity);
		velocity.x += acceleration.x * deltaTime;
		if (currentState != CLIMBING) {
			velocity.y += acceleration.y * deltaTime;
			velocity.y = MathUtils.clamp(velocity.y, -Constants.PLAYER_MAX_SPEED, Constants.PLAYER_MAX_SPEED);
		} else {
			velocity.y = MathUtils.clamp(velocity.y, -Constants.PLAYER_MOVE_SPEED * deltaTime,
					Constants.PLAYER_MOVE_SPEED * deltaTime);
		}
	}

	public void draw(SpriteBatch batch) {
		this.setRegion(aManager.getKeyFrame());
		super.flip(!right, false);
		super.draw(batch);
		boolean b = !right;
		if (currentState == CLIMBING)
			b = true;
		inventory.getWeapon().draw(batch, new Vector2(getX(), getY()), new Vector2(getWidth(), getHeight()),
				currentState, b, aManager.getFrameIndex());

	}

	@Override
	public boolean hit(Rectangle r, int damage) {
		if (stunnedCounter <= 0) {
			aManager.setAnimation("hit", 0, false);
			health -= damage;
			stunnedCounter = 0.5f;
			if(getY() > 0)
				velocity.y = Constants.PLAYER_JUMP_SPEED / 2;
			if (getX() + getWidth() / 2 < r.x + r.width / 2)
				velocity.x = -0.5f;
			else
				velocity.x = 0.5f;

			int i = GamePreferences.instance.sound ? 1 : 0;
			Assets.instance.songs.hit.play(i * GamePreferences.instance.volSound);
		}
		return true;
	}

	public void setState(int nState) {
		if (nState == currentState)
			return;
		switch (currentState) {
		case IDLE:
			currentState = nState;
			if (nState == RUNNING)
				aManager.setAnimation("run", 0, true);
			else if (nState == FALLING)
				aManager.setAnimation("falling", 0, true);
			else if (nState == CLIMBING)
				aManager.setAnimation("climbing", 0, true);
			break;
		case RUNNING:
			currentState = nState;
			if (nState == FALLING)
				aManager.setAnimation("falling", 0, true);
			else if (nState == IDLE)
				aManager.setAnimation("idle", 0, true);
			else if (nState == CLIMBING)
				aManager.setAnimation("climbing", 0, true);
			break;
		case FALLING:
			if (nState == IDLE) {
				currentState = nState;
				aManager.setAnimation("idle", 0, true);
			} else if (nState == CLIMBING) {
				currentState = nState;
				aManager.setAnimation("climbing", 0, true);
			}
			break;
		case CLIMBING:
			if (nState == IDLE) {
				currentState = nState;
				aManager.setAnimation("idle", 0, true);
			}
			break;
		}

	}

	public void resolveInput(float deltaTime) {
		if (stunnedCounter <= 0) {
			velocity.x = 0;
			if (Gdx.input.isKeyPressed(Keys.A)) {
				velocity.x -= Constants.PLAYER_MOVE_SPEED * deltaTime;
				right = false;
				setState(RUNNING);
			}
			if (Gdx.input.isKeyPressed(Keys.D)) {
				velocity.x += Constants.PLAYER_MOVE_SPEED * deltaTime;
				right = true;
				setState(RUNNING);
			}
			if (Gdx.input.isKeyPressed(Keys.SPACE) && getState() != FALLING) {
				setState(FALLING);
				if (getState() != CLIMBING)
					velocity.y = Constants.PLAYER_JUMP_SPEED;
			}
			// int prevState = currentState;
			if (onLadder()) {
				if (Gdx.input.isKeyPressed(Keys.W)) {
					velocity.y = Constants.PLAYER_MOVE_SPEED * deltaTime;
					setState(CLIMBING);
				}
				if (Gdx.input.isKeyPressed(Keys.S)) {
					velocity.y -= Constants.PLAYER_MOVE_SPEED * deltaTime;
					setState(CLIMBING);
				}
				if (!Gdx.input.isKeyPressed(Keys.S) && !Gdx.input.isKeyPressed(Keys.W) && currentState == CLIMBING) {
					velocity.y = 0;
				}
			} else {
				if (currentState == CLIMBING)
					setState(IDLE);
			}
		}
	}

	public boolean onLadder() {
		TiledMapTileLayer l = (TiledMapTileLayer) world.getMap().getLayers().get("Ladders");
		for (int x = 0; x < l.getWidth(); x++)
			for (int y = 0; y < l.getHeight(); y++) {
				if (l.getCell(x, y) != null) {
					Rectangle r = new Rectangle((x + 0.07f) * Constants.TILE_SIZE_PIXELS * Constants.TILE_SIZE,
							y * Constants.TILE_SIZE_PIXELS * Constants.TILE_SIZE,
							Constants.TILE_SIZE_PIXELS * Constants.TILE_SIZE * 0.86f,
							Constants.TILE_SIZE_PIXELS * Constants.TILE_SIZE);
					if (r.overlaps(this.getBoundingRectangle())) {
						return true;
					}
				}
			}
		return false;
	}

	public void keyUp(int keycode) {
		if (keycode == Keys.A) {
			if (!Gdx.input.isButtonPressed(Keys.D) && getState() == RUNNING)
				setState(IDLE);
		} else if (keycode == Keys.D) {
			if (!Gdx.input.isButtonPressed(Keys.A) && getState() == RUNNING)
				setState(IDLE);
		}
	}

	// sets the player to tile x, tile y
	@Override
	public void setPosition(float x, float y) {
		super.setPosition((x - 1) * Constants.TILE_SIZE_PIXELS * Constants.TILE_SIZE,
				(y - 1) * Constants.TILE_SIZE_PIXELS * Constants.TILE_SIZE);
	}

	public String getSpawnMap() {
		return spawnMap;
	}
	
	public Vector2 getSpawnLocation() {
		return spawnLocation;
	}
	
	public void setSpawnMap(String s) {
		spawnMap = s;
	}
	
	public void setSpawnLocation(Vector2 pos) {
		spawnLocation = pos;
	}
	
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if ((System.currentTimeMillis() - lastAttack) / 1000f > attackCooldown) {
			aManager.setAnimation("slash", 0, false);
			if (currentState == CLIMBING) {
				setState(IDLE); // climbing doesn't support setting the state to
								// FALLING

				world.attack(0, inventory.getWeapon().getRange(), inventory.attack());
			} else {
				if (world.attack(0, inventory.getWeapon().getRange(), inventory.attack())) {
					int i = GamePreferences.instance.sound ? 1 : 0;
					Assets.instance.songs.swordHit.play(i * GamePreferences.instance.volSound);
				} else {
					int i = GamePreferences.instance.sound ? 1 : 0;
					Assets.instance.songs.swoosh.play(i * GamePreferences.instance.volSound);
				}
			}
			lastAttack = System.currentTimeMillis();
		}
		return false;
	}

}
