package com.hong.firstgame;

import java.util.List;

import android.util.Log;

import com.hong.framework.Game;
import com.hong.framework.Graphics;
import com.hong.framework.Input.TouchEvent;
import com.hong.framework.Screen;

public class GameScreen extends Screen {
	Paddle paddle;

	public GameScreen(Game game) {
		super(game);
		paddle = new Paddle(400, 1180);

	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent events = touchEvents.get(i);
			if (events.type == TouchEvent.TOUCH_DOWN) {
				if (events.x < 380 && events.y > 400) {					
					paddle.turnLeft();
				}
				if (events.x > 410 && events.y > 400) {				
					paddle.turnRight();
				}
			} else if (events.type == TouchEvent.TOUCH_UP) {
				paddle.stop();
			}
		}
		paddle.update(deltaTime);
	}

	@Override
	public void present(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawImage(Assets.background, 0, 0);
		g.drawImage(Assets.paddle, (int) paddle.x, paddle.y);

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
