package com.hong.firstgame;

import java.util.ArrayList;
import java.util.List;

import com.hong.framework.Game;

abstract class LevelHandler {
	protected int count;
	protected List<Tile> tiles;
	protected Game game;

	public LevelHandler(Game game) {
		this.game = game;
		tiles = new ArrayList<Tile>();
	}

	abstract public void loadMap();

	abstract public void update();

	abstract public boolean complete();

}
