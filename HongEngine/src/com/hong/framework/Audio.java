package com.hong.framework;
/*
 * Stream music or load sound 
 * effect from memory
 */
public interface Audio {
	public Music newMusic(String filename);

	public Sound newSound(String filename);
}
