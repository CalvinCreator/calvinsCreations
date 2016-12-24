package com.codesmith.scripting;

import com.codesmith.world.GameSprite;
import com.codesmith.world.MovableMapObject;
import com.codesmith.world.Player;

public class MoveAction extends ScriptAction {
	
	float velX, velY;
	float duration;
	
	public MoveAction(float x, float y, float time) {
		velX = x / time;
		velY = y / time;
		duration = time;
	}
	
	public ScriptAction execute(GameSprite target, float deltaTime) {
		duration -= deltaTime;
		target.translate(velX * deltaTime, velY * deltaTime);
		target.getVelocity().x = 0;
		if(duration <= 0) {
			if(next == null)
				target.setState(GameSprite.IDLE);
			return next;
		}

		if(!(target instanceof Player) && target.getState() != GameSprite.FALLING) 
			if(velX != 0) {
				target.setState(GameSprite.RUNNING);
				target.right = velX > 0 ? false : true;
			}
			else 
				target.setState(GameSprite.IDLE);
		else if(target instanceof Player) {
			target.right = velX >= 0 ? true : false;
			if(target.getState() != GameSprite.FALLING) {
				int state = velX != 0 ? GameSprite.RUNNING : GameSprite.IDLE;
				target.setState(state);
			}
		}
		
		return this;
	}
	
	public String toString() {
		if(next != null)
			return "Move Action: " + velX + "," + velY + "," + duration + " " + next.toString();
		return "Move Action: " + velX + "," + velY + "," + duration;
	}

	@Override
	public ScriptAction execute(MovableMapObject obj, float deltaTime) {
		duration -= deltaTime;
		obj.translateSpecial(velX * deltaTime, velY * deltaTime);
		if(duration <= 0)
			return next;
		return this;
	}

}
