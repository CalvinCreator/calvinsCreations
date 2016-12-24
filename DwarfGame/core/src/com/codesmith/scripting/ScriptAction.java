package com.codesmith.scripting;

import java.util.LinkedList;

import com.badlogic.gdx.math.Vector2;
import com.codesmith.utils.Constants;
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
		LinkedList<String> q = new LinkedList<String>();
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
				if(args[0].equals("P"))
					action.add(new ProximityAction(world.getPlayer(), Float.valueOf(args[1])));
				else
					action.add(new ProximityAction(new Vector2(world.getMovableMapObjects().get(0).getX() * Constants.TILE_SIZE, world.getMovableMapObjects().get(0).getY() * Constants.TILE_SIZE), Float.valueOf(args[1])));
				break;
			case "jumpAction":
				throw new Exception("NOT IMPLEMENTED");
				//break;
			}
		}
		return action;
	}

}
