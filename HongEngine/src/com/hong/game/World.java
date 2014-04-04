package com.hong.game;

import java.util.Random;

public class World {

	static final int WORLD_WIDTH = 10;
	static final int WORLD_HEIGHT = 13;
	static final float TICK_INITIAL = 0.4f;
	static final float TICK_DECREMENT = 0.03f;
	public Worm worm;
	public Apple apple;
	Random random = new Random();
	boolean ground[][] = new boolean[WORLD_WIDTH][WORLD_HEIGHT];
	float tickTime = 0;
	float tick = TICK_INITIAL;

	public World() {
		worm = new Worm();
		placeApple();
	}

	private void placeApple() {
		for (int x = 0; x < WORLD_WIDTH; x++) {
			for (int y = 0; y < WORLD_HEIGHT; y++) {
				ground[x][y] = false;
			}
		}
		int len = worm.parts.size();
		for (int i = 0; i < len; i++) {
			WormPart part = worm.parts.get(i);
			ground[part.x][part.y] = true;
		}
		int appleX = random.nextInt(WORLD_WIDTH);
		int appleY = random.nextInt(WORLD_HEIGHT);
		while (true) {
			if (ground[appleX][appleY] == false) {
				break;
			}
			appleX += 1;
			if (appleX >= WORLD_WIDTH) {
				appleX = 0;
				appleY += 1;
				if (appleY >= WORLD_HEIGHT) {
					appleY = 0;
				}
			}
		}
		apple = new Apple(appleX, appleY);

	}

	public void update(float deltaTime) {
		tickTime += deltaTime;
		while (tickTime > tick) {
			tickTime -= tick;
			worm.advance();
			if (worm.checkBitten()) {

			}
			WormPart head = worm.parts.get(0);
			if(head.x == apple.x&&head.y==apple.y){
				worm.eat();
				placeApple();
			}
		}
	}

}
