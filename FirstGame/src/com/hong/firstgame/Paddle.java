package com.hong.firstgame;

import android.util.Log;

public class Paddle {
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int NEUTRAL = 2;
	public static final int WIDTH = 192;
	public static final int HEIGHT = 32;
	public float x;
	public int y;
	public int speed;
	public int direction;

	public Paddle(int x, int y) {
		this.x = x;
		this.y = y;

	}

	public void turnLeft() {
		speed = 500;
		direction = LEFT;
	}

	public void turnRight() {
		speed = 500;
		direction = RIGHT;
	}

	public void stop() {
		speed = 0;
	}

	public void update(float deltaTime) {

		if (direction == LEFT && x > 0) {
			x -= speed * deltaTime;
		} else if (direction == RIGHT && x + WIDTH < 800) {
			x += speed * deltaTime;
		} else if (direction == NEUTRAL) {
			stop();
		}

	}
}
