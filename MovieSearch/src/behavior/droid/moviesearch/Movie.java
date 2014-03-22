package behavior.droid.moviesearch;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable, Serializable {

	private static final long serialVersionUID = 1L;

	private String title, rating, critics_consensus, release_date, rated,
			synopsis, poster, links;
	private int year, runtime;

	public Movie(String title, String rating, String critics_consensus,
			String release_date, String rated, String synopsis, String poster,
			int year, int runtime, String links) {
		super();
		this.title = title;
		this.rating = rating;
		this.critics_consensus = critics_consensus;
		this.release_date = release_date;
		this.rated = rated;
		this.synopsis = synopsis;
		this.poster = poster;
		this.year = year;
		this.runtime = runtime;
		this.links = links;
	}

	public Movie(Parcel in) {
		this.title = in.readString();
		this.rating = in.readString();
		this.critics_consensus = in.readString();
		this.release_date = in.readString();
		this.rated = in.readString();
		this.synopsis = in.readString();
		this.poster = in.readString();
		this.year = in.readInt();
		this.runtime = in.readInt();
		this.links = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(title);
		dest.writeString(rating);
		dest.writeString(critics_consensus);
		dest.writeString(release_date);
		dest.writeString(rated);
		dest.writeString(synopsis);
		dest.writeString(poster);
		dest.writeInt(year);
		dest.writeInt(runtime);
		dest.writeString(links);
	}

	public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
		public Movie createFromParcel(Parcel in) {
			return new Movie(in);
		}

		public Movie[] newArray(int size) {
			return new Movie[size];
		}
	};

	public static Parcelable.Creator<Movie> getCreator() {
		return CREATOR;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getCritics_consensus() {
		return critics_consensus;
	}

	public void setCritics_consensus(String critics_consensus) {
		this.critics_consensus = critics_consensus;
	}

	public String getRelease_date() {
		return release_date;
	}

	public void setRelease_date(String release_date) {
		this.release_date = release_date;
	}

	public String getRated() {
		return rated;
	}

	public void setRated(String rated) {
		this.rated = rated;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getRuntime() {
		return runtime;
	}

	public void setRuntime(int runtime) {
		this.runtime = runtime;
	}

	public String getLinks() {
		return links;
	}

	public void setLinks(String link) {
		this.links = link;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	@Override
	public String toString() {
		return "Movie [title=" + title + ", rating=" + rating
				+ ", critics_consensus=" + critics_consensus
				+ ", release_date=" + release_date + ", rated=" + rated
				+ ", synopsis=" + synopsis + ", poster=" + poster + ", year="
				+ year + ", runtime=" + runtime + "]";
	}

}
