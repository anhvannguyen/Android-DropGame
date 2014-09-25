package me.anhvannguyen.drop.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import me.anhvannguyen.drop.Drop;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Drop Game";
		config.width = 800;
		config.height = 480;
		config.resizable = false;
		new LwjglApplication(new Drop(), config);
	}
}
