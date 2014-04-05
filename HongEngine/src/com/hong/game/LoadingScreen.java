package com.hong.game;

import com.hong.framework.Game;
import com.hong.framework.Graphics;
import com.hong.framework.Graphics.ImageFormat;
import com.hong.framework.Screen;

/*
 * initialize assets and set screen to main screen
 */
public class LoadingScreen extends Screen {

	public LoadingScreen(Game game) {
		// superclass holds game instance
		super(game);
	}

	@Override
	public void update(float deltaTime) {
		Graphics g = game.getGraphics();
		Assets.apple = g.newImage("apple.png", ImageFormat.ARGB4444);
		Assets.background = g.newImage("background.png", ImageFormat.RGB565);
		Assets.headDown = g.newImage("headDown.png", ImageFormat.ARGB4444);
		Assets.headLeft = g.newImage("headLeft.png", ImageFormat.ARGB4444);
		Assets.headRight = g.newImage("headRight.png", ImageFormat.ARGB4444);
		Assets.headUp = g.newImage("headUp.png", ImageFormat.ARGB4444);
		Assets.dead = game.getAudio().newSound("dead.ogg");
		Assets.eat = game.getAudio().newSound("eat.ogg");
		Assets.music = game.getAudio().newMusic("music.mp3");
		Assets.pause = game.getAudio().newSound("pause.ogg");
		Assets.resume = game.getAudio().newSound("resume.ogg");
		Assets.tail = g.newImage("tail.png", ImageFormat.ARGB4444);
		Assets.start = g.newImage("start.png", ImageFormat.ARGB4444);
		Assets.gameOver = g.newImage("gameover.png", ImageFormat.ARGB4444);
		Assets.pausebutton = g
				.newImage("pausebutton.png", ImageFormat.ARGB4444);
		Assets.ready = g.newImage("ready.png", ImageFormat.ARGB4444);
		game.setScreen(new MainMenuScreen(game));
	}

	@Override
	public void present(float deltaTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
