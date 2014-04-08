package com.hong.firstgame;

import com.hong.firstgame.Tile.Type;
import com.hong.framework.Game;
import com.hong.framework.Graphics;

public class Level2 extends LevelHandler {

	public Level2(Game game) {
		super(game);
	}

	@Override
	public void loadMap() {
		for (int i = 1; i < 8; i++) {
			for (int j = 8; j < 12; j++) {
				Tile t = new Tile(i * 96, j * 32, Type.LOW);
				tiles.add(t);
			}
		}

		for (int i = 1; i < 8; i += 2) {
			for (int j = 4; j < 6; j++) {
				Tile t = new Tile(i * 96, j * 32, Type.MEDIUM);
				tiles.add(t);
			}
		}
	}

	@Override
	public void update() {

		Graphics g = game.getGraphics();
		for (int i = 0; i < tiles.size(); i++) {
			Tile t = tiles.get(i);
			if (t == null) {
				continue;
			}
			if (i >= 28) {
				g.drawImage(Assets.tile4, (int) t.x - 33, (int) t.y);
			} else {
				g.drawImage(Assets.tile1, (int) t.x - 33, (int) t.y);
			}
		}

	}

	@Override
	public boolean complete() {
		return count == 36;
	}

}
