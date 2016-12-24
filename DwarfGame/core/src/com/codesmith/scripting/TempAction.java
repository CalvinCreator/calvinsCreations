package com.codesmith.scripting;

import com.codesmith.world.GameSprite;
import com.codesmith.world.MovableMapObject;

public class TempAction extends ScriptAction {

	@Override
	public ScriptAction execute(GameSprite target, float deltaTime) {
		return next;
	}

	@Override
	public ScriptAction execute(MovableMapObject obj, float deltaTime) {
		return next;
	}
	
	public String toString() {
		return "TempAction " + next.toString();
	}

}
