package com.codesmith.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.codesmith.graphics.Assets;
import com.codesmith.graphics.ParticleAnimation;
import com.codesmith.utils.CameraHelper;
import com.codesmith.utils.Constants;
import com.codesmith.world.Enemy;
import com.codesmith.world.Gate;
import com.codesmith.world.MovingPlatform;
import com.codesmith.world.World;

public class Renderer {
	public static final String TAG = Renderer.class.getName();
	
	private World world;
	
	private TiledMapRenderer mapRenderer;
	private OrthographicCamera camera, guiCamera;
	private SpriteBatch batch;
	private CameraHelper camHelper;
	
	ShapeRenderer r = new ShapeRenderer();
	
	private int[] backgroundLayers, foregroundLayers;
	
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
		batch.setProjectionMatrix(camera.combined);
		r.setProjectionMatrix(camera.combined);
		
		//Draw every layer behind sprites
		mapRenderer.render(backgroundLayers);
		
		batch.begin();
		
		//Draw moving platforms
		for(MovingPlatform p : world.getMovingPlatforms()) 
			p.draw(batch);
		
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
		
		r.begin(ShapeType.Line);
		
		r.end();

	}
	
	private void drawGui(SpriteBatch batch) {
		for(int i = 0; i < world.getPlayer().getMaxHealth(); i++) 
			if(i < world.getPlayer().getHealth())
				batch.draw(Assets.instance.icons.fullHeart, 3 + 22 * i, Constants.VIEWPORT_GUI_HEIGHT - 20);
			else
				batch.draw(Assets.instance.icons.emptyHeart, 3 + 22 * i, Constants.VIEWPORT_GUI_HEIGHT - 20);
	}
	
	public void updateMapData() throws Exception {
		camHelper.setTarget(world.getPlayer());
		camHelper.setPosition(world.getPlayer().getPosition());
		camHelper.apply();

		mapRenderer = new OrthogonalTiledMapRenderer(world.getMap(), Constants.TILE_SIZE);
		int pLayer = world.getMap().getLayers().getIndex("Ladders");
		if(pLayer == -1)
			throw new Exception("Ladder Layer not found in map.");
		
		backgroundLayers = new int[pLayer];
		for(int x = 1; x <= pLayer; x++)
			backgroundLayers[x-1] = x;
		
		int foregroundLayer = world.getMap().getLayers().getIndex("Foreground");
		if(foregroundLayer == -1)
			throw new Exception("Foregound layer not found in map.");

		foregroundLayers = new int[foregroundLayer - pLayer];
		for(int x = pLayer + 1; x <= foregroundLayer; x++)
			foregroundLayers[x - 1 - pLayer] = x;
	}
	
	public void resize(int width, int height) {
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
		camera.update();
		
		guiCamera.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
		guiCamera.viewportWidth = Constants.VIEWPORT_GUI_HEIGHT / height * width;
		guiCamera.position.set(guiCamera.viewportWidth / 2, guiCamera.viewportHeight / 2, 0);
		guiCamera.update();
	}
	
	public void dispose() {
		batch.dispose();
		r.dispose();
	}
}
