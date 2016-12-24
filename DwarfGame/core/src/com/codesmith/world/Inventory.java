package com.codesmith.world;

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
