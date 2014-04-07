package com.hong.firstgame;

public class Tile {
	public static final int HEIGHT = 32;
	public static final int WIDTH = 96;
	public int x, y;
	public Type type;

	enum Type {
		HIGH(3), MEDIUM(2), LOW(1), DEAD(0);

		int life;

		Type(int life) {
			this.life = life;
		}
	}

	public Tile(int x, int y, Type type) {
		this.x = x;
		this.y = y;
		this.type = type;
	}

	public void decrementLife() {
		switch(type.life){
		case 3:
			type = Type.MEDIUM;
			break;
		case 2:
			type = Type.LOW;
			break;
		case 1:
			type = Type.DEAD;
			break;
	
		}
	}

	public boolean dead() {
		return type.life == 0;
	}

}
