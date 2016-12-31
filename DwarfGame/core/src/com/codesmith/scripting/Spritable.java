package com.codesmith.scripting;

import com.badlogic.gdx.graphics.g2d.Sprite;

public interface Spritable {
	
	public ScriptAction execute(Sprite s, float deltaTime);

}
