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
		Assets.complete = g.newImage("complete.png", ImageFormat.ARGB4444);
		Assets.paddle = g.newImage("paddle.png", ImageFormat.ARGB4444);
		Assets.pong = g.newImage("pong.png", ImageFormat.ARGB4444);
		Assets.tile1 = g.newImage("tile1.png", ImageFormat.ARGB4444);
		Assets.tile2 = g.newImage("tile2.png", ImageFormat.ARGB4444);
		Assets.tile3 = g.newImage("tile3.png", ImageFormat.ARGB4444);
		Assets.tile4 = g.newImage("tile4.png", ImageFormat.ARGB4444);
		Assets.tile5 = g.newImage("tile5.png", ImageFormat.ARGB4444);

		Assets.gameover = g.newImage("gameover.png", ImageFormat.ARGB4444);
		Assets.start = g.newImage("start.png", ImageFormat.ARGB4444);
		Assets.pause = g.newImage("pause.png", ImageFormat.ARGB4444);
		Assets.pauseMenu = g.newImage("pauseMenu.png", ImageFormat.ARGB4444);
		Assets.pauseClick = game.getAudio().newSound("pauseClick.ogg");
		Assets.play = game.getAudio().newSound("resume.ogg");
		Assets.dead = game.getAudio().newSound("dead.ogg");
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
