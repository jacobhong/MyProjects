package com.example.moviesearch;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import android.util.Log;

public class SerializeObject {

	public static byte[] serialize(Serializable obj) {
		ByteArrayOutputStream baos = null;
		ObjectOutputStream os = null;
		try {
			baos = new ByteArrayOutputStream();
			os = new ObjectOutputStream(baos);
			os.writeObject(obj);
			byte[] bytes = baos.toByteArray();
			return bytes;
		} catch (IOException e) {
			Log.e("BOA", "error during serializing");
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
					Log.e("BOA", "error during close");
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					Log.e("BOA", "error during close");
				}
			}
		}
		return null;
	}

	public static ArrayList<Movie> deserialize(byte[] bytes) {
		ByteArrayInputStream bais = null;
		ObjectInputStream is = null;
		try {
			bais = new ByteArrayInputStream(bytes);
			is = new ObjectInputStream(bais);
			ArrayList<Movie> movies = (ArrayList<Movie>) is.readObject();
			return movies;
		} catch (IOException e) {
			Log.e("BAI", "error during deserializing");
		} catch (ClassNotFoundException e) {
			Log.e("BAI", "error during deserializing");
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					Log.e("BAI", "error during deserializing");
				}
			}
			if (bais != null) {
				try {
					bais.close();
				} catch (IOException e) {
					Log.e("BAI", "error during deserializing");
				}
			}
		}
		return null;
	}
}
