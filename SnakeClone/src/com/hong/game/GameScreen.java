package com.hong.game;

import java.util.List;

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
				if (events.x < 70 && events.y < 70) {
					state = GameState.PAUSED;
					return;
				}
				if (events.x < 350 && events.y > 400) {
					world.worm.turnLeft();
				}
				if (events.x > 750 && events.y > 400) {
					world.worm.turnRight();
				}
			}
		}
		world.update(deltaTime);
		if (world.gameOver) {
			state = GameState.GAMEOVER;
		}
	}

	private void updatePaused(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (event.x > 0 && event.x <= 70) {
					if (event.y > 0 && event.y <= 70) {
						state = GameState.RUNNING;
						return;
					}
					if (event.y > 148 && event.y < 196) {
						game.setScreen(new MainMenuScreen(game));
						return;
					}
				}
			}
		}
	}

	@Override
	public void present(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawImage(Assets.background, 0, 0);
		g.drawImage(Assets.pausebutton, 0, 0);
		drawWorld(world);
		if (state == GameState.READY)
			drawReadyUI();
		if (state == GameState.RUNNING)
			drawRunningUI();
		if (state == GameState.PAUSED)
			drawPausedUI();
		if (state == GameState.GAMEOVER)
			drawGameOverUI();

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
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	private void updateGameOver(List<TouchEvent> touchEvents) {
		game.setScreen(new MainMenuScreen(game));
		return;
	}

	private void drawReadyUI() {
		Graphics g = game.getGraphics();
		g.drawImage(Assets.ready, 200, 300);

	}

	private void drawRunningUI() {
		Graphics g = game.getGraphics();
		

	}

	private void drawPausedUI() {
		Graphics g = game.getGraphics();
		g.drawImage(Assets.pausebutton, 0, 0);

	}

	private void drawGameOverUI() {
		Graphics g = game.getGraphics();
		g.drawImage(Assets.gameOver, 62, 100);

	}

	@Override
	public void pause() {
		if (state == GameState.RUNNING)
			state = GameState.PAUSED;
	}

}
