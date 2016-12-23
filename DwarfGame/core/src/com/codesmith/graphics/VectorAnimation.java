package com.codesmith.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class VectorAnimation extends Animation {
	
	private TextureRegion[] frames;
	private Vector2 v;

	public VectorAnimation(float frameDuration, TextureRegion[] keyFrames, Vector2 v) {
		super(frameDuration, keyFrames);
		frames = keyFrames;
		this.v = v;
	}

	public TextureRegion getKeyFrame() {
		if(v.y >= 0)
			return frames[0];
		return frames[1];
	}
	
	public int getFrameIndex() {
		if(v.y >= 0)
			return 0;
		return 1;
	}

}
