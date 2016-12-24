package com.codesmith.scripting;

import com.badlogic.gdx.Gdx;
import com.codesmith.world.GameSprite;
import com.codesmith.world.MovableMapObject;
import com.codesmith.world.Player;

public class JumpAction extends ScriptAction {
	
	private float vel;
	
	public JumpAction(float vel) {
		this.vel = vel;
	}

	@Override
	public ScriptAction execute(GameSprite target, float deltaTime) {
		target.getVelocity().y = vel;
		return next;
	}

	@Override
	public ScriptAction execute(MovableMapObject obj, float deltaTime) {
		Gdx.app.error("JumpAction", "JumpAction added to MoveableMapObject " + obj.toString());
		return next;
	}
	
	

}
