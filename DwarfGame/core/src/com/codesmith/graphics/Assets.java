package com.codesmith.graphics;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable, AssetErrorListener {
	public static final String TAG = Assets.class.getName();

	public static final Assets instance = new Assets();
	private AssetManager assetManager;

	public CharacterAssets gameAssets;
	public MapAssets mapAssets;
	public ParticleAnimations pAnimations;
	public Icons icons;
	public Songs songs;
	public TextureAtlas menuAssets, backgrounds;
	
	public TextureRegion credits;

	private Assets() {
	}

	public void init(AssetManager assetManager) {
		this.assetManager = assetManager;

		assetManager.setErrorListener(this);

		assetManager.load("images/sprites.pack", TextureAtlas.class);
		assetManager.load("maps/tilesets/map.pack", TextureAtlas.class);
		assetManager.load("images/icons.pack", TextureAtlas.class);
		assetManager.load("images/pAnimations.pack", TextureAtlas.class);
		assetManager.load("images/backgrounds.pack", TextureAtlas.class);
		assetManager.load("menuImages/menuAssets.pack", TextureAtlas.class);
		assetManager.finishLoading();
		TextureAtlas atlas = assetManager.get("images/sprites.pack");
		for (Texture t : atlas.getTextures())
			t.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		gameAssets = new CharacterAssets(atlas);
		Gdx.app.log(TAG, "sprites.pack loaded.");
		
		TextureAtlas bAtlas = assetManager.get("images/backgrounds.pack");
		for (Texture t : atlas.getTextures())
			t.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		backgrounds = bAtlas;
		Gdx.app.log(TAG, "backgrounds.pack loaded.");
		

		atlas = assetManager.get("images/pAnimations.pack");
		for (Texture t : atlas.getTextures())
			t.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		pAnimations = new ParticleAnimations(atlas);
		Gdx.app.log(TAG, "pAnimations.pack loaded.");
		
		atlas = assetManager.get("images/icons.pack");
		for (Texture t : atlas.getTextures())
			t.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		icons = new Icons(atlas);
		Gdx.app.log(TAG, "images.pack loaded.");
		
		atlas = assetManager.get("maps/tilesets/map.pack");
		for (Texture t : atlas.getTextures())
			t.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		mapAssets = new MapAssets(atlas);
		Gdx.app.log(TAG, "map.pack loaded.");
		
		atlas = assetManager.get("menuImages/menuAssets.pack");
		for (Texture t : atlas.getTextures())
			t.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		menuAssets = atlas;
		Gdx.app.log(TAG, "menuAssets.pack loaded.");
		
		assetManager.load("music/trackOne.mp3", Music.class);
		assetManager.load("music/trackTwo.mp3", Music.class);
		assetManager.load("music/trackThree.mp3", Music.class);
		assetManager.load("music/trackFour.mp3", Music.class);
		assetManager.finishLoading();
		Music[] songs = {assetManager.get("music/trackOne.mp3"), assetManager.get("music/trackTwo.mp3"), assetManager.get("music/trackThree.mp3"), assetManager.get("music/trackFour.mp3")};
		this.songs = new Songs(songs);
		Gdx.app.log(TAG, "music loaded.");
		
		credits = new TextureRegion(new Texture("images/credits.png"));

	}

	@Override
	public void dispose() {
		assetManager.dispose();
		credits.getTexture().dispose();
	}

	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", (Exception) throwable);
	}

	public class MapAssets {
		public final AtlasRegion thinBlueLeft, thinBlueMiddle, thinBlueRight, thinBlueUp, thinBlueVerticalMiddle, ladderBottom, ladderTop, ladderMiddle;
		
		public MapAssets(TextureAtlas atlas) {
			thinBlueLeft = atlas.findRegion("70");
			thinBlueMiddle = atlas.findRegion("71");
			thinBlueRight = atlas.findRegion("72");
			thinBlueUp = atlas.findRegion("61");
			thinBlueVerticalMiddle = atlas.findRegion("62");
			ladderBottom = atlas.findRegion("98");
			ladderTop = atlas.findRegion("76");
			ladderMiddle = atlas.findRegion("87");
		}
	}
	
	public class CharacterAssets {
		public final AtlasRegion run1, run2, run3, run4, jump1, jump2, jump3, jump4, hit1, hit2, slash1, slash2, slash3, climb1, climb2, climb3, climb4, 
		skeletonWalk1, skeletonWalk2, skeletonWalk3, devilWalk1, devilWalk2, devilWalk3, devilWalk4, devilAttack1, devilAttack2, devilAttack3, devilAttack4;

		public CharacterAssets(TextureAtlas atlas) {
			run1 = atlas.findRegion("dwarfRun1");
			run2 = atlas.findRegion("dwarfRun2");
			run3 = atlas.findRegion("dwarfRun3");
			run4 = atlas.findRegion("dwarfRun4");
			jump1 = atlas.findRegion("dwarfJump1");
			jump2 = atlas.findRegion("dwarfJump2");
			jump3 = atlas.findRegion("dwarfJump3");
			jump4 = atlas.findRegion("dwarfJump4");
			hit1 = atlas.findRegion("dwarfHit1");
			hit2 = atlas.findRegion("dwarfHit2");
			slash1 = atlas.findRegion("dwarfAttack1");
			slash2 = atlas.findRegion("dwarfAttack2");
			slash3 = atlas.findRegion("dwarfAttack3");
			climb1 = atlas.findRegion("dwarfClimb1");
			climb2 = atlas.findRegion("dwarfClimb2");
			climb3 = atlas.findRegion("dwarfClimb3");
			climb4 = atlas.findRegion("dwarfClimb4");
			skeletonWalk1 = atlas.findRegion("skeletonWalk1");
			skeletonWalk2 = atlas.findRegion("skeletonWalk2");
			skeletonWalk3 = atlas.findRegion("skeletonWalk3");
			devilWalk1 = atlas.findRegion("devilWalk1");
			devilWalk2 = atlas.findRegion("devilWalk2");
			devilWalk3 = atlas.findRegion("devilWalk3");
			devilWalk4 = atlas.findRegion("devilWalk4");
			devilAttack1 = atlas.findRegion("devilAttack1");
			devilAttack2 = atlas.findRegion("devilAttack2");
			devilAttack3 = atlas.findRegion("devilAttack3");
			devilAttack4 = atlas.findRegion("devilAttack4");
		}
	}
	
	public class ParticleAnimations {
		
		public final AtlasRegion death1, death2, death3, death4, death5, death6, death7, death8, death9, death10, death11, death12, death13,
		death14, death15, death16, death17, death18, death19, death20, death21, death22, death23;
		
		public ParticleAnimations(TextureAtlas atlas) {
			death1 = atlas.findRegion("death8");
			death2 = atlas.findRegion("death9");
			death3 = atlas.findRegion("death10");
			death4 = atlas.findRegion("death11");
			death5 = atlas.findRegion("death12");
			death6 = atlas.findRegion("death13");
			death7 = atlas.findRegion("death14");
			death8 = atlas.findRegion("death15");
			death9 = atlas.findRegion("death16");
			death10 = atlas.findRegion("death17");
			death11 = atlas.findRegion("death18");
			death12 = atlas.findRegion("death19");
			death13 = atlas.findRegion("death20");
			death14 = atlas.findRegion("death21");
			death15 = atlas.findRegion("death22");
			death16 = atlas.findRegion("death23");
			death17 = atlas.findRegion("death24");
			death18 = atlas.findRegion("death25");
			death19 = atlas.findRegion("death26");
			death20 = atlas.findRegion("death27");
			death21 = atlas.findRegion("death28");
			death22 = atlas.findRegion("death29");
			death23 = atlas.findRegion("death30");
		}
		
	}
	
	public class Icons {
		public final AtlasRegion amethyst, axe1, axe2, axe3, axe4, axe5, blackBar, blackDagger, blueBar, copperBar, diamond, 
		emerald, emptyHeart, fullHeart, greenBar, mace1, mace2, platinumBar, redCoin, ruby, saphire, shortSword, silverBar, spear, sword1, sword2, 
		sword3, sword4, sword5, sword6, sword7, sword8, sword9, sword10, sword11, sword12, sword13, sword14, 
		sword15, sword16, wand1, wand2, wand3, warhammer1, warhammer2, warhammer3, warhammer4, gate1, gate2, gate3, gate4;
		public Icons(TextureAtlas atlas) {
			amethyst = atlas.findRegion("amethyst");
			axe1 = atlas.findRegion("axe1");
			axe2 = atlas.findRegion("axe2");
			axe3 = atlas.findRegion("axe3");
			axe4 = atlas.findRegion("axe4");
			axe5 = atlas.findRegion("axe5");
			blackBar = atlas.findRegion("blackBar");
			blackDagger = atlas.findRegion("blackDagger");
			blueBar = atlas.findRegion("blueBar");
			copperBar = atlas.findRegion("copperBar");
			diamond = atlas.findRegion("diamond");
			emerald = atlas.findRegion("emerald");
			emptyHeart = atlas.findRegion("emptyHeart");
			fullHeart = atlas.findRegion("fullHeart");
			greenBar = atlas.findRegion("greenBar");
			mace1 = atlas.findRegion("mace1");
			mace2 = atlas.findRegion("mace2");
			platinumBar = atlas.findRegion("platinumBar");
			redCoin = atlas.findRegion("redCoin");
			ruby = atlas.findRegion("ruby");
			saphire = atlas.findRegion("ruby");
			shortSword = atlas.findRegion("shortSword");
			silverBar = atlas.findRegion("silverBar");
			spear = atlas.findRegion("spear");
			sword1 = atlas.findRegion("sword1");
			sword2 = atlas.findRegion("sword2");
			sword3 = atlas.findRegion("sword3");
			sword4 = atlas.findRegion("sword4");
			sword5 = atlas.findRegion("sword5");
			sword6 = atlas.findRegion("sword6");
			sword7 = atlas.findRegion("sword7");
			sword8 = atlas.findRegion("sword8");
			sword9 = atlas.findRegion("sword9");
			sword10 = atlas.findRegion("sword10");
			sword11 = atlas.findRegion("sword11");
			sword12 = atlas.findRegion("sword12");
			sword13 = atlas.findRegion("sword13");
			sword14 = atlas.findRegion("sword14");
			sword15 = atlas.findRegion("sword15");
			sword16 = atlas.findRegion("sword16");
			wand1 = atlas.findRegion("wand1");
			wand2 = atlas.findRegion("wand2");
			wand3 = atlas.findRegion("wand3");
			warhammer1 = atlas.findRegion("warhammer1");
			warhammer2 = atlas.findRegion("warhammer2");
			warhammer3 = atlas.findRegion("warhammer3");
			warhammer4 = atlas.findRegion("warhammer4");
			gate1 = atlas.findRegion("gate1");
			gate2 = atlas.findRegion("gate2");
			gate3 = atlas.findRegion("gate3");
			gate4 = atlas.findRegion("gate4");
		}
	}

	public class Songs {
		public final Music trackOne, trackTwo, trackThree, trackFour;
		public final Sound swordHit, hit, swoosh;
		public final ArrayList<Music> allSongs;
		public final ArrayList<Sound> allSounds;
		
		public Music currentSong;
		
		public Songs(Music[] songs) {
			trackOne = songs[0];
			trackTwo = songs[1];
			trackThree = songs[2];
			trackFour = songs[3];
			swordHit = Gdx.audio.newSound(Gdx.files.internal("sounds/swordHit.mp3"));
			hit  = Gdx.audio.newSound(Gdx.files.internal("sounds/hit.mp3"));
			swoosh  = Gdx.audio.newSound(Gdx.files.internal("sounds/swoosh.mp3"));
			
			allSongs = new ArrayList<Music>();
			allSongs.add(trackOne);
			allSongs.add(trackTwo);
			allSongs.add(trackThree);
			allSongs.add(trackFour);
			
			allSounds = new ArrayList<Sound>();
			allSounds.add(swordHit);
			allSounds.add(hit);
			allSounds.add(swoosh);
		}
	
	}

}
