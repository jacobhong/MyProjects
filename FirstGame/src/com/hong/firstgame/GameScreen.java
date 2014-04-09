package com.hong.firstgame;

import java.util.List;
import java.util.logging.Level;

/*
 * game engine / controller handles user input
 * and updates models and renders them
 */
import com.hong.framework.Game;
import com.hong.framework.Graphics;
import com.hong.framework.Input.TouchEvent;
import com.hong.framework.Music;
import com.hong.framework.Screen;

/*
 * game controller, updates models and renders screen
 */
public class GameScreen extends Screen {
	private Paddle paddle;
	private Pong pong;
	private LevelHandler[] levels;
	private int level;
	private LevelHandler currentLevel;
	private Graphics g;
	private GameState gameState;
	private Music music;

	enum GameState {
		READY, RUNNING, PAUSE, GAMEOVER, COMPLETE
	}

	public GameScreen(Game game) {
		super(game);
		levels = (LevelHandler[]) new LevelHandler[4];
		levels[0] = new Level1(game);
		gameState = GameState.READY;
		g = game.getGraphics();
		paddle = new Paddle(400, 1180);
		pong = new Pong(400, 500);
		level = 1;
		currentLevel = levels[0];
		levels[0].loadMap();
		music = Assets.music;
	}

	@Override
	public void update(float deltaTime) {
		// Receive user input and call appropriate event handler
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
		// if user touches the screen
		if (touchEvents.size() > 0)
			gameState = GameState.RUNNING;
	}

	private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {
		// while running play music, update models, check collision, check game
		// over, check if level is complete
		int len = touchEvents.size();
		music.play();
		for (int i = 0; i < len; i++) {
			TouchEvent events = touchEvents.get(i);
			if (events.type == TouchEvent.TOUCH_DOWN) {
				if (events.x < 100 && events.y < 100) {
					// if user hits the pause button
					Assets.pauseClick.play(1);
					gameState = GameState.PAUSE;
					music.pause();
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
		// update models
		paddle.update(deltaTime);
		pong.update(deltaTime);
		for (int i = 0; i < currentLevel.tiles.size(); i++) {
			// iterate through tiles and check collision
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
			Assets.hit.play(1);
			pong.reverse();
		}
		if (pong.inBounds == false) {
			// if ball goes out of bounds
			Assets.dead.play(1);
			gameState = GameState.GAMEOVER;
		}
		if (currentLevel.complete()) {
			// if all tiles removed
			Assets.completeSound.play(1);
			gameState = GameState.COMPLETE;
			level++;
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
		// if user hits resume or quit while paused
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (event.x > 210 && event.x <= 610) {
					if (event.y > 400 && event.y <= 600) {
						Assets.play.play(1);
						gameState = GameState.RUNNING;
						return;
					}
					if (event.y > 601 && event.y < 800) {
						Assets.dead.play(1);
						game.setScreen(new MainMenuScreen(game));
						return;
					}
				}
			}
		}
	}

	private void updateGameOver(List<TouchEvent> touchEvents) {
		// user must return to main menu if dead
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (event.x >= 210 && event.x <= 610 && event.y >= 700
						&& event.y <= 800) {
					Assets.play.play(1);
					game.setScreen(new MainMenuScreen(game));
					return;
				}
			}
		}
	}

	private void updateComplete(List<TouchEvent> touchEvents) {
		// check which is currentLevel and load it
		if (level == 2) {
			levels[1] = new Level2(game);
			currentLevel = levels[1];
			levels[1].loadMap();
		}
		if (level == 3) {
			levels[2] = new Level3(game);
			currentLevel = levels[2];
			levels[2].loadMap();
		}
		if (level == 4) {
			levels[3] = new Level3(game);
			currentLevel = levels[3];
			levels[3].loadMap();
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
		// renders the screen and current level
		// checks what state to render
		g.drawImage(Assets.background, 0, 0);
		g.drawImage(Assets.paddle, (int) paddle.x, (int) paddle.y);
		g.drawImage(Assets.pong, (int) pong.x, (int) pong.y);
		g.drawImage(Assets.pause, 0, 0);
		if (currentLevel == levels[0]) {
			levels[0].update();
		} else if (currentLevel == levels[1]) {
			levels[1].update();
		} else if (currentLevel == levels[2]) {
			levels[2].update();
		} else if (currentLevel == levels[3]) {
			levels[3].update();
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
