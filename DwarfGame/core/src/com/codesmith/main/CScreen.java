package com.codesmith.main;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;

public abstract class CScreen implements Screen {
	
	protected float alpha = 1;
	
	public boolean keyDown(int keycode) {return false;}
	public boolean keyUp(int keycode){return false;}
	public boolean keyTyped(char character){return false;}
	public boolean touchDown(int screenX, int screenY, int pointer, int button){return false;}
	public boolean touchUp(int screenX, int screenY, int pointer, int button){return false;}
	public boolean touchDragged(int screenX, int screenY, int pointer){return false;}
	public boolean mouseMoved(int screenX, int screenY){return false;}
	public boolean scrolled(int amount){return false;}
	
	public abstract Batch getBatch();

	@Override
	public void pause() {}
	@Override
	public void resume() {}
	
	public void setAlpha(float a) {
		alpha = a;
	}
	
	public float getAlpha() {
		return alpha;
	}
}
