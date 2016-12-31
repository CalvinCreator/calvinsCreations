package com.codesmith.scripting;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.codesmith.graphics.Assets;
import com.codesmith.utils.Constants;
import com.codesmith.world.Devil;
import com.codesmith.world.Enemy;
import com.codesmith.world.GameSprite;
import com.codesmith.world.MovableMapObject;
import com.codesmith.world.World;

public abstract class ScriptAction {
	
	protected ScriptAction next = null;
	
	public abstract ScriptAction execute(GameSprite target, float deltaTime);
	
	public abstract ScriptAction execute(MovableMapObject obj, float deltaTime);
	
	public ScriptAction add(ScriptAction next) {
		if(this.next == null)
			this.next = next;
		else
			this.next.add(next);
		return next;
	}
	
	public static ScriptAction loadScript(String file, World world) throws Exception {
		Queue<String> q = new LinkedList<String>();
		ScriptAction action = new TempAction();
		
		while(file != "") {
			try {
				String s = file.substring(file.indexOf("<") + 1, file.indexOf(">"));
				String t = "<" + s + ">";
				file = file.substring(file.indexOf(t) + t.length());
				q.add(s);
			} catch(Exception e) { file = "";}
		}
		
		while(!q.isEmpty()) {
			String s = q.remove();
			String[] args = q.remove().split(",");
			switch(s) {
			case "moveAction": 
				action.add(new MoveAction(Float.valueOf(args[0]), Float.valueOf(args[1]), Float.valueOf(args[2])));
				break;
			case "proximityAction":
				if(args.length == 3) 
					action.add(new ProximityAction(new Vector2(Integer.valueOf(args[0]) * Constants.TILE_SIZE, Integer.valueOf(args[1]) * Constants.TILE_SIZE), Float.valueOf(args[2])));
				else if(args[0].equals("P"))
					action.add(new ProximityAction(world.getPlayer(), Float.valueOf(args[1])));
				else
					action.add(new ProximityAction(new Vector2(getMovableMapObject(Integer.valueOf(args[0]), world).getX() * Constants.TILE_SIZE, getMovableMapObject(Integer.valueOf(args[0]), world).getY() * Constants.TILE_SIZE), Float.valueOf(args[1])));
				break;
			case "jumpAction":
				throw new Exception("NOT IMPLEMENTED");
				//break;
			case "deathAction":
				GameSprite d = null;
				for(Enemy e: world.getEnemies())
					if(e instanceof Devil)
						d = e;
				action.add(new DeathAction(d));
				break;
			case "fadeAction": 
				action.add(new FadeAction(Float.valueOf(args[0]), Float.valueOf(args[1])));
				break;
			case "musicChangeAction":
				Music m = null;
				switch(args[0]) {
				case "1": m = Assets.instance.songs.trackOne;
				break;
				case "2":m = Assets.instance.songs.trackTwo;
				break; 
				case "3": m = Assets.instance.songs.trackThree;
				break;
				case "4": m = Assets.instance.songs.trackFour;
				break;
				}
				action.add(new MusicChangeAction(m));
				break;
			}
		}
		return action;
	}
	
	//returns the object with the scriptId of 'target'
	private static MovableMapObject getMovableMapObject(int target, World w) {
		for(MovableMapObject o: w.getMovableMapObjects())
			if(o.scriptId == target)
				return o;
		return null;
	}

}
