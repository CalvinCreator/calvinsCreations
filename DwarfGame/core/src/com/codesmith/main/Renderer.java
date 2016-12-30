package com.codesmith.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.codesmith.graphics.Assets;
import com.codesmith.graphics.ParticleAnimation;
import com.codesmith.utils.CameraHelper;
import com.codesmith.utils.Constants;
import com.codesmith.world.Enemy;
import com.codesmith.world.Gate;
import com.codesmith.world.ItemSprite;
import com.codesmith.world.MovableMapObject;
import com.codesmith.world.MovingPlatform;
import com.codesmith.world.World;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public class Renderer {
	public static final String TAG = Renderer.class.getName();
	
	private World world;
	
	private OrthogonalTiledMapRenderer mapRenderer;
	private OrthographicCamera camera, guiCamera;
	private SpriteBatch batch;
	private CameraHelper camHelper;
	
	public boolean debug = false;
	
	private float alpha = 1;
	
	ShapeRenderer r = new ShapeRenderer();
	
	private int[] layersBehindMovableWalls, otherBackgroundLayers, foregroundLayers;
	
	public Renderer(World world, CameraHelper camHelper) {
		this.world = world;
		mapRenderer = new OrthogonalTiledMapRenderer(world.getMap(), Constants.TILE_SIZE);
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		this.camHelper = camHelper;
		camHelper.setCamera(camera, world);
		camHelper.apply();
		guiCamera = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		guiCamera.position.set(0, 0, 0);
		guiCamera.update();
	}
	
	public void render() {
		camHelper.apply();
		mapRenderer.setView(camera);
		mapRenderer.getBatch().setColor(1, 1, 1, alpha);
		batch.setProjectionMatrix(camera.combined);
		batch.setColor(1, 1, 1, alpha);
		r.setProjectionMatrix(camera.combined);
		
		//Draw every layer behind sprites
		mapRenderer.render(layersBehindMovableWalls);
		batch.setColor(1, 1, 1, alpha);
		batch.begin();
		for(MovableMapObject m : world.getMovableMapObjects())
			m.draw(batch);
		batch.end();
		mapRenderer.render(otherBackgroundLayers);
		
		batch.setColor(1, 1, 1, alpha);
		batch.begin();
		//Draw moving platforms
		for(MovingPlatform p : world.getMovingPlatforms()) 
			p.draw(batch);
		
		for(ItemSprite s: world.getItems())
			s.draw(batch);
		
		for(Gate g : world.getGates())
			g.draw(batch);
		
		for(ParticleAnimation m: world.getParticleAnimations())
			m.render(batch);
		for(Enemy e : world.getEnemies())
			e.draw(batch);
		world.getPlayer().draw(batch);
		
		batch.end();
		
		//Draw ever layer in front of player
		mapRenderer.render(foregroundLayers);
		batch.setProjectionMatrix(guiCamera.combined);
		batch.begin();
		drawGui(batch);
		batch.end(); 
		
		if(debug) {
			r.setColor(0, 1, 0, alpha);
			r.begin(ShapeType.Line);
				for(Rectangle re: world.getRects())
					r.rect(re.x, re.y, re.width, re.height);
			r.end();
		}
	}
	
	public void draw(Texture t, float alpha) {
		batch.setProjectionMatrix(camera.combined);
		batch.setColor(1, 1, 1, alpha);
		batch.begin();
		batch.draw(t, 0, 0);
		batch.end();
	}
	
	private void drawGui(SpriteBatch batch) {
		for(int i = 0; i < world.getPlayer().getMaxHealth(); i++) 
			if(i < world.getPlayer().getHealth())
				batch.draw(Assets.instance.icons.fullHeart, 3 + 22 * i, Constants.VIEWPORT_GUI_HEIGHT - 20);
			else
				batch.draw(Assets.instance.icons.emptyHeart, 3 + 22 * i, Constants.VIEWPORT_GUI_HEIGHT - 20);
	}
	
	public void setAlpha(float a) {
		alpha = a;
	}
	
	public void updateMapData() throws Exception {
		camHelper.setTarget(world.getPlayer());
		camHelper.setPosition(world.getPlayer().getPosition());
		camHelper.apply();
	
		mapRenderer = new OrthogonalTiledMapRenderer(world.getMap(), Constants.TILE_SIZE);
		
		int pLayer = world.getMap().getLayers().getIndex("Grass");
		if(pLayer == -1)
			throw new Exception("Grass Layer not found in map.");
		layersBehindMovableWalls = new int[pLayer];
		for(int x = 1; x <= pLayer; x++)
			layersBehindMovableWalls[x-1] = x;
		
		int ladderLayer = world.getMap().getLayers().getIndex("Ladders");
		if(ladderLayer == -1)
			throw new Exception("Ladders Layer not found in map.");
		int foregroundLayer = world.getMap().getLayers().getIndex("Foreground");
		if(foregroundLayer == -1)
			throw new Exception("Foregound layer not found in map.");
		
		otherBackgroundLayers = new int[ladderLayer - pLayer];
		for(int i = pLayer + 1; i <= ladderLayer; i++)
			otherBackgroundLayers[i - 1 - pLayer] = i;
		
		foregroundLayers = new int[foregroundLayer - ladderLayer];
		for(int x = ladderLayer + 1; x <= foregroundLayer; x++)
			foregroundLayers[x - 1 - ladderLayer] = x;
	}
	
	public void resize(int width, int height) {
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
		camera.update();
		
		guiCamera.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
		guiCamera.viewportWidth = Constants.VIEWPORT_GUI_HEIGHT / height * width;
		guiCamera.position.set(guiCamera.viewportWidth / 2, guiCamera.viewportHeight / 2, 0);
		guiCamera.update();
	}
	
	public Batch getBatch() {
		return batch;
	}
	
	public void dispose() {
		batch.dispose();
		r.dispose();
	}
}
