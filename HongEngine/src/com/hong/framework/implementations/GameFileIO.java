package com.hong.framework.implementations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.hong.framework.FileIO;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Environment;
import android.preference.PreferenceManager;
/*
 * handles reading and writing to external storage
 */
public class GameFileIO implements FileIO {
	Context context;
	AssetManager assetManager;
	String externalStoragePath;

	public GameFileIO(Context context) {
		this.context = context;
		assetManager = context.getAssets();
		externalStoragePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator;
	}

	@Override
	public InputStream readAsset(String fileName) throws IOException {
		return assetManager.open(fileName);
	}

	@Override
	public InputStream readFile(String fileName) throws IOException {
		return new FileInputStream(externalStoragePath + fileName);
	}

	@Override
	public OutputStream writeFile(String fileName) throws IOException {
		return new FileOutputStream(externalStoragePath + fileName);
	}

	public SharedPreferences getPreferences() {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

}
