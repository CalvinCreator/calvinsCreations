package com.codesmith.ai;

import com.badlogic.gdx.math.Vector2;
import com.codesmith.graphics.AnimationManager;
import com.codesmith.graphics.Assets;
import com.codesmith.utils.Constants;
import com.codesmith.world.Devil;
import com.codesmith.world.GameSprite;
import com.codesmith.world.Player;

public class DevilAI implements AI {
	
	private Devil devil;
	private AnimationManager aManager;
	private float moveSpeed;
	private Player player;
	
	//can only attack once every 2 seconds
	private long lastAttack = 0;
	
	private boolean attacking = false;

	private boolean agroed = false;
	private long soundTimer = Long.MAX_VALUE;
	
	public DevilAI(float moveSpeed, Devil d, AnimationManager a, Player p) {
		this.moveSpeed = moveSpeed;
		devil = d;
		aManager = a;
		player = p;
	}

	@Override
	public Vector2 getVelocity(float deltaTime) {
		if(Math.abs(player.getX() + player.getWidth() /2 - devil.getX() - devil.getPlayerCollisionRectangle().width / 2) > 1) {
			if(devil.getState() == GameSprite.RUNNING && agroed && devil.right) 
				return new Vector2(moveSpeed * deltaTime, 0);
			else if(devil.getState() == GameSprite.RUNNING && agroed && !devil.right) 
				return new Vector2(-moveSpeed * deltaTime, 0);
		}
		return new Vector2(0, 0);
	}

	@Override
	public void update(Vector2 nTarget, Vector2 nPosition) {
		if(!agroed && player.getPosition().dst(devil.getPosition()) < 40) {
			agroed = true;
			devil.setState(GameSprite.RUNNING);
		}
		
		if(attacking && System.currentTimeMillis() - soundTimer > 500) {
			soundTimer = Long.MAX_VALUE;
			Assets.instance.songs.swoosh.play();
		}
		
		if(attacking && System.currentTimeMillis() - lastAttack > aManager.getLength("attack") * 1000 + 200) {
			devil.setState(GameSprite.RUNNING);
			attacking = false;
			lastAttack = System.currentTimeMillis();
		} else {
			if(Math.abs(player.getX() + player.getWidth() /2 - devil.getX() - devil.getPlayerCollisionRectangle().width / 2) > 1.5) {
				if(player.getOriginX() + player.getX() < devil.getOriginX() + devil.getX() && devil.getState() == GameSprite.RUNNING)
					devil.right = false;
				if(player.getOriginX() + player.getX() > devil.getOriginX() + devil.getX() && devil.getState() == GameSprite.RUNNING)
					devil.right = true;
			}
			
			float distance = player.getPosition().add(new Vector2(player.getOriginX(), player.getOriginY())).dst(devil.getPosition().add(new Vector2(devil.getOriginX(), devil.getOriginY())));
			if(distance < 17 && System.currentTimeMillis() - 2000 - lastAttack > 0) {
				lastAttack = System.currentTimeMillis();
				devil.setState(GameSprite.ATTACKING);
				attacking = true;
				soundTimer = System.currentTimeMillis();
			}
		}
	}

	@Override
	public boolean right() {
		return devil.right;
	}

	@Override
	public boolean agroed() {
		return true;
	}

	@Override
	public void addAgro(int n) {}

}
