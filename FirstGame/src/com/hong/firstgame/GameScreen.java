package com.hong.firstgame;

import java.util.List;

/*
 * game engine / controller handles user input
 * and updates models and renders them
 */
import com.hong.framework.Game;
import com.hong.framework.Graphics;
import com.hong.framework.Input.TouchEvent;
import com.hong.framework.Screen;

public class GameScreen extends Screen {
	private Paddle paddle;
	private Pong pong;
	private Level1 level1;
	private Level2 level2;
	private Level3 level3;
	private int progression;
	private LevelHandler currentLevel;
	private Graphics g;
	private GameState gameState = GameState.READY;

	enum GameState {
		READY, RUNNING, PAUSE, GAMEOVER, COMPLETE
	}

	public GameScreen(Game game) {
		super(game);
		g = game.getGraphics();
		paddle = new Paddle(400, 1180);
		pong = new Pong(400, 500);
		level1 = new Level1(game);
		currentLevel = level1;
		currentLevel.loadMap();
		progression = 1;
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		if (gameState == GameState.READY) {
			updateReady(touchEvents);
		}
		if (gameState == GameState.RUNNING) {
			updateRunning(touchEvents, deltaTime);
		}
		if (gameState == GameState.PAUSE) {
			updatePause(touchEvents);
		}
		if (gameState == GameState.GAMEOVER) {
			updateGameOver(touchEvents);
		}
		if (gameState == GameState.COMPLETE) {
			updateComplete(touchEvents);
		}
	}

	private void updateReady(List<TouchEvent> touchEvents) {
		if (touchEvents.size() > 0)
			gameState = GameState.RUNNING;
	}

	private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent events = touchEvents.get(i);
			if (events.type == TouchEvent.TOUCH_DOWN) {
				if (events.x < 100 && events.y < 100) {
					Assets.pauseClick.play(1);
					gameState = GameState.PAUSE;
					return;
				}
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
		pong.update(deltaTime);
		for (int i = 0; i < currentLevel.tiles.size(); i++) {
			Tile t = currentLevel.tiles.get(i);
			if (tileCollision(t, pong)) {
				t.decrementLife();
				if (t.dead()) {
					currentLevel.tiles.set(i, null);
					currentLevel.count++;
				}
				pong.reverse();
			}
		}
		if (paddleCollision(paddle, pong)) {
			pong.reverse();
		}
		if (pong.inBounds == false) {
			gameState = GameState.GAMEOVER;
		}
		if (currentLevel.complete()) {
			gameState = GameState.COMPLETE;
		}

	}

	private boolean paddleCollision(Paddle paddle, Pong pong) {
		if (pong.x <= paddle.x + Paddle.WIDTH) {
			if (pong.x >= paddle.x) {
				if (pong.y <= paddle.y + Paddle.HEIGHT) {
					if (pong.y + Pong.HEIGHT >= paddle.y) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean tileCollision(Tile tile, Pong pong) {
		if (tile == null) {
			return false;
		}
		if (pong.x <= tile.x + Tile.WIDTH - 1) {
			if (pong.x >= tile.x) {
				if (pong.y <= tile.y + Tile.HEIGHT) {
					if (pong.y + Pong.HEIGHT >= tile.y) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private void updatePause(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (event.x > 210 && event.x <= 610) {
					if (event.y > 400 && event.y <= 600) {
						Assets.pauseClick.play(1);
						gameState = GameState.RUNNING;
						return;
					}
					if (event.y > 601 && event.y < 800) {
						game.setScreen(new MainMenuScreen(game));
						return;
					}
				}
			}
		}
	}

	private void updateGameOver(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (event.x >= 210 && event.x <= 610 && event.y >= 700
						&& event.y <= 800) {
					Assets.pauseClick.play(1);
					game.setScreen(new MainMenuScreen(game));
					return;
				}
			}
		}
	}

	private void updateComplete(List<TouchEvent> touchEvents) {
		progression++;
		if (progression == 2) {
			level2 = new Level2(game);
			currentLevel = level2;
			currentLevel.loadMap();
		}
		if (progression == 3) {
			level3 = new Level3(game);
			currentLevel = level3;
			currentLevel.loadMap();
		}
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (event.x >= 210 && event.x <= 610 && event.y >= 400
						&& event.y <= 800) {
					Assets.pauseClick.play(1);
					pong.x = 400;
					pong.y = 500;
					gameState = GameState.READY;
					return;
				}
			}
		}
	}

	@Override
	public void present(float deltaTime) {
		g.drawImage(Assets.background, 0, 0);
		g.drawImage(Assets.paddle, (int) paddle.x, (int) paddle.y);
		g.drawImage(Assets.pong, (int) pong.x, (int) pong.y);
		g.drawImage(Assets.pause, 0, 0);
		if (currentLevel == level1) {
			level1.update();
		} else if (currentLevel == level2) {
			level2.update();
		} else if (currentLevel == level3) {
			level3.update();
		}
		if (gameState == GameState.RUNNING)
			drawRunningUI();
		if (gameState == GameState.PAUSE)
			drawPausedUI();
		if (gameState == GameState.GAMEOVER)
			drawGameOverUI();
		if (gameState == GameState.COMPLETE) {
			drawCompleteUI();
		}

	}

	private void drawRunningUI() {
	}

	private void drawPausedUI() {
		g.drawImage(Assets.pauseMenu, 210, 400);
	}

	private void drawGameOverUI() {
		g.drawImage(Assets.gameover, 210, 400);
	}

	private void drawCompleteUI() {
		g.drawImage(Assets.complete, 210, 400);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

}
