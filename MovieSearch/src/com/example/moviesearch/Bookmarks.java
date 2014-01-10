package com.example.moviesearch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Bookmarks extends ListActivity {
	private static File directory;
	private static final String FILENAME = "Bookmarks";
	private static ArrayList<Movie> movies = new ArrayList<Movie>();
	private ArrayList<String> tempList;

	public Bookmarks(File file) {
		directory = file;
		Log.e("bookmarkdir", directory + "");
	}

	public Bookmarks() {
		loadBookmark();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("BOOKMAKRCREAT", "");
		loadBookmark();
		if (movies != null) {
			Log.e("null", "a");
			setListAdapter(new MovieAdapter(this, R.layout.viewmovie, movies,
					(int) Runtime.getRuntime().maxMemory()));
		} else {
			Log.e("notnull", "a");
			tempList = new ArrayList<String>();
			tempList.add("NO BOOKMARKS FOUND");
			setListAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, tempList));
		}

	}

	public void addBookmark(Movie movie) {
		try {
			if (!movies.contains(movie)) {
				movies.add(movie);
				FileOutputStream fos = new FileOutputStream(new File(directory,
						FILENAME));
				ObjectOutputStream os = new ObjectOutputStream(fos);
				os.writeObject(movies);
				Log.e("saving bookmarks", movies.toString());
				os.flush();
				os.close();
			} else {
				Log.e("dupe", "not adding dupe movie");
			}
		} catch (FileNotFoundException e) {
			Log.e("FileNotFound from Bookmarks", e + "");
		} catch (IOException e) {
			Log.e("IOException from Bookmarks", e + "");
		}
	}

	public void loadBookmark() {
		try {
			FileInputStream fis = new FileInputStream(new File(directory,
					FILENAME));
			ObjectInputStream is = new ObjectInputStream(fis);
			movies = (ArrayList<Movie>) is.readObject();
			Log.e("loading bookmarks", movies.toString());
			is.close();
		} catch (FileNotFoundException e) {
			Log.e("FileNotFound from Bookmarks", e + "");
		} catch (IOException e) {
			Log.e("IOException from Bookmarks", e + "");
		} catch (ClassNotFoundException e) {
			Log.e("ClassNotFound from Bookmarks", e + "");
		}

	}

}