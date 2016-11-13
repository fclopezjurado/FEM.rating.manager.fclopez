package es.upm.miw.ratingmanager.dbmanager.parcelables;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by franlopez on 09/11/2016.
 */

public class Movie implements Parcelable {
    private int _id;
    private int _movieId;
    private String _posterPath;
    private String _overview;
    private String _releaseDate;
    private String _title;
    private String _originalTitle;
    private int _voteCount;
    private double _voteAverage;
    private Long _createdAt;

    public Movie(int id, int movieId, String posterPath, String overview, String releaseDate,
                 String title, String originalTitle, int voteCount, double voteAverage,
                 Long createdAt) {
        this.set_id(id);
        this.set_movieId(movieId);
        this.set_posterPath(posterPath);
        this.set_overview(overview);
        this.set_releaseDate(releaseDate);
        this.set_title(title);
        this.set_originalTitle(originalTitle);
        this.set_voteCount(voteCount);
        this.set_voteAverage(voteAverage);
        this.set_createdAt(createdAt);
    }

    protected Movie(Parcel in) {
        this.set_id(in.readInt());
        this.set_movieId(in.readInt());
        this.set_posterPath(in.readString());
        this.set_overview(in.readString());
        this.set_releaseDate(in.readString());
        this.set_title(in.readString());
        this.set_originalTitle(in.readString());
        this.set_voteCount(in.readInt());
        this.set_voteAverage(in.readDouble());
        this.set_createdAt((Long) in.readValue(Long.class.getClassLoader()));
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_movieId() {
        return _movieId;
    }

    public void set_movieId(int _movieId) {
        this._movieId = _movieId;
    }

    public String get_posterPath() {
        return _posterPath;
    }

    public void set_posterPath(String _posterPath) {
        this._posterPath = _posterPath;
    }

    public String get_overview() {
        return _overview;
    }

    public void set_overview(String _overview) {
        this._overview = _overview;
    }

    public String get_releaseDate() {
        return _releaseDate;
    }

    public void set_releaseDate(String _releaseDate) {
        this._releaseDate = _releaseDate;
    }

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public String get_originalTitle() {
        return _originalTitle;
    }

    public void set_originalTitle(String _originalTitle) {
        this._originalTitle = _originalTitle;
    }

    public int get_voteCount() {
        return _voteCount;
    }

    public void set_voteCount(int _voteCount) {
        this._voteCount = _voteCount;
    }

    public Long get_createdAt() {
        return _createdAt;
    }

    public void set_createdAt(Long _createdAt) {
        this._createdAt = _createdAt;
    }

    public double get_voteAverage() {
        return _voteAverage;
    }

    public void set_voteAverage(double _voteAverage) {
        this._voteAverage = _voteAverage;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "_id=" + _id +
                ", _movieId=" + _movieId +
                ", _posterPath='" + _posterPath + '\'' +
                ", _overview='" + _overview + '\'' +
                ", _releaseDate='" + _releaseDate + '\'' +
                ", _title='" + _title + '\'' +
                ", _originalTitle='" + _originalTitle + '\'' +
                ", _voteCount=" + _voteCount +
                ", _voteAverage=" + _voteAverage +
                ", _createdAt=" + _createdAt +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.get_id());
        parcel.writeInt(this.get_movieId());
        parcel.writeString(this.get_posterPath());
        parcel.writeString(this.get_overview());
        parcel.writeString(this.get_releaseDate());
        parcel.writeString(this.get_title());
        parcel.writeString(this.get_originalTitle());
        parcel.writeInt(this.get_voteCount());
        parcel.writeDouble(this.get_voteAverage());
        parcel.writeValue(this.get_createdAt());
    }
}
