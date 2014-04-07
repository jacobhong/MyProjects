package com.hong.firstgame;

import com.hong.framework.Game;
import com.hong.framework.Graphics;
import com.hong.framework.Graphics.ImageFormat;
import com.hong.framework.Screen;

public class LoadingScreen extends Screen {

	public LoadingScreen(Game game) {
		super(game);

	}

	@Override
	public void update(float deltaTime) {
		Graphics g = game.getGraphics();
		Assets.background = g.newImage("background.png", ImageFormat.RGB565);
		Assets.paddle = g.newImage("paddle.png", ImageFormat.ARGB4444);
		Assets.start = g.newImage("start.png", ImageFormat.ARGB4444);
		Assets.play = game.getAudio().newSound("resume.ogg");
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
