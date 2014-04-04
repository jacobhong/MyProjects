package com.hong.game;

import java.util.List;

import android.graphics.Color;

import com.hong.framework.Game;
import com.hong.framework.Graphics;
import com.hong.framework.Image;
import com.hong.framework.Input.TouchEvent;
import com.hong.framework.Screen;

/*
 * Game life cycle controlled here. This is run on
 * the main game loop and updates the world
 */
public class GameScreen extends Screen {
	GameState state = GameState.READY;
	World world;

	enum GameState {
		READY, RUNNING, PAUSED, GAMEOVER
	}

	public GameScreen(Game game) {
		super(game);
		world = new World();
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		if (state == GameState.READY) {
			updateReady(touchEvents);
		}
		if (state == GameState.RUNNING) {
			updateRunning(touchEvents, deltaTime);
		}
		if (state == GameState.PAUSED) {
			updatePaused(touchEvents);
		}
		if (state == GameState.GAMEOVER) {
			updateGameOver(touchEvents);
		}

	}

	private void updateReady(List<TouchEvent> touchEvents) {
		if (touchEvents.size() > 0) {
			state = GameState.RUNNING;
		}
	}

	private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent events = touchEvents.get(i);
			if (events.type == TouchEvent.TOUCH_DOWN) {
				if (events.x < 64 && events.y > 416) {
					world.worm.turnLeft();
				}
				if (events.x > 256 && events.y > 416) {
					world.worm.turnRight();
				}
			}
		}
		world.update(deltaTime);
	}

	private void updateGameOver(List<TouchEvent> touchEvents) {
		// TODO Auto-generated method stub

	}

	private void updatePaused(List<TouchEvent> touchEvents) {
		// TODO Auto-generated method stub

	}

	@Override
	public void present(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawImage(Assets.background, 0, 0);
		drawWorld(world);

	}

	private void drawWorld(World world) {
		Graphics g = game.getGraphics();
		Worm worm = world.worm;
		WormPart head = worm.parts.get(0);
		Apple apple = world.apple;
		Image appleImage = Assets.apple;
		int x = apple.x * 32;
		int y = apple.y * 32;
		g.drawImage(appleImage, x, y);
		int len = worm.parts.size();
		for (int i = 1; i < len; i++) {
			WormPart part = worm.parts.get(i);
			x = part.x * 32;
			y = part.y * 32;
			g.drawImage(Assets.tail, x, y);
		}
		Image headImage = null;
		if (worm.direction == Worm.UP)
			headImage = Assets.headUp;
		if (worm.direction == Worm.LEFT)
			headImage = Assets.headLeft;
		if (worm.direction == Worm.DOWN)
			headImage = Assets.headDown;
		if (worm.direction == Worm.RIGHT)
			headImage = Assets.headRight;
		x = head.x * 32 + 16;
		y = head.y * 32 + 16;
		g.drawImage(headImage, x - headImage.getWidth() / 2,
				y - headImage.getHeight() / 2);

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
