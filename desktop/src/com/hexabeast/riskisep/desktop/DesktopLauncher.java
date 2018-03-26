package com.hexabeast.riskisep.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.hexabeast.riskisep.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1600;
		config.height = 900;
		config.resizable = true;
		config.fullscreen = false;
		config.samples = 16;
		//config.useGL30 = true;
		config.title = "RISKISEP";
		new LwjglApplication(new Main(), config);
	}
}
