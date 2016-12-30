package com.codesmith.scripting;

import com.badlogic.gdx.Gdx;
import com.codesmith.main.CreditScreen;
import com.codesmith.main.MainLoop;
import com.codesmith.main.Renderer;
import com.codesmith.world.GameSprite;
import com.codesmith.world.MovableMapObject;

public class FadeAction extends ScriptAction {
	
	private Renderer r;
	
	public FadeAction(Renderer r, float d1, float d2) {
		this.r = r;
	}

	@Override
	public ScriptAction execute(GameSprite target, float deltaTime) {
		((MainLoop)(Gdx.app.getApplicationListener())).fadeTransition(new CreditScreen(), 4, 1);
		return next;
	}

	@Override
	public ScriptAction execute(MovableMapObject obj, float deltaTime) {
		((MainLoop)(Gdx.app.getApplicationListener())).fadeTransition(new CreditScreen(), 4, 1);
		return next;
	}

}
