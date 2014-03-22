package behavior.droid.moviesearch;

import java.util.ArrayList;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class ViewMovies extends ActionBarActivity implements
		OnItemClickListener {
	private ArrayList<Movie> movies;
	private Bookmarks bookmark;
	private ListView view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewmovie);
		view = (ListView) findViewById(R.id.movie_list);
		int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		Bundle extras = getIntent().getExtras();
		ArrayList<Movie> movielist = extras.getParcelableArrayList("movielist");
		movies = movielist;
		MovieAdapter adapter = new MovieAdapter(this, R.layout.viewmovie,
				movielist, maxMemory);
		view.setAdapter(adapter);
		view.setOnItemClickListener(ViewMovies.this);
		getSupportActionBar().setHomeButtonEnabled(true);

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
			Intent about = new Intent(ViewMovies.this, About.class);
			startActivity(about);
			break;
		case R.id.bookmarks:
			Intent bookmarks = new Intent(ViewMovies.this, Bookmarks.class);
			startActivity(bookmarks);
			break;
		case R.id.homeAsUp:
			NavUtils.navigateUpFromSameTask(this);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(final AdapterView<?> arg0, View arg1,
			final int arg2, long arg3) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ViewMovies.this);
		builder.setTitle("ID LIKE TO");
		builder.setPositiveButton("ADD BOOKMARK", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				new Bookmarks().addBookmark(movies.get(arg2),
						getApplicationContext());
				Toast.makeText(arg0.getContext(), "Added bookmark",
						Toast.LENGTH_SHORT).show();
			}
		});
		builder.setNegativeButton("VISIT IMDB PAGE", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent imdb = new Intent(Intent.ACTION_VIEW);
				imdb.setData(Uri.parse(movies.get(arg2).getLinks()));
				startActivity(imdb);
			}
		});
		builder.create();
		builder.show();

	}

}
