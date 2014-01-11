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
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class Bookmarks extends ListActivity implements OnItemLongClickListener {
	private static File directory;
	private static final String FILENAME = "/Bookmarks";
	private static ArrayList<Movie> movies = new ArrayList<Movie>();
	private MovieAdapter adapter;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		directory = getFilesDir();
		loadBookmark();
		adapter = new MovieAdapter(this, R.layout.viewmovie, movies,
				(int) Runtime.getRuntime().maxMemory());
		setListAdapter(adapter);
		getListView().setOnItemLongClickListener(this);
	}

	public void addBookmark(Movie movie, Context context) {
		try {
			if (!movies.contains(movie)) {
				directory = context.getFilesDir();
				movies.add(movie);
				FileOutputStream fos = new FileOutputStream(new File(directory,
						FILENAME));
				ObjectOutputStream os = new ObjectOutputStream(fos);
				os.writeObject(movies);
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
			FileInputStream fis = new FileInputStream(directory + FILENAME);
			ObjectInputStream is = new ObjectInputStream(fis);
			movies = (ArrayList<Movie>) is.readObject();
			is.close();
		} catch (FileNotFoundException e) {
			Log.e("FileNotFound from Bookmarks", e + "");
		} catch (IOException e) {
			Log.e("IOException from Bookmarks", e + "");
		} catch (ClassNotFoundException e) {
			Log.e("ClassNotFoundException from Bookmarks", e + "");
		}

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		movies.remove(arg2);
		Toast.makeText(getApplicationContext(), "removed bookmark",
				Toast.LENGTH_SHORT).show();
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(new File(directory, FILENAME));
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(movies);
			os.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			adapter.notifyDataSetInvalidated();
		}
		return true;
	}

}