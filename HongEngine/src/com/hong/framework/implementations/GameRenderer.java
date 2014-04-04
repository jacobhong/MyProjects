package com.hong.framework.implementations;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameRenderer extends SurfaceView implements Runnable {
	AndroidGame game; // need game instance to draw
	Bitmap framebuffer; // draw images to buffer @ target size and gets resized
						// to surface
	Thread renderThread = null; // main game loop
	SurfaceHolder holder; // draw images from buffer
	volatile boolean running = false; // handles game loop

	public GameRenderer(AndroidGame game, Bitmap framebuffer) {
		super(game);
		this.game = game;
		this.framebuffer = framebuffer;
		this.holder = getHolder();
	}

	public void resume() {
		running = true;
		renderThread = new Thread(this);
		renderThread.start();
	}

	public void pause() {
		running = false;
		while (true) {
			// run until thread dies
			try {
				renderThread.join();
				return;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		// dstRect will be size of target resolution
		Rect dstRect = new Rect();
		long startTime = System.nanoTime();
		while (running) {
			if (!holder.getSurface().isValid()) {
				// restart loop until surface view is valid
				continue;
			}
			float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
			startTime = System.nanoTime();

			game.getCurrentScreen().update(deltaTime);
			game.getCurrentScreen().present(deltaTime);

			Canvas canvas = holder.lockCanvas();
			canvas.getClipBounds(dstRect);
			canvas.drawBitmap(framebuffer, null, dstRect, null);
			holder.unlockCanvasAndPost(canvas);
		}
	}

}
