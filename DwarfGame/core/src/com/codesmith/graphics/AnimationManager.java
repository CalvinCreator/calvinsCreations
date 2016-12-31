package com.codesmith.graphics;

import java.util.HashMap;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class AnimationManager {
	
	private HashMap<String, Animation> animations;
	private Animation currentAnimation;
	private Animation previousAnimation;
	private float stateTime;
	private boolean looping;
	
	public AnimationManager() {
		animations = new HashMap<String, Animation>();
		stateTime = 0;
	}
	
	public void update(float s) {
		stateTime += s;
	}
	
	public void updateWithClimbing(float s, Vector2 velocity) {
		if(currentAnimation == animations.get("climbing") && velocity.y != 0)
			stateTime += s;
		else if(currentAnimation != animations.get("climbing"))
			stateTime += s;
	}
	
	public TextureRegion getKeyFrame() {
		if(currentAnimation instanceof VectorAnimation)
			return ((VectorAnimation)currentAnimation).getKeyFrame(stateTime, false);
		if(stateTime > currentAnimation.getAnimationDuration() && !looping)
			currentAnimation = previousAnimation;
		return currentAnimation.getKeyFrame(stateTime, looping);
	}
	
	public float getLength(String key) {
		return animations.get(key).getAnimationDuration();
	}
	
	public void addAnimation(String name, Animation a) {
		animations.put(name, a);
	}
	
	public void setAnimationFrame(int frame) {
		stateTime = frame * currentAnimation.getFrameDuration();
	}
	
	public int getFrameIndex() {
		if(currentAnimation instanceof VectorAnimation) {
			return ((VectorAnimation)currentAnimation).getFrameIndex();
		}
		return currentAnimation.getKeyFrameIndex(stateTime % (currentAnimation.getAnimationDuration()));
	}
	
	public void setAnimation(String name, int frame, boolean loop) {
		if(!loop)
			previousAnimation = currentAnimation;
		currentAnimation = animations.get(name);
		stateTime = frame * currentAnimation.getFrameDuration();
		looping = loop;
	}

}
