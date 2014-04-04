package com.hong.framework.implementations;

import android.media.SoundPool;

import com.hong.framework.Sound;

public class GameSound implements Sound {
	
	int soundId;
	SoundPool soundPool;

	public GameSound(SoundPool soundPool, int soundId) {
		this.soundId = soundId;
		this.soundPool = soundPool;
	}

	@Override
	public void play(float volume) {
		soundPool.play(soundId, volume, volume, 0, 0, 1);

	}

	@Override
	public void dispose() {
		soundPool.unload(soundId);

	}

}
