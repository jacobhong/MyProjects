package com.hong.firstgame;

import com.hong.firstgame.Tile.Type;
import com.hong.framework.Game;
import com.hong.framework.Graphics;

public class Level3 extends LevelHandler {

	public Level3(Game game) {
		super(game);
	}

	@Override
	public void loadMap() {
		for (int i = 1; i < 8; i += 2) {
			for (int j = 14; j < 20; j += 2) {
				Tile t = new Tile(i * 96, j * 32, Type.MEDIUM);
				tiles.add(t);
			}
		}

		for (int i = 1; i < 8; i += 1) {
			for (int j = 2; j < 5; j += 2) {
				Tile t = new Tile(i * 96, j * 32, Type.HIGH);
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
			if (i >= 12) {
				g.drawImage(Assets.tile5, (int) t.x - 33, (int) t.y);
			} else {
				g.drawImage(Assets.tile4, (int) t.x - 33, (int) t.y);
			}
		}

	}

	@Override
	public boolean complete() {
		return count == 26;
	}

}
