package com.codesmith.world;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.codesmith.graphics.ParticleAnimation;
import com.codesmith.main.Renderer;
import com.codesmith.utils.Constants;

public class World {
	public static final String TAG = World.class.getName();

	private TiledMap map;
	private Player player;
	private ArrayList<Enemy> enemies;
	private ArrayList<MovingPlatform> platforms;
	private ArrayList<ParticleAnimation> pAnimations;
	private ArrayList<Gate> gates;
	private Renderer renderer;
	private String currentMap = "";

	public World() {
		player = new Player(this);
		enemies = new ArrayList<Enemy>();
		platforms = new ArrayList<MovingPlatform>();
		pAnimations = new ArrayList<ParticleAnimation>();
		gates = new ArrayList<Gate>();
	}

	public void update(float deltaTime) {
		
		if(player.getHealth() <= 0) 
			setMap(currentMap);
		// Update Particle Animations
		for (int i = 0; i < pAnimations.size(); i++)
			if (!pAnimations.get(i).update(deltaTime))
				pAnimations.remove(i--);

		for (Gate g : gates)
			g.update(deltaTime);

		// Update all sprites
		player.update(deltaTime);
		for (int i = 0; i < enemies.size(); i++) {
			if (enemies.get(i).getHealth() <= 0) {
				Enemy e = enemies.remove(i--);
				pAnimations
						.add(new ParticleAnimation("death", e.getPosition(), new Vector2(e.getWidth(), e.getHeight())));

			} else
				enemies.get(i).update(deltaTime);
		}

		for (MovingPlatform p : platforms) {
			p.update(deltaTime);
		}

		// Resolve Map Collisions
		HashSet<WorldRectangle> rects = new HashSet<WorldRectangle>();
		for (MapObject obj : map.getLayers().get(0).getObjects())
			rects.add(new WorldRectangle((((RectangleMapObject) obj).getRectangle()), 0));
		for (MovingPlatform p : platforms)
			rects.add(new WorldRectangle(((p).getBoundingRectangle()), 3, p));
		for (Gate g : gates)
			rects.add(new WorldRectangle(g.getCollisionBox(), 4, g));
		for (Enemy e : enemies) {
			e.setPosition(resolveMapCollisions(rects, e));
		}
		for (Enemy e : enemies) {
			Rectangle r = e.getBoundingRectangle();
			r.x /= Constants.TILE_SIZE;
			r.y /= Constants.TILE_SIZE;
			r.width /= Constants.TILE_SIZE;
			r.height /= Constants.TILE_SIZE;
			rects.add(new WorldRectangle(r, 1, e));
		}
		player.setPosition(resolveMapCollisions(rects, player));

	}

	public Player getPlayer() {
		return player;
	}

	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}

	public void setMap(String mapPath) {
		currentMap = mapPath;
		if (map != null)
			map.dispose();
		map = new TmxMapLoader().load(mapPath);
		gates = new ArrayList<Gate>();
		platforms = new ArrayList<MovingPlatform>();
		enemies = new ArrayList<Enemy>();
		
		player = new Player(this);
		player.setPosition(Float.valueOf((String)map.getProperties().get("spawnX")), Float.valueOf((String)map.getProperties().get("spawnY")));
		
		if (map.getLayers().get("Mobs") != null) {
			TiledMapTileLayer tileLayer = (TiledMapTileLayer) map.getLayers().get("Mobs");
			for (int x = 0; x < tileLayer.getWidth(); x++)
				for (int y = 0; y < tileLayer.getHeight(); y++)
					if (tileLayer.getCell(x, y) != null)
						switch (tileLayer.getCell(x, y).getTile().getId()) {
						case 178:
							enemies.add(new Skeleton(x, y, player));
							break;
						default:
							enemies.add(new Skeleton(x, y, player));
						}

			map.getLayers().remove(map.getLayers().get("Mobs"));
		}
		if (map.getLayers().get("Moving Platforms") != null) {
			for (MapObject obj : map.getLayers().get("Moving Platforms").getObjects()) {
				try {
					platforms.add(new MovingPlatform((RectangleMapObject) obj));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (map.getLayers().get("Gate") != null) {
			for (MapObject obj : map.getLayers().get("Gate").getObjects()) {
				try {
					RectangleMapObject rect = (RectangleMapObject) obj;
					float x = obj.getProperties().get("x", Float.class);
					float y = obj.getProperties().get("y", Float.class);
					String destination = obj.getProperties().get("xTile", String.class) + "," + obj.getProperties().get("yTile", String.class);
					gates.add(new Gate(Integer.valueOf((String) rect.getProperties().get("gateId")),
							rect.getProperties().get("map", String.class), new Vector2(x, y), destination));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		try {
			renderer.updateMapData();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public ArrayList<ParticleAnimation> getParticleAnimations() {
		return pAnimations;
	}

	public void dispose() {
		map.dispose();
	}

	public TiledMap getMap() {
		return map;
	}

	public Vector2 resolveMapCollisions(HashSet<WorldRectangle> rects, GameSprite s) {
		Rectangle p = s.getBoundingRectangle();
		boolean falling = true;
		int prevState = s.getState();
		boolean done = false;
		for (WorldRectangle r : rects) {
			r = new WorldRectangle(new Rectangle(r.x * Constants.TILE_SIZE, r.y * Constants.TILE_SIZE,
					r.width * Constants.TILE_SIZE, r.height * Constants.TILE_SIZE), r.id, r.getParent());

			if(r.id == WorldRectangle.GATE)
				((Gate)r.getParent()).collide = false;
			if (r.x < p.x + p.width + s.velocity.x && r.x + r.width > p.x + s.velocity.x
					&& r.y < p.y + p.height + s.velocity.y && r.height + r.y > p.y + s.velocity.y) {
				
				//Collide with anything except Gate objects
				if (r.id == WorldRectangle.GATE) {
					((Gate)r.getParent()).collide = true;
					if(((Gate)r.getParent()).open() && Gdx.input.isKeyPressed(Keys.S)) {
						setMap("maps/" + ((Gate)r.getParent()).getMap() + ".tmx");
						String destination = ((Gate)r.getParent()).getDestination();
						if(!destination.contains("spawn")) {
							String[] locs = destination.split(",");
							player.setPosition(Float.valueOf(locs[0]), Float.valueOf(locs[1]));
						}
					}
				} else {
					if (r.id == WorldRectangle.ENEMY)
						player.hit(r, ((Enemy) r.getParent()).getDamage());

					// R is bellow player
					if (p.contains(r.x - s.velocity.x, r.y + r.height - s.velocity.y)
							&& p.contains(r.x + r.width - s.velocity.x, r.y + r.height - s.velocity.y)) {
						falling = false;
						
						s.velocity.y = 0;
						p.y = r.y + r.height;
						if (r.id == 3) {
							MovingPlatform plat = (MovingPlatform) (r.getParent());
							if (plat.path == 0) {
								s.velocity.x += plat.delta;
								done = true;
							} else if(plat.path == 1) {
								r.y -= 10;
								s.velocity.y = 0;
								done = true;
							}
						}
					}
					// R is left of player
					if (p.contains(r.x + r.width - s.velocity.x, r.y - s.velocity.y)
							&& p.contains(r.x + r.width - s.velocity.x, r.y + r.height - s.velocity.y)) {
						s.velocity.x = 0;
						p.x = r.x + r.width;
					}
					// R is above player
					if (p.contains(r.x - s.velocity.x, r.y - s.velocity.y)
							&& p.contains(r.x + r.width - s.velocity.x, r.y - s.velocity.y)) {
						s.velocity.y = 0;
						p.y = r.y - p.height;
					}
					// R is right of player
					if (p.contains(r.x - s.velocity.x, r.y - s.velocity.y)
							&& p.contains(r.x - s.velocity.x, r.y + r.height - s.velocity.y)) {
						s.velocity.x = 0;
						p.x = r.x - p.width;
					}

					// Bottom left
					if (r.contains(p.x + s.velocity.x, p.y + s.velocity.y)) {
						float x = r.x + r.width - p.x;
						float y = r.y + r.height - p.y;
						if (Math.abs(x) > Math.abs(y)) {
							falling = false;
							s.velocity.y = 0;
							p.setY(r.y + r.height);
							if (r.id == 3) {
								MovingPlatform plat = (MovingPlatform) (r.getParent());
								if (plat.path == 0 && !done) {
									s.velocity.x += plat.delta;
									done = true;
								} else if(plat.path == 1 && !done) {
									r.y -= 10;
									s.velocity.y = 0;
									done = true;
								}
							}
						} else {
							s.velocity.x = 0;
							p.setX(r.x + r.width);
						}
					} // bottom left

					// Top left
					if (r.contains(p.x + s.velocity.x, p.y + p.height + s.velocity.y)) {
						float x = r.x + r.width - p.x;
						float y = p.y + p.height - r.y;
						if (Math.abs(x) > Math.abs(y)) {
							s.velocity.y = 0;
							p.setY(r.y - p.height);
						} else {
							s.velocity.x = 0;
							p.setX(r.x + r.width);
						}
					} // top left

					// Top Right
					if (r.contains(p.x + p.width + s.velocity.x, p.y + p.height + s.velocity.y)) {
						float x = p.x + p.width - r.x;
						float y = p.y + p.height - r.y;
						if (Math.abs(x) > Math.abs(y)) {
							s.velocity.y = 0;
							p.setY(r.y - p.height);
						} else {
							s.velocity.x = 0;
							p.setX(r.x - p.width);
						}
					} // top right

					// Bottom right
					if (r.contains(p.x + p.width + s.velocity.x, p.y + s.velocity.y)) {
						float x = p.x + p.width - r.x;
						float y = r.y + r.height - p.y;
						if (Math.abs(x) > Math.abs(y)) {
							falling = false;
							s.velocity.y = 0;
							p.setY(r.y + r.height);
							if (r.id == 3) {
								MovingPlatform plat = (MovingPlatform) (r.getParent());
								if (plat.path == 0 && !done) {
									s.velocity.x += plat.delta;
									done = true;
								} else if(plat.path == 1 && !done) {
									r.y -= 10;
									s.velocity.y = 0;
									done = true;
								}
							}
						} else {
							s.velocity.x = 0;
							p.setX(r.x - p.width);
						}
					} // bottom right
				}
			}
		}

		if (falling)
			s.setState(GameSprite.FALLING);
		else if (prevState != GameSprite.FALLING)
			s.setState(prevState);
		else
			s.setState(GameSprite.IDLE);

		return new Vector2(p.x + s.velocity.x, p.y + s.velocity.y);

	}// resolveCollisions

	// takes in an int to describe the attack type, sees if the attack hit any
	// enemies, and if so calls the enemy's hit method
	// and returns true. else returns false.
	public boolean attack(int type, float range, int damage) {
		for (Enemy e : enemies) {
			if (player.getX() + player.getWidth() <= e.getX() && player.dist(e) <= range && player.right)
				return e.hit(player.getBoundingRectangle(), damage);
			else if (e.getX() + e.getWidth() <= player.getX() && player.dist(e) <= range && !player.right)
				return e.hit(player.getBoundingRectangle(), damage);
		}
		return false;
	}

	public HashSet<Rectangle> getRects() {
		HashSet<Rectangle> rects = new HashSet<Rectangle>();
		rects.add(player.getBoundingRectangle());
		for (MapObject obj : map.getLayers().get(0).getObjects()) {
			Rectangle r = new Rectangle(((RectangleMapObject) obj).getRectangle());
			Rectangle re = new Rectangle(r.x * Constants.TILE_SIZE, r.y * Constants.TILE_SIZE,
					r.width * Constants.TILE_SIZE, r.height * Constants.TILE_SIZE);
			rects.add(new Rectangle(re));
		}
		for (Enemy e : enemies) {
			Rectangle r = e.getBoundingRectangle();
			rects.add(r);
		}
		return rects;
	}

	public ArrayList<MovingPlatform> getMovingPlatforms() {
		return platforms;
	}

	public ArrayList<Gate> getGates() {
		return gates;
	}
	
	public void setRenderer(Renderer r) {
		renderer = r;
	}

}