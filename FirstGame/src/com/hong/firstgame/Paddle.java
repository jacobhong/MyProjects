package com.hong.firstgame;

public class Paddle {
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int NEUTRAL = 2;
	public static final int WIDTH = 192;
	public static final int HEIGHT = 32;
	public float x, y;
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
		direction = NEUTRAL;
	}

	public void update(float deltaTime) {

		x += (direction == LEFT ? -1 : +1) * speed * deltaTime;

		if (x < 0) {
			x = 0;
			stop();
		} else if (x+WIDTH > 800) {
			x = 800-WIDTH;
			stop();
		}
	}
}
