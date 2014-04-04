package com.hong.framework.implementations;

import java.io.IOException;
import java.io.InputStream;

import com.hong.framework.Graphics;
import com.hong.framework.Image;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.Paint.Style;

/*
 * create images/shapes to frameBuffer
 */

public class GameGraphics implements Graphics {
	AssetManager assets;
	Bitmap frameBuffer;
	Canvas canvas;
	Paint paint;
	Rect srcRect = new Rect();
	Rect dstRect = new Rect();

	public GameGraphics(AssetManager assets, Bitmap frameBuffer) {
		this.assets = assets;
		this.frameBuffer = frameBuffer;
		this.canvas = new Canvas(frameBuffer);
		this.paint = new Paint();
	}

	@Override
	public Image newImage(String fileName, ImageFormat format) {
		Config config = null;
		// check color depth of image, set preference
		if (format == ImageFormat.RGB565) {
			config = Config.RGB_565;
		} else if (format == ImageFormat.ARGB4444) {
			config = Config.ARGB_4444;
		} else {
			config = Config.ARGB_8888;
		}
		Options options = new Options();
		options.inPreferredConfig = config;
		InputStream in = null;
		Bitmap bitmap = null;
		try {
			in = assets.open(fileName);
			bitmap = BitmapFactory.decodeStream(in);
			if (bitmap == null) {
				throw new RuntimeException("couldnt load bitmap from asset");
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("couldnt load bitmap from asset");
			
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		// set format, might not get preferred settings so
		// double check
		if (bitmap.getConfig() == Config.RGB_565) {
			format = ImageFormat.RGB565;
		} else if (bitmap.getConfig() == Config.ARGB_4444) {
			format = ImageFormat.ARGB4444;
		} else {
			format = ImageFormat.ARGB8888;

		}
		return (Image) new GameImage(bitmap, format);
	}

	@Override
	public void clear(int color) {
		canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8,
				(color & 0xff));

	}

	@Override
	public void drawPixel(int x, int y, int color) {
		paint.setColor(color);
		canvas.drawPoint(x, y, paint);

	}

	@Override
	public void drawLine(int x, int y, int x2, int y2, int color) {
		paint.setColor(color);
		canvas.drawLine(x, y, x2, y2, paint);

	}

	@Override
	public void drawRect(int x, int y, int width, int height, int color) {
		paint.setColor(color);
		paint.setStyle(Style.FILL);
		canvas.drawRect(x, y, x + width - 1, y + width - 1, paint);

	}

	@Override
	public void drawImage(Image image, int x, int y, int srcX, int srcY,
			int srcWidth, int srcHeight) {
		srcRect.left = srcX;
		srcRect.top = srcY;
		srcRect.right = srcX + srcWidth - 1;
		srcRect.bottom = srcY + srcHeight - 1;

		dstRect.left = x;
		dstRect.top = y;
		dstRect.right = x + srcWidth - 1;
		dstRect.bottom = y + srcHeight - 1;
		canvas.drawBitmap(((GameImage) image).bitmap, srcRect, dstRect, null);

	}

	@Override
	public void drawImage(Image image, int x, int y) {
		canvas.drawBitmap(((GameImage) image).bitmap, x, y, null);

	}

	@Override
	public int getWidth() {
		return frameBuffer.getWidth();
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return frameBuffer.getHeight();
	}

}
