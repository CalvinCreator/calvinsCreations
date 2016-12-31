package com.codesmith.graphics;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class VectorAnimation extends Animation {
	
	/**
	 * @uml.property  name="frames"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	private TextureRegion[] frames;
	/**
	 * @uml.property  name="v"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
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
