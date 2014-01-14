package behavior.droid.moviesearch;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class ImageManager {
	private LruCache<String, Bitmap> mMemoryCache;

	public ImageManager(int memory) {
		int maxMemory = memory;
		int cacheSize = maxMemory / 8;
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				// cache measure in kb instead of number of items
				return ((bitmap.getRowBytes() * bitmap.getHeight()) / 1024);

			}
		};
	}

	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemoryCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	public Bitmap getBitmapFromMemoryCache(String key) {
		return mMemoryCache.get(key);
	}
}
