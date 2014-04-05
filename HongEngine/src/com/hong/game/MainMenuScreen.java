package com.hong.game;

import java.util.List;

import com.hong.framework.Game;
import com.hong.framework.Graphics;
import com.hong.framework.Input.TouchEvent;
import com.hong.framework.Screen;

public class MainMenuScreen extends Screen {

	public MainMenuScreen(Game game) {
		super(game);
	}

	@Override
	public void update(float deltaTime) {
		Graphics g = game.getGraphics();
		List<TouchEvent> events = game.getInput().getTouchEvents();
		int len = events.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = events.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (inBounds(event, g.getWidth() / 2, 300, 250, 180)) {
					Assets.resume.play(1);
					game.setScreen(new GameScreen(game));
				}
			}
		}
	}

	private boolean inBounds(TouchEvent event, int x, int y, int width,
			int height) {
		if (event.x > x && event.x < x + width - 1 && event.y > y
				&& event.y < y + height - 1)
			return true;
		else
			return false;
	}

	@Override
	public void present(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawImage(Assets.background, 0, 0);
		g.drawImage(Assets.start, g.getWidth() / 2, 300);

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
