package com.example.moviesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ParseJSON {
	private ArrayList<Movie> movies;
	private String genres;
	private String rated;
	private String language;
	private int rating;
	private String country;
	private String release_date;
	private String title;
	private String directors;
	private String actors;
	private String plot_simple;
	private String poster;
	private String runtime;
	private String imdb_url;

	public ArrayList<Movie> parseJson(String json) {
		movies = new ArrayList<Movie>();
		try {
			JSONArray jArray = new JSONArray(json);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject j = jArray.getJSONObject(i);

				genres = j.optString("genres", "unavailable").replace("[", "")
						.replace("]", "").replaceAll("\"", "");
				rated = j.optString("rated", "notfo5und");
				language = j.optString("language", "unavailable")
						.replace("[", "").replace("]", "").replaceAll("\"", "");
				rating = j.optInt("rating", 404);
				country = j.optString("country", "unavailable")
						.replace("[", "").replace("]", "").replaceAll("\"", "");
				release_date = j.optString("release_date", "unavailable");
				title = j.optString("title", "nofound").replace("\n", "");
				directors = j.optString("directors", "unavailable")
						.replace("[", "").replace("]", "").replaceAll("\"", "");
				actors = j.optString("actors", "unavailable").replace("[", "")
						.replace("]", "").replaceAll("\"", "");
				plot_simple = j.optString("plot_simple", "unavailable");
				try {
					JSONObject c = j.getJSONObject("poster");
					poster = c.getString("cover");
				} catch (Exception e) {
					poster = "unavailable";
				}
				runtime = j.optString("runtime", "unavailable")
						.replace("[", "").replace("]", "").replaceAll("\"", "");
				imdb_url = j.optString("imdb_url", "unavailable");
				Movie movie = new Movie(genres, rated, language, rating,
						country, release_date, title, directors, actors,
						plot_simple, poster, runtime, imdb_url);
				movies.add(movie);
			}

		} catch (JSONException e) {
			Log.e("jsonerror", "", e);
		}
		return movies;
	}
}
