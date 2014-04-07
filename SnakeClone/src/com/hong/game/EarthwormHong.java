package com.hong.game;

import com.hong.framework.Screen;
import com.hong.framework.implementations.AndroidGame;

public class EarthwormHong extends AndroidGame {
	/*
	 * entry point into game, loads assets and switches to game screen
	 */
	@Override
	public Screen getStartScreen() {
		return new LoadingScreen(this);
	}
}
