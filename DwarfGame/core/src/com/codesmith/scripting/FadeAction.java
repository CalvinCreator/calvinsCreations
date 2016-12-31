package com.codesmith.scripting;

import com.badlogic.gdx.Gdx;
import com.codesmith.main.CreditScreen;
import com.codesmith.main.MainLoop;
import com.codesmith.main.Renderer;
import com.codesmith.world.GameSprite;
import com.codesmith.world.MovableMapObject;

public class FadeAction extends ScriptAction {
	
	/**
	 * @uml.property  name="r"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	
	private float d1, d2;
	
	public FadeAction(float d1, float d2) {
		this.d1 = d1;
		this.d2 = d2;
	}

	@Override
	public ScriptAction execute(GameSprite target, float deltaTime) {
		((MainLoop)(Gdx.app.getApplicationListener())).fadeTransition(new CreditScreen(), d1, d2);
		return next;
	}

	@Override
	public ScriptAction execute(MovableMapObject obj, float deltaTime) {
		((MainLoop)(Gdx.app.getApplicationListener())).fadeTransition(new CreditScreen(), d1, d2);
		return next;
	}

}
