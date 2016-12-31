package com.codesmith.world;

public class Inventory {
	
	/**
	 * @uml.property  name="weapon"
	 * @uml.associationEnd  
	 */
	private Weapon weapon;
	
	/**
	 * @return
	 * @uml.property  name="weapon"
	 */
	public Weapon getWeapon() {
		return weapon;
	}
	
	/**
	 * @param nWeapon
	 * @uml.property  name="weapon"
	 */
	public void setWeapon(Weapon nWeapon) {
		weapon = nWeapon;
	}
	
	public int attack() {
		return weapon.attack();
	}

}
