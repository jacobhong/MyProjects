package com.hong.framework.implementations;

import android.graphics.Bitmap;

import com.hong.framework.Graphics.ImageFormat;
import com.hong.framework.Image;
/*
 * store information regarding Image
 */
public class GameImage implements Image {
	Bitmap bitmap;
	ImageFormat format;

	public GameImage(Bitmap bitmap, ImageFormat format) {
		this.bitmap = bitmap;
		this.format = format;
	}

	@Override
	public int getWidth() {
		return bitmap.getWidth();
	}

	@Override
	public int getHeight() {
		return bitmap.getHeight();
	}

	@Override
	public ImageFormat getFormat() {
		return format;
	}

	@Override
	public void dispose() {
		bitmap.recycle();

	}
}
