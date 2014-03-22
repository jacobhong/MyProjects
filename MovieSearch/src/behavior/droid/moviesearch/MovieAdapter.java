package behavior.droid.moviesearch;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
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
			holder.title = (TextView) view.findViewById(R.id.title);
			holder.rating = (TextView) view.findViewById(R.id.rating);
			holder.critics_consensus = (TextView) view
					.findViewById(R.id.critics_consensus);
			holder.release_date = (TextView) view
					.findViewById(R.id.release_date);
			holder.rated = (TextView) view.findViewById(R.id.rated);
			holder.synopsis = (TextView) view.findViewById(R.id.synopsis);
			holder.year = (TextView) view.findViewById(R.id.year);
			holder.runtime = (TextView) view.findViewById(R.id.runtime);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		Movie movie = movieData.get(position);
		if (movie != null) {

			Typeface typeface = Typeface.createFromAsset(getContext()
					.getAssets(), "fonts/robotocondensed-bold.ttf");
			holder.rating.setTypeface(typeface);
			holder.rating.setText("Rating: " + movie.getRating());
			holder.title.setText("Title: " + movie.getTitle());
			holder.title.setTag(movie.getTitle());
			Log.i("TAG", "" + holder.title.getTag());
			holder.poster.setImageBitmap(null);
			holder.critics_consensus.setText("consensus: "
					+ movie.getCritics_consensus());
			holder.release_date.setText("date: " + movie.getRelease_date());
			holder.rated.setText("rated: " + movie.getRated());
			holder.synopsis.setText("synopsis: " + movie.getSynopsis());
			holder.year.setText("year: " + movie.getYear());
			holder.runtime.setText("runtime: " + movie.getRuntime());
			if (!movie.getPoster().equals("not found")) {
				new DownloadTask(holder, movie, manage).execute(movie
						.getPoster());
			}

		}
		return view;
	}

	public class ViewHolder {
		private TextView title, rating, critics_consensus, release_date, rated,
				synopsis, year, runtime;
		private ImageView poster;

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
			if (movie.getTitle().equals(holder.title.getTag())) {
				holder.poster.setImageBitmap(result);
			}

		}

	}
}
