package com.hong.framework.implementations;

import com.hong.framework.Audio;
import com.hong.framework.FileIO;
import com.hong.framework.Game;
import com.hong.framework.Graphics;
import com.hong.framework.Input;
import com.hong.framework.Screen;
import com.hong.game.Assets;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;

/*
 * implements Game interface, handles the 
 * life cycle
 */

public class AndroidGame extends Activity implements Game {
	GameRenderer renderView;
	Graphics graphics;
	Audio audio;
	Input input;
	FileIO fileIO;
	Screen screen;
	WakeLock wakeLock;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// remove title bar, go full screen
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		int frameBufferWidth = 1280;
		int frameBufferHeight = 800;

		// artificial frameBuffer the size of target resolution
		// use RGB_565 to save memory
		Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth,
				frameBufferHeight, Config.RGB_565);

		// must scale different screens to target resolution(frameBuffer)
		// coordinates
		float scaleX = (float) frameBufferWidth
				/ getWindowManager().getDefaultDisplay().getWidth();
		float scaleY = (float) frameBufferHeight
				/ getWindowManager().getDefaultDisplay().getHeight();

		// initialize game component and set view to a surface view
		renderView = new GameRenderer(this, frameBuffer);
		graphics = new GameGraphics(getAssets(), frameBuffer);
		fileIO = new GameFileIO(this);
		audio = new GameAudio(this);
		input = new GameInput(renderView, scaleX, scaleY);
		screen = getStartScreen();
		setContentView(renderView);

		// prevent screen from sleeping/dimming
		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,
				"GLGame");
	}

	@Override
	public void onResume() {
		// game started here
		super.onResume();
		wakeLock.acquire();
		screen.resume();
		renderView.resume();
		//Assets.music.play();
		
	}

	@Override
	public void onPause() {
		// pause game and if finishing then free memory
		super.onPause();
		wakeLock.release();
		renderView.pause();
		screen.pause();
		//Assets.music.pause();
		if (isFinishing()) {
			screen.dispose();
			//Assets.music.dispose();
		}
	}

	@Override
	public Input getInput() {
		return input;
	}

	@Override
	public FileIO getFileIO() {
		return fileIO;
	}

	@Override
	public Graphics getGraphics() {
		return graphics;
	}

	@Override
	public Audio getAudio() {
		return audio;
	}

	@Override
	public void setScreen(Screen screen) {
		if (screen == null)
			throw new IllegalArgumentException("Screen must not be null");
		this.screen.pause();
		this.screen.dispose();
		screen.resume();
		screen.update(0);
		this.screen = screen;

	}

	@Override
	public Screen getCurrentScreen() {
		return screen;
	}

	@Override
	public Screen getStartScreen() {
		// will be overridden by final implementation
		return null;
	}

}
