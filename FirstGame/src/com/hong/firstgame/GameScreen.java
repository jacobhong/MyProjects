package com.hong.firstgame;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.hong.firstgame.Tile.Type;
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

	private GameState gameState = GameState.READY;
	private List<Tile> tiles;

	enum GameState {
		READY, RUNNING, PAUSE, GAMEOVER
	}

	public GameScreen(Game game) {
		super(game);
		paddle = new Paddle(400, 1180);
		pong = new Pong(500, 500);
		tiles = new ArrayList<Tile>();
		loadMap();

	}

	private void loadMap() {
		for (int i = 1; i < 8; i++) {
			for (int j = 6; j < 9; j++) {
				Tile t = new Tile(i * 96, j * 32, Type.MEDIUM);
				tiles.add(t);
			}
		}

		for (int i = 2; i < 7; i++) {
			for (int j = 3; j < 6; j++) {
				Tile t = new Tile(i * 96, j * 32, Type.HIGH);
				tiles.add(t);
			}
		}

	}

	private void displayMap() {

		Graphics g = game.getGraphics();
		for (int i = 0; i < tiles.size(); i++) {
			Tile t = tiles.get(i);
			if (t == null) {
				continue;
			}
			if (i >= 21) {
				g.drawImage(Assets.tile5, t.x, t.y);
			} else {
				g.drawImage(Assets.tile2, t.x, t.y);
			}
		}
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
		for (int i = 0; i < tiles.size(); i++) {
			Tile t = tiles.get(i);
			if (tileCollision(t, pong)) {
				t.decrementLife();
				Log.i("LIFE", " " + t.type + " # " + i);
				if (t.dead()) {
					tiles.set(i, null);
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

	}

	private boolean paddleCollision(Paddle paddle, Pong pong) {
		if (pong.x <= paddle.x + Paddle.WIDTH) {
			if (pong.x >= paddle.x) {
				if (pong.y + Pong.HEIGHT >= paddle.y) {
					return true;
				}
			}
		}
		return false;

	}

	private boolean tileCollision(Tile tile, Pong pong) {
		if (tile == null) {
			return false;
		}
		if (pong.x <= tile.x + Tile.WIDTH) {
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

	@Override
	public void present(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawImage(Assets.background, 0, 0);
		g.drawImage(Assets.paddle, (int) paddle.x, paddle.y);
		g.drawImage(Assets.pong, (int) pong.x, (int) pong.y);
		g.drawImage(Assets.pause, 0, 0);
		displayMap();
		if (gameState == GameState.RUNNING)
			drawRunningUI();
		if (gameState == GameState.PAUSE)
			drawPausedUI();
		if (gameState == GameState.GAMEOVER)
			drawGameOverUI();

	}

	private void drawRunningUI() {
	}

	private void drawPausedUI() {
		Graphics g = game.getGraphics();
		g.drawImage(Assets.pauseMenu, 210, 400);
	}

	private void drawGameOverUI() {
		Graphics g = game.getGraphics();
		g.drawImage(Assets.gameover, 210, 400);
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
