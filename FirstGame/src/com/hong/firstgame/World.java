package com.hong.firstgame;

import java.util.Random;


public class World {

	static final int WORLD_WIDTH = 25;
	static final int WORLD_HEIGHT = 40;
	static final float TICK_INITIAL = 0.3f;
	static final float TICK_DECREMENT = 0.03f;
	public Paddle paddle;

	boolean ground[][] = new boolean[WORLD_WIDTH][WORLD_HEIGHT];
	float tickTime = 0;
	float tick = TICK_INITIAL;
	public boolean gameOver = false;

	public World() {
		paddle = new Paddle(400, 1180);
	}

	public void update(float deltaTime) {
		tickTime += deltaTime;
		while (tickTime > tick) {
			tickTime -= tick;
			paddle.advance();
			
		}
	}
}
