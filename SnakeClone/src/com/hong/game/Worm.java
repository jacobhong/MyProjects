package com.hong.game;

import java.util.ArrayList;
import java.util.List;

public class Worm {
	public static final int UP = 0;
	public static final int LEFT = 1;
	public static final int DOWN = 2;
	public static final int RIGHT = 3;
	public List<WormPart> parts = new ArrayList<WormPart>();
	public int direction;

	public Worm() {
		direction = UP;
		parts.add(new WormPart(5, 6));
		parts.add(new WormPart(5, 7));
		parts.add(new WormPart(5, 8));
	}

	public void turnLeft() {
		direction += 1;
		if (direction > RIGHT) {
			direction = UP;
		}
	}

	public void turnRight() {
		direction -= 1;
		if (direction < UP) {
			direction = RIGHT;
		}
	}

	public void eat() {
		Assets.eat.play(1);
		WormPart end = parts.get(parts.size() - 1);
		parts.add(new WormPart(end.x, end.y));
		
	}

	public void advance() {
		WormPart head = parts.get(0);
		int len = parts.size() - 1;
		for (int i = len; i > 0; i--) {
			WormPart before = parts.get(i - 1);
			WormPart part = parts.get(i);
			part.x = before.x;
			part.y = before.y;
		}
		if (direction == UP)
			head.y -= 1;
		if (direction == LEFT)
			head.x -= 1;
		if (direction == DOWN)
			head.y += 1;
		if (direction == RIGHT)
			head.x += 1;
		if (head.x < 0)
			head.x = 40;
		if (head.x > 40)
			head.x = 0;
		if (head.y < 0)
			head.y = 25;
		if (head.y > 25)
			head.y = 0;
	}

	public boolean checkBitten() {
		int len = parts.size();
		WormPart head = parts.get(0);
		for (int i = 1; i < len; i++) {
			WormPart part = parts.get(i);
			if (part.x == head.x && part.y == head.y)
				return true;
		}
		return false;
	}

}
