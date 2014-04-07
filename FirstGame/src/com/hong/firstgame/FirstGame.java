package com.hong.firstgame;

import com.hong.framework.Screen;
import com.hong.framework.implementations.AndroidGame;


public class FirstGame extends AndroidGame {

	@Override
	public Screen getStartScreen() {
		return new LoadingScreen(this);
	}
}
