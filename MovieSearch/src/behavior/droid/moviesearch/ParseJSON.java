package behavior.droid.moviesearch;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

public class ParseJSON {

	private ArrayList<Movie> movies;
	private String title, rating, critics_consensus, poster, release_date,
			rated, synopsis, links;
	private int year, runtime;

	public ArrayList<Movie> parseJson(String json) {
		movies = new ArrayList<Movie>();

		try {
			JSONObject obj = new JSONObject(json);
			JSONArray jArray = obj.getJSONArray("movies");
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject jsonObj = jArray.getJSONObject(i);
				title = jsonObj.optString("title", "not found");
				rating = jsonObj.optString("mpaa_rating", "not found");
				critics_consensus = jsonObj.optString("critics_consensus",
						"not found");
				JSONObject jsonPoster = jsonObj.getJSONObject("posters");
				poster = jsonPoster.optString("thumbnail", "not found");
				JSONObject jsonRelease_date = jsonObj
						.getJSONObject("release_dates");
				release_date = jsonRelease_date.optString("theater",
						"not found");
				JSONObject jsonRatings = jsonObj.getJSONObject("ratings");
				rated = jsonRatings.optString("critics_score", "not found");
				synopsis = jsonObj.optString("synopsis", "not found");
				year = jsonObj.optInt("year", 404);
				runtime = jsonObj.optInt("runtime", 404);
				JSONObject jsonLinks = jsonObj.getJSONObject("links");
				links = jsonLinks.optString("alternate", "not found");
				Movie movie = new Movie(title, rating, critics_consensus,
						release_date, rated, synopsis, poster, year, runtime,
						links);
				movies.add(movie);
			}
		} catch (JSONException e) {
		}
		return movies;
	}
}
