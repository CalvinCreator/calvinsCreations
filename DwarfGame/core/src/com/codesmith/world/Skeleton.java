package com.codesmith.world;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.codesmith.ai.BasicAI;
import com.codesmith.graphics.Assets;
import com.codesmith.scripting.ProximityAction;
import com.codesmith.utils.Constants;

public class Skeleton extends Enemy {
	
	public Skeleton(float x, float y, Player player) {
		super(x, y, Assets.instance.gameAssets.skeletonWalk3, player, 0.8f, 0.8f);
		widthScale = 0.75f;
		
		ai = new BasicAI(Constants.PLAYER_MOVE_SPEED / 2);
		
		health = 3;
		damage = 1;
		
		//Create Animations
		AtlasRegion[] idle = {Assets.instance.gameAssets.skeletonWalk3};
		aManager.addAnimation("idle", new Animation(0.12f, idle));
		AtlasRegion[] run = {Assets.instance.gameAssets.skeletonWalk1, Assets.instance.gameAssets.skeletonWalk2, Assets.instance.gameAssets.skeletonWalk3};
		aManager.addAnimation("run", new Animation(0.09f, run));
		aManager.setAnimation("idle", 0, true);
	}

	@Override
	public void update(float deltaTime) {
		if(stunnedCounter > 0)
			stunnedCounter -= deltaTime;
		
		velocity.x += acceleration.x * deltaTime;
		velocity.y += acceleration.y * deltaTime;
		velocity.y = MathUtils.clamp(velocity.y, -Constants.PLAYER_MAX_SPEED, 
									Constants.PLAYER_MAX_SPEED);

		if(action == null || action instanceof ProximityAction) {
			ai.update(player.getPosition(), this.getPosition());
			if(stunnedCounter > 0)
				velocity.y = ai.getVelocity(deltaTime).add(velocity).y;
			else {
				velocity.y = ai.getVelocity(deltaTime).add(velocity).y;
				velocity.x = ai.getVelocity(deltaTime).x;
			}
		
			velocity.x = MathUtils.clamp(velocity.x, -Constants.PLAYER_MOVE_SPEED / 2, Constants.PLAYER_MOVE_SPEED / 2);
		
			if(currentState != FALLING && velocity.x == 0) 
				setState(IDLE);
			else
				setState(RUNNING);
			
			if(action instanceof ProximityAction)
				action = action.execute(this, deltaTime);
		} else 
			action = action.execute(this, deltaTime);
		
		aManager.update(deltaTime);
	}

	@Override
	public void setState(int nState) {
		if(nState == currentState)
			return;
		
		switch(currentState) {
		case IDLE:
			currentState = nState;
			if(nState == RUNNING) 
				aManager.setAnimation("run", 0, true);
			break;
		case RUNNING:
			currentState = nState;
			if(nState == IDLE)
				aManager.setAnimation("idle", 0, true);
			break;
		case FALLING:
			if(nState == IDLE) {
				currentState = nState;
				aManager.setAnimation("idle", 0, true);
			}
			break;
		}
	}

}