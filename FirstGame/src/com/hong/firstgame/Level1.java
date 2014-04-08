package com.hong.firstgame;

import com.hong.firstgame.Tile.Type;
import com.hong.framework.Game;
import com.hong.framework.Graphics;

public class Level1 extends LevelHandler {

	public Level1(Game game) {
		super(game);

	}

	public void loadMap() {
		for (int i = 1; i < 8; i++) {
			for (int j = 6; j < 9; j++) {
				Tile t = new Tile(i * 96, j * 32, Type.LOW);
				tiles.add(t);
			}
		}

		for (int i = 2; i < 7; i++) {
			for (int j = 3; j < 6; j++) {
				Tile t = new Tile(i * 96, j * 32, Type.LOW);
				tiles.add(t);
			}
		}

	}

	public void update() {

		Graphics g = game.getGraphics();
		for (int i = 0; i < tiles.size(); i++) {
			Tile t = tiles.get(i);
			if (t == null) {
				continue;

			}
			g.drawImage(Assets.tile1, (int) t.x - 33, (int) t.y);

		}
	}

	@Override
	public boolean complete() {
		return count == 36;
	}

}
