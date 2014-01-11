package com.example.moviesearch;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	private TextView searchTextview, json_null;
	private Button searchButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		

		initialize();

	}

	private void initialize() {
		searchTextview = (TextView) findViewById(R.id.movie_edittext);
		json_null = (TextView) findViewById(R.id.json_null);
		searchButton = (Button) findViewById(R.id.search_button);
		searchButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new SearchTask().execute();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.about:				
			break;
		case R.id.bookmarks:
			bookmarks();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void bookmarks() {
		Intent bookmarks = new Intent(MainActivity.this, Bookmarks.class);		
		startActivity(bookmarks);
	}

	private void about() {
		//show dialogue of tips and usage
	}

	private class SearchTask extends AsyncTask<Void, Void, ArrayList<Movie>> {
		ProgressDialog dialogue;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialogue = ProgressDialog.show(MainActivity.this,
					"Searching movies", "loading..please wait");
			dialogue.setCancelable(true);
			dialogue.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					cancel(true);
				}
			});
		}

		@Override
		protected ArrayList<Movie> doInBackground(Void... params) {
			HttpService service = new HttpService();
			String json = service.getJSON(searchTextview.getText().toString());
			ParseJSON parse = new ParseJSON();
			ArrayList<Movie> movies = parse.parseJson(json);
			return movies;
		}

		@Override
		protected void onPostExecute(ArrayList<Movie> result) {
			super.onPostExecute(result);
			dialogue.dismiss();
			if (result.isEmpty()) {
				json_null.setText("no results found please try another movie");
				searchTextview.setText("");
			} else {
				json_null.setText("");
				searchTextview.setText("");
				Intent viewmovies = new Intent(MainActivity.this,
						ViewMovies.class);
				viewmovies.putParcelableArrayListExtra("movielist", result);
				startActivity(viewmovies);
			}
		}

	}

}
