package com.codesmith.world;

import com.badlogic.gdx.Gdx;
import com.codesmith.graphics.Assets;

public class Inventory {
	
	private Weapon weapon;
	
	public Weapon getWeapon() {
		return weapon;
	}
	
	public void setWeapon(Weapon nWeapon) {
		weapon = nWeapon;
	}
	
	public int attack() {
		return weapon.attack();
	}

}
