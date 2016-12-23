package com.codesmith.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.codesmith.graphics.Assets;
import com.codesmith.utils.Constants;

public class Weapon {

	private int fCount = -1;
	private long lastAttack = 0;
	private float delta = 0.08f;

	private int damage;
	private float range;
	private int id;
	private TextureRegion img;

	public Weapon(int id) {
		this.id = id;
		switch (id) {
		case 0:
			img = Assets.instance.icons.sword11;
			range = 10f;
			damage = 1;
		}
	}

	public int getId() {
		return id;
	}

	public int attack() {
		fCount = 0;
		lastAttack = System.currentTimeMillis();
		return damage;
	}
	
	public float getRange() {
		return range;
	}

	public void draw(SpriteBatch batch, Vector2 pPos, Vector2 pDim, int pState, boolean flip, int frame) {
		img.flip(flip, false);

		if (fCount >= 0) {

			drawAttack(batch, pPos, pDim, flip, fCount, Constants.SCALE);

		} else if (pState == Player.RUNNING) {
			drawRunning(batch, pPos, pDim, flip, frame, Constants.SCALE);
		} else if (pState == Player.FALLING) {
			drawFalling(batch, pPos, pDim, flip, frame, Constants.SCALE);
		} else if (pState == Player.IDLE) {
			drawIdle(batch, pPos, pDim, flip, frame, Constants.SCALE);
		} else if (pState == Player.CLIMBING) {
			batch.draw(img, pPos.x + pDim.x * 0.07f, pPos.y + pDim.y * 0.23f, 0, 0,
					Constants.SCALE * img.getRegionWidth(), Constants.SCALE * img.getRegionHeight(), 1, 1, 0);
		}

		img.flip(flip, false);
	}

	private void drawAttack(SpriteBatch batch, Vector2 pPos, Vector2 pDim, boolean flip, int frame, float scale) {
		switch (frame) {
		case 0:
			if (!flip)
				batch.draw(img, pPos.x + pDim.x * 0.07f, pPos.y + pDim.y * 0.23f, 0, 0, scale * img.getRegionWidth(),
						scale * img.getRegionHeight(), 1, 1, 360 - 30);
			else
				batch.draw(img, pPos.x - pDim.x * 0.04f, pPos.y - pDim.y * 0.11f, 0, 0, scale * img.getRegionWidth(),
						scale * img.getRegionHeight(), 1, 1, 30);
			break;
		case 1:
			if (!flip)
				batch.draw(img, pPos.x + pDim.x * 0.25f, pPos.y + pDim.y * 0.25f, 0, 0, scale * img.getRegionWidth(),
						scale * img.getRegionHeight(), 1, 1, 360 + 10);
			else
				batch.draw(img, pPos.x - pDim.x * 0.2f, pPos.y + pDim.y * 0.57f, 0, 0, scale * img.getRegionWidth(),
						scale * img.getRegionHeight(), 1, 1, -10);
			break;
		case 2:
			if (!flip)
				batch.draw(img, pPos.x + pDim.x * 0.15f, pPos.y + pDim.y * 0.15f, 0, 0, scale * img.getRegionWidth(),
						scale * img.getRegionHeight(), 1, 1, 360 - 30);
			else
				batch.draw(img, pPos.x + pDim.x * 0.05f, pPos.y - pDim.y * 0.1f, 0, 0, scale * img.getRegionWidth(),
						scale * img.getRegionHeight(), 1, 1, 30);
			break;
		case 3:
			if (!flip)
				batch.draw(img, pPos.x + pDim.x * 0.08f, pPos.y + pDim.y * 0.25f, 0, 0, scale * img.getRegionWidth(),
						scale * img.getRegionHeight(), 1, 1, 360 - 50);
			else
				batch.draw(img, pPos.x + pDim.x * 0.13f, pPos.y - pDim.y * 0.25f, 0, 0, scale * img.getRegionWidth(),
						scale * img.getRegionHeight(), 1, 1, 40);
			break;
		}
		if ((System.currentTimeMillis() - lastAttack) / 1000f > delta) {
			fCount++;
			lastAttack = System.currentTimeMillis();
		}
		if (fCount > 3)
			fCount = -1;
	}

	private void drawIdle(SpriteBatch batch, Vector2 pPos, Vector2 pDim, boolean flip, int frame, float scale) {
		if (!flip)
			drawRunning(batch, pPos, pDim, flip, 1, scale);
		else
			drawRunning(batch, pPos, pDim, flip, 2, scale);
	}

	private void drawRunning(SpriteBatch batch, Vector2 pPos, Vector2 pDim, boolean flip, int frame, float scale) {
		switch (frame) {
		case 0:
			if (!flip) {
				batch.draw(img, pPos.x - pDim.x * 0.03f, pPos.y + pDim.y * 0.25f, 0, 0, scale * img.getRegionWidth(),
						scale * img.getRegionHeight(), 1, 1, 360 - 55);
			} else {
				batch.draw(img, pPos.x + pDim.x * 0.03f, pPos.y + pDim.y * 0.25f, img.getRegionWidth() * scale, 0,
						scale * img.getRegionWidth(), scale * img.getRegionHeight(), 1, 1, 75);
			}
			break;
		case 1:
			if (!flip) {
				batch.draw(img, pPos.x - pDim.x * 0.06f, pPos.y + pDim.y * 0.28f, 0, 0, scale * img.getRegionWidth(),
						scale * img.getRegionHeight(), 1, 1, 360 - 62);
			} else {
				batch.draw(img, pPos.x + pDim.x * 0.03f, pPos.y + pDim.y * 0.26f, img.getRegionWidth() * scale, 0,
						scale * img.getRegionWidth(), scale * img.getRegionHeight(), 1, 1, 62);
			}
			break;
		case 2:
			if (!flip) {
				batch.draw(img, pPos.x - pDim.x * 0.04f, pPos.y + pDim.y * 0.30f, 0, 0, scale * img.getRegionWidth(),
						scale * img.getRegionHeight(), 1, 1, 360 - 75);
			} else {
				batch.draw(img, pPos.x + pDim.x * 0.03f, pPos.y + pDim.y * 0.26f, img.getRegionWidth() * scale, 0,
						scale * img.getRegionWidth(), scale * img.getRegionHeight(), 1, 1, 55);
			}
			break;
		case 3:
			if (!flip) {
				batch.draw(img, pPos.x - pDim.x * 0.06f, pPos.y + pDim.y * 0.27f, 0, 0, scale * img.getRegionWidth(),
						scale * img.getRegionHeight(), 1, 1, 360 - 62);
			} else {
				batch.draw(img, pPos.x + pDim.x * 0.03f, pPos.y + pDim.y * 0.26f, img.getRegionWidth() * scale, 0,
						scale * img.getRegionWidth(), scale * img.getRegionHeight(), 1, 1, 62);
			}
			break;
		}
	}

	private void drawFalling(SpriteBatch batch, Vector2 pPos, Vector2 pDim, boolean flip, int frame, float scale) {
		switch (frame) {
		case 0:
			if (!flip) {
				batch.draw(img, pPos.x - pDim.x * 0.03f, pPos.y + pDim.y * 0.27f, 0, 0, scale * img.getRegionWidth(),
						scale * img.getRegionHeight(), 1, 1, 360 - 80);
			} else {
				batch.draw(img, pPos.x - pDim.x * 0.05f, pPos.y + pDim.y * 0.18f, img.getRegionWidth() * scale, 0,
						scale * img.getRegionWidth(), scale * img.getRegionHeight(), 1, 1, 70);
			}
			break;
		case 1:
			if (!flip) {
				batch.draw(img, pPos.x - pDim.x * 0.08f, pPos.y + pDim.y * 0.24f, 0, 0, scale * img.getRegionWidth(),
						scale * img.getRegionHeight(), 1, 1, 360 - 70);
			} else {
				batch.draw(img, pPos.x - pDim.x * 0.02f, pPos.y + pDim.y * 0.15f, img.getRegionWidth() * scale, 0,
						scale * img.getRegionWidth(), scale * img.getRegionHeight(), 1, 1, 60);
			}
			break;
		}
	}

	public float getDamage() {
		return damage;
	}

}
