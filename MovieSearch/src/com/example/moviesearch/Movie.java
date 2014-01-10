package com.example.moviesearch;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable, Serializable {
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

	@Override
	public String toString() {
		return "Movie [genres=" + genres + ", rated=" + rated + ", language="
				+ language + ", rating=" + rating + ", country=" + country
				+ ", release_date=" + release_date + ", title=" + title
				+ ",  directors=" + directors + ", actors=" + actors
				+ ", plot_simple=" + plot_simple + ", poster=" + poster
				+ ",  runtime=" + runtime + ", imdb_url=" + imdb_url + "]";
	}

	public Movie(String genres, String rated, String language, int rating,
			String country, String release_date, String title,
			String directors, String actors, String plot_simple, String poster,
			String runtime, String imdb_url) {
		super();
		this.genres = genres;
		this.rated = rated;
		this.language = language;
		this.rating = rating;
		this.country = country;
		this.release_date = release_date;
		this.title = title;

		this.directors = directors;
		this.actors = actors;
		this.plot_simple = plot_simple;
		this.poster = poster;
		this.runtime = runtime;
		this.imdb_url = imdb_url;
	}

	public Movie(Parcel in) {
		this.genres = in.readString();
		this.rated = in.readString();
		this.language = in.readString();
		this.rating = in.readInt();
		this.country = in.readString();
		this.release_date = in.readString();
		this.title = in.readString();

		this.directors = in.readString();
		this.actors = in.readString();
		this.plot_simple = in.readString();
		this.poster = in.readString();
		this.runtime = in.readString();
		this.imdb_url = in.readString();

	}

	public String getGenres() {
		return genres;
	}

	public void setGenres(String genres) {
		this.genres = genres;
	}

	public String getRated() {
		return rated;
	}

	public void setRated(String rated) {
		this.rated = rated;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRelease_date() {
		return release_date;
	}

	public void setRelease_date(String release_date) {
		this.release_date = release_date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDirectors() {
		return directors;
	}

	public void setDirectors(String directors) {
		this.directors = directors;
	}

	public String getActors() {
		return actors;
	}

	public void setActors(String actors) {
		this.actors = actors;
	}

	public String getPlot_simple() {
		return plot_simple;
	}

	public void setPlot_simple(String plot_simple) {
		this.plot_simple = plot_simple;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public String getRuntime() {
		return runtime;
	}

	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}

	public String getImdb_url() {
		return imdb_url;
	}

	public void setImdb_url(String imdb_url) {
		this.imdb_url = imdb_url;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(genres);
		dest.writeString(rated);
		dest.writeString(language);
		dest.writeInt(rating);
		dest.writeString(country);
		dest.writeString(release_date);
		dest.writeString(title);
		dest.writeString(directors);
		dest.writeString(actors);
		dest.writeString(plot_simple);
		dest.writeString(poster);
		dest.writeString(runtime);
		dest.writeString(imdb_url);

	}

	public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
		public Movie createFromParcel(Parcel in) {
			return new Movie(in);
		}

		public Movie[] newArray(int size) {
			return new Movie[size];
		}
	};
}
