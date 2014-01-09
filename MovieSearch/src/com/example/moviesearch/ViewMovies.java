package com.example.moviesearch;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ArrayAdapter;

public class ViewMovies extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		Bundle extras = getIntent().getExtras();
		ArrayList<Movie> movielist = extras.getParcelableArrayList("movielist");
		MovieAdapter adapter = new MovieAdapter(this, R.layout.viewmovie,
				movielist, maxMemory);
		setListAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

}
