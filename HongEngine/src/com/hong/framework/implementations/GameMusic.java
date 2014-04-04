package com.hong.framework.implementations;

import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import com.hong.framework.Music;

/*
 * player must be prepared before playing
 * 
 * callback occurs on UI thread so must
 * synchronize methods for boolean isPrepared
 */
public class GameMusic implements Music, OnCompletionListener {
	MediaPlayer player;
	boolean isPrepared = false;

	public GameMusic(AssetFileDescriptor descriptor) {
		player = new MediaPlayer();
		try {
			player.setDataSource(descriptor.getFileDescriptor(),
					descriptor.getStartOffset(), descriptor.getLength());
			player.prepare();
			isPrepared = true;
			player.setOnCompletionListener(this);
		} catch (Exception e) {
			throw new RuntimeException("GameMusic: Could not load music");
		}
	}

	@Override
	public void play() {
		if (player.isPlaying()) {
			return;
		}
		try {
			synchronized (this) {
				if (!isPrepared) {
					player.prepare();
				}
				player.start();
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		player.stop();
		synchronized (this) {
			isPrepared = false;
		}
	}

	@Override
	public void pause() {
		if (player.isPlaying()) {
			player.pause();
		}
	}

	@Override
	public void setLooping(boolean looping) {
		player.setLooping(looping);
	}

	@Override
	public void setVolume(float volume) {
		player.setVolume(volume, volume);
	}

	@Override
	public boolean isPlaying() {
		return player.isPlaying();
	}

	@Override
	public boolean isStopped() {
		return !isPrepared;
	}

	@Override
	public boolean isLooping() {
		return player.isLooping();
	}

	@Override
	public void dispose() {
		// must stop player before releasing or runtime error occurs
		if (player.isPlaying()) {
			player.stop();
		}
		player.release();
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		synchronized (this) {
			isPrepared = false;
		}

	}

}
