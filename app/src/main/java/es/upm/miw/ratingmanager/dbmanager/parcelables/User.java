package es.upm.miw.ratingmanager.dbmanager.parcelables;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by franlopez on 09/11/2016.
 */

public class User implements Parcelable {
    private int _id;
    private String _name;
    private String _password;
    private String _apiKey;
    private String _requestToken;
    private String _sessionId;

    public User(int id, String name, String password, String apiKey, String requestToken,
                String sessionId) {
        this.set_id(id);
        this.set_name(name);
        this.set_password(password);
        this.set_apiKey(apiKey);
        this.set_requestToken(requestToken);
        this.set_sessionId(sessionId);
    }

    protected User(Parcel in) {
        this.set_id(in.readInt());
        this.set_name(in.readString());
        this.set_password(in.readString());
        this.set_apiKey(in.readString());
        this.set_requestToken(in.readString());
        this.set_sessionId(in.readString());
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_password() {
        return _password;
    }

    public void set_password(String _password) {
        this._password = _password;
    }

    public String get_apiKey() {
        return _apiKey;
    }

    public void set_apiKey(String _apiKey) {
        this._apiKey = _apiKey;
    }

    public String get_requestToken() {
        return _requestToken;
    }

    public void set_requestToken(String _requestToken) {
        this._requestToken = _requestToken;
    }

    public String get_sessionId() {
        return _sessionId;
    }

    public void set_sessionId(String _sessionId) {
        this._sessionId = _sessionId;
    }

    @Override
    public String toString() {
        return "User{" +
                "_id=" + _id +
                ", _name='" + _name + '\'' +
                ", _password='" + _password + '\'' +
                ", _apiKey='" + _apiKey + '\'' +
                ", _requestToken='" + _requestToken + '\'' +
                ", _sessionId='" + _sessionId + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.get_id());
        parcel.writeString(this.get_name());
        parcel.writeString(this.get_password());
        parcel.writeString(this.get_apiKey());
        parcel.writeString(this.get_requestToken());
        parcel.writeString(this.get_sessionId());
    }
}
