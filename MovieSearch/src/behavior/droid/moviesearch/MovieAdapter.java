package behavior.droid.moviesearch;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieAdapter extends ArrayAdapter<Movie> {
	private ArrayList<Movie> movieData;
	private Activity context;
	private ViewHolder holder;
	private HttpService service;
	private int maxMemory;
	private ImageManager manage;

	public MovieAdapter(Context context, int resource,
			ArrayList<Movie> objects, int memory) {
		super(context, resource, objects);
		this.context = (Activity) context;
		service = new HttpService();
		movieData = objects;
		maxMemory = memory;
		manage = new ImageManager(maxMemory);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		holder = new ViewHolder();
		if (view == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.movie_item, parent, false);
			holder.poster = (ImageView) view.findViewById(R.id.poster);
			holder.release_date = (TextView) view
					.findViewById(R.id.release_date);
			holder.title = (TextView) view.findViewById(R.id.title);
			holder.genres = (TextView) view.findViewById(R.id.genres);
			holder.runtime = (TextView) view.findViewById(R.id.runtime);
			holder.directors = (TextView) view.findViewById(R.id.directors);
			holder.actors = (TextView) view.findViewById(R.id.actors);
			holder.plot_simple = (TextView) view.findViewById(R.id.plot_simple);
			holder.rated = (TextView) view.findViewById(R.id.rated);
			holder.language = (TextView) view.findViewById(R.id.language);
			holder.rating = (TextView) view.findViewById(R.id.rating);
			holder.country = (TextView) view.findViewById(R.id.country);
			holder.imdb_url = (TextView) view.findViewById(R.id.imdburl);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		Movie movie = movieData.get(position);
		if (movie != null) {
			Typeface typeface = Typeface.createFromAsset(getContext()
					.getAssets(), "fonts/robotocondensed-bold.ttf");
			holder.genres.setTypeface(typeface);
			holder.genres.setText("Genres: " + movie.getGenres());
			holder.rated.setTypeface(typeface);
			holder.rated.setText("Rated: " + movie.getRated());
			holder.language.setTypeface(typeface);
			holder.language.setText("Language: " + movie.getLanguage());
			holder.rating.setTypeface(typeface);
			holder.rating.setText("Rating: " + movie.getRating());
			holder.country.setTypeface(typeface);
			holder.country.setText("Country: " + movie.getCountry());
			holder.release_date.setTypeface(typeface);
			holder.release_date.setText("Release date: "
					+ movie.getRelease_date());
			holder.genres.setTypeface(typeface);
			holder.title.setText("Title: " + movie.getTitle());
			holder.directors.setText("Directors: " + movie.getDirectors());
			holder.actors.setText("Actors: " + movie.getActors());
			holder.plot_simple
					.setText("Plot: " + movie.getPlot_simple());
			holder.runtime.setTypeface(typeface);
			holder.runtime.setText("Runtime: " + movie.getRuntime());
			holder.poster.setImageBitmap(null);
			if (!movie.getPoster().equals("unavailable")) {
				holder.imdb_url.setTag(movie.getImdb_url());
				new DownloadTask(holder, movie, manage).execute(movie
						.getPoster());
			}
		
		}
		return view;
	}

	public class ViewHolder {
		private ImageView poster;
		private TextView genres;
		private TextView release_date;
		private TextView title;
		private TextView runtime;
		private TextView directors;
		private TextView actors;
		private TextView plot_simple;
		private TextView rated;
		private TextView language;
		private TextView rating;
		private TextView country;
		private TextView imdb_url;

	}

	private class DownloadTask extends AsyncTask<String, Void, Bitmap> {
		ImageManager manage;
		private ViewHolder holder;
		private Movie movie;

		public DownloadTask(ViewHolder holder, Movie movie, ImageManager manage) {
			this.holder = holder;
			this.movie = movie;
			this.manage = manage;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap bitmap = manage.getBitmapFromMemoryCache(params[0]);
			if (bitmap != null) {
				return bitmap;
			} else {
				Bitmap bitmap2 = service.getBitmap(params[0]);
				if (bitmap2 != null) {
					manage.addBitmapToMemoryCache(params[0], bitmap2);
				}
				return bitmap2;

			}

		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if (movie.getImdb_url().equals(holder.imdb_url.getTag())) {
				holder.poster.setImageBitmap(result);
			}

		}

	}
}
