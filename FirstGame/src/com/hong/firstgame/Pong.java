package com.hong.firstgame;

public class Pong {
	public float x, y;
	public int speedX, speedY;
	public static final int LEFT = 0;
	public static final int RIGHT = 0;
	public static final int WIDTH = 32;
	public static final int HEIGHT = 32;
	public static final int DIAMETER = 15;
	public int direction;
	public boolean inBounds;

	public Pong(int x, int y) {
		this.x = x;
		this.y = y;
		speedX = 550;
		speedY = 550;
		inBounds = true;
	}

	public void stop() {
		speedX = 0;
		speedY = 0;
	}

	public void update(float deltaTime) {
		x += speedX * deltaTime;
		y += speedY * deltaTime;
		if (inBounds) {
			if (x + WIDTH > 799) {
				speedX *= -1;
			}
			if (x < 1) {
				speedX *= -1;
			}
			if (y > 1250) {
				inBounds = false;

			}
			if (y < 0) {
				speedY *= -1;
			}
		}

	}

	public void reverse() {
		speedY *= -1;
	}

}
