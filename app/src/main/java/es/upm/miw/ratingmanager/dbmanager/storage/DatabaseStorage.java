package es.upm.miw.ratingmanager.dbmanager.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import es.upm.miw.ratingmanager.dbmanager.contracts.MoviesContract;
import es.upm.miw.ratingmanager.dbmanager.contracts.RatingsContract;
import es.upm.miw.ratingmanager.dbmanager.contracts.UsersContract;
import es.upm.miw.ratingmanager.dbmanager.parcelables.Movie;
import es.upm.miw.ratingmanager.dbmanager.parcelables.Rating;
import es.upm.miw.ratingmanager.dbmanager.parcelables.User;

/**
 * Created by franlopez on 09/11/2016.
 */

public class DatabaseStorage extends SQLiteOpenHelper {
    private static final String DATABASE_FILE = "ratingManager.db";
    private static final int VERSION_NUMBER = 1;

    public DatabaseStorage(Context context) {
        super(context, DATABASE_FILE, null, VERSION_NUMBER);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQLToCreateTable = "CREATE TABLE " + MoviesContract.moviesTableDefinition.TABLE_NAME
                + "( " + MoviesContract.moviesTableDefinition.COLUMN_NAME_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MoviesContract.moviesTableDefinition.COLUMN_NAME_MOVIE_ID + " INTEGER, " +
                MoviesContract.moviesTableDefinition.COLUMN_NAME_POSTER_PATH + " TEXT, " +
                MoviesContract.moviesTableDefinition.COLUMN_NAME_OVERVIEW + " TEXT, " +
                MoviesContract.moviesTableDefinition.COLUMN_NAME_RELEASE_DATE + " TEXT, " +
                MoviesContract.moviesTableDefinition.COLUMN_NAME_MOVIE_TITLE + " TEXT, " +
                MoviesContract.moviesTableDefinition.COLUMN_NAME_ORIGINAL_TITLE + " TEXT, " +
                MoviesContract.moviesTableDefinition.COLUMN_NAME_VOTE_COUNT + " INTEGER, " +
                MoviesContract.moviesTableDefinition.COLUMN_NAME_VOTE_AVERAGE + " REAL, " +
                MoviesContract.moviesTableDefinition.COLUMN_NAME_RECORD_CREATED_AT + " INTEGER" +
                " );";

        sqLiteDatabase.execSQL(SQLToCreateTable);

        SQLToCreateTable = "CREATE TABLE " + RatingsContract.ratingsTableDefinition.TABLE_NAME
                + "( " + RatingsContract.ratingsTableDefinition.COLUMN_NAME_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RatingsContract.ratingsTableDefinition.COLUMN_NAME_MOVIE_ID + " INTEGER, " +
                RatingsContract.ratingsTableDefinition.COLUMN_NAME_RATING + " REAL" +
                " );";

        sqLiteDatabase.execSQL(SQLToCreateTable);

        SQLToCreateTable = "CREATE TABLE " + UsersContract.usersTableDefinition.TABLE_NAME
                + "( " + UsersContract.usersTableDefinition.COLUMN_NAME_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                UsersContract.usersTableDefinition.COLUMN_NAME_USER_NAME + " TEXT, " +
                UsersContract.usersTableDefinition.COLUMN_NAME_USER_PASSWORD + " TEXT, " +
                UsersContract.usersTableDefinition.COLUMN_NAME_USER_API_KEY + " TEXT, " +
                UsersContract.usersTableDefinition.COLUMN_NAME_REQUEST_TOKEN + " TEXT, " +
                UsersContract.usersTableDefinition.COLUMN_NAME_SESSION_ID + " TEXT" +
                " );";

        sqLiteDatabase.execSQL(SQLToCreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String SQLToDropTable = "DROP TABLE IF EXISTS "
                + MoviesContract.moviesTableDefinition.TABLE_NAME;

        sqLiteDatabase.execSQL(SQLToDropTable);

        SQLToDropTable = "DROP TABLE IF EXISTS "
                + RatingsContract.ratingsTableDefinition.TABLE_NAME;

        sqLiteDatabase.execSQL(SQLToDropTable);

        SQLToDropTable = "DROP TABLE IF EXISTS "
                + UsersContract.usersTableDefinition.TABLE_NAME;

        sqLiteDatabase.execSQL(SQLToDropTable);
    }

    public long count(String tableName) {
        SQLiteDatabase SQLiteOpenHelper = getReadableDatabase();
        return DatabaseUtils.queryNumEntries(SQLiteOpenHelper, tableName);
    }

    public long insertMovie(int movieId, String posterPath, String overview, String releaseDate,
                            String title, String originalTitle, int voteCount, double voteAverage,
                            Long createdAt) {

        SQLiteDatabase SQLiteOpenHelper = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(MoviesContract.moviesTableDefinition.COLUMN_NAME_MOVIE_ID, movieId);
        values.put(MoviesContract.moviesTableDefinition.COLUMN_NAME_POSTER_PATH, posterPath);
        values.put(MoviesContract.moviesTableDefinition.COLUMN_NAME_OVERVIEW, overview);
        values.put(MoviesContract.moviesTableDefinition.COLUMN_NAME_RELEASE_DATE, releaseDate);
        values.put(MoviesContract.moviesTableDefinition.COLUMN_NAME_MOVIE_TITLE, title);
        values.put(MoviesContract.moviesTableDefinition.COLUMN_NAME_ORIGINAL_TITLE, originalTitle);
        values.put(MoviesContract.moviesTableDefinition.COLUMN_NAME_VOTE_COUNT, voteCount);
        values.put(MoviesContract.moviesTableDefinition.COLUMN_NAME_VOTE_AVERAGE, voteAverage);
        values.put(MoviesContract.moviesTableDefinition.COLUMN_NAME_RECORD_CREATED_AT, createdAt);

        return SQLiteOpenHelper.insert(MoviesContract.moviesTableDefinition.TABLE_NAME, null,
                values);
    }

    public long insertRating(int movieId, double rating) {

        SQLiteDatabase SQLiteOpenHelper = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(RatingsContract.ratingsTableDefinition.COLUMN_NAME_MOVIE_ID, movieId);
        values.put(RatingsContract.ratingsTableDefinition.COLUMN_NAME_RATING, rating);

        return SQLiteOpenHelper.insert(RatingsContract.ratingsTableDefinition.TABLE_NAME, null,
                values);
    }

    public long insertUser(String name, String password, String apiKey, String requestToken,
                           String sessionId) {

        SQLiteDatabase SQLiteOpenHelper = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(UsersContract.usersTableDefinition.COLUMN_NAME_USER_NAME, name);
        values.put(UsersContract.usersTableDefinition.COLUMN_NAME_USER_PASSWORD, password);
        values.put(UsersContract.usersTableDefinition.COLUMN_NAME_USER_API_KEY, apiKey);
        values.put(UsersContract.usersTableDefinition.COLUMN_NAME_REQUEST_TOKEN, requestToken);
        values.put(UsersContract.usersTableDefinition.COLUMN_NAME_SESSION_ID, sessionId);

        return SQLiteOpenHelper.insert(UsersContract.usersTableDefinition.TABLE_NAME, null, values);
    }

    public Cursor getMoviesByReleaseDateInCursor(String releaseDateGreaterThanOrEqual,
                                                 String releaseDateLessThanOrEqual) {
        String SQLWhereStatement = "date("
                + MoviesContract.moviesTableDefinition.COLUMN_NAME_RELEASE_DATE
                + ") BETWEEN date(?) AND date(?)";

        SQLiteDatabase SQLiteOpenHelper = this.getReadableDatabase();
        return SQLiteOpenHelper.query(MoviesContract.moviesTableDefinition.TABLE_NAME, null,
                SQLWhereStatement, new String[]{releaseDateGreaterThanOrEqual,
                        releaseDateLessThanOrEqual}, null, null, null);
    }

    public ArrayList<Movie> getMoviesByReleaseDate(String releaseDateGreaterThanOrEqual,
                                                   String releaseDateLessThanOrEqual) {

        ArrayList<Movie> movies = new ArrayList<>();
        Cursor cursor = this.getMoviesByReleaseDateInCursor(releaseDateGreaterThanOrEqual,
                releaseDateLessThanOrEqual);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Movie movie = new Movie(
                        cursor.getInt(cursor.getColumnIndex(
                                MoviesContract.moviesTableDefinition.COLUMN_NAME_MOVIE_ID)),
                        cursor.getInt(cursor.getColumnIndex(
                                MoviesContract.moviesTableDefinition.COLUMN_NAME_MOVIE_ID)),
                        cursor.getString(cursor.getColumnIndex(
                                MoviesContract.moviesTableDefinition.COLUMN_NAME_POSTER_PATH)),
                        cursor.getString(cursor.getColumnIndex(
                                MoviesContract.moviesTableDefinition.COLUMN_NAME_OVERVIEW)),
                        cursor.getString(cursor.getColumnIndex(
                                MoviesContract.moviesTableDefinition.COLUMN_NAME_RELEASE_DATE)),
                        cursor.getString(cursor.getColumnIndex(
                                MoviesContract.moviesTableDefinition.COLUMN_NAME_MOVIE_TITLE)),
                        cursor.getString(cursor.getColumnIndex(
                                MoviesContract.moviesTableDefinition.COLUMN_NAME_ORIGINAL_TITLE)),
                        cursor.getInt(cursor.getColumnIndex(
                                MoviesContract.moviesTableDefinition.COLUMN_NAME_VOTE_COUNT)),
                        cursor.getDouble(cursor.getColumnIndex(
                                MoviesContract.moviesTableDefinition.COLUMN_NAME_VOTE_AVERAGE)),
                        cursor.getLong(cursor.getColumnIndex(
                                MoviesContract.moviesTableDefinition.COLUMN_NAME_RECORD_CREATED_AT))
                );

                movies.add(movie);
                cursor.moveToNext();
            }
        }

        cursor.close();
        return movies;
    }

    public Cursor getRatingByMovieIDInCursor(int movieId) {
        SQLiteDatabase SQLiteOpenHelper = this.getReadableDatabase();
        String SQLWhereStatement = RatingsContract.ratingsTableDefinition.COLUMN_NAME_MOVIE_ID
                + " = ?";

        return SQLiteOpenHelper.query(
                RatingsContract.ratingsTableDefinition.TABLE_NAME, null, SQLWhereStatement,
                new String[]{Integer.toString(movieId)}, null, null, null);
    }

    public Rating getRatingByMovieID(int movieId) {
        Rating rating = null;
        Cursor cursor = this.getRatingByMovieIDInCursor(movieId);

        if (cursor.moveToFirst()) {
            rating = new Rating(
                    cursor.getInt(cursor.getColumnIndex(
                            RatingsContract.ratingsTableDefinition.COLUMN_NAME_ID)),
                    cursor.getInt(cursor.getColumnIndex(
                            RatingsContract.ratingsTableDefinition.COLUMN_NAME_MOVIE_ID)),
                    cursor.getDouble(cursor.getColumnIndex(
                            RatingsContract.ratingsTableDefinition.COLUMN_NAME_RATING))
            );
            cursor.close();
        }

        return rating;
    }

    public User getAppUser() {
        SQLiteDatabase SQLiteOpenHelper = this.getReadableDatabase();
        User user = null;

        Cursor cursor = SQLiteOpenHelper.query(UsersContract.usersTableDefinition.TABLE_NAME, null,
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(cursor.getColumnIndex(
                            UsersContract.usersTableDefinition.COLUMN_NAME_ID)),
                    cursor.getString(cursor.getColumnIndex(
                            UsersContract.usersTableDefinition.COLUMN_NAME_USER_NAME)),
                    cursor.getString(cursor.getColumnIndex(
                            UsersContract.usersTableDefinition.COLUMN_NAME_USER_PASSWORD)),
                    cursor.getString(cursor.getColumnIndex(
                            UsersContract.usersTableDefinition.COLUMN_NAME_USER_API_KEY)),
                    cursor.getString(cursor.getColumnIndex(
                            UsersContract.usersTableDefinition.COLUMN_NAME_REQUEST_TOKEN)),
                    cursor.getString(cursor.getColumnIndex(
                            UsersContract.usersTableDefinition.COLUMN_NAME_SESSION_ID))
            );
            cursor.close();
        }

        return user;
    }

    public int deleteMoviesByReleaseDate(String releaseDateGreaterThanOrEqual,
                                         String releaseDateLessThanOrEqual) {
        SQLiteDatabase SQLiteOpenHelper = this.getReadableDatabase();
        String SQLWhereStatement = "date("
                + MoviesContract.moviesTableDefinition.COLUMN_NAME_RELEASE_DATE
                + ") BETWEEN date(?) AND date(?)";

        return SQLiteOpenHelper.delete(MoviesContract.moviesTableDefinition.TABLE_NAME,
                SQLWhereStatement, new String[]{releaseDateGreaterThanOrEqual,
                        releaseDateLessThanOrEqual});
    }
}
