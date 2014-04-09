package com.hong.firstgame;

import com.hong.framework.Screen;
import com.hong.framework.implementations.AndroidGame;

/*
 * starting point of the game
 */
public class FirstGame extends AndroidGame {

	@Override
	public Screen getStartScreen() {
		return new LoadingScreen(this);
	}
}
