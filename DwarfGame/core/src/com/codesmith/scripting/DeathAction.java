package com.codesmith.scripting;

import com.codesmith.world.GameSprite;
import com.codesmith.world.MovableMapObject;

public class DeathAction extends ScriptAction implements Updatable {
	
	private GameSprite s;
	
	public DeathAction(GameSprite target) {
		s = target;
	}

	@Override
	public ScriptAction execute(GameSprite target, float deltaTime) {
		if(s.getHealth() <= 0)
			return next;
		return this;
	}

	@Override
	public ScriptAction execute(MovableMapObject obj, float deltaTime) {
		if(s.getHealth() <= 0)
			return next;
		return this;
	}

	@Override
	public ScriptAction update() {
		if(s.getHealth() <= 0)
			return next;
		return this;
	}
	
	

}
