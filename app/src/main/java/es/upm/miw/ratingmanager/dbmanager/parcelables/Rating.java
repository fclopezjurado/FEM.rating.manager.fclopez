package es.upm.miw.ratingmanager.dbmanager.parcelables;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by franlopez on 09/11/2016.
 */

public class Rating implements Parcelable {
    private int _id;
    private int _movieId;
    private double _rating;

    public Rating(int id, int movieId, double rating) {
        this.set_id(id);
        this.set_movieId(movieId);
        this.set_rating(rating);
    }

    protected Rating(Parcel in) {
        this.set_id(in.readInt());
        this.set_movieId(in.readInt());
        this.set_rating(in.readDouble());
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

    public double get_rating() {
        return _rating;
    }

    public void set_rating(double _rating) {
        this._rating = _rating;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "_id=" + _id +
                ", _movieId=" + _movieId +
                ", _rating=" + _rating +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Rating> CREATOR = new Creator<Rating>() {
        @Override
        public Rating createFromParcel(Parcel in) {
            return new Rating(in);
        }

        @Override
        public Rating[] newArray(int size) {
            return new Rating[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.get_id());
        parcel.writeInt(this.get_movieId());
        parcel.writeDouble(this.get_rating());
    }
}
