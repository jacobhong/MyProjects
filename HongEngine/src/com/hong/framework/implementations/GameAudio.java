package com.hong.framework.implementations;

import java.io.IOException;

import com.hong.framework.Audio;
import com.hong.framework.Music;
import com.hong.framework.Sound;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

public class GameAudio implements Audio {
	AssetManager assetManager; //need to load assets
	SoundPool soundPool; //reuse sound effects to save memory

	public GameAudio(Activity activity) {
		//have to allow volume control for game		 
		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		assetManager = activity.getAssets();
		soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0); //20 max concurrent sounds
	}

	@Override
	public Music newMusic(String filename) {
		//loads song from assets
		try {
			AssetFileDescriptor descriptor = assetManager.openFd(filename);
			return new GameMusic(descriptor);
		} catch (IOException e) {
			throw new RuntimeException("Couldn't looad " + filename);
		}

	}

	@Override
	public Sound newSound(String filename) {
		//loads sound clip from assets and load into soundPool
		try {
			AssetFileDescriptor descriptor = assetManager.openFd(filename);
			int soundId = soundPool.load(descriptor, 0);
			return new GameSound(soundPool, soundId);
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load " + filename);
		}
	}
}
