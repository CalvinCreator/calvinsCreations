package com.codesmith.main.desktop;	
import java.awt.Toolkit;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.*;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.codesmith.main.MainLoop;

public class DesktopLauncher {
	
	private static boolean rebuildAtlas = false;
	private static boolean drawDebugOutline = false;
	
	//for david
	
	public static void main (String[] arg) {
		if(rebuildAtlas) {
			Settings settings = new Settings();
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.duplicatePadding = false;
			settings.debug = drawDebugOutline;
			TexturePacker.process(settings, "maps/tileset", "../", "test.pack");
		}
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Toolkit.getDefaultToolkit().getScreenSize().width;
		config.height = Toolkit.getDefaultToolkit().getScreenSize().height;
		config.fullscreen = true;
		config.foregroundFPS = 60;
		config.vSyncEnabled = true;
		config.resizable = false;
		config.addIcon("images/dwarfHeadb.png", Files.FileType.Internal);
		config.title = "Codesmith Alpha"; 
		
		new LwjglApplication(new MainLoop(), config);
	}
}
