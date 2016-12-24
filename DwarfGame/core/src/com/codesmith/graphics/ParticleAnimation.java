package com.codesmith.graphics;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class ParticleAnimation {

	private Animation animation;
	private float counter;
	private float stateTime = 0;
	private Vector2 pos, bounds;
	private Vector2 vel;

	public ParticleAnimation(String key, Vector2 pos, Vector2 bounds) {
		switch (key) {
		case "death":
			TextureRegion[] t = { Assets.instance.pAnimations.death1, Assets.instance.pAnimations.death2,
					Assets.instance.pAnimations.death3, Assets.instance.pAnimations.death4,
					Assets.instance.pAnimations.death5, Assets.instance.pAnimations.death6,
					Assets.instance.pAnimations.death7, Assets.instance.pAnimations.death8,
					Assets.instance.pAnimations.death9, Assets.instance.pAnimations.death10,
					Assets.instance.pAnimations.death11, Assets.instance.pAnimations.death12,
					Assets.instance.pAnimations.death13, Assets.instance.pAnimations.death14,
					Assets.instance.pAnimations.death15, Assets.instance.pAnimations.death16,
					Assets.instance.pAnimations.death17, Assets.instance.pAnimations.death18,
					Assets.instance.pAnimations.death19, Assets.instance.pAnimations.death20,
					Assets.instance.pAnimations.death21, Assets.instance.pAnimations.death22,
					Assets.instance.pAnimations.death23};
			animation = new Animation(0.08f, t);
			vel = new Vector2(0, -0.015f);
			break;
		}
		counter = animation.getAnimationDuration();
		this.pos = pos;
		this.bounds = bounds;
	}

	// updates animation and returns true if the animation is still alive
	public boolean update(float deltaTime) {
		pos.add(vel);
		stateTime += deltaTime;
		counter -= deltaTime;
		return counter > 0;
	}

	public void render(SpriteBatch batch) {
		batch.draw(animation.getKeyFrame(stateTime), pos.x, pos.y,
				bounds.x * 0.5f, bounds.y * 0.5f, bounds.x, bounds.y, 1, 1, 0);
	}

}
